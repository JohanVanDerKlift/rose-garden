package nl.johanvanderklift.roseGarden.controller;

import nl.johanvanderklift.roseGarden.dto.WebOrderDetailInputDto;
import nl.johanvanderklift.roseGarden.dto.WebOrderInputDto;
import nl.johanvanderklift.roseGarden.dto.WebOrderOutputDto;
import nl.johanvanderklift.roseGarden.service.WebOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/weborder")
public class WebOrderController {

    private final WebOrderService webOrderService;

    public WebOrderController(WebOrderService webOrderService) {
        this.webOrderService = webOrderService;
    }

    @GetMapping
    public ResponseEntity<List<WebOrderOutputDto>> getAllWebOrdersByUsername(@AuthenticationPrincipal UserDetails userDetails) {
        List<WebOrderOutputDto> dtos = webOrderService.getAllWebOrdersByUsername(userDetails.getUsername());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<WebOrderOutputDto>> getAllWebOrdersByUsername(@RequestParam String username) {
        List<WebOrderOutputDto> dtos = webOrderService.getAllWebOrdersByUsername(username);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{webOrderId}")
    public ResponseEntity<WebOrderOutputDto> getWebOrderById(@PathVariable String webOrderId, @AuthenticationPrincipal UserDetails user) {
        WebOrderOutputDto dto = webOrderService.getWebOrderById(webOrderId, user);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/admin/{webOrderId}")
    public ResponseEntity<WebOrderOutputDto> getWebOrderById(@PathVariable String webOrderId) {
        WebOrderOutputDto dto = webOrderService.getWebOrderById(webOrderId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<Object> createWebOrder(@RequestBody WebOrderInputDto dto, @AuthenticationPrincipal UserDetails user, BindingResult br) {
        if (br.hasFieldErrors()) {
            return getBindingResult(br);
        } else {
            String newId = webOrderService.createWebOrder(dto, user);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + newId).toUriString());
            return ResponseEntity.created(uri).body(newId);
        }
    }

    @PutMapping
    public ResponseEntity<Long> addWebOrderDetailToWebOrder(@RequestBody WebOrderDetailInputDto dto) {
        Long newId = webOrderService.addWebOrderDetailToWebOrder(dto);
        return ResponseEntity.ok(newId);
    }

    @PatchMapping("/{webOrderId}/address/{addressId}")
    public ResponseEntity<String> confirmWebOrder(@PathVariable String webOrderId, @PathVariable Long addressId, @AuthenticationPrincipal UserDetails user) {
        String newId = webOrderService.confirmOrder(webOrderId, addressId, user);
        return ResponseEntity.ok(newId);
    }

    @PatchMapping("/{webOrderId}")
    public ResponseEntity<String> changeWebOrderStatus(@PathVariable String webOrderId, @RequestParam String webOrderStatus) {
        String newWebOrderStatus = webOrderService.changeOrderStatus(webOrderId, webOrderStatus);
        return ResponseEntity.ok(newWebOrderStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteWebOrder(@PathVariable String id) {
        webOrderService.deleteWebOrder(id);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<Object> getBindingResult(BindingResult br) {
        StringBuilder sb = new StringBuilder();
        for (FieldError fe : br.getFieldErrors()) {
            sb.append(fe.getField()).append(": ").append(fe.getDefaultMessage()).append("\n");
        }
        return ResponseEntity.badRequest().body(sb.toString());
    }

}
