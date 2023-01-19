package com.petrenko;

import com.petrenko.model.*;
import com.petrenko.service.AnalyticOfInvoices;
import com.petrenko.service.ShopService;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        ShopService shopService = ShopService.getInstance();
        shopService.createAndSaveAndPrintRandomInvoice(15);
        System.out.println();

        AnalyticOfInvoices analyticOfInvoices = new AnalyticOfInvoices(shopService.allInvoices());
        analyticOfInvoices.printAllAnalytic();





    }
}

