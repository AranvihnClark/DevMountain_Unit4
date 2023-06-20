package com.devmountain.noteApp.services;

import com.devmountain.noteApp.dtos.NoteDto;
import com.devmountain.noteApp.entities.Note;
import com.devmountain.noteApp.entities.User;
import com.devmountain.noteApp.repositories.NoteRepository;
import com.devmountain.noteApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * This is used to grab all the notes a user has made.
     * If no notes have been saved, an empty list will be returned.
     *
     * @param userId the id of the user in question
     * @return a list of all notes based on the parameter userId.
     */
    @Override
    @Transactional
    public List<NoteDto> getAllUserNotes(Long userId) {
        // We need an optional for users as we will be using their id as the identifier
        Optional<User> userOptional = userRepository.findById(userId);

        // Now to check if the user id exists.
        if (userOptional.isPresent()) {
            // A list of notes is created based on all notes of the userOptional (user) from the database.
            List<Note> notes = noteRepository.findAllByUserEquals(userOptional.get());

            // Leaving this as is so that I can refer back to it as I am still not confident in writing this out to have it work properly.
            // The .stream() lets us search for all notes and the .map() converts each note found into a new NoteDto.
            // This is needed as one, we want to return a list of NoteDto and two, we need it to be so because we don't want to use the actual notes themselves but a copy.
            // The .collect() is used to create an Object Collection that holds the list of NoteDtos.
            List<NoteDto> noteList = notes.stream().map(note -> new NoteDto(note)).collect(Collectors.toList());
            // NOTE!!! Collection beats List, Queue, and Set in the interface hierarchy, and Iterable beats Collection.
            // This means we could have used Iterable in place of Collection (I believe) but there is no need jump two levels in the hierarchy.

            return noteList;
        }

        // In case if there are no notes by the user, we return an empty list.
        // Remember, a 'Collection' is a single Object (I think) of objects.
        return Collections.emptyList();

        // There is no need to 'saveAndFlush' as we aren't adding, deleting, or updating our database.
    }

    /**
     * This adds a note created by the user and saves it to our database.
     *
     * @param noteDto the dto of the note that the user wants to save in our database.
     * @param userId the id of the user that is logged in.
     */
    @Override
    @Transactional
    public void addNote(NoteDto noteDto, Long userId) {
        // First, we want to find the user's id, so we create an optional to search for it to avoid nulls.
        Optional<User> userOptional = userRepository.findById(userId);

        // Because noteDto isn't exactly a note but just a 'dto' and we can't manipulate it, we use it as a base to create a new Note.
        Note note = new Note(noteDto);

        // Kept the below as is to practice writing it out.
        // This checks to see if the user exists and, if it does, it will set our note with the user based on the userId.
        if (userOptional.isPresent()) {
            note.setUser(userOptional.get());
        }

        // Then, of course, we add this note to our database.
        noteRepository.saveAndFlush(note);
    }

    /**
     * This deletes a note in our database based on the id of the note.
     *
     * @param noteId the id of the note in question.
     */
    @Override
    @Transactional
    public void deleteNote(Long noteId) {
        // I was originally going to have the method search for the user's id first but then realized it was unnecessary.
        // The user is already logged in and choosing the note they want to delete so we, in theory, only need the note's id.

        // This searches for the note based on the note's id.
        Optional<Note> noteOptional = noteRepository.findById(noteId);

        // Leaving this as below for practice.
        // If the note exists, it will be deleted.
        if (noteOptional.isPresent()) {
            noteRepository.delete(noteOptional.get());
        }
    }

    /**
     * This is to update a note that already exists in our database.
     * Because we need two parameters from the Note object, we can simplify our parameters by using just the below.
     *
     * @param noteDto the dto of our Note that will be used to update our parameter.
     */
    @Override
    @Transactional
    public void updateNote(NoteDto noteDto) {
        // Searches for the note we want to update.
        Optional<Note> noteOptional = noteRepository.findById(noteDto.getId());

        // If the note exists, we will update the note as below.
        // Changed the below to Intellij's format for practice.
        noteOptional.ifPresent(note -> {
            note.setBody(noteDto.getBody());
            noteRepository.saveAndFlush(note);
        });
    }

    @Override
    @Transactional
    public Optional<NoteDto> findNote(Long noteId) {
        // First we need to create an optional to search for the note to avoid nulls.
        Optional<Note> noteOptional = noteRepository.findById(noteId);

        // If the note is exists, we will return the note to the user.
        // Left the code below as is and didn't use IntelliJ's functional style option.
        if (noteOptional.isPresent()) {

            // I did have to look at the instructions' picture for this and had to reconstruct this method.
            // I was originally going to just have the return type be just 'Note'.
            // But looking at the instructions' example, I see that we use Optional<NoteDto> for two reasons.
            // Optional is used to avoid a null return (as below we return an empty optional).
            // NoteDto is used because we can't return a Note as it is not a data transfer object. <-- Need to remember this.
            return Optional.of(new NoteDto(noteOptional.get()));
        } else {
            return Optional.empty();
        }
    }
}
