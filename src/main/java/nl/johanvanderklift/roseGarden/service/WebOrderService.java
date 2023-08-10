package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.exception.AddressNotFoundException;
import nl.johanvanderklift.roseGarden.model.*;
import nl.johanvanderklift.roseGarden.repository.AddressRepository;
import nl.johanvanderklift.roseGarden.repository.UserRepository;
import nl.johanvanderklift.roseGarden.repository.WebOrderRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WebOrderService {

    private final WebOrderRepository webOrderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public WebOrderService(WebOrderRepository webOrderRepository, UserRepository userRepository, AddressRepository addressRepository) {
        this.webOrderRepository = webOrderRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    public Long createWebOrder(List<WebOrderDetail> webOrderDetailList, Long addressId, UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findById(username).orElseThrow();
        Optional<Address> opAddress = addressRepository.findById(addressId);
        if (opAddress.isEmpty()) {
            throw new AddressNotFoundException("Address was not found in the database.");
        } else {
            Address address = opAddress.get();
            if (!addressRepository.findByUser_UsernameOrderByIdAsc(username).contains(address)) {
                throw new AddressNotFoundException("Address does not belong to current user.");
            } else {
                WebOrder webOrder = new WebOrder();
                webOrder.setWebOrderStatus(WebOrderStatus.PENDING);
                webOrder.setUser(user);
                webOrder.setAddress(address);
                webOrder.setWebOrderDetails(webOrderDetailList);
                webOrderRepository.save(webOrder);
                return webOrder.getId();
            }
        }
    }
}
