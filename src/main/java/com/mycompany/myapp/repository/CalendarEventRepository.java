package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CalendarEvent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CalendarEvent entity.
 */
@SuppressWarnings("unused")
public interface CalendarEventRepository extends JpaRepository<CalendarEvent,Long> {

    @Query("select calendarEvent from CalendarEvent calendarEvent where calendarEvent.owner.login = ?#{principal.username}")
    List<CalendarEvent> findByOwnerIsCurrentUser();

}
