package com.ageinghippy.data.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Meal {
    private int id;
    private LocalDate date;
    private LocalTime time;

    public Meal() {
    }

    public Meal(int id, LocalDate date, LocalTime time) {
        this.id = id;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
