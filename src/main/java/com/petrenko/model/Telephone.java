package com.petrenko.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Telephone extends Product {
    private String series;
    private String model;
    private String screenType;
    private int price;

    public Telephone() {
        super(TypeProduct.TELEPHONE);
    }

    public Telephone(String series, String model, String screenType, int price) {
        super(TypeProduct.TELEPHONE);
        this.series = series;
        this.model = model;
        this.screenType = screenType;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Telephone{" +
                "id='" + super.getId() + '\'' +
                ", series='" + series + '\'' +
                ", model='" + model + '\'' +
                ", screenType='" + screenType + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Telephone telephone = (Telephone) o;
        return Objects.equals(series, telephone.series) &&
                Objects.equals(model, telephone.model) &&
                Objects.equals(screenType, telephone.screenType) &&
                Objects.equals(price, telephone.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(series, model, screenType, price);
    }
}
