package com.mycompany.myapp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A CalendarEvent.
 */
@Entity
@Table(name = "calendar_event")
public class CalendarEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "start", nullable = false)
    private ZonedDateTime start;

    @Column(name = "end")
    private ZonedDateTime end;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private User owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public CalendarEvent title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public CalendarEvent start(ZonedDateTime start) {
        this.start = start;
        return this;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public CalendarEvent end(ZonedDateTime end) {
        this.end = end;
        return this;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public CalendarEvent description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public CalendarEvent owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CalendarEvent calendarEvent = (CalendarEvent) o;
        if (calendarEvent.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, calendarEvent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CalendarEvent{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", start='" + start + "'" +
            ", end='" + end + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
