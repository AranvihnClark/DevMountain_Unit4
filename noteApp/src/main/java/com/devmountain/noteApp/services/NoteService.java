package com.devmountain.noteApp.services;

import com.devmountain.noteApp.dtos.NoteDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface NoteService {
    @Transactional
    List<NoteDto> getAllUserNotes(Long userId);

    @Transactional
    void addNote(NoteDto noteDto, Long userId);

    @Transactional
    void deleteNote(Long noteId);

    @Transactional
    void updateNote(NoteDto noteDto);

    @Transactional
    Optional<NoteDto> findNote(Long noteId);
}
