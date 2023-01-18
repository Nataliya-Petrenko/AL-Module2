package com.petrenko.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Television extends Product {
    private String series;
    private int diagonal;
    private String screenType;
    private String country;
    private int price;

    public Television() {
        super(TypeProduct.TELEVISION);
    }

    public Television(String series, int diagonal, String screenType, String country, int price) {
        super(TypeProduct.TELEVISION);
        this.series = series;
        this.diagonal = diagonal;
        this.screenType = screenType;
        this.country = country;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Television{" +
                "id='" + super.getId() + '\'' +
                ", series='" + series + '\'' +
                ", diagonal='" + diagonal + '\'' +
                ", screenType='" + screenType + '\'' +
                ", country='" + country + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Television that = (Television) o;
        return diagonal == that.diagonal &&
                price == that.price &&
                Objects.equals(series, that.series) &&
                Objects.equals(screenType, that.screenType) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(series, diagonal, screenType, country, price);
    }
}
