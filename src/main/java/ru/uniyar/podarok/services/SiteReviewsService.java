package ru.uniyar.podarok.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uniyar.podarok.dtos.AddSiteReviewDto;
import ru.uniyar.podarok.dtos.SiteReviewsDto;
import ru.uniyar.podarok.entities.SiteReviews;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.exceptions.SiteReviewNotFoundException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.SiteReviewsRepository;
import ru.uniyar.podarok.utils.SiteReviewsDtoConverter;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SiteReviewsService {
    private UserService userService;
    private SiteReviewsRepository siteReviewsRepository;
    private SiteReviewsDtoConverter siteReviewsDtoConverter;

    public List<SiteReviewsDto> getSiteReviews() {
        List<SiteReviews> siteReviews = siteReviewsRepository.findTop6ByAcceptedTrue();
        return siteReviews.stream()
                .map(siteReviewsDtoConverter::convertToSiteReviewsDto)
                .collect(Collectors.toList());
    }

    public List<SiteReviewsDto> getSiteReviewsByAcceptedStatus(boolean accepted) {
        List<SiteReviews> siteReviews = accepted
                ? siteReviewsRepository.findByAcceptedTrue()
                : siteReviewsRepository.findByAcceptedFalse();
        return siteReviews.stream()
                .map(siteReviewsDtoConverter::convertToSiteReviewsDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addSiteReview(AddSiteReviewDto addSiteReviewDto) throws UserNotFoundException, UserNotAuthorizedException {
        User user = userService.getCurrentAuthenticationUser();
        SiteReviews siteReview = user.getSiteReviews();
        if (siteReview == null) {
            siteReview = new SiteReviews();
            siteReview.setUser(user);
        }
        siteReview.setMark(addSiteReviewDto.getMark());
        siteReview.setReview(addSiteReviewDto.getReview());
        siteReview.setAccepted(false);
        siteReviewsRepository.save(siteReview);
    }

    @Transactional
    public void changeAcceptedStatusSiteReviews(Long id) throws SiteReviewNotFoundException {
        SiteReviews siteReviews = siteReviewsRepository.findById(id)
                .orElseThrow(() -> new SiteReviewNotFoundException("Отзыв с id " + id + " не найден!"));
        siteReviews.setAccepted(true);
        siteReviewsRepository.save(siteReviews);
    }

    @Transactional
    public void deleteSiteReviews(Long id) throws SiteReviewNotFoundException {
        if (!siteReviewsRepository.existsById(id)) {
            throw new SiteReviewNotFoundException("Отзыв о сайте с id " + id + " не найден!");
        }
        siteReviewsRepository.deleteById(id);
    }
}
