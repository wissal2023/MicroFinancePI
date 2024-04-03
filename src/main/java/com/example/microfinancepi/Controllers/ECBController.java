package com.example.microfinancepi.Controllers;

import com.example.microfinancepi.services.ECBApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ECBController {

    private final ECBApiService ecbApiService;

    @Autowired
    public ECBController(ECBApiService ecbApiService) {
        this.ecbApiService = ecbApiService;
    }

    @GetMapping("/tmm")
    public String getTMMRate() {
        return ecbApiService.fetchTMMRate();
    }
}