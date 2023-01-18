package com.petrenko.service;

import com.petrenko.model.Customer;
import com.petrenko.model.Invoice;
import com.petrenko.model.Product;
import com.petrenko.model.TypeInvoice;
import com.petrenko.repository.InvoiceRepository;

import java.util.HashSet;
import java.util.LinkedList;

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

    public Invoice createInvoice(final HashSet<Product> setProducts, final Customer customer) {
        TypeInvoice typeInvoice = typeInvoiceBySummaPrices(setProducts);
        Invoice invoice = new Invoice(setProducts, customer, typeInvoice);
        return invoice;
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

    public void save(final Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    public void print(final Invoice invoice) {
        System.out.println(invoice);
    }

    public LinkedList<Invoice> getAllInvoices() {
        return invoiceRepository.getAll();
    }
}
