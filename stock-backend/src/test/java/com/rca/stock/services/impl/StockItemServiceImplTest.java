package com.rca.stock.services.impl;

import com.rca.stock.dto.notification.NotificationMessage;
import com.rca.stock.dto.stock.StockItemDto;
import com.rca.stock.exception.StockItemNotFoundException;
import com.rca.stock.mapper.NotificationMapper;
import com.rca.stock.mapper.StockMapper;
import com.rca.stock.model.Notification;
import com.rca.stock.model.StockItem;
import com.rca.stock.model.User;
import com.rca.stock.repository.NotifcationRepository;
import com.rca.stock.repository.StockItemRepository;
import com.rca.stock.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockItemServiceImplTest {

    @InjectMocks
    private StockItemServiceImpl stockItemService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotifcationRepository notifcationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;


    @Mock
    private StockItemRepository stockItemRepository;

    @Mock
    private StockMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void addStockItem_ShouldSaveAndReturnStockItem() {
        StockItemDto stockItemDto = new StockItemDto("Item 1" ,"Category 1", 10.0, BigDecimal.valueOf(10.0), false);
        StockItem stockItem = StockItem.builder()
                .name("Item 1")
                .category("Category 1")
                .price(BigDecimal.valueOf(10.0))
                .isNotified(false)
                .build();

        when(mapper.toStockItem(stockItemDto)).thenReturn(stockItem);
        when(stockItemRepository.save(stockItem)).thenReturn(stockItem);


        StockItem savedStockItem = stockItemService.addStockItem(stockItemDto);

        assertNotNull(savedStockItem);

        assertEquals(stockItem.getName(), savedStockItem.getName());
        verify(stockItemRepository, times(1)).save(stockItem);
    }


    @Test
    void updateStockItem_ShouldUpdateAndReturnStockItem(){
        Integer id = 1;
        StockItemDto stockItemDto = new StockItemDto("Updated Item", "Updated Category", 5.0, BigDecimal.valueOf(50), false);

        StockItem stockItem = StockItem.builder()
                .name("Item 1")
                .category("Category 1")
                .price(BigDecimal.valueOf(10.0))
                .isNotified(false)
                .build();

        when(stockItemRepository.findById(id)).thenReturn(Optional.of(stockItem));
        when(stockItemRepository.save(stockItem)).thenReturn(stockItem);

        StockItem savedStockItem = stockItemService.updateStockItem(id, stockItemDto);

        assertNotNull(savedStockItem);
        verify(stockItemRepository, times(1)).findById(id);
        verify(stockItemRepository, times(1)).save(stockItem);
    }


    @Test
    void updateStockItem_ShouldNotUpdateNotFoundStockItem(){
        Integer id = 1;

        StockItemDto stockItemDto = new StockItemDto("Updated Item", "Updated Category", 5.0, BigDecimal.valueOf(50), false);
        when(stockItemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(StockItemNotFoundException.class, () -> stockItemService.updateStockItem(id , stockItemDto));
        verify(stockItemRepository, times(1)).findById(id);
        verify(stockItemRepository, never()).save(any(StockItem.class));

    }


    @Test
    void deleteStockItem_ShouldDeleteStockItem(){
        Integer id = 1;
        StockItem stockItem = StockItem.builder()
                .name("Item 1")
                .category("Category 1")
                .price(BigDecimal.valueOf(10.0))
                .isNotified(false)
                .build();
        when(stockItemRepository.findById(id)).thenReturn(Optional.of(stockItem));
        stockItemService.deleteStockItem(id);

        verify(stockItemRepository, times(1)).findById(id);
        verify(stockItemRepository, times(1)).delete(stockItem);
    }

    @Test
    void deleteStockItem_ShouldNotDeleteNotFoundStockItem(){
        Integer id = 1;
        when(stockItemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(StockItemNotFoundException.class, () -> stockItemService.deleteStockItem(id ));
        verify(stockItemRepository, times(1)).findById(id);
        verify(stockItemRepository, never()).delete(any(StockItem.class));
    }


    @Test
    void getStockItemById_shouldReturnStockItem(){
        Integer id = 1;
        StockItem stockItem = StockItem.builder()
                .name("Item 1")
                .category("Category 1")
                .price(BigDecimal.valueOf(10.0))
                .isNotified(false)
                .build();

        when(stockItemRepository.findById(id)).thenReturn(Optional.of(stockItem));

        StockItem result = stockItemService.getStockItemById(id);
        assertNotNull(result);
        verify(stockItemRepository, times(1)).findById(id);
    }

    @Test
    void getStockItemById_shouldReturnNotFoundStockItem(){
        Integer id = 1;
        when(stockItemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(StockItemNotFoundException.class, () -> stockItemService.getStockItemById(id ));
        verify(stockItemRepository , times(1)).findById(id);
    }


    @Test
    void restockStockItem_ShouldUpdateStockItem(){
        Integer id = 1;
        double additionalQuantity = 10.0;

        StockItem stockItem = StockItem.builder()
                .name("Item 1")
                .category("Category 1")
                .quantity(5.0)
                .price(BigDecimal.valueOf(10.0))
                .isNotified(false)
                .build();

        when(stockItemRepository.findById(id)).thenReturn(Optional.of(stockItem));
        when(stockItemRepository.save(stockItem)).thenReturn(stockItem);

        StockItem result = stockItemService.restockStockItem(id , additionalQuantity);
        assertNotNull(result);
        assertEquals(15.0, result.getQuantity());
        verify(stockItemRepository, times(1)).findById(id);
        verify(stockItemRepository, times(1)).save(stockItem);
    }


    @Test
    void consumeStockItem_ShouldUpdateStockItem(){
        Integer id = 1;
        double consumedQuantity = 10.0;

        StockItem stockItem = StockItem.builder()
                .name("Item 1")
                .category("Category 1")
                .quantity(30.0)
                .price(BigDecimal.valueOf(10.0))
                .consumedQuantity(0.0)
                .isNotified(false)
                .build();

        User mockedUser = mock(User.class);
        when(mockedUser.fullName()).thenReturn("Test User");
        when(mockedUser.getEmail()).thenReturn("test@example.com");

        when(stockItemRepository.findById(id)).thenReturn(Optional.of(stockItem));
        when(stockItemRepository.save(stockItem)).thenReturn(stockItem);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockedUser);

        SecurityContextHolder.setContext(securityContext);
        StockItem result = stockItemService.consumeStockItem(id , consumedQuantity);
        assertNotNull(result);

        assertEquals(20, result.getQuantity());
        verify(stockItemRepository, times(1)).findById(id);
        verify(stockItemRepository, times(1)).save(stockItem);

    }

    @Test
    void consumeStockItem_ShouldThrowException_WhenNotEnoughStock() {

        Integer id = 1;
        double consumeAmount = 15.0;
        StockItem stockItem = new StockItem();
        stockItem.setQuantity(10.0);
        when(stockItemRepository.findById(id)).thenReturn(Optional.of(stockItem));

        assertThrows(IllegalArgumentException.class, () -> stockItemService.consumeStockItem(id, consumeAmount));
    }

    @Test
    void checkAndNotifyLowStock_ShouldSendNotification_WhenStockIsLow() {
        Integer id = 1;
        StockItem stockItem = StockItem.builder()
                .name("Item 1")
                .category("Category 1")
                .quantity(5.0)
                .price(BigDecimal.valueOf(10.0))
                .isNotified(false)
                .build();

        when(stockItemRepository.findById(id)).thenReturn(Optional.of(stockItem));

        User mockUser = mock(User.class);
        when(mockUser.fullName()).thenReturn("Test User");
        when(mockUser.getEmail()).thenReturn("test@example.com");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        SecurityContextHolder.setContext(securityContext);

        NotificationMessage notificationMessage = new NotificationMessage(
                stockItem.getName(),
                stockItem.getCategory(),
                "Stock level is low for category: " + stockItem.getCategory(),
                stockItem.getQuantity(),
                mockUser,
                mockUser.getEmail()
        );

        Notification mockNotification = mock(Notification.class);


        when(notificationMapper.toNotification(notificationMessage)).thenReturn(mockNotification);
        doNothing().when(notificationService).sendNotification(any());

        stockItemService.consumeStockItem(1,1.0);

        verify(notifcationRepository, times(1)).save(any());
        verify(notificationService, times(1)).sendNotification(any());
        assertTrue(stockItem.getIsNotified());
    }
}