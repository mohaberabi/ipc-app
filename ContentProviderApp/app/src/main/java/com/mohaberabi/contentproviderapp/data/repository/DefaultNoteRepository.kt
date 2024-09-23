package com.mohaberabi.contentproviderapp.data.repository

import com.mohaberabi.contentproviderapp.domain.model.NoteModel
import com.mohaberabi.contentproviderapp.domain.repository.NoteRepository
import com.mohaberabi.contentproviderapp.domain.source.local.NotesLocalDataSource
import kotlinx.coroutines.flow.Flow

class DefaultNoteRepository(
    private val notesLocalDataSource: NotesLocalDataSource
) : NoteRepository {


    override fun getAllNotes(): Flow<List<NoteModel>> = notesLocalDataSource.getAllNotes()

    override suspend fun addNote(note: NoteModel): Result<Unit> {
        try {
            notesLocalDataSource.addNote(note)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}