package dev.pasq.deal_track.repository;

import dev.pasq.deal_track.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByAsin(String asin);
}
