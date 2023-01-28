package com.petrenko.service;

import com.petrenko.model.Product;
import com.petrenko.exceptions.ProductDescriptionException;
import com.petrenko.model.Telephone;
import com.petrenko.model.Television;
import com.petrenko.repository.ProductRepository;
import com.petrenko.util.ConverterCsvToListOfMap;
import com.petrenko.util.RandomGenerator;
import com.petrenko.util.ReadResources;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;

public class ProductService {
    private final ProductRepository productRepository;
    private static ProductService instance;
    private static final String SEPARATOR_FOR_CSV = ",";

    private ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService(ProductRepository.getInstance());
        }
        return instance;
    }

    public void productsToRepositoryFromCsvFile(String file) throws FileNotFoundException {
        if (file == null) {
            throw new FileNotFoundException();
        }
        List<Map<String, String>> listOfMap;
        if (file.endsWith(".csv")) {
            InputStreamReader input = ReadResources.input(file);
            listOfMap = ConverterCsvToListOfMap.csvToListOfMap(input, SEPARATOR_FOR_CSV);
        } else {
            throw new IllegalArgumentException("Unknown resource-file format. Provide only csv-file");
        }
        for (Map<String, String> map : listOfMap) {
            try {
                Product product = mapToProduct(map);
                productRepository.save(product);
            } catch (ProductDescriptionException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Product mapToProduct(Map<String, String> map) {
        if (map == null) {
            throw new NullPointerException("Map from file not exist");
        }

        final Function<Map<String, String>, Product> function = m -> {
            if (m.get("type").equals("Telephone")) {
                Telephone telephone = new Telephone();
                setFieldsOfProduct(m, telephone);
                return telephone;
            } else if (m.get("type").equals("Television")) {
                Television television = new Television();
                setFieldsOfProduct(m, television);
                return television;
            } else {
                throw new ProductDescriptionException("Type of product in resources-file not exist. " +
                        "This map will not convert to product: " + m);
            }
        };

        return function.apply(map);
    }

    private void setFieldsOfProduct(Map<String, String> map, Telephone telephone) {
//        if (checkFieldExist(map, "series")) {
            telephone.setSeries(map.get("series"));
//        }
//        if (checkFieldExist(map, "model")) {
            telephone.setModel(map.get("model"));
//        }
//        if (checkFieldExist(map, "screen type")) {
            telephone.setScreenType(map.get("screen type"));
//        }
//        if (checkFieldExist(map, "price")) {
            telephone.setPrice(Integer.parseInt(map.get("price")));
//        }
    }

    private void setFieldsOfProduct(Map<String, String> map, Television television) {
//        if (checkFieldExist(map, "series")) {
            television.setSeries(map.get("series"));
//        }
//        if (checkFieldExist(map, "diagonal")) {
            television.setDiagonal(Integer.parseInt(map.get("diagonal")));
//        }
//        if (checkFieldExist(map, "screen type")) {
            television.setScreenType(map.get("screen type"));
//        }
//        if (checkFieldExist(map, "country")) {
            television.setCountry(map.get("country"));
//        }
//        if (checkFieldExist(map, "price")) {
            television.setPrice(Integer.parseInt(map.get("price")));
//        }
    }

//    private boolean checkFieldExist(final Map<String, String> map, final String field) {
//        if (map.get(field).equals("") || map.get(field).equals("none") || field == null) {
//            throw new ProductDescriptionException("One or several description fields of product " +
//                    "in resources-file not exist. " +
//                    "This map will not convert to product: " + map);
//        }
//        return true;
//    }

    public Set<Product> allProduct() {
        return productRepository.getAll();
    }

    public void printAllProduct() {
        productRepository.getAll()
                .forEach(System.out::println);
    }

    public Set<Product> createRandomSetProduct(int numberOfProducts) {
        if (numberOfProducts < 0) {
            numberOfProducts = 0;
        }
        Product[] allProduct = productRepository.getAll().toArray(new Product[0]);
        if (allProduct.length == 0) {
            throw new NullPointerException("Repository of products is empty. You can't create invoice");
        }
        HashSet<Product> newSet = new HashSet<>();

            while (newSet.size() < numberOfProducts) {
                int randomIndex = RandomGenerator.randomNumber(allProduct.length - 1);
                newSet.add(allProduct[randomIndex]);
            }
        return newSet;
    }
}
