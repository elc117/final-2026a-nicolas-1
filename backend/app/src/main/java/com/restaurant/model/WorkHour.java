package com.restaurant.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

// nao sei se vou implementar essa parte ainda, vai depender do tempo

@Getter
@Setter
public class WorkHour {
    private Long id = null;
    private LocalDate date;
    private LocalTime clockIn;
    private LocalTime clockOut;

    public WorkHour(LocalDate date, LocalTime clockIn, LocalTime clockOut){
        this.date = date;
        this.clockIn = clockIn;
        this.clockOut = clockOut;
    }

    
    public WorkHour() {
        this.date = null;
        this.clockIn = null;
        this.clockOut = null;
    }
}
