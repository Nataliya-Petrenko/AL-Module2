package com.petrenko.repository;

import com.petrenko.model.Invoice;

import java.util.*;

public class InvoiceRepository implements Crud<Invoice> {
    private static InvoiceRepository instance;
    private static final Map<String, Invoice> INVOICES = new HashMap<>();

    private InvoiceRepository() {
    }

    public static InvoiceRepository getInstance() {
        if (instance == null) {
            instance = new InvoiceRepository();
        }
        return instance;
    }

    @Override
    public void save(Invoice value) {
        if (value == null || INVOICES.get(value.getId()) != null) {
            return;
        }
        INVOICES.put(value.getId(), value);
    }

    @Override
    public Set<Invoice> getAll() {
        return new HashSet<>(INVOICES.values());
    }

}
