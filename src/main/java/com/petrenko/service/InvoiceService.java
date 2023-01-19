package com.petrenko.service;

import com.petrenko.model.Customer;
import com.petrenko.model.Invoice;
import com.petrenko.model.Product;
import com.petrenko.repository.InvoiceRepository;
import lombok.NonNull;

import java.util.HashSet;

public class InvoiceService {
    private static InvoiceService instance;
    private final InvoiceRepository invoiceRepository;

    private InvoiceService(final InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public static InvoiceService getInstance() {
        if (instance == null) {
            instance = new InvoiceService(InvoiceRepository.getInstance());
        }
        return instance;
    }

    public Invoice createInvoice(@NonNull final HashSet<Product> setProducts, @NonNull final Customer customer) {
        return new Invoice(setProducts, customer);
    }

    public void save(final Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    public void print(final Invoice invoice) {
        if (invoice != null) {
            System.out.println(invoice);
        }
    }

    public HashSet<Invoice> allInvoices() {
        return invoiceRepository.getAll();
    }
}
