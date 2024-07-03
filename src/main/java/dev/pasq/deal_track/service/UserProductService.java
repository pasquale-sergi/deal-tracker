package dev.pasq.deal_track.service;

import dev.pasq.deal_track.entity.Product;

public interface UserProductService {

    void addProductToUser(Long userId, String productAsin);
}
