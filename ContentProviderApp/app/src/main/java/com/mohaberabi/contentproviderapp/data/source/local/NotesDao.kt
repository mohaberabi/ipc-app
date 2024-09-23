package com.mohaberabi.contentproviderapp.data.source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface NotesDao {


    @Upsert


    suspend fun addNote(note: NoteEntity): Long


    @Query("SELECT * FROM notes")

    fun getAllNotes(): Flow<List<NoteEntity>>


    @Query("DELETE FROM notes WHERE id=:id")
    suspend fun deleteNote(id: Long)
}