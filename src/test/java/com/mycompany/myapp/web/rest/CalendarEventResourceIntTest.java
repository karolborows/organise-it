package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.OrganiseItApp;

import com.mycompany.myapp.domain.CalendarEvent;
import com.mycompany.myapp.repository.CalendarEventRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CalendarEventResource REST controller.
 *
 * @see CalendarEventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrganiseItApp.class)
public class CalendarEventResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Inject
    private CalendarEventRepository calendarEventRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCalendarEventMockMvc;

    private CalendarEvent calendarEvent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CalendarEventResource calendarEventResource = new CalendarEventResource();
        ReflectionTestUtils.setField(calendarEventResource, "calendarEventRepository", calendarEventRepository);
        this.restCalendarEventMockMvc = MockMvcBuilders.standaloneSetup(calendarEventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalendarEvent createEntity(EntityManager em) {
        CalendarEvent calendarEvent = new CalendarEvent()
                .title(DEFAULT_TITLE)
                .start(DEFAULT_START)
                .end(DEFAULT_END)
                .description(DEFAULT_DESCRIPTION);
        return calendarEvent;
    }

    @Before
    public void initTest() {
        calendarEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createCalendarEvent() throws Exception {
        int databaseSizeBeforeCreate = calendarEventRepository.findAll().size();

        // Create the CalendarEvent

        restCalendarEventMockMvc.perform(post("/api/calendar-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calendarEvent)))
            .andExpect(status().isCreated());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEvents = calendarEventRepository.findAll();
        assertThat(calendarEvents).hasSize(databaseSizeBeforeCreate + 1);
        CalendarEvent testCalendarEvent = calendarEvents.get(calendarEvents.size() - 1);
        assertThat(testCalendarEvent.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCalendarEvent.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testCalendarEvent.getEnd()).isEqualTo(DEFAULT_END);
        assertThat(testCalendarEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = calendarEventRepository.findAll().size();
        // set the field null
        calendarEvent.setStart(null);

        // Create the CalendarEvent, which fails.

        restCalendarEventMockMvc.perform(post("/api/calendar-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(calendarEvent)))
            .andExpect(status().isBadRequest());

        List<CalendarEvent> calendarEvents = calendarEventRepository.findAll();
        assertThat(calendarEvents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCalendarEvents() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get all the calendarEvents
        restCalendarEventMockMvc.perform(get("/api/calendar-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendarEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(sameInstant(DEFAULT_START))))
            .andExpect(jsonPath("$.[*].end").value(hasItem(sameInstant(DEFAULT_END))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getCalendarEvent() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);

        // Get the calendarEvent
        restCalendarEventMockMvc.perform(get("/api/calendar-events/{id}", calendarEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(calendarEvent.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.start").value(sameInstant(DEFAULT_START)))
            .andExpect(jsonPath("$.end").value(sameInstant(DEFAULT_END)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCalendarEvent() throws Exception {
        // Get the calendarEvent
        restCalendarEventMockMvc.perform(get("/api/calendar-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCalendarEvent() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);
        int databaseSizeBeforeUpdate = calendarEventRepository.findAll().size();

        // Update the calendarEvent
        CalendarEvent updatedCalendarEvent = calendarEventRepository.findOne(calendarEvent.getId());
        updatedCalendarEvent
                .title(UPDATED_TITLE)
                .start(UPDATED_START)
                .end(UPDATED_END)
                .description(UPDATED_DESCRIPTION);

        restCalendarEventMockMvc.perform(put("/api/calendar-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCalendarEvent)))
            .andExpect(status().isOk());

        // Validate the CalendarEvent in the database
        List<CalendarEvent> calendarEvents = calendarEventRepository.findAll();
        assertThat(calendarEvents).hasSize(databaseSizeBeforeUpdate);
        CalendarEvent testCalendarEvent = calendarEvents.get(calendarEvents.size() - 1);
        assertThat(testCalendarEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCalendarEvent.getStart()).isEqualTo(UPDATED_START);
        assertThat(testCalendarEvent.getEnd()).isEqualTo(UPDATED_END);
        assertThat(testCalendarEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteCalendarEvent() throws Exception {
        // Initialize the database
        calendarEventRepository.saveAndFlush(calendarEvent);
        int databaseSizeBeforeDelete = calendarEventRepository.findAll().size();

        // Get the calendarEvent
        restCalendarEventMockMvc.perform(delete("/api/calendar-events/{id}", calendarEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CalendarEvent> calendarEvents = calendarEventRepository.findAll();
        assertThat(calendarEvents).hasSize(databaseSizeBeforeDelete - 1);
    }
}
