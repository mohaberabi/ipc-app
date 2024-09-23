package com.mohaberabi.contentproviderapp.domain.repository

import com.mohaberabi.contentproviderapp.domain.model.NoteModel
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun addNote(note: NoteModel): Result<Unit>


    fun getAllNotes(): Flow<List<NoteModel>>
}