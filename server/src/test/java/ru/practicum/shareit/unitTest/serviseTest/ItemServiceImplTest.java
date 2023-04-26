package ru.practicum.shareit.unitTest.serviseTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.vo.Status;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class ItemServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    @InjectMocks
    ItemServiceImpl itemService;

    User userFirst = new User()
            .setId(1)
            .setName("this.getName")
            .setEmail("this.get@Email.ru");
    User userSecond = new User()
            .setId(2)
            .setName("this.getSecondName")
            .setEmail("this.getSecond@Email.ru");
    User userThird = new User()
            .setId(3)
            .setName("this.getThirdName")
            .setEmail("this.getThird@Email.ru");
    User userFourth = new User()
            .setId(4)
            .setName("this.getFourthName")
            .setEmail("this.getFourth@Email.ru");
    User userFive = new User()
            .setId(5)
            .setName("this.getFiveName")
            .setEmail("this.getFive@Email.ru");

    ItemRequest itemRequestFirst = new ItemRequest()
            .setId(1)
            .setDescription("Хочу Банан")
            .setRequestor(userFirst)
            .setCreated(LocalDateTime.parse("2019-01-21T05:47:08.644"));

    Item itemFirst = new Item()
            .setId(1)
            .setName("hammer")
            .setDescription("heavy")
            .setAvailable(true)
            .setOwner(userFirst);
    Item itemSecond = new Item()
            .setId(2)
            .setName("axe")
            .setDescription("blunt, but can be used as a hammer")
            .setAvailable(true)
            .setOwner(userSecond);
    Item itemThird = new Item()
            .setId(3)
            .setName("chainsaw")
            .setDescription("do brrr")
            .setAvailable(true)
            .setOwner(userSecond);
    Item itemByFirstRequest = new Item()
            .setId(4)
            .setName("Банан")
            .setDescription("еш")
            .setOwner(userSecond)
            .setAvailable(true)
            .setRequest(itemRequestFirst);
    Booking bookingFirst = new Booking()
            .setId(1)
            .setStart(LocalDateTime.parse("2010-01-21T05:47:08.644"))
            .setEnd(LocalDateTime.parse("2019-01-21T05:47:08.644"))
            .setItem(itemSecond)
            .setBooker(userFirst)
            .setStatus(Status.WAITING);
    Booking bookingSecond = new Booking()
            .setId(2)
            .setStart(LocalDateTime.parse("2019-01-21T05:47:08.644"))
            .setEnd(LocalDateTime.parse("2100-01-21T05:47:08.644"))
            .setItem(itemSecond)
            .setBooker(userThird)
            .setStatus(Status.WAITING);
    Booking bookingThird = new Booking()
            .setId(3)
            .setStart(LocalDateTime.parse("2110-01-21T05:47:08.644"))
            .setEnd(LocalDateTime.parse("2120-01-21T05:47:08.644"))
            .setItem(itemSecond)
            .setBooker(userFirst)
            .setStatus(Status.WAITING);
    Booking bookingFourth = new Booking()
            .setId(4)
            .setStart(LocalDateTime.parse("2121-01-21T05:47:08.644"))
            .setEnd(LocalDateTime.parse("2124-01-21T05:47:08.644"))
            .setItem(itemSecond)
            .setBooker(userFive)
            .setStatus(Status.WAITING);
    Booking bookingFive = new Booking()
            .setId(5)
            .setStart(LocalDateTime.parse("2121-01-21T05:47:08.644"))
            .setEnd(LocalDateTime.parse("2124-01-21T05:47:08.644"))
            .setItem(itemSecond)
            .setBooker(userFive)
            .setStatus(Status.WAITING);
    Comment commentFirst = new Comment()
            .setId(1)
            .setText("Text")
            .setAuthor(userFirst)
            .setItem(itemSecond)
            .setCreated(LocalDateTime.parse("2010-01-21T05:47:08.644"));

    @Test
    void addItem_whenUserIsNull() {
        int userBasicId = 0;
        when(userRepository.findUserById(userBasicId)).thenReturn(null);
        Assertions.assertThrows(UserNotFoundException.class, () -> itemService.addItem(userBasicId, new ItemDto()));
    }

    @Test
    void addItem_whenItemRequestIsNull() {
        int userBasicId = 0;

        Item itemToSave = new Item();
        when(userRepository.findUserById(userBasicId)).thenReturn(new User());
        itemToSave.setOwner(userRepository.findUserById(userBasicId));
        when(itemRepository.save(itemToSave)).thenReturn(itemToSave);
        ItemDto itemDto = itemService.addItem(userBasicId, itemToSave.toItemDto());
        Assertions.assertEquals(itemDto, itemToSave.toItemDto());
        verify(itemRepository).save(itemToSave);
    }

    @Test
    void addItem_whenItemRequestIs() {
        int userBasicId = 0;

        ItemRequest itemRequest = new ItemRequest();
        Item itemToSave = new Item();
        when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(itemRequest);
        when(userRepository.findUserById(userBasicId)).thenReturn(new User());
        itemToSave
                .setOwner(userRepository.findUserById(userBasicId))
                .setRequest(itemRequestRepository.findById(itemRequest.getId()));
        when(itemRepository.save(itemToSave)).thenReturn(itemToSave);
        ItemDto itemDto = itemService.addItem(userBasicId, itemToSave.toItemDto());
        Assertions.assertEquals(itemDto, itemToSave.toItemDto());
        verify(itemRepository).save(itemToSave);
    }

    @Test
    void updateItem_whenItemIsNull() {
        int userBasicId = 0;
        int itemBasicId = 0;

        when(itemRepository.findItemById(itemBasicId)).thenReturn(null);
        Assertions.assertThrows(ItemNotFoundException.class,
                () -> itemService.updateItem(userBasicId, itemBasicId, new Item()));
    }

    @Test
    void updateItem_whenUserIsNull() {
        int userBasicId = 0;
        int itemBasicId = 0;

        when(userRepository.findUserById(itemBasicId)).thenReturn(null);
        when(itemRepository.findItemById(itemBasicId)).thenReturn(new Item());
        Assertions.assertThrows(UserNotFoundException.class,
                () -> itemService.updateItem(userBasicId, itemBasicId, new Item()));
    }

    @Test
    void updateItem_whenOtherUser() {
        int userBasicId = 0;
        int itemBasicId = 0;

        when(userRepository.findUserById(itemBasicId)).thenReturn(new User());
        when(itemRepository.findItemById(itemBasicId)).thenReturn(new Item().setOwner(new User().setId(2)));
        Assertions.assertThrows(UserNotFoundException.class,
                () -> itemService.updateItem(userBasicId, itemBasicId, new Item()));
    }

    @Test
    void updateItem_AllWright() {

        Item itemNew = new Item()
                .setId(1)
                .setName("1")
                .setDescription("1")
                .setAvailable(true);
        Item itemOld = new Item()
                .setId(1)
                .setName("0")
                .setDescription("0")
                .setAvailable(false)
                .setOwner(new User()).setOwner(new User().setId(1));
        when(userRepository.findUserById(itemNew.getId())).thenReturn(new User().setId(1));
        when(itemRepository.findItemById(1)).thenReturn(itemOld);
        when(itemRepository.save(itemOld)).thenReturn(itemOld);
        Assertions.assertEquals(itemService.updateItem(1, 1, itemNew), itemOld);
    }

    @Test
    void findItemById_whenItemIsNull() {
        int userBasicId = 0;
        int itemBasicId = 0;

        when(itemRepository.findItemById(itemBasicId)).thenReturn(null);
        when(userRepository.findUserById(userBasicId)).thenReturn(new User());
        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.findItemById(userBasicId, itemBasicId));
    }

    @Test
    void findItemById_whenUserIsNull() {
        int userBasicId = 0;
        int itemBasicId = 0;

        when(userRepository.findUserById(userBasicId)).thenReturn(null);
        Assertions.assertThrows(UserNotFoundException.class, () -> itemService.findItemById(userBasicId, itemBasicId));
    }

    @Test
    void findItemById_foOwner() {

        ItemDto itemDto = itemSecond.toItemDto().setLastBooking(bookingFirst.toBookingDto());
        when(itemRepository.findItemById(itemSecond.getId())).thenReturn(itemSecond);
        when(userRepository.findUserById(userSecond.getId())).thenReturn(userSecond);
        when(bookingRepository.findBookingsByItemIdAndStatusIsNot(itemSecond.getId(), Status.REJECTED))
                .thenReturn(List.of(bookingFirst));
        Assertions.assertEquals(itemService.findItemById(userSecond.getId(), itemSecond.getId()), itemDto);
    }

    @Test
    void findItemById_foOtherUser() {
        ItemDto itemDto = itemSecond.toItemDto();
        when(itemRepository.findItemById(itemSecond.getId())).thenReturn(itemSecond);
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);
        Assertions.assertEquals(itemService.findItemById(userFirst.getId(), itemSecond.getId()), itemDto);
    }

    @Test
    void getAllUserItemsTest_whenUserIsNull() {
        int userBasicId = 0;
        when(userRepository.findUserById(userBasicId)).thenReturn(null);
        Assertions.assertThrows(UserNotFoundException.class,
                () -> itemService.getAllUserItems(userBasicId, 0, 0));
    }

    @Test
    void getAllUserItemsTest_withoutCommentAndBooking() {

        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);
        when(itemRepository.findItemsByOwnerIdOrderByIdAsc(userFirst.getId(),
                PageRequest.of(0, 1)))
                .thenReturn(new PageImpl<>(List.of(itemFirst)));
        when(bookingRepository.findBookingsByItemOwnerIdAndStatusIsNot(userFirst.getId(), Status.REJECTED))
                .thenReturn(List.of());
        when(commentRepository.findCommentByItemOwnerId(userFirst.getId())).thenReturn(List.of());
        Assertions.assertEquals(itemService.getAllUserItems(userFirst.getId(), 0, 1),
                List.of(itemFirst.toItemDto()));
    }

    @Test
    void getAllUserItemsTest_withCommentAndBooking_pageIsFull() {
        ItemDto itemSecondDto = itemSecond
                .toItemDto()
                .setLastBooking(bookingSecond.toBookingDto())
                .setNextBooking(bookingThird.toBookingDto());
        itemSecondDto.addComment(commentFirst);
        ItemDto itemThirdDto = itemThird.toItemDto();
        when(userRepository.findUserById(userSecond.getId())).thenReturn(userSecond);
        when(itemRepository.findItemsByOwnerIdOrderByIdAsc(userSecond.getId(),
                PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(itemSecond, itemThird)));
        when(bookingRepository.findBookingsByItemOwnerIdAndStatusIsNot(userSecond.getId(), Status.REJECTED))
                .thenReturn(List.of(bookingFive, bookingFourth, bookingThird, bookingSecond, bookingFirst));
        when(commentRepository.findCommentByItemOwnerId(userSecond.getId())).thenReturn(List.of(commentFirst));
        Assertions.assertEquals(itemService.getAllUserItems(userSecond.getId(), 0, 10),
                List.of(itemSecondDto, itemThirdDto));
    }

    @Test
    void getAllUserItemsTest_pageOne_SizeOne() {
        ItemDto itemThirdDto = itemThird.toItemDto();
        when(userRepository.findUserById(userSecond.getId())).thenReturn(userSecond);
        when(itemRepository.findItemsByOwnerIdOrderByIdAsc(userSecond.getId(),
                PageRequest.of(1, 1)))
                .thenReturn(new PageImpl<>(List.of(itemThird)));
        when(bookingRepository.findBookingsByItemOwnerIdAndStatusIsNot(userSecond.getId(), Status.REJECTED))
                .thenReturn(List.of());
        when(commentRepository.findCommentByItemOwnerId(userSecond.getId())).thenReturn(List.of());
        Assertions.assertEquals(itemService.getAllUserItems(userSecond.getId(), 1, 1),
                List.of(itemThirdDto));
    }

    @Test
    void searchItems_emptyText() {
        Assertions.assertEquals(itemService.searchItems(" ", 0, 10), List.of());
    }

    @Test
    void searchItems_pageIsFull() {
        String text = "HaMMer";
        when(itemRepository
                .findItemByNameOrDescriptionContainsIgnoreCaseAndAvailableIsTrue(text, text,
                        PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of(itemFirst, itemSecond)));
        Assertions.assertEquals(itemService.searchItems(text, 0, 10),
                List.of(itemFirst.toItemDto(), itemSecond.toItemDto()));
    }

    @Test
    void addComment_whenItemIsNull() {
        int userBasicId = 0;
        int itemBasicId = 0;

        when(itemRepository.findItemById(itemBasicId)).thenReturn(null);
        Assertions.assertThrows(ValidationException.class,
                () -> itemService.addComment(userBasicId, itemBasicId,commentFirst.toCommentDto()));
    }

    @Test
    void addComment_whenUserIsNull() {
        int userBasicId = 0;
        int itemBasicId = 0;
        when(itemRepository.findItemById(itemBasicId)).thenReturn(new Item());
        when(userRepository.findUserById(userBasicId)).thenReturn(null);
        Assertions.assertThrows(ValidationException.class,
                () -> itemService.addComment(userBasicId, itemBasicId,commentFirst.toCommentDto()));
    }

    @Test
    void addComment_whenBookingIsNull() {
        int userBasicId = 0;
        int itemBasicId = 0;
        when(itemRepository.findItemById(itemBasicId)).thenReturn(new Item());
        when(userRepository.findUserById(userBasicId)).thenReturn(new User());
        when(bookingRepository.findFirstBookingsByBookerIdAndItemId(userBasicId, itemBasicId)).thenReturn(null);
        Assertions.assertThrows(ValidationException.class,
                () -> itemService.addComment(userBasicId, itemBasicId,commentFirst.toCommentDto()));
    }

    @Test
    void addComment_whenAllRight() {
        when(itemRepository.findItemById(itemSecond.getId())).thenReturn(itemSecond);
        when(userRepository.findUserById(userFirst.getId())).thenReturn(userFirst);
        when(bookingRepository.findFirstBookingsByBookerIdAndItemId(userFirst.getId(), itemSecond.getId()))
                .thenReturn(bookingFirst);

        when(commentRepository.save(any())).thenReturn(commentFirst);
        Assertions.assertEquals(itemService
                        .addComment(userFirst.getId(), itemSecond.getId(), commentFirst.toCommentDto()),
                commentFirst.toCommentDto());
    }
}
