package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;


import java.util.List;


public interface ItemRequestService {

    ItemRequestDto addRequest(int userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> findAllByRequestorId(int reqestorId);

    List<ItemRequestDto> findAll(int reqestorId, int from, int size);

    ItemRequestDto findById(int userId, int id);
}
