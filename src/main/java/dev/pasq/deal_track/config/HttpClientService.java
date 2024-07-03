package dev.pasq.deal_track.config;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
public class HttpClientService {

    private final AsyncHttpClient client;

    public HttpClientService(){
        this.client = new DefaultAsyncHttpClient();
    }

    public CompletableFuture<String> getProductDetails(String asin){
        String url = "https://real-time-amazon-data.p.rapidapi.com/product-details?asin=" + asin + "&country=US";

        System.out.println("Request URL: " + url); // Log the request URL
        return client.prepareGet(url)
                .setHeader("x-rapidapi-key", "0f6c296c60msha3039c5ea530129p1949c6jsnd0605ffff42f")
                .setHeader("x-rapidapi-host", "real-time-amazon-data.p.rapidapi.com")
                .execute()
                .toCompletableFuture()
                .thenApply(response -> {
                    System.out.println("Response status: " + response.getStatusCode()); // Log the response status code
                    return response.getResponseBody();
                })
                .exceptionally(ex -> {
                    System.out.println("Request failed: " + ex.getMessage());
                    return "";
                });
    }

    public void close() throws IOException {
        client.close();
    }

}
