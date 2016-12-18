package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Note;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Note entity.
 */
@SuppressWarnings("unused")
public interface NoteRepository extends JpaRepository<Note,Long> {

    @Query("select note from Note note where note.owner.login = ?#{principal.username}")
    List<Note> findByOwnerIsCurrentUser();

}
