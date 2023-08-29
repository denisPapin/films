package com.dp.homework.films.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Entity
@Table(name = "orders")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends GenericModel {


    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_ORUS"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "film_id", foreignKey = @ForeignKey(name = "FK_ORFM"))
    private Film film;

    @Column(name = "rent_date", nullable = false)
    private LocalDate rentDate;

    @Column(name = "rent_period", nullable = false)
    private Integer rentPeriod;

    @Column(name = "purchase")
    private boolean purchase;


}
