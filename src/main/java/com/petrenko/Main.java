package com.petrenko;

import com.petrenko.service.AnalyticOfInvoices;
import com.petrenko.service.ShopService;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        ShopService shopService = ShopService.getInstance();
        try {
            shopService.createAndSaveAndPrintRandomInvoice(15);
            System.out.println();

            AnalyticOfInvoices analyticOfInvoices = new AnalyticOfInvoices(shopService.allInvoices());
            analyticOfInvoices.printAllAnalytic();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }

    }
}

