package com.example.microfinancepi.Controllers;

import com.example.microfinancepi.services.EventChatBotService;
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

    @Autowired
    public EventChatBotRestController(EventChatBotService eventChatbotService) {
        this.eventChatbotService = eventChatbotService;
    }

    @PostMapping("/chatbot/{eventId}")
    public ResponseEntity<String> handleParticipantQuestion(
            @PathVariable Integer eventId,
            @RequestBody Map<String, String> request) {
        String userQuestion = request.get("question");
        String response = eventChatbotService.getResponseForEvent(userQuestion, eventId);
        return ResponseEntity.ok(response);
    }
}
