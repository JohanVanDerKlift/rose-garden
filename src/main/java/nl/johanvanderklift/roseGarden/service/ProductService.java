package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.dto.ProductInputDto;
import nl.johanvanderklift.roseGarden.dto.ProductOutputDto;
import nl.johanvanderklift.roseGarden.exception.ProductNotFoundException;
import nl.johanvanderklift.roseGarden.exception.WebOrderNotFoundException;
import nl.johanvanderklift.roseGarden.model.Product;
import nl.johanvanderklift.roseGarden.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public ProductOutputDto getProductById(Long id) {
        Optional<Product> opProduct = productRepository.findById(id);
        if (opProduct.isEmpty()) {
            throw new ProductNotFoundException(id);
        } else {
            Product product = opProduct.get();
            return transferProductToDto(product);
        }
    }

    public List<ProductOutputDto> getProductByNameContains(String name) {
        List<Product> products= productRepository.getByNameContainsIgnoreCase(name);
        List<ProductOutputDto> dtoList= new ArrayList<>();
        for (Product product : products) {
            dtoList.add(transferProductToDto(product));
        }
        return dtoList;
    }

    public Long saveProduct(ProductInputDto dto) {
        Product newProduct = new Product();
        Product product = productRepository.save(tranferDtoToProduct(dto, newProduct));
        return product.getId();
    }

    public ProductOutputDto updateProduct(ProductInputDto dto, Long id) {
        Optional<Product> opProduct= productRepository.findById(id);
        if (opProduct.isEmpty()) {
            throw new ProductNotFoundException(id);
        } else {
            Product product = opProduct.get();
            Product newProduct = productRepository.save(tranferDtoToProduct(dto, product));
            return transferProductToDto(newProduct);
        }
    }

    public ProductOutputDto toggleAvailability(Long id) {
        Optional<Product> opProduct= productRepository.findById(id);
        if (opProduct.isEmpty()) {
            throw new ProductNotFoundException(id);
        } else {
            Product product = opProduct.get();
            product.setAvailability(!product.getAvailability());
            productRepository.save(product);
            return transferProductToDto(product);
        }
    }

    public void deleteProduct(Long id) {
        Optional<Product> opProduct = productRepository.findById(id);
        if (opProduct.isEmpty()) {
            throw new ProductNotFoundException(id);
        } else {
            productRepository.delete(opProduct.get());
        }
    }

    public ProductOutputDto transferProductToDto(Product product) {
        ProductOutputDto dto = new ProductOutputDto();
        dto.id = product.getId();
        dto.name = product.getName();
        dto.price = product.getPrice();
        dto.availability = product.getAvailability();
        dto.description = product.getDescription();
        dto.tax = product.getTax();
        return dto;
    }

    public Product tranferDtoToProduct(ProductInputDto dto, Product product) {
        product.setName(dto.name);
        product.setDescription(dto.description);
        product.setPrice(dto.price);
        product.setTax(dto.tax);
        product.setAvailability(true);
        return product;
    }

}
