package dev.pasq.deal_track.service;

import dev.pasq.deal_track.entity.Product;
import dev.pasq.deal_track.entity.UserProduct;

import java.util.List;

public interface UserProductService {

    void addProductToUser(Long userId, String productAsin);

    void deleteProductToUser( String productAsin);

    List<UserProduct> getUserProducts(Long userId);

    List<UserProduct> getAllProds();

    Product parseProductDetails(String apiResponse, String productAsin);
}
