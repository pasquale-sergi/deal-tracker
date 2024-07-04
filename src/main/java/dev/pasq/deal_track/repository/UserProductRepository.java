package dev.pasq.deal_track.repository;

import dev.pasq.deal_track.entity.Product;
import dev.pasq.deal_track.entity.User;
import dev.pasq.deal_track.entity.UserProduct;
import dev.pasq.deal_track.entity.UserProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProductRepository extends JpaRepository<UserProduct, UserProductId> {
  boolean existsByUserIdAndProductAsin(Long userId, String asin);

  List<UserProduct> findAllByUserId(Long userId);


}
