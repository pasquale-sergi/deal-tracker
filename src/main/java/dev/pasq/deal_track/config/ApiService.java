package dev.pasq.deal_track.config;

import dev.pasq.deal_track.entity.Product;
import dev.pasq.deal_track.entity.UserProduct;
import dev.pasq.deal_track.repository.UserProductRepository;
import dev.pasq.deal_track.service.UserProductService;
import dev.pasq.deal_track.service.UserProductServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class ApiService {

    private HttpClientService httpClientService;
    private UserProductRepository userProductRepository;

    private UserProductServiceImpl userProductService;
    @Scheduled(fixedRate = 600000)
    public void makeApiCall(){
        Long userId = 1L;
        List<UserProduct> userProducts = userProductRepository.findAllByUserId(userId);

        List<CompletableFuture> futures = new ArrayList<>();

        userProducts.forEach(userProduct -> {
            String asin = userProduct.getProduct().getAsin();
            CompletableFuture<String> apiResponseFuture = httpClientService.getProductDetails(asin);

            CompletableFuture<Void> processingFuture = apiResponseFuture.thenAccept(apiResponse -> {
                try {
                    Product product = userProductService.parseProductDetails(apiResponse, asin);
                    if(userProduct.getLastPriceTracked().compareTo(product.getPrice())>0){
                        System.out.println("The product "+product.getProduct_name()+" has a new all time low price of $"+product.getPrice()+"\nLast price tracked: $"+userProduct.getLastPriceTracked());
                    }else {
                        System.out.println("The price of the product " + product.getProduct_name() + " with asin " + product.getAsin() + " at " + new Date() + " is not better than the last tracked price.\nLast Tracked Price: $  "+userProduct.getLastPriceTracked());

                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Error retrieving info calling the API for ASIN " + asin + ": " + e.getMessage());
                }
            }).exceptionally(ex->{
                System.err.println("API call failed for ASIN: "+asin+": "+ex.getMessage());
                return null;
            });
            futures.add(processingFuture);

        });

        //wait for all the futures to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }
}
