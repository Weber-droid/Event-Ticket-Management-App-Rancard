package com.rancard.eventmanagement.app.repository;

import com.rancard.eventmanagement.app.model.Event;
import com.rancard.eventmanagement.app.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByEvent(Event event);

    long countByPurchaserIsNotNull();

    long countByScannedTrue();
}
