package com.example.microfinancepi.Controllers;

import com.example.microfinancepi.entities.Event;
import com.example.microfinancepi.services.EventChatBotService;
import com.example.microfinancepi.services.EventService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@SecurityRequirement(name = "bearerAuth")

@RestController
public class EventChatBotRestController {
    EventChatBotService eventChatbotService;
    EventService eventService;


    public EventChatBotRestController(EventChatBotService eventChatbotService) {
        this.eventChatbotService = eventChatbotService;
    }
    @Autowired
    public EventChatBotRestController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/chatbot/{eventName}")
    public ResponseEntity<String> handleParticipantQuestion(
            @PathVariable String eventName,
            @RequestBody Map<String, String> request) {
        String userQuestion = request.get("question");
        String response = eventChatbotService.getResponseForEvent(userQuestion, eventName);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/chatbot/recommendation")
    public ResponseEntity<String> handleRecommendationQuestion() {
        String response = eventService.getRecommendationResponse();
        if (response != null && !response.isEmpty()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok("No recommended events found.");
        }
    }
}
