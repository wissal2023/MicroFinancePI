package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.OfferLoan;
import com.example.microfinancepi.repositories.OfferLoanRepository;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sun.activation.registries.LogSupport.log;


@Slf4j
@Service
    public class ECBApiService {
    public float fetchTmmValue() throws IOException {
        float tmmValue = 0.0f;
        try (WebClient client = new WebClient()) {
            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(false);
            HtmlPage page = client.getPage("https://tunisiacompetitiveness.tn/sectors/category/secteur-monetaire");
            List<HtmlTable> tables = page.getByXPath("//table");
            if (!tables.isEmpty()) {
                HtmlTable table = tables.get(0);
                List<TmmValue> tmmValues = table.getBodies().get(0).getRows().stream()
                        .map(row -> new TmmValue(row.getCell(0).getTextContent(),
                                row.getCell(1).getTextContent()))
                        .collect(Collectors.toList());
                Optional<TmmValue> tmmValueOptional = tmmValues.stream()
                        .filter(value -> value.getIndicator().contains("Taux du marché monétaire"))
                        .findFirst();
                if (tmmValueOptional.isPresent()) {
                    tmmValue = tmmValueOptional.get().getValue();
                    log.info("Found TMM Value: {}", tmmValue);
                } else {
                    log.warn("TMM Value not found.");
                }
            } else {
                log.warn("No table found on the page.");
            }
        } catch (IOException e) {
            log.error("Error fetching TMM value", e);
            throw e;
        }
        return tmmValue;
    }
    private static class TmmValue {
        private final String indicator;
        private final float value;

        public TmmValue(String indicator, String valueAsString) {
            this.indicator = indicator;
            this.value = Float.parseFloat(valueAsString);
        }

        public String getIndicator() {
            return indicator;
        }
        public float getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Indicator: " + indicator + ", Value: " + value;
        }
    }


    /*
    OfferLoan offerLoan;
    public float fetchAndUpdateTmmValue() throws IOException {
        Document doc = Jsoup.connect("https://tunisiacompetitiveness.tn/sectors/category/secteur-monetaire").get();

        Elements tmmElements = doc.select("tbody#detailsNat");
        for (Element row : tmmElements.select("tr")) {
            Elements columns = row.select("td");
            String indicator = columns.get(0).text();
            if (indicator.contains("Taux du marché monétaire")) {
                String tmmString = columns.get(1).text().trim();
                float tmm = Float.parseFloat(tmmString);
                offerLoan.setTmm(tmm);
                return tmm;
            }
        }
        throw new RuntimeException("TMM value not found on the webpage");
    }



     */
//web scrapping
}