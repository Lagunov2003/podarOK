package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.uniyar.podarok.entities.Gift;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftResponseDto {
    private Gift gift;
    private List<GiftDto> similarGifts;
}
