package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.dto.ProductInputDto;
import nl.johanvanderklift.roseGarden.dto.ProductOutputDto;
import nl.johanvanderklift.roseGarden.exception.ProductNotFoundException;
import nl.johanvanderklift.roseGarden.model.Product;
import nl.johanvanderklift.roseGarden.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;
    @Mock
    ProductRepository productRepository;

    Product product1;
    Product product2;
    Product productUpdated;
    ProductInputDto dto = new ProductInputDto();

    @BeforeEach
    void setUp() {
        product1 = new Product(1L, 15.25, "Test product", "Description of test product", true, 21.0, null);
        product2 = new Product(2L, 17.25, "Test product2", "Description of test product2", false, 9.0, null);
        productUpdated = new Product(1L, 16.25, "Test product updated", "Description of test product updated", true, 9.0, null);

        dto.name = "Test Product updated";
        dto.description = "Description of test product updated";
        dto.price = 16.25;
        dto.tax = 9.0;
    }

    @Test
    @DisplayName("Should return list of correct products")
    void getAllProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        // Act
        List<ProductOutputDto> products = productService.getAllProducts();

        // Assert
        assertEquals("Test product", products.get(0).name);
        assertEquals("Description of test product", products.get(0).description);
        assertEquals(15.25, products.get(0).price);
        assertTrue(products.get(0).availability);
        assertEquals(21.0, products.get(0).tax);

        assertEquals("Test product2", products.get(1).name);
        assertEquals("Description of test product2", products.get(1).description);
        assertEquals(17.25, products.get(1).price);
        assertFalse(products.get(1).availability);
        assertEquals(9.0, products.get(1).tax);
    }

    @Test
    @DisplayName("Should return correct product by id")
    void getProductById() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        ProductOutputDto product = productService.getProductById(1L);

        // Assert
        assertEquals("Test product", product.name);
        assertEquals("Description of test product", product.description);
        assertEquals(15.25, product.price);
        assertTrue(product.availability);
        assertEquals(21.0, product.tax);

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(3L));
    }

    @Test
    @DisplayName("Should return correct product by name contains")
    void getProductByNameContains() {
        // Arrange
        when(productRepository.getByNameContainsIgnoreCase("product")).thenReturn(List.of(product1, product2));
        when(productRepository.getByNameContainsIgnoreCase("notExists")).thenReturn(List.of());

        // Act
        List<ProductOutputDto> products = productService.getProductByNameContains("product");
        List<ProductOutputDto> emptyList = productService.getProductByNameContains("notExists");

        // Assert
        assertEquals("Test product", products.get(0).name);
        assertEquals("Description of test product", products.get(0).description);
        assertEquals(15.25, products.get(0).price);
        assertTrue(products.get(0).availability);
        assertEquals(21.0, products.get(0).tax);

        assertEquals("Test product2", products.get(1).name);
        assertEquals("Description of test product2", products.get(1).description);
        assertEquals(17.25, products.get(1).price);
        assertFalse(products.get(1).availability);
        assertEquals(9.0, products.get(1).tax);

        assertEquals(0, emptyList.size());
    }

    @Test
    @DisplayName("Should save product and return new id")
    void saveProduct() {
        // Arrange
        ProductInputDto dto = new ProductInputDto();
        dto.name = "Test Product";
        dto.description = "Description of test product";
        dto.price = 15.25;
        dto.tax = 21.0;

        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // Act
        Long newId = productService.saveProduct(dto);

        // Assert
        assertEquals(product1.getId(), newId);

    }

    @Test
    @DisplayName("Should return updated product")
    void updateProduct() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(productUpdated);

        // Act
        ProductOutputDto productOutputDto = productService.updateProduct(dto, 1L);

        // Assert
        assertEquals(productOutputDto.name, productUpdated.getName());
        assertEquals(productOutputDto.price, productUpdated.getPrice());
        assertEquals(productOutputDto.description, productUpdated.getDescription());
        assertEquals(productOutputDto.tax, productUpdated.getTax());

    }

    @Test
    @DisplayName("Should toggle and return availability")
    void toggleAvailability() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product1));

        // Act
        ProductOutputDto dto = productService.toggleAvailability(1L);

        // Assert
        assertFalse(dto.availability);
    }

    @Test
    @DisplayName("Should delete product by id")
    void deleteProduct() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product1));

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository).delete(product1);
    }

    @Test
    @DisplayName("shouldThrowProductNotFoundException")
    void shouldThrowProductNotFoundException() {
        when(productRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(3L));
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(dto, 3L));
        assertThrows(ProductNotFoundException.class, () -> productService.toggleAvailability(3L));
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(3L));

    }
}