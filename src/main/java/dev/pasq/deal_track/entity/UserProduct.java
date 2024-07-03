package dev.pasq.deal_track.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_product")
public class UserProduct {
    @EmbeddedId
    private UserProductId id;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne
    @MapsId("productAsin")
    @JoinColumn(name="product_asin")
    private Product product;
    @Column(name="tracking_status")
    private String trackingStatus;
    @Column(name="last_price_tracked")
    private BigDecimal lastPriceTracked;


}
