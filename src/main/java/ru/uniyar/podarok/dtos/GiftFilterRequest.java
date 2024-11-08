package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftFilterRequest {
    private List<Long> categories;
    private List<Long> occasions;
    private Boolean gender;
    private BigDecimal budget;
    private Integer age;
    private Boolean urgency;

    public boolean hasAnyFilter() {
        return (categories != null && !categories.isEmpty()) ||
                (occasions != null && !occasions.isEmpty()) ||
                gender != null ||
                budget != null ||
                age != null ||
                urgency != null;
    }
}
