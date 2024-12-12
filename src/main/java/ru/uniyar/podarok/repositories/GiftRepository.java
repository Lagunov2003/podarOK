package ru.uniyar.podarok.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.uniyar.podarok.entities.Gift;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface GiftRepository extends JpaRepository<Gift, Long> {
    @Query("SELECT DISTINCT g FROM Gift g " +
            "LEFT JOIN FETCH g.photos photos")
    Page<Gift> findAllGifts(Pageable pageable);

    @Query("SELECT DISTINCT g FROM Gift g " +
            "LEFT JOIN FETCH g.photos photos " +
            "LEFT JOIN FETCH g.recommendation rec " +
            "LEFT JOIN FETCH g.categories categories " +
            "LEFT JOIN FETCH g.occasions occasions " +
            "WHERE (:budget IS NULL OR g.price <= :budget) " +
            "AND (:gender IS NULL OR rec.gender = :gender) " +
            "AND (:age IS NULL OR (rec.minAge <= :age AND rec.maxAge >= :age)) " +
            "AND (:categories IS NULL OR categories.id IN :categories) " +
            "AND (:occasions IS NULL OR occasions.id IN :occasions)")
    Page<Gift> findGiftsByFilter(
            @Param("budget") BigDecimal budget,
            @Param("gender") Boolean gender,
            @Param("age") Integer age,
            @Param("categories") List<Long> categories,
            @Param("occasions") List<Long> occasions,
            Pageable pageable
    );

    Optional<Gift> findById(Long id);

    @Query("SELECT g FROM Gift g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Gift> findGiftsByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT DISTINCT g FROM Gift g " +
            "LEFT JOIN FETCH g.photos photos " +
            "LEFT JOIN FETCH g.categories categories " +
            "LEFT JOIN FETCH g.occasions occasions " +
            "WHERE (:categories IS NULL OR categories.id IN :categories) " +
            "OR (:occasions IS NULL OR occasions.id IN :occasions)")
    List<Gift> findGiftsByCategoriesOrOccasions(
            @Param("categories") List<Long> categories,
            @Param("occasions") List<Long> occasions
    );

    @Query("SELECT g FROM Gift g WHERE g.giftGroup.id = :groupId")
    List<Gift> findGiftsByGroupId(@Param("groupId") Long groupId);

    @Modifying
    @Query(value = "UPDATE Gift " +
            "SET price=:price, recommendation_id=:recommendation_id, description=:description, name=:name, group_id=:group_id " +
            "WHERE id=:id", nativeQuery = true)
    void updateGift(@Param("id") Long id,
                    @Param("price") BigDecimal price,
                    @Param("recommendation_id") Long recommendationId,
                    @Param("description") String description,
                    @Param("name") String name,
                    @Param("group_id") Long groupId);

    @Modifying
    @Query(value = "DELETE FROM gift_category " +
            "WHERE gift_id=:gift_id", nativeQuery = true)
    void deleteGiftCategories(@Param("gift_id") Long giftId);

    @Modifying
    @Query(value = "UPDATE gift_occasion SET occasion_id=:occasion_id " +
            "WHERE gift_id=:gift_id", nativeQuery = true)
    void updateGiftOccasion(@Param("gift_id") Long giftId, @Param("occasion_id") Long occasionId);

    @Modifying
    @Query(value = "DELETE FROM gift_photo " +
            "WHERE gift_id=:gift_id", nativeQuery = true)
    void deleteGiftPhotos(@Param("gift_id") Long giftId);

    @Modifying
    @Query(value = "DELETE FROM gift_feature " +
            "WHERE gift_id=:gift_id", nativeQuery = true)
    void deleteGiftFeatures(@Param("gift_id") Long giftId);

    @Modifying
    @Query(value = "UPDATE Gift_Recommendations " +
            "SET gender=:gender, min_age=:min_age, max_age=:max_age " +
            "WHERE id=:id", nativeQuery = true)
    void updateGiftRecommendation(@Param("id") Long id,
                                  @Param("gender") Boolean gender,
                                  @Param("min_age") Long minAge,
                                  @Param("max_age") Long maxAge);


    @Query(value = "INSERT INTO Gift(price, recommendation_id, description, name, group_id) " +
            "VALUES(:price, :recommendation_id, :description, :name, :group_id) RETURNING id", nativeQuery = true)
    Integer addGift(@Param("price") BigDecimal price,
                    @Param("recommendation_id") Long recommendationId,
                    @Param("description") String description,
                    @Param("name") String name,
                    @Param("group_id") Long groupId);

    @Modifying
    @Query(value = "INSERT INTO gift_category(gift_id, category_id) VALUES(:gift_id, :category_id) ", nativeQuery = true)
    void addGiftCategory(@Param("gift_id") Long giftId, @Param("category_id") Long categoryId);

    @Modifying
    @Query(value = "INSERT INTO gift_occasion(gift_id, occasion_id) VALUES(:gift_id, :occasion_id)", nativeQuery = true)
    void addGiftOccasion(@Param("gift_id") Long giftId, @Param("occasion_id") Long occasionId);

    @Modifying
    @Query(value = "INSERT INTO Gift_Photo(gift_id, photo_url) VALUES(:gift_id, :photo_url)", nativeQuery = true)
    void addGiftPhoto(@Param("gift_id") Long giftId, @Param("photo_url") String photoUrl);

    @Modifying
    @Query(value = "INSERT INTO Gift_Feature(gift_id, item_name, item_value) " +
            "VALUES(:gift_id, :item_name, :item_value)", nativeQuery = true)
    void addGiftFeature(@Param("gift_id") Long giftId,
                        @Param("item_name") String itemName,
                        @Param("item_value") String itemValue);

    @Query(value = "INSERT INTO Gift_Recommendations(gender, min_age, max_age) " +
            "VALUES(:gender, :min_age, :max_age) RETURNING id", nativeQuery = true)
    Integer addGiftRecommendation(@Param("gender") Boolean gender,
                                  @Param("min_age") Long minAge,
                                  @Param("max_age") Long maxAge);

    Page<Gift> findAllByOrderByPriceAsc(Pageable pageable);

    Page<Gift> findAllByOrderByPriceDesc(Pageable pageable);

    @Query(value = "SELECT g.* " +
            "FROM gift g " +
            "LEFT JOIN review r ON g.id = r.gift_id " +
            "GROUP BY g.id " +
            "ORDER BY COALESCE(AVG(r.rating), 0) DESC",
            countQuery = "SELECT COUNT(DISTINCT g.id) " +
                    "FROM gift g " +
                    "LEFT JOIN review r ON g.id = r.gift_id",
            nativeQuery = true)
    Page<Gift> findAllOrderByAverageRatingDesc(Pageable pageable);
}
