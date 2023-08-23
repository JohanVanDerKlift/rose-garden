package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.dto.WebOrderDetailInputDto;
import nl.johanvanderklift.roseGarden.dto.WebOrderInputDto;
import nl.johanvanderklift.roseGarden.dto.WebOrderOutputDto;
import nl.johanvanderklift.roseGarden.exception.*;
import nl.johanvanderklift.roseGarden.model.*;
import nl.johanvanderklift.roseGarden.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WebOrderService {

    private final WebOrderRepository webOrderRepository;
    private final WebOrderDetailRepository webOrderDetailRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    public WebOrderService(WebOrderRepository webOrderRepository, WebOrderDetailRepository webOrderDetailRepository, UserRepository userRepository, AddressRepository addressRepository, ProductRepository productRepository) {
        this.webOrderRepository = webOrderRepository;
        this.webOrderDetailRepository = webOrderDetailRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
    }

    public List<WebOrderOutputDto> getAllWebOrdersByUsername(String username) {
        List<WebOrder> webOrders = webOrderRepository.findByUser_Username(username);
        List<WebOrderOutputDto> dtos = new ArrayList<>();
        for (WebOrder webOrder : webOrders) {
            dtos.add(transferWebOrderToDto(webOrder));
        }
        return dtos;
    }

    public WebOrderOutputDto getWebOrderById(String webOrderId) {
        Optional<WebOrder> opWebOrder = webOrderRepository.findById(webOrderId);
        if (opWebOrder.isEmpty()) {
            throw new WebOrderNotFoundException(webOrderId);
        } else {
            return transferWebOrderToDto(opWebOrder.get());
        }
    }

    public WebOrderOutputDto getWebOrderById(String webOrderId, String username) {
        Optional<WebOrder> opWebOrder = webOrderRepository.findById(webOrderId);
        Optional<User> opUser = userRepository.findById(username);
        if (opWebOrder.isEmpty()) {
            throw new WebOrderNotFoundException(webOrderId);
        } else if (opUser.isEmpty()) {
            throw new UserNotFoundException(username);
        } else {
            WebOrder webOrder = opWebOrder.get();
            User user = opUser.get();
            if (user.getWebOrders().contains(webOrder)) {
                return transferWebOrderToDto(webOrder);
            } else {
                throw new NotAuthorisedException("You are not authorised to see this order.");
            }
        }
    }

    public String createWebOrder(WebOrderInputDto dto, String username) {
        User user = userRepository.findById(username).orElseThrow();
        Optional<Product> opProduct = productRepository.findById(dto.productId);
        if (opProduct.isEmpty()) {
            throw new ProductNotFoundException(dto.productId);
        } else {
            Product product = opProduct.get();
            WebOrder webOrder = new WebOrder();
            webOrder.setWebOrderStatus(WebOrderStatus.PENDING);
            webOrder.setUser(user);
            WebOrder newWebOrder = webOrderRepository.save(webOrder);
            WebOrderDetail webOrderDetail = new WebOrderDetail();
            webOrderDetail.setQuantity(dto.quantity);
            webOrderDetail.setProduct(product);
            webOrderDetail.setWebOrder(newWebOrder);
            webOrderDetailRepository.save(webOrderDetail);
            return newWebOrder.getId();
        }
    }

    public Long addWebOrderDetailToWebOrder(WebOrderDetailInputDto dto) {
        Optional<Product> opProduct = productRepository.findById(dto.productId);
        Optional<WebOrder> opWebOrder = webOrderRepository.findById(dto.webOrderId);
        if (opProduct.isEmpty()) {
            throw new ProductNotFoundException(dto.productId);
        } else if (opWebOrder.isEmpty()) {
            throw new WebOrderNotFoundException(dto.webOrderId);
        } else {
            WebOrder webOrder = opWebOrder.get();
            Product product = opProduct.get();
            List<WebOrderDetail> webOrderDetails = webOrder.getWebOrderDetails();
            for (WebOrderDetail webOrderDetail : webOrderDetails) {
                if (webOrderDetail.getProduct() == product) {
                    if (webOrderDetail.getQuantity() + dto.quantity <= 0){
                        webOrderDetailRepository.delete(webOrderDetail);
                        return 0L;
                    } else {
                        webOrderDetail.setQuantity(webOrderDetail.getQuantity() + dto.quantity);
                        WebOrderDetail newWebOrderDetail = webOrderDetailRepository.save(webOrderDetail);
                        return newWebOrderDetail.getId();
                    }
                }
            }
            if (dto.quantity > 0) {
                WebOrderDetail webOrderDetail = new WebOrderDetail();
                webOrderDetail.setQuantity(dto.quantity);
                webOrderDetail.setProduct(product);
                webOrderDetail.setWebOrder(webOrder);
                WebOrderDetail newWebOrderDetail = webOrderDetailRepository.save(webOrderDetail);
                return newWebOrderDetail.getId();
            }
        }
        throw new WebOrderDetailException("WebOrderDetail was not saved.");
    }

    public String confirmOrder(String webOrderId, Long addressId, String username) {
        Optional<WebOrder> opWebOrder = webOrderRepository.findById(webOrderId);
        Optional<Address> opAddress = addressRepository.findById(addressId);
         if (opWebOrder.isEmpty()) {
            throw new WebOrderNotFoundException(webOrderId);
         } else if (opAddress.isEmpty()) {
             throw new AddressNotFoundException("Address with id " + addressId + " was not found");
         } else {
             Address address = opAddress.get();
             if (!addressRepository.findByUser_UsernameOrderByIdAsc(username).contains(address)) {
                 throw new AddressNotFoundException("Address does not belong to current user.");
             }
             WebOrder webOrder = opWebOrder.get();
             webOrder.setAddress(address);
             webOrder.setOrderDateTime(LocalDateTime.now());
             webOrder.setWebOrderStatus(WebOrderStatus.RECEIVED);
             WebOrder newWebOrder = webOrderRepository.save(webOrder);
             return newWebOrder.getId();
         }
    }

    public String changeOrderStatus(String webOrderId, String status) {
        Optional<WebOrder> opWebOrder = webOrderRepository.findById(webOrderId);
        if (opWebOrder.isEmpty()) {
            throw new WebOrderNotFoundException(webOrderId);
        } else {
            WebOrder webOrder = opWebOrder.get();
            if (Objects.equals(status.toUpperCase(), "DELIVERING")) {
                webOrder.setWebOrderStatus(WebOrderStatus.DELIVERING);
            } else if (Objects.equals(status.toUpperCase(), "PROCESSED")) {
                webOrder.setWebOrderStatus(WebOrderStatus.PROCESSED);
            } else {
                throw new WebOrderStatusException("Invalid status.");
            }
           webOrderRepository.save(webOrder);
            return webOrder.getWebOrderStatus().toString();
        }
    }

    public void deleteWebOrder(String id) {
        Optional<WebOrder> opWebOrder = webOrderRepository.findById(id);
        if (opWebOrder.isEmpty()) {
            throw new WebOrderNotFoundException(id);
        } else {
            webOrderRepository.delete(opWebOrder.get());
        }
    }

    private WebOrderOutputDto transferWebOrderToDto(WebOrder webOrder) {
        WebOrderOutputDto dto = new WebOrderOutputDto();
        dto.id = webOrder.getId();
        dto.webOrderStatus = webOrder.getWebOrderStatus().toString();
        dto.address = webOrder.getAddress();
        dto.webOrderDetails = webOrder.getWebOrderDetails();
        dto.tax = webOrder.getTax();
        dto.totalPrice = webOrder.getTotalPrice();
        dto.totalPriceExTax = webOrder.getTotalPriceExTax();
        return dto;
    }

}
