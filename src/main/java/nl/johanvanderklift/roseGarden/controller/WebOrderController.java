package nl.johanvanderklift.roseGarden.controller;

import nl.johanvanderklift.roseGarden.dto.WebOrderDetailDto;
import nl.johanvanderklift.roseGarden.dto.WebOrderInputDto;
import nl.johanvanderklift.roseGarden.service.WebOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weborder")
public class WebOrderController {

    private final WebOrderService webOrderService;

    public WebOrderController(WebOrderService webOrderService) {
        this.webOrderService = webOrderService;
    }

    @PostMapping
    public ResponseEntity<Long> createWebOrder(@RequestBody WebOrderInputDto dto, @AuthenticationPrincipal UserDetails user) {
        Long newId = webOrderService.createWebOrder(dto, user);
        return ResponseEntity.ok(newId);
    }

    @PostMapping("/add")
    public ResponseEntity<Long> addWebOrderDetailToWebOrder(@RequestBody WebOrderDetailDto dto) {
        Long newId = webOrderService.addWebOrderDetailToWebOrder(dto);
        return ResponseEntity.ok(newId);
    }
}
