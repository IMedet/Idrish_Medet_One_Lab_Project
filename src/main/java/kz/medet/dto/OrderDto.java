package kz.medet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private Timestamp timeCreated;

    @Builder.Default
    private List<ProductDto> products = new ArrayList<>();
}
