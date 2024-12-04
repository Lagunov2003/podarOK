package ru.uniyar.podarok.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.uniyar.podarok.dtos.*;
import ru.uniyar.podarok.dtos.AddGiftDto;
import ru.uniyar.podarok.dtos.ChangeGiftDto;
import ru.uniyar.podarok.dtos.OrderDataDto;
import ru.uniyar.podarok.exceptions.OrderNotFoundException;
import ru.uniyar.podarok.services.AdminService;

@RestController
@AllArgsConstructor
public class AdminPanelController {
    private AdminService adminService;
    @GetMapping("/getOrders")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getOrders(@RequestParam String status) {
        return ResponseEntity.ok(adminService.getOrders(status));
    }

    @PutMapping("/changeOrderStatus")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeOrderStatus(@RequestBody OrderDataDto orderDataDto) {
        try {
            adminService.changeOrderStatus(orderDataDto);
            return ResponseEntity.ok().body("Статус заказа успешно изменён!");
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteGift")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteGift(@RequestParam Long id) {
        try {
            adminService.deleteGift(id);
            return ResponseEntity.ok("Подарок успешно удалён!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/changeGift")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeGift(@RequestBody ChangeGiftDto changeGiftDto) {
        try {
            adminService.changeGift(changeGiftDto);
            return ResponseEntity.ok("Подарок успешно изменён!");
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/addGift")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addGift(@RequestBody AddGiftDto addGiftDto) {
        adminService.addGift(addGiftDto);
        return ResponseEntity.ok("Подарок успешно добавлен!");
    }

    @PostMapping("/addGroup")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addGroup(@RequestBody AddGroupDto addGroupDto) {
        adminService.addGroup(addGroupDto);
        return ResponseEntity.ok("Группа подарков успешно добавлена!");
    }

    @GetMapping("/getAcceptedSiteReviews")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAcceptedSiteReviews() {
        return ResponseEntity.ok(adminService.getSiteReviews(true));
    }

    @GetMapping("/getNotAcceptedSiteReviews")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getNotAcceptedSiteReviews() {
        return ResponseEntity.ok(adminService.getSiteReviews(false));
    }

    @PutMapping("/changeAcceptedStatusSiteReviews")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeAcceptedStatusSiteReviews(@RequestParam Long id) {
        try {
            adminService.changeAcceptedStatusSiteReviews(id);
            return ResponseEntity.ok("Отзыв подтверждён!");
        } catch(EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteNotAcceptedSiteReviews")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteNotAcceptedSiteReviews(@RequestParam Long id) {
        adminService.deleteNotAcceptedSiteReviews(id);
        return ResponseEntity.ok("Отзыв о сайте отклонён!");
    }
}
