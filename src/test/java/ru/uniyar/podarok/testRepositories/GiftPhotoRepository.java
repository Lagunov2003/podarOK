package ru.uniyar.podarok.testRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uniyar.podarok.entities.GiftPhoto;

public interface GiftPhotoRepository extends JpaRepository<GiftPhoto, Long> {
}
