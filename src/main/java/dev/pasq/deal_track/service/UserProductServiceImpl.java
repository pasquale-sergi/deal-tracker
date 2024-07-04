package dev.pasq.deal_track.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.pasq.deal_track.config.HttpClientService;
import dev.pasq.deal_track.entity.Product;
import dev.pasq.deal_track.entity.User;
import dev.pasq.deal_track.entity.UserProduct;
import dev.pasq.deal_track.entity.UserProductId;
import dev.pasq.deal_track.repository.ProductRepository;
import dev.pasq.deal_track.repository.UserProductRepository;
import dev.pasq.deal_track.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor

public class UserProductServiceImpl implements UserProductService{

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private UserProductRepository userProductRepository;
    private HttpClientService clientService;
    @Override
    public void addProductToUser(Long userId, String productAsin) {
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User not found with id: "+userId));
        //if user found

        //check first if the product exist in the database already
        Product product = productRepository.findByAsin(productAsin).orElseGet(()->{
            //fetch the API
            CompletableFuture<String> apiResponseFuture = clientService.getProductDetails(productAsin);
            String apiResponse = apiResponseFuture.join();
            //System.out.println("Api response: "+apiResponse);
            Product newProduct = parseProductDetails(apiResponse, productAsin);
            return productRepository.save(newProduct);
        });

        boolean isProductTrackedByUser = userProductRepository.existsByUserIdAndProductAsin(userId, productAsin);
        if(isProductTrackedByUser){
            throw new IllegalArgumentException("Product already being tracked.Check your list.");
        }

        UserProductId userProductId = new UserProductId();
        userProductId.setProductAsin(productAsin);
        userProductId.setUserId(userId);

        UserProduct userProduct = new UserProduct();
        userProduct.setId(userProductId);
        userProduct.setUser(user);
        userProduct.setProduct(product);
        userProduct.setTrackingStatus("ACTIVE");
        userProduct.setLastPriceTracked(product.getPrice());

        userProductRepository.save(userProduct);
    }

    @Override
    public void deleteProductToUser(String productAsin) {
        Long userId = 1L;
        Product product = productRepository
                .findByAsin(productAsin)
                .orElseThrow(()->new IllegalArgumentException("Product not found with given ASIN: "+productAsin));

        User user = userRepository
                .findById(userId)
                .orElseThrow(()->new IllegalArgumentException("User not found with given id: "+userId));

        UserProductId userProductIdToDelete = new UserProductId(userId, productAsin);

        // Find the UserProduct entry to delete
        UserProduct userProductToDelete = userProductRepository.findById(userProductIdToDelete)
                .orElseThrow(() -> new IllegalArgumentException("UserProduct not found for userId: " + userId + " and productAsin: " + productAsin));

        // Delete the UserProduct
        userProductRepository.delete(userProductToDelete);
    }

    @Override
    public Product parseProductDetails(String apiResponse,String productAsin){

        try{
            ObjectMapper mapper = new ObjectMapper();
            //parse JSON response into JsonNode
            JsonNode rootNode = mapper.readTree(apiResponse);
            //System.out.println("Root node: "+rootNode);
            JsonNode dataNode = rootNode.get("data");
            //System.out.println("datanode: "+dataNode);

            //extract product details
            String productPrice = getStringFromJsonNode(dataNode, "product_price");
            String productName = getStringFromJsonNode(dataNode, "product_title");

            String productUrl =getStringFromJsonNode(dataNode, "product_url");
            if(productPrice==null) throw new IllegalArgumentException("Missing product price");
            if(productName==null) throw new IllegalArgumentException("Missing product name");

            if(productUrl==null) throw new IllegalArgumentException("Missing product url");



            //create product object
            Product product = new Product();
            product.setAsin(productAsin);
            product.setProduct_name(productName);
            product.setPrice(new BigDecimal(productPrice));
            product.setUrl(productUrl);

            return product;

        }catch(IOException e){
            throw new IllegalArgumentException("Error parsing the Api Response");
        }
    }

    private String getStringFromJsonNode(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        return fieldNode != null ? fieldNode.asText() : null;
    }
}
