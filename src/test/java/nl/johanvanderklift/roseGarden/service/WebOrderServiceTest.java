package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.dto.WebOrderDetailInputDto;
import nl.johanvanderklift.roseGarden.dto.WebOrderInputDto;
import nl.johanvanderklift.roseGarden.dto.WebOrderOutputDto;
import nl.johanvanderklift.roseGarden.exception.*;
import nl.johanvanderklift.roseGarden.model.*;
import nl.johanvanderklift.roseGarden.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebOrderServiceTest {

    @Mock
    WebOrderRepository webOrderRepository;
    @Mock
    WebOrderDetailRepository webOrderDetailRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    AddressRepository addressRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    WebOrder webOrder;
    @InjectMocks
    WebOrderService webOrderService;

    WebOrder webOrder1 = new WebOrder();
    WebOrder webOrder2 = new WebOrder();
    Address address = new Address();
    User user1 = new User();
    User user2 = new User();
    WebOrderDetail webOrderDetail1 = new WebOrderDetail();
    WebOrderDetail webOrderDetail2 = new WebOrderDetail();
    Product product1 = new Product();
    Product product2 = new Product();

    @BeforeEach
    void setUp() {
        product1.setName("Test product 1");
        product1.setPrice(10.0);
        product1.setAvailability(true);

        product2.setName("Test product 2");
        product2.setPrice(20.0);
        product2.setAvailability(true);

        user1.setUsername("Testusername");
        user1.setEmail("test@test.nl");
        user1.setPassword("Password1");
        user1.setWebOrders(List.of(webOrder1));

        user2.setUsername("Testusername2");
        user2.setEmail("test2@test.nl");
        user2.setPassword("Password2");

        address.setId(1L);
        address.setAddressLine1("Teststreet");
        address.setCity("Testerton");
        address.setUser(user1);

        webOrder1.setUser(user1);
        webOrder1.setAddress(address);
        webOrder1.setId("2023-ORD-00001");
        webOrder1.setWebOrderStatus(WebOrderStatus.PENDING);

        webOrder2.setUser(user1);
        webOrder2.setAddress(address);
        webOrder2.setId("2023-ORD-00002");
        webOrder2.setWebOrderStatus(WebOrderStatus.PROCESSED);

        webOrderDetail1.setWebOrder(webOrder1);
        webOrderDetail1.setId(1L);
        webOrderDetail1.setProduct(product1);
        webOrderDetail1.setQuantity(5);

        webOrderDetail2.setId(2L);
        webOrderDetail2.setProduct(product2);
        webOrderDetail2.setQuantity(10);
        webOrderDetail2.setWebOrder(webOrder1);
    }

    @Test
    @DisplayName("Should return list of webOrders")
    void getAllWebOrdersByUsername() {
        when(webOrderRepository.findByUser_Username(user1.getUsername())).thenReturn(List.of(webOrder1, webOrder2));
        when(webOrderRepository.findByUser_Username(user2.getUsername())).thenReturn(List.of());

        List<WebOrderOutputDto> dtos1 = webOrderService.getAllWebOrdersByUsername("Testusername");
        List<WebOrderOutputDto> dtos2 = webOrderService.getAllWebOrdersByUsername("Testusername2");

        assertEquals(webOrder1.getId(), dtos1.get(0).id);
        assertEquals(webOrder1.getWebOrderStatus().toString(), dtos1.get(0).webOrderStatus);

        assertEquals(webOrder2.getId(), dtos1.get(1).id);
        assertEquals(webOrder2.getWebOrderStatus().toString(), dtos1.get(1).webOrderStatus);

        assertEquals(0, dtos2.size());
    }

    @Test
    @DisplayName("Should return correct webOrderOutputDto by id")
    void getWebOrderById() {
        when(webOrderRepository.findById("2023-ORD-00001")).thenReturn(Optional.of(webOrder1));

        WebOrderOutputDto dto = webOrderService.getWebOrderById("2023-ORD-00001");

        assertEquals(webOrder1.getAddress(), dto.address);
        assertEquals(webOrder1.getWebOrderStatus().toString(), dto.webOrderStatus);
    }

    @Test
    @DisplayName("Should return correct webOrderOutputDto by id and username")
    void getWebOrderByIdAndUsername() {
        when(webOrderRepository.findById("2023-ORD-00001")).thenReturn(Optional.of(webOrder1));
        when(userRepository.findById("Testusername")).thenReturn(Optional.of(user1));

        WebOrderOutputDto dto = webOrderService.getWebOrderById("2023-ORD-00001", "Testusername");

        assertEquals(webOrder1.getAddress(), dto.address);
        assertEquals(webOrder1.getWebOrderStatus().toString(), dto.webOrderStatus);
    }

    @Test
    @DisplayName("Should save webOrder and return correct id")
    void createWebOrder() {
        when(userRepository.findById("Testusername")).thenReturn(Optional.of(user1));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(webOrderRepository.save(any(WebOrder.class))).thenReturn(webOrder1);

        WebOrderInputDto dto = new WebOrderInputDto();
        dto.productId = 1L;
        dto.quantity = 2;
        dto.addressId = 1L;

        String newId = webOrderService.createWebOrder(dto, "Testusername");

        assertEquals(webOrder1.getId(), newId);
    }

    @Test
    @DisplayName("Should add quantity to existing webOrderDetail and return id")
    void addWebOrderDetailAlreadyExistsToWebOrder() {
        webOrder1.setWebOrderDetails(List.of(webOrderDetail1));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(webOrderRepository.findById("2023-ORD-00001")).thenReturn(Optional.of(webOrder1));
        when(webOrderDetailRepository.save(any(WebOrderDetail.class))).thenReturn(webOrderDetail1);

        WebOrderDetailInputDto dto = new WebOrderDetailInputDto();
        dto.webOrderId = webOrder1.getId();
        dto.quantity = 10;
        dto.productId = 1L;

        Long newId = webOrderService.addWebOrderDetailToWebOrder(dto);

        assertEquals(1, newId);
    }

    @Test
    @DisplayName("Should delete existing webOrderDetail when quantity <= 0")
    void addWebOrderDetailToWebOrderAndDelete() {
        webOrder1.setWebOrderDetails(List.of(webOrderDetail1));

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product1));
        when(webOrderRepository.findById(anyString())).thenReturn(Optional.of(webOrder1));

        WebOrderDetailInputDto dto = new WebOrderDetailInputDto();
        dto.webOrderId = webOrder1.getId();
        dto.quantity = -10;
        dto.productId = 3L;

        Long deleteCode = webOrderService.addWebOrderDetailToWebOrder(dto);

        assertEquals(0, deleteCode);
    }

    @Test
    @DisplayName("Should add webOrderDetail and return id")
    void addWebOrderDetailToWebOrder() {
        webOrder1.setWebOrderDetails(List.of(webOrderDetail1));

        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        when(webOrderRepository.findById("2023-ORD-00001")).thenReturn(Optional.of(webOrder1));
        when(webOrderDetailRepository.save(any(WebOrderDetail.class))).thenReturn(webOrderDetail2);

        WebOrderDetailInputDto dto = new WebOrderDetailInputDto();
        dto.webOrderId = webOrder1.getId();
        dto.quantity = 10;
        dto.productId = 2L;

        Long newId = webOrderService.addWebOrderDetailToWebOrder(dto);

        assertEquals(2L, newId);
    }

    @Test
    @DisplayName("Should return correct order id when confirming webOrder")
    void confirmOrder() {
        user1.setAddresses(List.of(address));
        when(webOrderRepository.findById("2023-ORD-00001")).thenReturn(Optional.of(webOrder1));
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.findByUser_UsernameOrderByIdAsc(user1.getUsername())).thenReturn(List.of(address));
        when(webOrderRepository.save(any(WebOrder.class))).thenReturn(webOrder1);

        String newWeborderId = webOrderService.confirmOrder(webOrder1.getId(), address.getId(), user1.getUsername());

        assertEquals("2023-ORD-00001", newWeborderId);
    }

    @Test
    @DisplayName("Should return correct webOrderStatus when changing the status of webOrder")
    void changeOrderStatus() {
        when(webOrderRepository.findById("2023-ORD-00001")).thenReturn(Optional.of(webOrder1));

        String newWebOrderStatus = webOrderService.changeOrderStatus(webOrder1.getId(), "DELIVERING");
        assertEquals(webOrder1.getWebOrderStatus().toString(), newWebOrderStatus);

        newWebOrderStatus = webOrderService.changeOrderStatus(webOrder1.getId(), "PROCESSED");
        assertEquals(webOrder1.getWebOrderStatus().toString(), newWebOrderStatus);


    }

    @Test
    @DisplayName("Should verify delete WebOrder")
    void deleteWebOrder() {
        when(webOrderRepository.findById(webOrder1.getId())).thenReturn(Optional.of(webOrder1));

        // Act
        webOrderService.deleteWebOrder(webOrder1.getId());

        // Assert
        verify(webOrderRepository).delete(webOrder1);
    }

    @Test
    @DisplayName("Should throw exception")
    void shouldThrowException() {
        user2.setWebOrders(List.of(webOrder1));
        WebOrderInputDto webOrderDto = new WebOrderInputDto();
        WebOrderDetailInputDto webOrderDetailInputDto = new WebOrderDetailInputDto();
        webOrderDto.productId = 1L;
        when(webOrderRepository.findById(webOrder1.getId())).thenReturn(Optional.empty());
        when(webOrderRepository.findById(webOrder2.getId())).thenReturn(Optional.of(webOrder2));
        when(userRepository.findById(user1.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findById(user2.getUsername())).thenReturn(Optional.of(user2));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());
        when(addressRepository.findById(2L)).thenReturn(Optional.of(address));

        assertThrows(WebOrderNotFoundException.class,
                () -> webOrderService.getWebOrderById(webOrder1.getId()));
        assertThrows(WebOrderNotFoundException.class,
                () -> webOrderService.getWebOrderById(webOrder1.getId(), user1.getUsername()));
        assertThrows(UserNotFoundException.class,
                () -> webOrderService.getWebOrderById(webOrder2.getId(), user1.getUsername()));
        assertThrows(NotAuthorisedException.class,
                () -> webOrderService.getWebOrderById(webOrder2.getId(), user2.getUsername()));
        assertThrows(ProductNotFoundException.class,
                () -> webOrderService.createWebOrder(webOrderDto, user2.getUsername()));
        webOrderDetailInputDto.productId = 1L;
        assertThrows(ProductNotFoundException.class,
                () -> webOrderService.addWebOrderDetailToWebOrder(webOrderDetailInputDto));
        webOrderDetailInputDto.productId = 2L;
        assertThrows(WebOrderNotFoundException.class,
                () -> webOrderService.addWebOrderDetailToWebOrder(webOrderDetailInputDto));
        webOrderDetailInputDto.webOrderId = webOrder2.getId();
        webOrderDetailInputDto.quantity = -1;
        assertThrows(WebOrderDetailException.class,
                () -> webOrderService.addWebOrderDetailToWebOrder(webOrderDetailInputDto));
        assertThrows(WebOrderNotFoundException.class,
                () -> webOrderService.confirmOrder(webOrder1.getId(), 1L, user1.getUsername()));
        assertThrows(AddressNotFoundException.class,
                () -> webOrderService.confirmOrder(webOrder2.getId(), 1L, user1.getUsername()));
        assertThrows(AddressNotFoundException.class,
                () -> webOrderService.confirmOrder(webOrder2.getId(), 2L, user1.getUsername()));
        assertThrows(WebOrderNotFoundException.class,
                () -> webOrderService.changeOrderStatus(webOrder1.getId(), "DELIVERING"));
        assertThrows(WebOrderStatusException.class,
                () -> webOrderService.changeOrderStatus(webOrder2.getId(), ""));
        assertThrows(WebOrderNotFoundException.class,
                () -> webOrderService.deleteWebOrder(webOrder1.getId()));

        product1.setAvailability(false);
        product2.setAvailability(false);
        webOrderDto.productId = 2L;

        assertThrows(ProductUnavailableException.class,
                () -> webOrderService.createWebOrder(webOrderDto, user2.getUsername()));
        assertThrows(ProductUnavailableException.class,
                () -> webOrderService.addWebOrderDetailToWebOrder(webOrderDetailInputDto));
    }
}