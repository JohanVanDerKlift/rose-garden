package nl.johanvanderklift.roseGarden.controller;

import jakarta.validation.Valid;
import nl.johanvanderklift.roseGarden.dto.ProductInputDto;
import nl.johanvanderklift.roseGarden.dto.ProductOutputDto;
import nl.johanvanderklift.roseGarden.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductOutputDto>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductOutputDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductOutputDto>> getProductByNameContains(@RequestParam String name) {
        return ResponseEntity.ok(productService.getProductByNameContains(name));
    }

    @PostMapping
    public ResponseEntity<Long> saveProduct(@Valid @RequestBody ProductInputDto dto) {
        return ResponseEntity.ok(productService.saveProduct(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductOutputDto> updateProduct(@RequestBody ProductInputDto dto, @PathVariable Long id) {
        ProductOutputDto outputDto = productService.updateProduct(dto, id);
        return ResponseEntity.ok(outputDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductOutputDto> toggleAvailability(@PathVariable Long id) {
        ProductOutputDto outputDto = productService.toggleAvailability(id);
        return ResponseEntity.ok(outputDto);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
