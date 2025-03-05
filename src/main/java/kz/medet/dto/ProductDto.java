package kz.medet.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "t_products")
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private String description;

    @ManyToMany(mappedBy = "products", fetch = FetchType.EAGER)
    private List<OrderDto> orders;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryDto category;

    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }

    @PrePersist
    public void setDefaultDescription() {
        if (this.description == null || this.description.isEmpty()) {
            this.description = "Product description";
        }
    }

}
