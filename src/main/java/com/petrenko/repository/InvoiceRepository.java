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
    public LinkedList<Invoice> getAll() {                        // TODO collection vs array?
        return new LinkedList<>(INVOICES.values());
    }

    @Override
    public Optional<Invoice> getById(String id) {
        return Optional.ofNullable(INVOICES.get(id));
    }

    @Override
    public void deleteById(String id) {
        INVOICES.remove(id);
    }
}
