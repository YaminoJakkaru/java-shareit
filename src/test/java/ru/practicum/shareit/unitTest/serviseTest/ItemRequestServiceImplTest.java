package ru.practicum.shareit.unitTest.serviseTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    int basicId = 0;

    User userFirst = new User()
            .setId(1)
            .setName("this.getName")
            .setEmail("this.get@Email.ru");
    User userSecond = new User()
            .setId(2)
            .setName("this.getSecondName")
            .setEmail("this.getSecond@Email.ru");

    ItemRequest itemRequestFirst = new ItemRequest()
            .setId(1)
            .setDescription("I am bored")
            .setRequestor(userFirst)
            .setCreated(LocalDateTime.parse("2019-01-21T05:47:08.644"));
    Item itemFirst = new Item()
            .setId(1)
            .setName("hammer")
            .setDescription("heavy")
            .setAvailable(true)
            .setOwner(userSecond)
            .setRequest(itemRequestFirst);

    @Test
    void addRequestTest_whenUserIsNull() {
        when(userRepository.findUserById(basicId)).thenReturn(null);
        Assertions.assertThrows(UserNotFoundException.class,
                () -> itemRequestService.addRequest(basicId, itemRequestFirst.toItemRequestDto()));
    }

    @Test
    void addRequestTest_whenAllRight() {
        when(userRepository.findUserById(basicId)).thenReturn(userFirst);
        when(itemRequestRepository.save(any())).thenReturn(itemRequestFirst);
        Assertions.assertEquals(itemRequestService.addRequest(basicId, itemRequestFirst.toItemRequestDto()),
                itemRequestFirst.toItemRequestDto());
        verify(itemRequestService).addRequest(basicId, itemRequestFirst.toItemRequestDto());
    }

    @Test
    void findAllByRequestorIdTest_whenUserIsNull() {
        when(userRepository.findUserById(basicId)).thenReturn(null);
        Assertions.assertThrows(UserNotFoundException.class,
                () -> itemRequestService.findAllByRequestorId(basicId));
    }

    @Test
    void findAllByRequestorIdTest_whenAllRight() {
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);
        when(itemRequestRepository.findAllByRequestorIdOrderByIdDesc(userFirst.getId()))
                .thenReturn(List.of(itemRequestFirst));
        when(itemRepository.findItemsByRequestRequestorIdOrderByIdAsc(userFirst.getId()))
                .thenReturn(List.of(itemFirst));
        ItemRequestDto itemRequestDto = itemRequestFirst.toItemRequestDto();
        itemRequestDto.addItem(itemFirst);
        Assertions.assertEquals(itemRequestService.
                findAllByRequestorId(userFirst.getId()),List.of(itemRequestDto));
    }

    @Test
    void findAllTest() {
        when(itemRequestRepository
                .findAllByIdIsNotOrderByIdDesc(userFirst.getId(), PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(itemRequestFirst)));
        when(itemRepository.findItemsByRequestIdInOrderByIdAsc(any()))
                .thenReturn(List.of(itemFirst));
        ItemRequestDto itemRequestDto = itemRequestFirst.toItemRequestDto();
        itemRequestDto.addItem(itemFirst);
        Assertions.assertEquals(itemRequestService.
                findAll(userFirst.getId(), 0, 10),List.of(itemRequestDto));
    }

    @Test
    void findByIdTest_whenUserIsNull() {
        when(userRepository.findUserById(basicId)).thenReturn(null);
        Assertions.assertThrows(UserNotFoundException.class,
                () -> itemRequestService.findById(basicId, basicId));
    }
    @Test
    void findByIdTest_whenItemRequestIsNull() {
        when(userRepository.findUserById(basicId)).thenReturn(new User());
        when(itemRequestRepository.findById(basicId)).thenReturn(null);
        Assertions.assertThrows(ItemRequestNotFoundException.class,
                () -> itemRequestService.findById(basicId, basicId));
    }

    @Test
    void findByIdTest_whenAllRight() {
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);
        when(itemRequestRepository.findById(itemRequestFirst.getId())).thenReturn(itemRequestFirst);
        when(itemRepository.findItemsByRequestIdOrderByIdAsc(userFirst.getId())).thenReturn(List.of(itemFirst));
        ItemRequestDto itemRequestDto = itemRequestFirst.toItemRequestDto();
        itemRequestDto.addItem(itemFirst);
        Assertions.assertEquals(itemRequestService.
                findById(userFirst.getId(),itemRequestFirst.getId()), itemRequestDto);
    }

}
