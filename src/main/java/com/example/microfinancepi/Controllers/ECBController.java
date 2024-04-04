package com.example.microfinancepi.Controllers;

import com.example.microfinancepi.services.ECBApiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ECBController {
    ECBApiService tmmValueService;
    @GetMapping("/tmm")
    public ResponseEntity<Float> getTmmValue() {
        try {
            float tmmValue = tmmValueService.fetchTmmValue();
            return ResponseEntity.ok(tmmValue);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /*
    ECBApiService ecbApiService;

    @GetMapping("/fetch-tmm")
    public ResponseEntity<String> updateTmm() {
        try {
            float tmm = ecbApiService.fetchAndUpdateTmmValue();
            return ResponseEntity.ok("TMM value updated: " + tmm);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update TMM value");
        }
    }

     */
}