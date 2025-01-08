package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uniyar.podarok.dtos.AddSiteReviewDto;
import ru.uniyar.podarok.dtos.SiteReviewsDto;
import ru.uniyar.podarok.entities.SiteReviews;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.SiteReviewAlreadyExistException;
import ru.uniyar.podarok.exceptions.SiteReviewNotFoundException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.SiteReviewsRepository;
import ru.uniyar.podarok.utils.converters.SiteReviewsDtoConverter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с отзывами о сайте.
 */
@Service
@AllArgsConstructor
public class SiteReviewsService {
    private UserService userService;
    private SiteReviewsRepository siteReviewsRepository;
    private SiteReviewsDtoConverter siteReviewsDtoConverter;

    /**
     * Получает список 6 последних принятых отзывов о сайте.
     *
     * @return список объектов {@link SiteReviewsDto} с данными принятых отзывов.
     */
    public List<SiteReviewsDto> getSiteReviews() {
        List<SiteReviews> siteReviews = siteReviewsRepository.findTop6ByAcceptedTrue();
        return siteReviews.stream()
                .map(siteReviewsDtoConverter::convertToSiteReviewsDto)
                .collect(Collectors.toList());
    }

    /**
     * Получает список отзывов о сайте по статусу принятия.
     *
     * @param accepted статус принятия отзыва (true — принятые, false — отклонённые).
     * @return список объектов {@link SiteReviewsDto} с данными отзывов.
     */
    public List<SiteReviewsDto> getSiteReviewsByAcceptedStatus(boolean accepted) {
        List<SiteReviews> siteReviews = accepted
                ? siteReviewsRepository.findByAcceptedTrue()
                : siteReviewsRepository.findByAcceptedFalse();
        return siteReviews.stream()
                .map(siteReviewsDtoConverter::convertToSiteReviewsDto)
                .collect(Collectors.toList());
    }

    /**
     * Добавляет новый отзыв о сайте для текущего аутентифицированного пользователя.
     * Если у пользователя нет отзыва, создаётся новый.
     * Отзыв по умолчанию не принят.
     *
     * @param addSiteReviewDto объект DTO с данными нового отзыва.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws SiteReviewAlreadyExistException если отзыв о сайте уже добавлен.
     */
    @Transactional
    public void addSiteReview(AddSiteReviewDto addSiteReviewDto)
            throws UserNotFoundException, UserNotAuthorizedException, SiteReviewAlreadyExistException {
        User user = userService.getCurrentAuthenticationUser();

        SiteReviews siteReview = user.getSiteReviews();
        if (siteReview != null) {
            throw new SiteReviewAlreadyExistException("Отзыв о сайте уже добавлен!");
        }

        siteReview = new SiteReviews();
        siteReview.setUser(user);
        siteReview.setMark(addSiteReviewDto.getMark());
        siteReview.setReview(addSiteReviewDto.getReview());
        siteReview.setAccepted(false);

        siteReviewsRepository.save(siteReview);
    }

    /**
     * Меняет статус принятия отзыва о сайте с отклонённого на принятый.
     *
     * @param id идентификатор отзыва.
     * @throws SiteReviewNotFoundException если отзыв с указанным id не найден.
     */
    @Transactional
    public void changeAcceptedStatusSiteReviews(Long id) throws SiteReviewNotFoundException {
        SiteReviews siteReviews = siteReviewsRepository.findById(id)
                .orElseThrow(() -> new SiteReviewNotFoundException("Отзыв с id " + id + " не найден!"));
        siteReviews.setAccepted(true);
        siteReviewsRepository.save(siteReviews);
    }

    /**
     * Удаляет отзыв о сайте по указанному идентификатору.
     *
     * @param id идентификатор отзыва.
     * @throws SiteReviewNotFoundException если отзыв с указанным id не найден.
     */
    @Transactional
    public void deleteSiteReviews(Long id) throws SiteReviewNotFoundException {
        if (!siteReviewsRepository.existsById(id)) {
            throw new SiteReviewNotFoundException("Отзыв о сайте с id " + id + " не найден!");
        }
        siteReviewsRepository.deleteById(id);
    }
}
