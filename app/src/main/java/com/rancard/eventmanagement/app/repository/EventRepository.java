package com.rancard.eventmanagement.app.repository;

import com.rancard.eventmanagement.app.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import com.rancard.eventmanagement.app.model.User;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByOwner(User owner);
}