package com.mohaberabi.contentproviderapp.domain.source.local

import com.mohaberabi.contentproviderapp.domain.model.NoteModel
import kotlinx.coroutines.flow.Flow

interface NotesLocalDataSource {

    suspend fun addNote(note: NoteModel)


    fun getAllNotes(): Flow<List<NoteModel>>
}