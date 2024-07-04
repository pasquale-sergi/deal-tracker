package dev.pasq.deal_track.config;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
public class HttpClientService {

    private final AsyncHttpClient client;
    @Value("${rapidapi.key}")
    private String rapidApiKey;
    @Value("${rapidapi.host}")
    private String rapidApiHost;

    public HttpClientService(){
        this.client = new DefaultAsyncHttpClient();
    }

    public CompletableFuture<String> getProductDetails(String asin){
        String url = "https://real-time-amazon-data.p.rapidapi.com/product-details?asin=" + asin + "&country=US";

        System.out.println("Request URL: " + url); // Log the request URL
        return client.prepareGet(url)
                .setHeader("x-rapidapi-key", rapidApiKey)
                .setHeader("x-rapidapi-host", rapidApiHost)
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
