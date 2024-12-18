package ru.uniyar.podarok.utils.Builders;

import org.springframework.stereotype.Component;
import ru.uniyar.podarok.entities.ConfirmationCode;

import java.time.LocalDate;

/**
 * Строитель для создания объектов {@link ConfirmationCode}.
 */
@Component
public class ConfirmationCodeBuilder {
    private String code;
    private Long ownUserId;
    private LocalDate expiryDate;

    /**
     * Устанавливает код подтверждения.
     *
     * @param code строка, представляющая код подтверждения
     * @return текущий объект {@link ConfirmationCodeBuilder} для дальнейшей настройки
     */
    public ConfirmationCodeBuilder setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Устанавливает ID владельца кода подтверждения.
     *
     * @param ownUserId ID пользователя
     * @return текущий объект {@link ConfirmationCodeBuilder} для дальнейшей настройки
     */
    public ConfirmationCodeBuilder setOwnUserId(Long ownUserId) {
        this.ownUserId = ownUserId;
        return this;
    }

    /**
     * Устанавливает дату истечения срока действия кода подтверждения.
     *
     * @param expiryDate дата истечения срока действия
     * @return текущий объект {@link ConfirmationCodeBuilder} для дальнейшей настройки
     */
    public ConfirmationCodeBuilder setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    /**
     * Строит объект {@link ConfirmationCode} с заданными параметрами.
     *
     * @return новый объект {@link ConfirmationCode}, содержащий все параметры, заданные через методы билдера
     */
    public ConfirmationCode build() {
        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode.setCode(this.code);
        confirmationCode.setOwnUserId(this.ownUserId);
        confirmationCode.setExpiryDate(this.expiryDate);
        return confirmationCode;
    }
}
