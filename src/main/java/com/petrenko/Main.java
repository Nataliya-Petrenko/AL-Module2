package com.petrenko;

import com.petrenko.service.ShopService;
import java.io.FileNotFoundException;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        ShopService shopService = ShopService.getInstance();
        shopService.createAndSaveAndPrintRandomInvoice(15);
        System.out.println();
        shopService.printAllAnalytic();
    }
}

