package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {


    Item findItemById(int itemId);

    Page<Item> findItemsByOwnerIdOrderByIdAsc(int userId, Pageable pageable);


    Page<Item> findItemByNameOrDescriptionContainsIgnoreCaseAndAvailableIsTrue(String name, String text, Pageable pageable);

    List<Item> findItemsByRequestRequestorIdOrderByIdAsc(int userId);

    List<Item> findItemsByRequestIdOrderByIdAsc(int userId);

    List<Item> findItemsByRequestIdInOrderByIdAsc(List<Integer> id);

}
