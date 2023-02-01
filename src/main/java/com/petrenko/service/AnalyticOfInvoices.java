package com.petrenko.service;

import com.petrenko.model.*;
import lombok.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toCollection;

public class AnalyticOfInvoices {
    private final Set<Invoice> invoiceSet;

    public AnalyticOfInvoices(Set<Invoice> invoiceSet) {
        this.invoiceSet = invoiceSet;
    }


    public long countProductsByCategory(@NonNull TypeProduct typeProduct) {
        return invoiceSet.stream()
                .map(Invoice::getProducts)
                .flatMap(Collection::stream)
                .filter(p -> p.getTypeProduct().equals(typeProduct))
                .count();
    }

    public Map<Integer, Customer> sumSmallestCheckAndBuyer() {
        Map<Integer, Customer> map = new HashMap<>();
        Function<Invoice, Integer> invoiceToSumPrices = i -> i.getProducts()
                .stream()
                .mapToInt(Product::getPrice)
                .sum();

        invoiceSet.stream()
                .min(Comparator.comparing(invoiceToSumPrices))
                .ifPresent(i -> map.put(invoiceToSumPrices.apply(i), i.getCustomer()));

        return map;
    }

    public int sumAllChecks() {
        return invoiceSet.stream()
                .map(Invoice::getProducts)
                .flatMap(Collection::stream)
                .mapToInt(Product::getPrice)
                .sum();
    }

    public long countInvoicesRetail() {
        return invoiceSet.stream()
                .filter(invoice -> invoice.getType().equals(TypeInvoice.RETAIL))
                .count();
    }

    public Set<Invoice> invoicesWithTheSameProductType() {

        Function<Invoice, TypeProduct> typeOfFirstProduct = invoice -> invoice.getProducts().stream()
                .findFirst()
                .map(Product::getTypeProduct)
                .orElse(TypeProduct.TELEPHONE);

        Predicate<Invoice> typesAreEquals = invoice -> invoice.getProducts().stream()
                .map(Product::getTypeProduct)
                .allMatch(typeProduct -> typeProduct.equals(typeOfFirstProduct.apply(invoice)));

        return invoiceSet.stream()
                .filter(typesAreEquals)
                .collect(toCollection(HashSet::new));
    }

    public LinkedList<Invoice> firstThreeChecks() {
        return invoiceSet.stream()
                .sorted(Comparator.comparing(Invoice::getInvoiceTime))
                .limit(3)
                .collect(toCollection(LinkedList::new));
    }

    public Set<Invoice> invoicesOfCustomersLowAge() {
        return invoiceSet.stream()
                .filter(i -> i.getCustomer().getAge() < 18)
                .peek(i -> i.setType(TypeInvoice.LOW_AGE))
                .collect(toCollection(HashSet::new));
    }

    public LinkedHashSet<Invoice> invoicesSortByReversAgeByNumberOfProductsBySumOfPrices() {
        Comparator<Invoice> compareByAge = Comparator.comparing(i -> i.getCustomer().getAge());
        Comparator<Invoice> numberOfProducts = Comparator.comparing(i -> i.getProducts().size());
        Comparator<Invoice> sumPricesOfProducts = Comparator.comparing(i -> i.getProducts().stream()
                .mapToInt(Product::getPrice)
                .sum());

        return invoiceSet.stream()
                .sorted(compareByAge.reversed()
                        .thenComparing(numberOfProducts)
                        .thenComparing(sumPricesOfProducts))
                .collect(toCollection(LinkedHashSet::new));

    }

    public void printCountProductsByCategory(TypeProduct typeProduct) {
        System.out.println("Count products by category " + typeProduct + ": " +
                countProductsByCategory(typeProduct));
        System.out.println();
    }

    public void printSumSmallestCheckAndBuyer() {
        System.out.println("Summa smallest check and buyer: ");
        System.out.println(sumSmallestCheckAndBuyer());
        System.out.println();
    }

    public void printSumAllChecks() {
        System.out.println("Summa all checks: " + sumAllChecks());
        System.out.println();
    }

    public void printCountInvoicesRetail() {
        System.out.println("Count invoices retail: " + countInvoicesRetail());
        System.out.println();
    }

    public void printInvoicesWithTheSameProductType() {
        System.out.println("Invoices with the same product type:");
        System.out.println(invoicesWithTheSameProductType());
        System.out.println();
    }

    public void printFirstThreeChecks() {
        System.out.println("First three checks:");
        System.out.println(firstThreeChecks());
        System.out.println();
    }

    public void printInvoicesOfCustomersLowAge() {
        System.out.println("Invoices of customers LowAge:");
        System.out.println(invoicesOfCustomersLowAge());
        System.out.println();
    }

    public void printInvoicesSortByReversAgeByNumberOfProductsBySumOfPrices() {
        System.out.println("Invoices sorted by age then by number of products then by sum of prices:");
        System.out.println(invoicesSortByReversAgeByNumberOfProductsBySumOfPrices());
        System.out.println();
    }

    public void printAllAnalytic() {
        printCountProductsByCategory(TypeProduct.TELEPHONE);
        printCountProductsByCategory(TypeProduct.TELEVISION);
        printSumSmallestCheckAndBuyer();
        printSumAllChecks();
        printCountInvoicesRetail();
        printInvoicesWithTheSameProductType();
        printFirstThreeChecks();
        printInvoicesOfCustomersLowAge();
        printInvoicesSortByReversAgeByNumberOfProductsBySumOfPrices();
    }
}
