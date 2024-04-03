package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.Event;
import com.example.microfinancepi.repositories.IEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventChatBotService {
    IEventRepository eventRepository;

    @Autowired
    public EventChatBotService(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event getEvent(Integer eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        return optionalEvent.orElse(null);
    }

    public String getResponseForEvent(String userQuestion, Integer eventId) {
        Event event = getEvent(eventId);
        if (event != null) {
            switch (userQuestion) {
                case "Quelle heure commence l'événement ?":
                    return "L'événement " + event.getNameEvent() + " commence à " + event.getDateEvent() + ".";
                case "Où se trouve l'événement ?":
                    return "L'événement " + event.getNameEvent() + " a lieu au Centre de conventions XYZ.";
                default:
                    return "Désolé, je n'ai pas de réponse pour cette question.";
            }
        } else {
            return "Aucun événement trouvé avec l'ID " + eventId;
        }
    }
}
