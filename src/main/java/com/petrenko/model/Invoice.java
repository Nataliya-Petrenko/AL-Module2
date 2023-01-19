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

    public Invoice(HashSet<Product> products, Customer customer) {
        this.id = UUID.randomUUID().toString();
        this.products = products;
        this.customer = customer;
        this.type = typeInvoiceBySummaPrices(products);
        this.invoiceTime = LocalDateTime.now();
    }

    private TypeInvoice typeInvoiceBySummaPrices(final HashSet<Product> setProducts) {
        int summaPrices = summaPricesFromHashSetProduct(setProducts);
        return summaPrices <= Invoice.LIMIT ? TypeInvoice.RETAIL : TypeInvoice.WHOLESALE;
    }

    private int summaPricesFromHashSetProduct(final HashSet<Product> setProducts) {
        return setProducts.stream()
                .map(Product::getPrice)
                .reduce(0, Integer::sum);
    }

    @Override
    public String toString() {
        return "[" + invoiceTime + "] [" + customer + "] [" + products + "]";
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
