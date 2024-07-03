package dev.pasq.deal_track.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
@Data
@Builder
@Embeddable
@AllArgsConstructor // Optional, if you want a constructor with all fields
@EqualsAndHashCode // Generates equals() and hashCode() methods
@NoArgsConstructor
public class UserProductId implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "product_asin")
    private String productAsin;

}
