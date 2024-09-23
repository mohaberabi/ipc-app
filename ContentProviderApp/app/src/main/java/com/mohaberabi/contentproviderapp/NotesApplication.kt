package com.mohaberabi.contentproviderapp

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.mohaberabi.contentproviderapp.data.di.NotesDi
import com.mohaberabi.contentproviderapp.data.repository.DefaultNoteRepository
import com.mohaberabi.contentproviderapp.data.source.local.NoteDatabase
import com.mohaberabi.contentproviderapp.data.source.local.RoomNoteLocalDataSource
import com.mohaberabi.contentproviderapp.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers

class NotesApplication : Application() {


    companion object {
        lateinit var noteDi: NotesDi
    }

    override fun onCreate() {
        super.onCreate()
        noteDi = NoteDiImpl(this)
    }


}


class NoteDiImpl(
    private val context: Context
) : NotesDi {

    private val database by lazy {
        Room.databaseBuilder(context, NoteDatabase::class.java, "notes.db").build()
    }
    private val localDataSource by lazy {

        RoomNoteLocalDataSource(database.notesDao(), Dispatchers.IO)
    }
    override val noteRepository: NoteRepository
        get() = DefaultNoteRepository(localDataSource)
}