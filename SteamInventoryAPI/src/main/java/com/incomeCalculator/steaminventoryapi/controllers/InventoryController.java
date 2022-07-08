package com.incomeCalculator.steaminventoryapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
public class InventoryController {

    private static final Logger log = LoggerFactory.getLogger(InventoryController.class);

    @GetMapping("/inventory")
    public String getInventory() {
        try {
            Process process = Runtime.getRuntime().exec("ruby getInventory.rb");
            process.waitFor();

            BufferedReader processIn = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = processIn.readLine()) != null) {
                log.info(line);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        return "Inventory";
    }

}
