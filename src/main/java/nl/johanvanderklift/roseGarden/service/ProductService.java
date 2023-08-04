package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.dto.ProductOutputDto;
import nl.johanvanderklift.roseGarden.entity.Product;
import nl.johanvanderklift.roseGarden.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductOutputDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductOutputDto> outputDtos = new ArrayList<>();
        for (Product product : products) {
            outputDtos.add(transferProductToDto(product));
        }
        return outputDtos;
    }

    private ProductOutputDto transferProductToDto(Product product) {
        ProductOutputDto dto = new ProductOutputDto();
        dto.id = product.getId();
        dto.name = product.getName();
        dto.price = product.getPrice();
        dto.tax = product.getTax();
        dto.description = product.getDescription();
        dto.tax = product.getTax();
        return dto;
    }
}
