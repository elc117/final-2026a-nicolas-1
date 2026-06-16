package com.restaurant.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.restaurant.model.enums.Employment;

import lombok.Getter;
import lombok.Setter;

// nao sei se vou implementar essa parte ainda, vai depender do tempo

@Getter
@Setter
public class WorkContract {
    private Long id = null;
    private BigDecimal salary;
    private Employment employmentType;
    private LocalDate admissionDate;

    public WorkContract(Long id, BigDecimal salary, Employment employmentType, LocalDate admissionDate) {
        this.salary = salary;
        this.employmentType = employmentType;
        this.admissionDate = admissionDate;
    }

    public WorkContract() {
        this.salary = null;
        this.employmentType = Employment.UNDEFINED;
        this.admissionDate = null;
    }
}
