package com.mohaberabi.contentproviderapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [
        NoteEntity::class,
    ],
    version = 1
)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
}