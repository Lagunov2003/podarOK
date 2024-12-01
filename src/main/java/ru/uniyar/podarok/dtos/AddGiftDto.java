package ru.uniyar.podarok.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddGiftDto {
    private String name;
    private BigDecimal price;
    private List<String> photos;

    private List<Long> categories;
    private List<Long> occasions;
    private Boolean gender;
    private Long minAge;
    private Long maxAge;

    private Long groupId;
    private String description;
    private HashMap<String, String> features;
}
