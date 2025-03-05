package kz.medet.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "t_customers")
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private List<OrderDto> orders;


    @Override
    public String toString() {
        return "CustomerDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", orders=" + orders +
                '}';
    }
}
