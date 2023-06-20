package com.devmountain.noteApp.entities;

import com.devmountain.noteApp.dtos.NoteDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// This generates a table in our database.
// @Data creates the setters and getters.
// @AllArgsConstructor creates a constructor using all arguments available.
// @NoArgsConstructor creates a constructor with no arguments.
@Entity
@Table(name = "Notes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    // Below are the table columns.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String body;

    @ManyToOne
    // Prevents infinite recursion when you deliver the resource up as Json through the RESTful API endpoint
    @JsonBackReference
    private User user;

    // Like the User class, this creates a new Note using the NoteDto (or a user's input).
    public Note (NoteDto notedto) {
        if (notedto.getBody() != null) {
            this.body = notedto.getBody();
        }
    }
}
