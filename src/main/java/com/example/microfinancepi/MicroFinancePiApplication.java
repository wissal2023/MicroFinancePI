package com.example.microfinancepi;

import com.example.microfinancepi.services.ECBApiService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSection;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
@EnableScheduling
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")public class MicroFinancePiApplication {

    ECBApiService tmmValueService;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(MicroFinancePiApplication.class, args);
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void fetchTmmValueTask() {
        try {
            float tmmValue = tmmValueService.fetchTmmValue();
            log.info("Fetched TMM Value: {}", tmmValue);
        } catch (IOException e) {
            log.error("Failed to fetch TMM Value", e);
        }
    }



}