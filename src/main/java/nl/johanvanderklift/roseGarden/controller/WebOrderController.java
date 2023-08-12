package nl.johanvanderklift.roseGarden.controller;

import nl.johanvanderklift.roseGarden.dto.WebOrderDetailInputDto;
import nl.johanvanderklift.roseGarden.dto.WebOrderInputDto;
import nl.johanvanderklift.roseGarden.dto.WebOrderOutputDto;
import nl.johanvanderklift.roseGarden.service.WebOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weborder")
public class WebOrderController {

    private final WebOrderService webOrderService;

    public WebOrderController(WebOrderService webOrderService) {
        this.webOrderService = webOrderService;
    }

    @GetMapping
    public ResponseEntity<List<WebOrderOutputDto>> getAllWebOrdersByUsername(@AuthenticationPrincipal UserDetails user) {
        List<WebOrderOutputDto> dtos = webOrderService.getAllWebOrdersByUsername(user);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<WebOrderOutputDto>> getAllWebOrdersByUsername(@RequestParam String username) {
        List<WebOrderOutputDto> dtos = webOrderService.getAllWebOrdersByUsername(username);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{webOrderId}")
    public ResponseEntity<WebOrderOutputDto> getWebOrderById(@PathVariable Long webOrderId, @AuthenticationPrincipal UserDetails user) {
        WebOrderOutputDto dto = webOrderService.getWebOrderById(webOrderId, user);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/admin/{webOrderId}")
    public ResponseEntity<WebOrderOutputDto> getWebOrderById(@PathVariable Long webOrderId) {
        WebOrderOutputDto dto = webOrderService.getWebOrderById(webOrderId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Long> createWebOrder(@RequestBody WebOrderInputDto dto, @AuthenticationPrincipal UserDetails user) {
        Long newId = webOrderService.createWebOrder(dto, user);
        return ResponseEntity.ok(newId);
    }

    @PutMapping
    public ResponseEntity<Long> addWebOrderDetailToWebOrder(@RequestBody WebOrderDetailInputDto dto) {
        Long newId = webOrderService.addWebOrderDetailToWebOrder(dto);
        return ResponseEntity.ok(newId);
    }

    @PatchMapping("/{webOrderId}/address/{addressId}")
    public ResponseEntity<Long> confirmWebOrder(@PathVariable Long webOrderId, @PathVariable Long addressId, @AuthenticationPrincipal UserDetails user) {
        Long newId = webOrderService.confirmOrder(webOrderId, addressId, user);
        return ResponseEntity.ok(newId);
    }

    @PatchMapping("/{webOrderId}")
    public ResponseEntity<String> changeWebOrderStatus(@PathVariable Long webOrderId, @RequestParam String webOrderStatus) {
        String newWebOrderStatus = webOrderService.changeOrderStatus(webOrderId, webOrderStatus);
        return ResponseEntity.ok(newWebOrderStatus);
    }

    @DeleteMapping("/{webOrderId}")
    public ResponseEntity deleteWebOrder(@PathVariable Long id) {
        webOrderService.deleteWebOrder(id);
        return ResponseEntity.ok().build();
    }

}
