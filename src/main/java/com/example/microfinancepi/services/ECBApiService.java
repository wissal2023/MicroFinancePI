package com.example.microfinancepi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

    @Service
    public class ECBApiService {

        private final RestTemplate restTemplate;

        @Autowired
        public ECBApiService(RestTemplate restTemp) {
            restTemplate = restTemp;
        }

        public String fetchTMMRate() {
            String url = "https://api.ecb.europa.eu/rates/api/data/YourSpecificEndpoint";
            // Replace "YourSpecificEndpoint" with the actual endpoint for the TMM or equivalent rate.
            return this.restTemplate.getForObject(url, String.class);
            // This assumes a simple scenario. You might need to map the response to a POJO.
        }
    }

