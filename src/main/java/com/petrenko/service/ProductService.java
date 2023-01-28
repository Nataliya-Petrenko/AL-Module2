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
            telephone.setSeries(map.get("series"));
            telephone.setModel(map.get("model"));
            telephone.setScreenType(map.get("screen type"));
            telephone.setPrice(Integer.parseInt(map.get("price")));
    }

    private void setFieldsOfProduct(Map<String, String> map, Television television) {
            television.setSeries(map.get("series"));
            television.setDiagonal(Integer.parseInt(map.get("diagonal")));
            television.setScreenType(map.get("screen type"));
            television.setCountry(map.get("country"));
            television.setPrice(Integer.parseInt(map.get("price")));
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
