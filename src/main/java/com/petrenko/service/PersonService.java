package com.petrenko.service;

import com.petrenko.model.Customer;
import com.petrenko.repository.PersonRepository;
import com.petrenko.util.RandomGenerator;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

public class PersonService {
    private static PersonService instance;
    private final PersonRepository personRepository;

    private PersonService(final PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public static PersonService getInstance() {
        if (instance == null) {
            instance = new PersonService(PersonRepository.getInstance());
        }
        return instance;
    }

    public Customer createAndSaveCustomerByRandom() {
        Customer customer = new Customer(createEmail(), RandomGenerator.randomNumber());
        personRepository.save(customer);
        return customer;
    }

    public Set<Customer> allCustomer() {
        return personRepository.getAll();
    }

    private String createEmail() {
        String newEmail = "";
        do {
            newEmail = RandomGenerator.randomText() + "@ukr.net";
        } while (checkEmailAlreadyExist(newEmail));
        return newEmail;
    }

    private boolean checkEmailAlreadyExist(@NonNull final String email) {
        return personRepository.getAll().stream()
                .anyMatch(customer -> customer.getEmail().equals(email));
    }
}
