package com.petrenko.service;

import com.petrenko.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class AnalyticOfInvoicesTest {
    private AnalyticOfInvoices target;
    private final HashSet<Invoice> invoiceSet = new HashSet<>();
    private final Customer[] customer = new Customer[4];
    private final Invoice[] invoice = new Invoice[4];

    @BeforeEach
    void setUp() throws InterruptedException {
        Product[] product = new Product[10];

        Set<Product> setOfProducts1 = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            product[i] = new Telephone("s" + i, "m" + i, "st" + i, i * 100);
            setOfProducts1.add(product[i]);
        }

        Set<Product> setOfProducts2 = new HashSet<>();
        for (int i = 5; i < 8; i++) {
            product[i] = new Television("s" + i, i * 10, "st" + i, "c" + i, i * 1000);
            setOfProducts2.add(product[i]);
        }

        Set<Product> setOfProducts3 = new HashSet<>(); // sumPrices = (0+3)*100 + (5+7)*1000 = 12300
        setOfProducts3.add(product[0]);
        setOfProducts3.add(product[3]);
        setOfProducts3.add(product[5]);
        setOfProducts3.add(product[7]);


        Set<Product> setOfProducts4 = new HashSet<>(); // sumPrices = (1+4)*100 + 6*1000 = 6400
        setOfProducts4.add(product[1]);
        setOfProducts4.add(product[4]);
        setOfProducts4.add(product[6]);

        for (int i = 0; i < 4; i++) {
            customer[i] = new Customer("email_" + i + "@Ukr.net", (i + 1) * 10);
        }

        // sumPrices = (0+1+2+3+4)*100=1000; productsType = all Telephone (5); customerAge = 10
        invoice[0] = new Invoice(setOfProducts1, customer[0]);
        Thread.sleep(1);
        //sumPrices = (5+6+7)*1000=18000; productsType = all Television (3); customerAge = 20
        invoice[1] = new Invoice(setOfProducts2, customer[1]);
        Thread.sleep(1);
        // sumPrices = (0+3)*100 + (5+7)*1000 = 12300; productsType = Telephone (2), Television (2); customerAge = 30
        invoice[2] = new Invoice(setOfProducts3, customer[2]);
        Thread.sleep(1);
        // sumPrices = (1+4)*100 + 6*1000 = 6500; productsType = Telephone (2), Television (1); customerAge = 40
        invoice[3] = new Invoice(setOfProducts4, customer[3]);

        invoiceSet.addAll(List.of(invoice));

        target = new AnalyticOfInvoices(invoiceSet);
    }

    @Test
    public void countProductsByCategory_Test_TypeProductTelephone() {
        final long expected = 9;
        final long actual = target.countProductsByCategory(TypeProduct.TELEPHONE);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void countProductsByCategory_Test_TypeProductTelevision() {
        final long expected = 6;
        final long actual = target.countProductsByCategory(TypeProduct.TELEVISION);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void sumSmallestCheckAndBuyer_Test() {
        final Map<Integer, Customer> expected = new HashMap<>();
        expected.put(1000, customer[0]);
        final Map<Integer, Customer> actual = target.sumSmallestCheckAndBuyer();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void sumAllChecks_Test() {
        final int expected = 1000 + 18000 + 12300 + 6500;
        final int actual = target.sumAllChecks();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void countInvoicesRetail_Test() {
        final long expected = 2;
        final long actual = target.countInvoicesRetail();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void invoicesWithTheSameProductType_Test() {
        final Set<Invoice> expected = new HashSet<>();
        expected.add(invoice[0]);
        expected.add(invoice[1]);
        final Set<Invoice> actual = target.invoicesWithTheSameProductType();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void firstThreeChecks_Test() throws InterruptedException {
        final LinkedList<Invoice> expected = new LinkedList<>();
        expected.add(invoice[0]);
        expected.add(invoice[1]);
        expected.add(invoice[2]);
        final LinkedList<Invoice> actual = target.firstThreeChecks();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void invoicesOfCustomersLowAge_Test_CheckCustomers() {
        final Set<Invoice> expected = new HashSet<>();
        expected.add(invoice[0]);
        final Set<Invoice> actual = target.invoicesOfCustomersLowAge();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void invoicesOfCustomersLowAge_Test_CheckChangedType() {
        final Set<Invoice> expectedSet = new HashSet<>();
        expectedSet.add(invoice[0]);
        final Optional<TypeInvoice> expected = Optional.of(TypeInvoice.LOW_AGE);

        final Set<Invoice> actualSet = target.invoicesOfCustomersLowAge();
        final Optional<TypeInvoice> actual = actualSet.stream()
                .map(Invoice::getType)
                .findAny();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void invoicesSortByReversAgeByNumberOfProductsBySumOfPrices_Test() {
        final LinkedHashSet<Invoice> expected = new LinkedHashSet<>();
        expected.add(invoice[3]);
        expected.add(invoice[2]);
        expected.add(invoice[1]);
        expected.add(invoice[0]);
        final LinkedHashSet<Invoice> actual = target.invoicesSortByReversAgeByNumberOfProductsBySumOfPrices();
        Assertions.assertEquals(expected, actual);
    }

}