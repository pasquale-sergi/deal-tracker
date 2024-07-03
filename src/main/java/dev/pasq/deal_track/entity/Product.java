package dev.pasq.deal_track.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {
    @Id
    private String asin;
    @Column(nullable = false)
    private String product_name;
    @Column(nullable = false)
    private String url;
    @Column
    private BigDecimal price;
    @OneToMany(mappedBy = "product")
    private Set<UserProduct> userProducts;
}
