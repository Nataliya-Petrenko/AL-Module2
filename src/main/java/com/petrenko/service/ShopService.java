package com.petrenko.service;

import com.petrenko.model.*;
import com.petrenko.repository.InvoiceRepository;
import com.petrenko.repository.PersonRepository;
import com.petrenko.repository.ProductRepository;
import com.petrenko.util.RandomGenerator;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toCollection;

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

    public static ShopService getInstance() throws FileNotFoundException {
        if (instance == null) {
            instance = new ShopService(PersonRepository.getInstance(),
                    ProductRepository.getInstance(),
                    InvoiceRepository.getInstance());
            setProductsToRepositoryFromCsvFile(FILE);
        }
        return instance;
    }

    private static void setProductsToRepositoryFromCsvFile(String file) throws FileNotFoundException {
        if (file == null) {
            throw new FileNotFoundException("File not exist");
        }

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

    public LinkedList<Product> allProduct() {
        return productService.allProduct();
    }

    public void printAllProduct() {
        productService.printAllProduct();
    }

    public long countProductsByCategory(TypeProduct typeProduct) {
        return invoiceService.getAllInvoices()
                .stream()
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

        invoiceService.getAllInvoices().stream()
                .min(Comparator.comparing(invoiceToSumPrices))
                .ifPresent(i -> map.put(invoiceToSumPrices.apply(i), i.getCustomer()));

        return map;
    }

    public int sumAllChecks() {
        return invoiceService.getAllInvoices().stream()
                .map(Invoice::getProducts)
                .flatMap(Collection::stream)
                .mapToInt(Product::getPrice)
                .sum();
    }

    public long countInvoicesRetail() {
        return invoiceService.getAllInvoices().stream()
                .filter(invoice -> invoice.getType().equals(TypeInvoice.RETAIL))
                .count();
    }

    public List<Invoice> invoicesWithTheSameProductType() {

        Function<Invoice, TypeProduct> typeOfFirstProduct = invoice -> invoice.getProducts().stream()
                .findFirst()
                .map(Product::getTypeProduct)
                .orElse(TypeProduct.TELEPHONE);

        Predicate<Invoice> typesAreEquals = invoice -> invoice.getProducts().stream()
                .map(Product::getTypeProduct)
                .allMatch(typeProduct -> typeProduct.equals(typeOfFirstProduct.apply(invoice)));

        return invoiceService.getAllInvoices().stream()
                .filter(typesAreEquals)
                .collect(toCollection(LinkedList::new));
    }

    public List<Invoice> firstThreeChecks() {
        return invoiceService.getAllInvoices().stream()
                .sorted(Comparator.comparing(Invoice::getInvoiceTime))
                .limit(3)
                .collect(toCollection(LinkedList::new));
    }

    public List<Invoice> invoicesOfCustomersLowAge() {
        return invoiceService.getAllInvoices().stream()
                .filter(i -> i.getCustomer().getAge() < 18)
                .peek(i -> i.setType(TypeInvoice.LOW_AGE))
                .collect(toCollection(LinkedList::new));
    }

    public LinkedList<Invoice> invoicesSortByAgeNumberOfProductsBySumOfPrices() {
        Comparator<Invoice> compareByAge = Comparator.comparing(i -> i.getCustomer().getAge());
        Comparator<Invoice> numberOfProducts = Comparator.comparing(i -> i.getProducts().size());
        Comparator<Invoice> sumPricesOfProducts = Comparator.comparing(i -> i.getProducts().stream()
                .mapToInt(Product::getPrice)
                .sum());

        return invoiceService.getAllInvoices().stream()
                .sorted(compareByAge.reversed()
                        .thenComparing(numberOfProducts)
                        .thenComparing(sumPricesOfProducts))
                .collect(toCollection(LinkedList::new));

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

    public void printInvoicesSortByAgeNumberOfProductsBySumOfPrices() {
        System.out.println("Invoices sorted by age then by number of products then by sum of prices:");
        System.out.println(invoicesSortByAgeNumberOfProductsBySumOfPrices());
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
        printInvoicesSortByAgeNumberOfProductsBySumOfPrices();
    }

}
