package com.petrenko.service;

import com.petrenko.exceptions.ProductDescriptionException;
import com.petrenko.model.*;
import com.petrenko.repository.InvoiceRepository;
import com.petrenko.repository.PersonRepository;
import com.petrenko.repository.ProductRepository;
import com.petrenko.util.RandomGenerator;

import java.io.*;
import java.util.*;

public class ShopService {
    private static ShopService instance;
    private final PersonRepository personRepository;
    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;
    private final PersonService personService = PersonService.getInstance();
    private static final ProductService productService = ProductService.getInstance();
    private final InvoiceService invoiceService = InvoiceService.getInstance();

    private static final String FILE = "products.csv";
    private static final int MAX_PRODUCTS_IN_ONE_RANDOM_INVOICE = 5;

    private ShopService(final PersonRepository personRepository,
                        final ProductRepository productRepository,
                        final InvoiceRepository invoiceRepository) {
        this.personRepository = personRepository;
        this.productRepository = productRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public static ShopService getInstance() {
        if (instance == null) {
            instance = new ShopService(PersonRepository.getInstance(),
                    ProductRepository.getInstance(),
                    InvoiceRepository.getInstance());
            setProductsToRepositoryFromCsvFile(FILE);
        }
        return instance;
    }

    private static void setProductsToRepositoryFromCsvFile(String file) {
        try {
            productService.productsToRepositoryFromCsvFile(file);
        } catch (NullPointerException | FileNotFoundException | ProductDescriptionException e) {
            System.out.println(e.getMessage());
        }
    }

    public HashSet<Invoice> createAndSaveAndPrintRandomInvoice(int numberOfInvoices) {
        HashSet<Invoice> invoices = new HashSet<>();
        while (invoices.size() < numberOfInvoices) {
            invoices.add(createAndSaveAndPrintRandomInvoice());
        }
        return invoices;
    }

    public Invoice createAndSaveAndPrintRandomInvoice() {
        final int numberOfProducts = RandomGenerator.randomNumber(MAX_PRODUCTS_IN_ONE_RANDOM_INVOICE) + 1;
        HashSet<Product> setProducts = productService.createRandomSetProduct(numberOfProducts);
        Customer customer = personService.createAndSaveCustomerByRandom();

        Invoice invoice = invoiceService.createInvoice(setProducts, customer);

        invoiceService.save(invoice);

        invoiceService.print(invoice);
        return invoice;
    }

    public HashSet<Customer> allCustomer() {
        return personService.allCustomer();
    }
    public HashSet<Product> allProduct() {
        return productService.allProduct();
    }
    public HashSet<Invoice> allInvoices() {
        return invoiceService.allInvoices();
    }

    public void printAllProduct() {
        productService.printAllProduct();
    }

}
