package br.com.microservices.orchestrated.orderservice.core.service;

import br.com.microservices.orchestrated.orderservice.config.exception.ValidationException;
import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.dto.EventFilters;
import br.com.microservices.orchestrated.orderservice.core.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public void notifyEnding(Event event) {
        event.setOrderId(event.getId());
        event.setCreatedAt(LocalDateTime.now());

        save(event);
        log.info("Order {} with saga notified! TransactionId: {}", event.getOrderId(), event.getTransactionId());
    }

    public List<Event> findAll() {
        return eventRepository.findAllByOrderByCreatedAtDesc();
    }

    public Event findByFilters(EventFilters eventFilters) {
        validateEmptyFilters(eventFilters);

        if (!isEmpty(eventFilters.getOrderId())) {
            return findByOrderId(eventFilters.getOrderId());
        } else {
            return findByTransactionId(eventFilters.getTransactionId());
        }
    }

    private Event findByOrderId(String orderId) {
        return eventRepository.findTop1ByOrderIdOrderByCreatedAtDesc(orderId)
                .orElseThrow(() -> new ValidationException("Event not found by orderID."));
    }

    private Event findByTransactionId(String transactionId) {
        return eventRepository.findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId)
                .orElseThrow(() -> new ValidationException("Event not found by transactionID."));
    }

    private void validateEmptyFilters(EventFilters eventFilters) {
        if (isEmpty(eventFilters.getOrderId()) && isEmpty(eventFilters.getTransactionId())) {
            throw new ValidationException("OrderID or TransactionID must be informed.");
        }
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }
}