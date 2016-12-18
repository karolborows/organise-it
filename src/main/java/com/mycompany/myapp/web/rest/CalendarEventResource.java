package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.CalendarEvent;

import com.mycompany.myapp.repository.CalendarEventRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CalendarEvent.
 */
@RestController
@RequestMapping("/api")
public class CalendarEventResource {

    private final Logger log = LoggerFactory.getLogger(CalendarEventResource.class);

    @Inject
    private CalendarEventRepository calendarEventRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /calendar-events : Create a new calendarEvent.
     *
     * @param calendarEvent the calendarEvent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new calendarEvent, or with status 400 (Bad Request) if the calendarEvent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/calendar-events")
    @Timed
    public ResponseEntity<CalendarEvent> createCalendarEvent(@Valid @RequestBody CalendarEvent calendarEvent) throws URISyntaxException {
        log.debug("REST request to save CalendarEvent : {}", calendarEvent);
        if (calendarEvent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("calendarEvent", "idexists", "A new calendarEvent cannot already have an ID")).body(null);
        }
        calendarEvent.setOwner(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get());
        CalendarEvent result = calendarEventRepository.save(calendarEvent);
        return ResponseEntity.created(new URI("/api/calendar-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("calendarEvent", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /calendar-events : Updates an existing calendarEvent.
     *
     * @param calendarEvent the calendarEvent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated calendarEvent,
     * or with status 400 (Bad Request) if the calendarEvent is not valid,
     * or with status 500 (Internal Server Error) if the calendarEvent couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/calendar-events")
    @Timed
    public ResponseEntity<CalendarEvent> updateCalendarEvent(@Valid @RequestBody CalendarEvent calendarEvent) throws URISyntaxException {
        log.debug("REST request to update CalendarEvent : {}", calendarEvent);
        if (calendarEvent.getId() == null) {
            return createCalendarEvent(calendarEvent);
        }
        CalendarEvent result = calendarEventRepository.save(calendarEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("calendarEvent", calendarEvent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /calendar-events : get all the calendarEvents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of calendarEvents in body
     */
    @GetMapping("/calendar-events")
    @Timed
    public List<CalendarEvent> getAllCalendarEvents() {
        log.debug("REST request to get all CalendarEvents");
        List<CalendarEvent> calendarEvents = calendarEventRepository.findByOwnerIsCurrentUser();
        return calendarEvents;
    }

    /**
     * GET  /calendar-events/:id : get the "id" calendarEvent.
     *
     * @param id the id of the calendarEvent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the calendarEvent, or with status 404 (Not Found)
     */
    @GetMapping("/calendar-events/{id}")
    @Timed
    public ResponseEntity<CalendarEvent> getCalendarEvent(@PathVariable Long id) {
        log.debug("REST request to get CalendarEvent : {}", id);
        CalendarEvent calendarEvent = calendarEventRepository.findOne(id);
        return Optional.ofNullable(calendarEvent)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /calendar-events/:id : delete the "id" calendarEvent.
     *
     * @param id the id of the calendarEvent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/calendar-events/{id}")
    @Timed
    public ResponseEntity<Void> deleteCalendarEvent(@PathVariable Long id) {
        log.debug("REST request to delete CalendarEvent : {}", id);
        calendarEventRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("calendarEvent", id.toString())).build();
    }

}
