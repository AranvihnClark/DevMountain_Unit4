package com.devmountain.noteApp.entities;

import com.devmountain.noteApp.dtos.NoteDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Notes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String body;

    @ManyToOne
    // Prevents infinite recursion when you deliver the resource up as Json through the RESTful API endpoint
    @JsonBackReference
    private User user;

    public Note (NoteDto notedto) {
        if (notedto.getBody() != null) {
            this.body = notedto.getBody();
        }
    }
}
