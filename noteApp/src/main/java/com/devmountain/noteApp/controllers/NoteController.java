package com.devmountain.noteApp.controllers;

import com.devmountain.noteApp.dtos.NoteDto;
import com.devmountain.noteApp.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {

    // Variables under the controller
    @Autowired
    private NoteService noteService;

    // Gets all of user's notes.
    @GetMapping("/user/{userId}")
    public List<NoteDto> getAllUserNotes(@PathVariable Long userId) {
        return noteService.getAllUserNotes(userId);
    }

    // Add a note to the user's id
    @PostMapping("/user/{userId}")
    public void addNote(@RequestBody NoteDto noteDto, @PathVariable Long userId) {
        noteService.addNote(noteDto, userId);
    }

    // Deletes a note based on the note's id.
    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable Long noteId) {
        noteService.deleteNote(noteId);
    }

    // Updates a note in the database.
    @PutMapping
    public void updateNote(@RequestBody NoteDto noteDto) {
        noteService.updateNote(noteDto);
    }

    // Gets a note from the database.
    @GetMapping("/{noteId}")
    public Optional<NoteDto> findNote(@PathVariable Long noteId) {
        return noteService.findNote(noteId);
    }
}
