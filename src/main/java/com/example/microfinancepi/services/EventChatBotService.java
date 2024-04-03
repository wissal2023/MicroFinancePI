package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.Event;
import com.example.microfinancepi.entities.EventStatus;
import com.example.microfinancepi.repositories.IEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventChatBotService {
    private final IEventRepository eventRepository;
    EventService eventService;

    @Autowired
    public EventChatBotService(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public String getResponseForEvent(String userQuestion, String eventName) {
        Event event = eventRepository.findByEventName(eventName);
        if (event != null) {
            switch (userQuestion) {
                case "What date does the event start?":
                case "date?":
                case "date":
                case "when":
                case "when?":
                case "What date does the event start":
                    if (event.getEventStatus() == EventStatus.PLANNED) {
                        return "The event " + event.getNameEvent() + " starts on " + event.getDateEvent() + " starting at 10 am.";
                    } else if (event.getEventStatus() == EventStatus.CANCELLED) {
                        return "The event " + event.getNameEvent() + " has been cancelled.";
                    } else if (event.getEventStatus() == EventStatus.REPORTED) {
                        return "The event " + event.getNameEvent() + " has been reported.";
                    }
                case "What type of event is this?":
                case "type?":
                case "type":
                case "What type of event is this":
                    if (event.getEventStatus() == EventStatus.PLANNED) {
                        return "The event " + event.getNameEvent() + " is of type " + event.getType() + ".";
                    } else if (event.getEventStatus() == EventStatus.CANCELLED) {
                        return "The event " + event.getNameEvent() + " has been cancelled.";
                    } else if (event.getEventStatus() == EventStatus.REPORTED) {
                        return "The event " + event.getNameEvent() + " has been reported.";
                    }
                case "Can you provide the description of this event?":
                case "description?":
                case "description":
                case "Can you provide the description of this event":
                    if (event.getEventStatus() == EventStatus.PLANNED) {
                        return "The event " + event.getNameEvent() + " has the following description: " + event.getDescriptionEvent() + ".";
                    } else if (event.getEventStatus() == EventStatus.CANCELLED) {
                        return "The event " + event.getNameEvent() + " has been cancelled.";
                    } else if (event.getEventStatus() == EventStatus.REPORTED) {
                        return "The event " + event.getNameEvent() + " has been reported.";
                    }
                default:
                    return "Sorry, I don't have an answer for this question.";
            }
        } else {
            return "No events found with name" + eventName;
        }



    }}
