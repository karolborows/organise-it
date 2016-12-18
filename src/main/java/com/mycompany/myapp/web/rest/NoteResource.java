package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Note;

import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.NoteRepository;
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
 * REST controller for managing Note.
 */
@RestController
@RequestMapping("/api")
public class NoteResource {

    private final Logger log = LoggerFactory.getLogger(NoteResource.class);

    @Inject
    private NoteRepository noteRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /notes : Create a new note.
     *
     * @param note the note to create
     * @return the ResponseEntity with status 201 (Created) and with body the new note, or with status 400 (Bad Request) if the note has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notes")
    @Timed
    public ResponseEntity<Note> createNote(@Valid @RequestBody Note note) throws URISyntaxException {
        log.debug("REST request to save Note : {}", note);
        if (note.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("note", "idexists", "A new note cannot already have an ID")).body(null);

        }
        note.setOwner(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get());
        Note result = noteRepository.save(note);
        return ResponseEntity.created(new URI("/api/notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("note", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notes : Updates an existing note.
     *
     * @param note the note to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated note,
     * or with status 400 (Bad Request) if the note is not valid,
     * or with status 500 (Internal Server Error) if the note couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notes")
    @Timed
    public ResponseEntity<Note> updateNote(@Valid @RequestBody Note note) throws URISyntaxException {
        log.debug("REST request to update Note : {}", note);
        if (note.getId() == null) {
            return createNote(note);
        }
        Note result = noteRepository.save(note);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("note", note.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notes : get all the notes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of notes in body
     */
    @GetMapping("/notes")
    @Timed
    public List<Note> getAllNotes() {
        log.debug("REST request to get all Notes");
        List<Note> notes = noteRepository.findByOwnerIsCurrentUser();
        return notes;
    }

    /**
     * GET  /notes/:id : get the "id" note.
     *
     * @param id the id of the note to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the note, or with status 404 (Not Found)
     */
    @GetMapping("/notes/{id}")
    @Timed
    public ResponseEntity<Note> getNote(@PathVariable Long id) {
        log.debug("REST request to get Note : {}", id);
        Note note = noteRepository.findOne(id);
        return Optional.ofNullable(note)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /notes/:id : delete the "id" note.
     *
     * @param id the id of the note to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notes/{id}")
    @Timed
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        log.debug("REST request to delete Note : {}", id);
        noteRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("note", id.toString())).build();
    }

}
