package nl.consumergram.consumergramv2.repositories;

import nl.consumergram.consumergramv2.models.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageDataRepository extends JpaRepository<ImageData, Long> {
}
