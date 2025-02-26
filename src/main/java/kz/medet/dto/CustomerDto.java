package kz.medet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CustomerDto {
    private Long id;
    private String firstName;
    private String lastName;

    @Builder.Default
    private List<OrderDto> orders = new ArrayList<>();
}
