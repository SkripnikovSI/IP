package com.example.exam;

import java.io.Serializable;

public class Car implements Serializable {
    public final long id;
    public final String model;
    public final String color;
    public final String number;
    public final String phone;
    public final int time;
    public final int box;

    public Car(long id, String model, String color, String number, String phone, int time, int box) {
        this.id = id;
        this.model = model;
        this.color = color;
        this.number = number;
        this.phone = phone;
        this.time = time;
        this.box = box;
    }
}
