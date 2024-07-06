package dev.pasq.deal_track.controller;

import dev.pasq.deal_track.entity.ProductRequestDto;
import dev.pasq.deal_track.entity.UserProduct;
import dev.pasq.deal_track.service.UserProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
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

    @DeleteMapping("/delete/{productAsin}")
    public ResponseEntity<String> deleteProductFromUserTracking(@PathVariable("productAsin") String productAsin){
        userProductService.deleteProductToUser(productAsin);
        return ResponseEntity.ok("Product deleted");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserProduct>> getUserProducts(@PathVariable("userId") Long userId) {
        List<UserProduct> userProducts = userProductService.getUserProducts(userId);
        System.out.println("we're at this point");
        return ResponseEntity.ok(userProducts);
    }
    //second try because the first one didnt work
    @GetMapping("/all")
    public ResponseEntity<List<UserProduct>> getAllProduts(){
        List<UserProduct> userProducts = userProductService.getAllProds();
        return ResponseEntity.ok(userProducts);
    }
}
