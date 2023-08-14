package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.dto.WebOrderOutputDto;
import nl.johanvanderklift.roseGarden.model.*;
import nl.johanvanderklift.roseGarden.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
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
    @InjectMocks
    WebOrderService webOrderService;

    WebOrder webOrder1 = new WebOrder();
    WebOrder webOrder2 = new WebOrder();
    Address address = new Address();
    User user = new User();
    WebOrderDetail webOrderDetail1 = new WebOrderDetail();
    WebOrderDetail webOrderDetail2 = new WebOrderDetail();
    Product product1 = new Product();
    Product product2 = new Product();

    @BeforeEach
    void setUp() {
        product1.setName("Test product 1");
        product1.setPrice(10.0);

        product2.setName("Test product 2");
        product2.setPrice(20.0);

        user.setUsername("Testusername");
        user.setEmail("test@test.nl");
        user.setPassword("Password1");

        address.setAddressLine1("Teststreet");
        address.setCity("Testerton");
        address.setUser(user);

        webOrder1.setUser(user);
        webOrder1.setAddress(address);
        webOrder1.setId("2023-ORD-00001");
        webOrder1.setWebOrderStatus(WebOrderStatus.PENDING);
        webOrder2.setUser(user);
        webOrder2.setAddress(address);
        webOrder1.setId("2023-ORD-00002");
        webOrder2.setWebOrderStatus(WebOrderStatus.PROCESSED);

        webOrderDetail1.setProduct(product1);
        webOrderDetail1.setQuantity(5);
        webOrderDetail1.setWebOrder(webOrder1);

        webOrderDetail2.setProduct(product2);
        webOrderDetail2.setQuantity(10);
        webOrderDetail2.setWebOrder(webOrder2);
    }

    @Test
    void getAllWebOrdersByUsername() {
        when(webOrderRepository.findByUser_Username(anyString())).thenReturn(List.of(webOrder1, webOrder2));

        List<WebOrderOutputDto> dtos = webOrderService.getAllWebOrdersByUsername("Testusername");

        assertEquals(dtos.get(0).id, webOrder1.getId());
        assertEquals(dtos.get(0).webOrderStatus, webOrder1.getWebOrderStatus().toString());
        assertEquals(10.5, dtos.get(0).tax);

        assertEquals(dtos.get(1).id, webOrder2.getId());
        assertEquals(dtos.get(1).webOrderStatus, webOrder2.getWebOrderStatus().toString());
    }

    @Test
    void getWebOrderById() {
    }

    @Test
    void testGetWebOrderById() {
    }

    @Test
    void createWebOrder() {
    }

    @Test
    void addWebOrderDetailToWebOrder() {
    }

    @Test
    void confirmOrder() {
    }

    @Test
    void changeOrderStatus() {
    }

    @Test
    void deleteWebOrder() {
    }
}