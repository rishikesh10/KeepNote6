package com.stackroute.keepnote.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.NoteUser;
import com.stackroute.keepnote.repository.NoteRepository;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class NoteServiceImpl implements NoteService {

	/*
	 * Autowiring should be implemented for the NoteRepository and
	 * MongoOperation. (Use Constructor-based autowiring) Please note that we
	 * should not create any object using the new keyword.
	 */
	@Autowired
	private NoteRepository noteRepository;

	/*
	 * This method should be used to save a new note.
	 */
	public boolean createNote(Note note) {
		try {
			if (note != null) {
				List<Note> noteList = new ArrayList<>();
				NoteUser noteUser = new NoteUser();
				noteUser.setUserId(note.getNoteCreatedBy());
				noteUser.setNotes(noteList);
				noteList.add(note);
				NoteUser currentNote = this.noteRepository.save(noteUser);
				if (currentNote != null) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/* This method should be used to delete an existing note. */

	public boolean deleteNote(String userId, int noteId) {
		NoteUser currentNoteUser = this.noteRepository.findById(userId).get();
		if (currentNoteUser != null) {
			try {
				List<Note> noteList = new ArrayList<>();
				NoteUser noteUser = new NoteUser();
				noteUser.setUserId(userId);
				if (!currentNoteUser.getNotes().isEmpty()) {
					for (Note note : currentNoteUser.getNotes()) {
						if (note.getNoteId() == noteId) {
							noteList.add(note);
						}
					}
					noteUser.setNotes(noteList);
					this.noteRepository.delete(noteUser);
				}

				return true;
			} catch (Exception e) {
				throw e;
			}
		} else {
			return false;
		}
	}

	/* This method should be used to delete all notes with specific userId. */

	public boolean deleteAllNotes(String userId) {
		NoteUser currentNoteUser = this.noteRepository.findById(userId).get();
		if (currentNoteUser != null) {
			this.noteRepository.delete(currentNoteUser);
			return true;
		}
		return false;
	}

	/*
	 * This method should be used to update a existing note.
	 */
	public Note updateNote(Note note, int id, String userId) throws NoteNotFoundExeption {

		NoteUser currentNoteUser = this.noteRepository.findById(userId).get();
		if (currentNoteUser != null) {

			List<Note> noteList = new ArrayList<>();
			NoteUser noteUser = new NoteUser();
			noteUser.setUserId(userId);
			if (!currentNoteUser.getNotes().isEmpty()) {
				for (Note cnote : currentNoteUser.getNotes()) {
					if (cnote.getNoteId() == id) {
						noteList.add(note);
					}
				}
				noteUser.setNotes(noteList);
				currentNoteUser = this.noteRepository.save(noteUser);
				return note;
			} else {
				return null;
			}
		} else {
			throw new NoteNotFoundExeption("Note  was not found");
		}

	}

	/*
	 * This method should be used to get a note by noteId created by specific
	 * user
	 */
	public Note getNoteByNoteId(String userId, int noteId) throws NoteNotFoundExeption {
		Note note = null;
		NoteUser currentNoteUser = this.noteRepository.findById(userId).get();
		if (currentNoteUser != null) {
			if (!currentNoteUser.getNotes().isEmpty()) {
				for (Note cnote : currentNoteUser.getNotes()) {
					if (cnote.getNoteId() == noteId)
						note = cnote;
				}
			} else {
				return null;
			}
		} else {
			throw new NoteNotFoundExeption("Note  was not found");
		}
		return note;
	}

	/*
	 * This method should be used to get all notes with specific userId.
	 */
	public List<Note> getAllNoteByUserId(String userId) {

		NoteUser currentNoteUser = this.noteRepository.findById(userId).get();
		if (currentNoteUser != null) {
			return currentNoteUser.getNotes();
		}
		return null;
	}

}
