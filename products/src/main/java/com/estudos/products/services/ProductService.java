package com.estudos.products.services;

import java.util.List;
import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.estudos.products.dto.ProductDTO;
import com.estudos.products.entities.Category;
import com.estudos.products.entities.Product;
import com.estudos.products.infra.s3.dtos.UploadObjectDTO;
import com.estudos.products.infra.s3.service.S3Service;
import com.estudos.products.repositories.CategoryRepository;
import com.estudos.products.repositories.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${aws.cloudfront.url}")
    private String cloudFrontUrl;

    public List<ProductDTO> getAllProducts() {

        List<ProductDTO> list = productRepository.findAll()
                .stream()
                .map(p -> ProductDTO.builder()
                        .title(p.getTitle())
                        .description(p.getDescription())
                        .price(p.getPrice())
                        .build())
                .toList();

        return list;
    }

    public ProductDTO getProductById(String stringID) {
        UUID productUUID = UUID.fromString(stringID);

        ProductDTO product = productRepository.findById(productUUID)
                .map(p -> ProductDTO.builder()
                        .title(p.getTitle())
                        .description(p.getDescription())
                        .price(p.getPrice())
                        .build())
                .get();

        return product;
    }

    public String postProduct(String productString, MultipartFile file)
            throws JsonMappingException, JsonProcessingException {
        UUID productUUID = UUID.randomUUID();
        String fileType = s3Service.getFileType(file.getOriginalFilename());

        productRepository.save(buildProduct(productString, productUUID, fileType));
        uploadFile(productUUID, file);

        return "";
    }

    public void deleteProduct(String stringID) {
        UUID uuid = UUID.fromString(stringID);
        productRepository.deleteById(uuid);
    }

    public Product buildProduct(String productString, UUID productUUID, String fileType)
            throws JsonMappingException, JsonProcessingException {
        ProductDTO dto = objectMapper.readValue(productString, ProductDTO.class);

        List<Category> categories = categoryRepository.findAllById(dto.categoriesIdList());
        String productId = productUUID.toString();

        Product product = Product.builder()
                .productId(productUUID)
                .title(dto.title())
                .description(dto.description())
                .categories(new HashSet<Category>(categories))
                .price(dto.price())
                .imageUrl(cloudFrontUrl + "/" + productId + fileType)
                .build();

        return product;
    }

    public void uploadFile(UUID productUUID, MultipartFile file) {
        String productId = productUUID.toString();

        UploadObjectDTO uploadObjectDTO = UploadObjectDTO.builder().id(productId).file(file)
                .build();

        try {
            s3Service.uploadFile(uploadObjectDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
