package com.petrenko.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
public class Invoice {
    public static final int LIMIT = 10_000;
    private final String id;
    private HashSet<Product> products;
    private Customer customer;
    private TypeInvoice type;
    private LocalDateTime invoiceTime;

    public Invoice(HashSet<Product> products, Customer customer, TypeInvoice type) {
        this.id = UUID.randomUUID().toString();
        this.products = products;
        this.customer = customer;
        this.type = type;
        this.invoiceTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "[" + invoiceTime + "] [" + customer + "] [" + products + "]"; //[time] [user-data] [invoice-data]
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(id, invoice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
