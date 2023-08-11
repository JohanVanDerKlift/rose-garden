package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.dto.WebOrderDetailDto;
import nl.johanvanderklift.roseGarden.dto.WebOrderInputDto;
import nl.johanvanderklift.roseGarden.exception.AddressNotFoundException;
import nl.johanvanderklift.roseGarden.exception.ProductNotFoundException;
import nl.johanvanderklift.roseGarden.exception.WebOrderNotFoundException;
import nl.johanvanderklift.roseGarden.model.*;
import nl.johanvanderklift.roseGarden.repository.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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

    public Long createWebOrder(WebOrderInputDto dto, UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findById(username).orElseThrow();
        Optional<Address> opAddress = addressRepository.findById(dto.addressId);
        if (opAddress.isEmpty()) {
            throw new AddressNotFoundException("Address was not found in the database.");
        } else {
            Address address = opAddress.get();
            if (!addressRepository.findByUser_UsernameOrderByIdAsc(username).contains(address)) {
                throw new AddressNotFoundException("Address does not belong to current user.");
            } else {
                Optional<Product> opProduct = productRepository.findById(dto.productId);
                Product product = opProduct.get();
                WebOrder webOrder = new WebOrder();
                webOrder.setWebOrderStatus(WebOrderStatus.PENDING);
                webOrder.setUser(user);
                webOrder.setAddress(address);
                webOrderRepository.save(webOrder);
                WebOrderDetail webOrderDetail = new WebOrderDetail();
                webOrderDetail.setQuantity(dto.quantity);
                webOrderDetail.setProduct(product);
                webOrderDetail.setWebOrder(webOrder);
                webOrderDetailRepository.save(webOrderDetail);
                return webOrder.getId();
            }
        }
    }

    public Long addWebOrderDetailToWebOrder(WebOrderDetailDto dto) {
        Optional<Product> opProduct = productRepository.findById(dto.productId);
        Optional<WebOrder> opWebOrder = webOrderRepository.findById(dto.webOrderId);
        if (opProduct.isEmpty()) {
            throw new ProductNotFoundException(dto.productId);
        } else if (opWebOrder.isEmpty()) {
            throw new WebOrderNotFoundException(dto.webOrderId);
        } else {
            WebOrderDetail webOrderDetail = new WebOrderDetail();
            Product product = opProduct.get();
            WebOrder webOrder = opWebOrder.get();
            webOrderDetail.setQuantity(dto.quantity);
            webOrderDetail.setProduct(product);
            webOrderDetail.setWebOrder(webOrder);
            webOrderDetailRepository.save(webOrderDetail);
            return webOrderDetail.getId();
        }
    }
}
