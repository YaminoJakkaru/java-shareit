package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {


    Item findItemById(int itemId);

    List<Item> findItemsByOwnerIdOrderByIdAsc(int userId);


    List<Item> findItemByNameOrDescriptionContainsIgnoreCaseAndAvailableIsTrue(String name,String text);

}
