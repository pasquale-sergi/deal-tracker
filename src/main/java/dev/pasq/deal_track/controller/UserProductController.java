package dev.pasq.deal_track.controller;

import dev.pasq.deal_track.entity.ProductRequestDto;
import dev.pasq.deal_track.service.UserProductService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class UserProductController {

    private final UserProductService userProductService;

    public UserProductController(UserProductService userProductService) {
        this.userProductService = userProductService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequestDto requestDto) {
        userProductService.addProductToUser(requestDto.getUserId(), requestDto.getProductAsin());
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");

    }
}
