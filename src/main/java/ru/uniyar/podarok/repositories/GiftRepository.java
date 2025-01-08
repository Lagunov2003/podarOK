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

/**
 * Репозиторий для работы с сущностью "Подарок".
 * Осуществляет операции с базой данных, такие как добавление, обновление, удаление и получение подарков.
 */
public interface GiftRepository extends JpaRepository<Gift, Long> {
    /**
     * Находит подарок по его идентификатору.
     * @param id Идентификатор подарка.
     * @return Опциональный подарок.
     */
    @Override
    Optional<Gift> findById(Long id);

    /**
     * Находит подарки по категориям или поводам.
     * @param categories Список идентификаторов категорий.
     * @param occasions Список идентификаторов поводов.
     * @return Список подарков, соответствующих фильтрам.
     */
    @Query("SELECT DISTINCT g FROM Gift g "
            + "LEFT JOIN FETCH g.photos photos "
            + "LEFT JOIN FETCH g.categories categories "
            + "LEFT JOIN FETCH g.occasions occasions "
            + "WHERE (:categories IS NULL OR categories.id IN :categories) "
            + "OR (:occasions IS NULL OR occasions.id IN :occasions)")
    List<Gift> findGiftsByCategoriesOrOccasions(
            @Param("categories") List<Long> categories,
            @Param("occasions") List<Long> occasions
    );

    /**
     * Находит подарки по идентификатору группы подарков.
     * @param groupId Идентификатор группы подарков.
     * @return Список подарков из данной группы.
     */
    @Query("SELECT g FROM Gift g WHERE g.giftGroup.id = :groupId")
    List<Gift> findGiftsByGroupId(@Param("groupId") Long groupId);

    /**
     * Обновляет данные о подарке.
     * @param id Идентификатор подарка.
     * @param price Цена подарка.
     * @param recommendationId Идентификатор рекомендации.
     * @param description Описание подарка.
     * @param name Название подарка.
     * @param groupId Идентификатор группы подарков.
     */
    @Deprecated
    @Modifying
    @Query(value = "UPDATE Gift "
            + "SET price=:price, recommendation_id=:recommendation_id, "
            + "description=:description, name=:name, group_id=:group_id "
            + "WHERE id=:id", nativeQuery = true)
    void updateGift(@Param("id") Long id,
                  @Param("price") BigDecimal price,
                  @Param("recommendation_id") Long recommendationId,
                  @Param("description") String description,
                  @Param("name") String name,
                  @Param("group_id") Long groupId);

    /**
     * Удаляет связи подарка с категориями.
     * @param giftId Идентификатор подарка.
     */
    @Deprecated
    @Modifying
    @Query(value = "DELETE FROM gift_category "
            + "WHERE gift_id=:gift_id", nativeQuery = true)
    void deleteGiftCategories(@Param("gift_id") Long giftId);

    /**
     * Обновляет связь подарка с поводом.
     * @param giftId Идентификатор подарка.
     * @param occasionId Идентификатор повода.
     */
    @Deprecated
    @Modifying
    @Query(value = "UPDATE gift_occasion SET occasion_id=:occasion_id "
            + "WHERE gift_id=:gift_id", nativeQuery = true)
    void updateGiftOccasion(@Param("gift_id") Long giftId, @Param("occasion_id") Long occasionId);

    /**
     * Удаляет фотографии, связанные с подарком.
     * @param giftId Идентификатор подарка.
     */
    @Deprecated
    @Modifying
    @Query(value = "DELETE FROM gift_photo "
            + "WHERE gift_id=:gift_id", nativeQuery = true)
    void deleteGiftPhotos(@Param("gift_id") Long giftId);

    /**
     * Удаляет особенности подарка.
     * @param giftId Идентификатор подарка.
     */
    @Deprecated
    @Modifying
    @Query(value = "DELETE FROM gift_feature "
            + "WHERE gift_id=:gift_id", nativeQuery = true)
    void deleteGiftFeatures(@Param("gift_id") Long giftId);

    /**
     * Обновляет рекомендации для подарка.
     * @param id Идентификатор рекомендации.
     * @param gender Пол, для которого подходит подарок.
     * @param minAge Минимальный возраст для подарка.
     * @param maxAge Максимальный возраст для подарка.
     */
    @Deprecated
    @Modifying
    @Query(value = "UPDATE Gift_Recommendations "
            + "SET gender=:gender, min_age=:min_age, max_age=:max_age "
            + "WHERE id=:id", nativeQuery = true)
    void updateGiftRecommendation(@Param("id") Long id,
                                  @Param("gender") Boolean gender,
                                  @Param("min_age") Long minAge,
                                  @Param("max_age") Long maxAge);

    /**
     * Добавляет новый подарок в базу данных.
     * @param price Цена подарка.
     * @param recommendationId Идентификатор рекомендации.
     * @param description Описание подарка.
     * @param name Название подарка.
     * @param groupId Идентификатор группы подарков.
     * @return Идентификатор нового подарка.
     */
    @Deprecated
    @Query(value = "INSERT INTO Gift(price, recommendation_id, description, name, group_id) "
            + "VALUES(:price, :recommendation_id, :description, :name, :group_id) RETURNING id", nativeQuery = true)
    Integer addGift(@Param("price") BigDecimal price,
                    @Param("recommendation_id") Long recommendationId,
                    @Param("description") String description,
                    @Param("name") String name,
                    @Param("group_id") Long groupId);

    /**
     * Добавляет подарок в категорию.
     * @param giftId Идентификатор подарка.
     * @param categoryId Идентификатор категории.
     */
    @Deprecated
    @Modifying
    @Query(value = "INSERT INTO gift_category(gift_id, category_id) VALUES(:gift_id, :category_id) ",
            nativeQuery = true)
    void addGiftCategory(@Param("gift_id") Long giftId, @Param("category_id") Long categoryId);

    /**
     * Добавляет подарок в повод.
     * @param giftId Идентификатор подарка.
     * @param occasionId Идентификатор повода.
     */
    @Deprecated
    @Modifying
    @Query(value = "INSERT INTO gift_occasion(gift_id, occasion_id) VALUES(:gift_id, :occasion_id)", nativeQuery = true)
    void addGiftOccasion(@Param("gift_id") Long giftId, @Param("occasion_id") Long occasionId);

    /**
     * Добавляет фотографию для подарка.
     * @param giftId Идентификатор подарка.
     * @param photoUrl URL фотографии.
     */
    @Deprecated
    @Modifying
    @Query(value = "INSERT INTO Gift_Photo(gift_id, photo_url) VALUES(:gift_id, :photo_url)", nativeQuery = true)
    void addGiftPhoto(@Param("gift_id") Long giftId, @Param("photo_url") String photoUrl);

    /**
     * Добавляет характеристику для подарка.
     * @param giftId Идентификатор подарка.
     * @param itemName Название характеристики.
     * @param itemValue Значение характеристики.
     */
    @Deprecated
    @Modifying
    @Query(value = "INSERT INTO Gift_Feature(gift_id, item_name, item_value) "
            + "VALUES(:gift_id, :item_name, :item_value)", nativeQuery = true)
    void addGiftFeature(@Param("gift_id") Long giftId,
                           @Param("item_name") String itemName,
                           @Param("item_value") String itemValue);

    /**
     * Добавляет новую рекомендацию для подарка.
     * @param gender Пол, для которого подходит подарок.
     * @param minAge Минимальный возраст.
     * @param maxAge Максимальный возраст.
     * @return Идентификатор новой рекомендации.
     */
    @Deprecated
    @Query(value = "INSERT INTO Gift_Recommendations(gender, min_age, max_age) "
            + "VALUES(:gender, :min_age, :max_age) RETURNING id", nativeQuery = true)
    Integer addGiftRecommendation(@Param("gender") Boolean gender,
                                  @Param("min_age") Long minAge,
                                  @Param("max_age") Long maxAge);

    /**
     * Находит все подарки по фильтрам с сортировкой по средней оценке в убывающем порядке.
     * @param budget Бюджет.
     * @param gender Пол.
     * @param age Возраст.
     * @param categories Категории.
     * @param occasions Поводы.
     * @param name Название подарка.
     * @param pageable Параметры пагинации.
     * @return Страница подарков.
     */
    @Query(value = "SELECT g.*, COALESCE(AVG(r.rating), 0) AS average_rating "
            + "FROM gift g "
            + "LEFT JOIN gift_photo gp ON g.id = gp.gift_id "
            + "LEFT JOIN gift_recommendations gr ON g.recommendation_id = gr.id "
            + "LEFT JOIN gift_category gc ON g.id = gc.gift_id "
            + "LEFT JOIN gift_occasion go ON g.id = go.gift_id "
            + "LEFT JOIN review r ON g.id = r.gift_id "
            + "WHERE (:budget IS NULL OR g.price <= :budget) "
            + "AND (:gender IS NULL OR gr.gender = :gender) "
            + "AND (:age IS NULL OR (gr.min_age <= :age AND gr.max_age >= :age)) "
            + "AND (:categories IS NULL OR gc.category_id IN (:categories)) "
            + "AND (:occasions IS NULL OR go.occasion_id IN (:occasions)) "
            + "AND LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))"
            + "GROUP BY g.id "
            + "ORDER BY average_rating DESC",
            countQuery = "SELECT COUNT(DISTINCT g.id) "
                    + "FROM gift g "
                    + "LEFT JOIN gift_recommendations gr ON g.recommendation_id = gr.id "
                    + "LEFT JOIN gift_category gc ON g.id = gc.gift_id "
                    + "LEFT JOIN gift_occasion go ON g.id = go.gift_id "
                    + "LEFT JOIN review r ON g.id = r.gift_id "
                    + "WHERE (:budget IS NULL OR g.price <= :budget) "
                    + "AND (:gender IS NULL OR gr.gender = :gender) "
                    + "AND (:age IS NULL OR (gr.min_age <= :age AND gr.max_age >= :age)) "
                    + "AND (:categories IS NULL OR gc.category_id IN (:categories)) "
                    + "AND (:occasions IS NULL OR go.occasion_id IN (:occasions))",
            nativeQuery = true)
    Page<Gift> findAllByFiltersByNameAndByAverageRatingDesc(
            @Param("budget") BigDecimal budget,
            @Param("gender") Boolean gender,
            @Param("age") Integer age,
            @Param("categories") List<Long> categories,
            @Param("occasions") List<Long> occasions,
            @Param("name") String name,
            Pageable pageable
    );

    /**
     * Находит все подарки по фильтрам с сортировкой по цене в возрастающем порядке.
     * @param budget Бюджет.
     * @param gender Пол.
     * @param age Возраст.
     * @param categories Категории.
     * @param occasions Поводы.
     * @param name Название подарка.
     * @param pageable Параметры пагинации.
     * @return Страница подарков.
     */
    @Query("SELECT DISTINCT g FROM Gift g "
            + "LEFT JOIN FETCH g.photos photos "
            + "LEFT JOIN FETCH g.recommendation rec "
            + "LEFT JOIN FETCH g.categories categories "
            + "LEFT JOIN FETCH g.occasions occasions "
            + "WHERE (:budget IS NULL OR g.price <= :budget) "
            + "AND (:gender IS NULL OR rec.gender = :gender) "
            + "AND (:age IS NULL OR (rec.minAge <= :age AND rec.maxAge >= :age)) "
            + "AND (:categories IS NULL OR categories.id IN (:categories)) "
            + "AND (:occasions IS NULL OR occasions.id IN (:occasions))"
            + "AND LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))"
            + "ORDER BY g.price ASC")
    Page<Gift> findAllByFiltersByNameAndByPriceAsc(
            @Param("budget") BigDecimal budget,
            @Param("gender") Boolean gender,
            @Param("age") Integer age,
            @Param("categories") List<Long> categories,
            @Param("occasions") List<Long> occasions,
            @Param("name") String name,
            Pageable pageable
    );

    /**
     * Находит все подарки по фильтрам с сортировкой по цене в убывающем порядке.
     * @param budget Бюджет.
     * @param gender Пол.
     * @param age Возраст.
     * @param categories Категории.
     * @param occasions Поводы.
     * @param name Название подарка.
     * @param pageable Параметры пагинации.
     * @return Страница подарков.
     */
    @Query("SELECT DISTINCT g FROM Gift g "
            + "LEFT JOIN FETCH g.photos photos "
            + "LEFT JOIN FETCH g.recommendation rec "
            + "LEFT JOIN FETCH g.categories categories "
            + "LEFT JOIN FETCH g.occasions occasions "
            + "WHERE (:budget IS NULL OR g.price <= :budget) "
            + "AND (:gender IS NULL OR rec.gender = :gender) "
            + "AND (:age IS NULL OR (rec.minAge <= :age AND rec.maxAge >= :age)) "
            + "AND (:categories IS NULL OR categories.id IN (:categories)) "
            + "AND (:occasions IS NULL OR occasions.id IN (:occasions))"
            + "AND LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))"
            + "ORDER BY g.price DESC")
    Page<Gift> findAllByFiltersByNameAndByPriceDesc(
            @Param("budget") BigDecimal budget,
            @Param("gender") Boolean gender,
            @Param("age") Integer age,
            @Param("categories") List<Long> categories,
            @Param("occasions") List<Long> occasions,
            @Param("name") String name,
            Pageable pageable
    );

    /**
     * Находит все подарки по фильтрам без сортировки.
     * @param budget Бюджет.
     * @param gender Пол.
     * @param age Возраст.
     * @param categories Категории.
     * @param occasions Поводы.
     * @param name Название подарка.
     * @param pageable Параметры пагинации.
     * @return Страница подарков.
     */
    @Query("SELECT DISTINCT g FROM Gift g "
            + "LEFT JOIN FETCH g.photos photos "
            + "LEFT JOIN FETCH g.recommendation rec "
            + "LEFT JOIN FETCH g.categories categories "
            + "LEFT JOIN FETCH g.occasions occasions "
            + "WHERE (:budget IS NULL OR g.price <= :budget) "
            + "AND (:gender IS NULL OR rec.gender = :gender) "
            + "AND (:age IS NULL OR (rec.minAge <= :age AND rec.maxAge >= :age)) "
            + "AND (:categories IS NULL OR categories.id IN (:categories)) "
            + "AND (:occasions IS NULL OR occasions.id IN (:occasions))"
            + "AND LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Gift> findAllByFiltersByName(
            @Param("budget") BigDecimal budget,
            @Param("gender") Boolean gender,
            @Param("age") Integer age,
            @Param("categories") List<Long> categories,
            @Param("occasions") List<Long> occasions,
            @Param("name") String name,
            Pageable pageable
    );
    /**
     * Возвращает все подарки.
     * @param pageable  Параметры пагинации.
     * @return Страница подарков.
     */
    @Query("SELECT DISTINCT g FROM Gift g "
            + "LEFT JOIN FETCH g.photos photos")
    Page<Gift> findAllGifts(Pageable pageable);
}
