package com.mohaberabi.contentproviderapp.data.source.local

import com.mohaberabi.contentproviderapp.domain.model.NoteModel
import com.mohaberabi.contentproviderapp.domain.source.local.NotesLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class RoomNoteLocalDataSource(
    private val dao: NotesDao,
    private val ioDispatcher: CoroutineDispatcher
) : NotesLocalDataSource {
    override suspend fun addNote(note: NoteModel) {

        withContext(ioDispatcher) {
            dao.addNote(note.toNoteEntity())
        }

    }

    override fun getAllNotes(): Flow<List<NoteModel>> {
        return dao.getAllNotes().map { list -> list.map { it.toNoteModel() } }.flowOn(ioDispatcher)
    }


}