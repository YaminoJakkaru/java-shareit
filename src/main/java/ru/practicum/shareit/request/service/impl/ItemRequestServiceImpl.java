package ru.practicum.shareit.request.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, ItemRepository itemRepository,
                                  UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public ItemRequestDto addRequest(int userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            log.warn("Попытка сделать запрос от имени несуществующего пользователя");
            throw new UserNotFoundException();
        }
        ItemRequest itemRequest = itemRequestDto.toItemRequest().setRequestor(user);
        return itemRequestRepository.save(itemRequest).toItemRequestDto();
    }

    @Override
    public List<ItemRequestDto> findAllByRequestorId(int reqestorId) {
        User user = userRepository.findUserById(reqestorId);
        if (user == null) {
            log.warn("Попытка получить данные запросов от пользователя");
            throw new UserNotFoundException();
        }
        Map<Integer, ItemRequestDto> requests = itemRequestRepository.findAllByRequestorIdOrderByIdDesc(reqestorId)
                .stream()
                .collect(Collectors.toMap(ItemRequest::getId, ItemRequest::toItemRequestDto));
        itemRepository.findItemsByRequestRequestorIdOrderByIdAsc(reqestorId)
                .forEach(item -> requests.get(item.getRequest().getId()).addItem(item));
        return new ArrayList<>(requests.values());
    }

    @Override
    public List<ItemRequestDto> findAll(int reqestorId, int from, int size) {

        Map<Integer, ItemRequestDto> requests = itemRequestRepository
                    .findAllByIdIsNotOrderByIdDesc(reqestorId, PageRequest.of(from > 0 ? from / size : 0, size))
                    .stream()
                    .collect(Collectors.toMap(ItemRequest::getId, ItemRequest::toItemRequestDto));
        itemRepository.findItemsByRequestIdInOrderByIdAsc(new ArrayList<>(requests.keySet()))
                .forEach(item -> requests.get(item.getRequest().getId()).addItem(item));
        return new ArrayList<>(requests.values());
    }

    @Override
    public ItemRequestDto findById(int userId, int id) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            log.warn("Попытка получить данные запроса от пользователя");
            throw new UserNotFoundException();
        }
       ItemRequest request = itemRequestRepository.findById(id);
       if (request == null) {
           log.warn("Запрошены данные несуществующего запроса");
           throw new ItemRequestNotFoundException();
       }
       ItemRequestDto requestDto = request.toItemRequestDto();
       requestDto.setItems(itemRepository.findItemsByRequestIdOrderByIdAsc(id)
               .stream()
               .map(Item::toItemDto)
               .collect(Collectors.toList()));
       return requestDto;
    }
}
