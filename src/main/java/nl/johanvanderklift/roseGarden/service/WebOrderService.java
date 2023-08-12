package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.dto.WebOrderDetailInputDto;
import nl.johanvanderklift.roseGarden.dto.WebOrderInputDto;
import nl.johanvanderklift.roseGarden.dto.WebOrderOutputDto;
import nl.johanvanderklift.roseGarden.exception.*;
import nl.johanvanderklift.roseGarden.model.*;
import nl.johanvanderklift.roseGarden.repository.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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

    public List<WebOrderOutputDto> getAllWebOrdersByUsername(UserDetails userDetails) {
        List<WebOrder> webOrders = webOrderRepository.findByUser_Username(userDetails.getUsername());
        List<WebOrderOutputDto> dtos = new ArrayList<>();
        for (WebOrder webOrder : webOrders) {
            dtos.add(transferWebOrderToDto(webOrder));
        }
        return dtos;
    }

    public List<WebOrderOutputDto> getAllWebOrdersByUsername(String username) {
        List<WebOrder> webOrders = webOrderRepository.findByUser_Username(username);
        List<WebOrderOutputDto> dtos = new ArrayList<>();
        for (WebOrder webOrder : webOrders) {
            dtos.add(transferWebOrderToDto(webOrder));
        }
        return dtos;
    }

    public WebOrderOutputDto getWebOrderById(Long webOrderId) {
        Optional<WebOrder> opWebOrder = webOrderRepository.findById(webOrderId);
        if (opWebOrder.isEmpty()) {
            throw new WebOrderNotFoundException(webOrderId);
        } else {
            return transferWebOrderToDto(opWebOrder.get());
        }
    }

    public WebOrderOutputDto getWebOrderById(Long webOrderId, UserDetails userDetails) {
        Optional<WebOrder> opWebOrder = webOrderRepository.findById(webOrderId);
        Optional<User> opUser = userRepository.findById(userDetails.getUsername());
        if (opWebOrder.isEmpty()) {
            throw new WebOrderNotFoundException(webOrderId);
        } else if (opUser.isEmpty()) {
            throw new UserNotFoundException(userDetails.getUsername());
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

    public Long createWebOrder(WebOrderInputDto dto, UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findById(username).orElseThrow();
        Optional<Product> opProduct = productRepository.findById(dto.productId);
        if (opProduct.isEmpty()) {
            throw new ProductNotFoundException(dto.productId);
        } else {
            Product product = opProduct.get();
            WebOrder webOrder = new WebOrder();
            webOrder.setWebOrderStatus(WebOrderStatus.PENDING);
            webOrder.setUser(user);
            webOrderRepository.save(webOrder);
            WebOrderDetail webOrderDetail = new WebOrderDetail();
            webOrderDetail.setQuantity(dto.quantity);
            webOrderDetail.setProduct(product);
            webOrderDetail.setWebOrder(webOrder);
            webOrderDetailRepository.save(webOrderDetail);
            return webOrder.getId();
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
            boolean isPresent = false;
            for (WebOrderDetail webOrderDetail : webOrderDetails) {
                if (webOrderDetail.getProduct() == product) {
                    isPresent = true;
                    if (webOrderDetail.getQuantity() + dto.quantity <= 0){
                        webOrderDetailRepository.delete(webOrderDetail);
                    } else {
                        webOrderDetail.setQuantity(webOrderDetail.getQuantity() + dto.quantity);
                        webOrderDetailRepository.save(webOrderDetail);
                        return webOrderDetail.getId();
                    }
                }
            }
            if (!isPresent && dto.quantity < 0) {
                WebOrderDetail webOrderDetail = new WebOrderDetail();
                webOrderDetail.setQuantity(dto.quantity);
                webOrderDetail.setProduct(product);
                webOrderDetail.setWebOrder(webOrder);
                webOrderDetailRepository.save(webOrderDetail);
                return webOrderDetail.getId();
            }
        }
        return 0L;
    }

    public Long confirmOrder(Long webOrderId, Long addressId, UserDetails userDetails) {
        Optional<WebOrder> opWebOrder = webOrderRepository.findById(webOrderId);
        Optional<Address> opAddress = addressRepository.findById(addressId);
         if (opWebOrder.isEmpty()) {
            throw new WebOrderNotFoundException(webOrderId);
         } else if (opAddress.isEmpty()) {
             throw new AddressNotFoundException("Address with id " + addressId + " was not found");
         } else {
             Address address = opAddress.get();
             if (!addressRepository.findByUser_UsernameOrderByIdAsc(userDetails.getUsername()).contains(address)) {
                 throw new AddressNotFoundException("Address does not belong to current user.");
             }
             WebOrder webOrder = opWebOrder.get();
             webOrder.setAddress(address);
             webOrder.setWebOrderStatus(WebOrderStatus.RECEIVED);
             webOrderRepository.save(webOrder);
             return webOrder.getId();
         }
    }

    public String changeOrderStatus(Long webOrderId, String status) {
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

    public void deleteWebOrder(Long id) {
        webOrderRepository.deleteById(id);
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
