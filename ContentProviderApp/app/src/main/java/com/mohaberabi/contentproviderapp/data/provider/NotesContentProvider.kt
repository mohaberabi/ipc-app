package com.mohaberabi.contentproviderapp.data.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import com.mohaberabi.contentproviderapp.data.source.local.NoteDatabase
import com.mohaberabi.contentproviderapp.data.source.local.NoteEntity
import com.mohaberabi.contentproviderapp.data.source.local.NotesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class NotesContentProvider : ContentProvider() {
    companion object {
        private const val NOTES_TABLE = "notes"
        private const val AUTH = "mohaberabi.notesapp.provider"
        private val URI = Uri.parse("content://${AUTH}/${NOTES_TABLE}")
    }


    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTH, NOTES_TABLE, 1)
        addURI(AUTH, NOTES_TABLE.plus("#"), 2)

    }
    private lateinit var database: NoteDatabase
    private lateinit var dao: NotesDao
    override fun onCreate(): Boolean {

        val context = context ?: return false
        database = Room.databaseBuilder(
            context, NoteDatabase::class.java,
            "notes.db"
        ).build()
        dao = database.notesDao()
        return true
    }


    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        order: String?
    ): Cursor {
        return when (uriMatcher.match(uri)) {
            1 -> {
                val queryBuilder = SupportSQLiteQueryBuilder.builder(NOTES_TABLE)
                    .columns(projection)
                    .selection(selection, selectionArgs)
                    .orderBy(order)
                    .create()
                runBlocking(Dispatchers.IO) {
                    database.query(queryBuilder)
                }
            }

            2 -> {
                val noteId = ContentUris.parseId(uri).toInt()
                val queryBuilder = SupportSQLiteQueryBuilder.builder(NOTES_TABLE)
                    .columns(projection)
                    .selection("${NoteEntity::id.name} = ?:", arrayOf(noteId.toString()))
                    .orderBy(order)
                    .create()
                runBlocking(Dispatchers.IO) {
                    database.query(queryBuilder)
                }
            }

            else -> throw IllegalArgumentException("NO URI")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            1 -> "vnd.android.cursor.dir/${AUTH}.${NOTES_TABLE}"
            2 -> "vnd.android.cursor.item/${AUTH}.${NOTES_TABLE}"
            else -> null
        }
    }

    override fun insert(
        uri: Uri,
        vals: ContentValues?,
    ): Uri {

        val noteEntity = NoteEntity(
            note = vals?.getAsString("note").orEmpty(),
        )
        val id = runBlocking(Dispatchers.IO) {
            dao.addNote(noteEntity)
        }
        context?.contentResolver?.notifyChange(URI, null)

        return ContentUris.withAppendedId(URI, id)
    }

    override fun delete(uri: Uri, p1: String?, p2: Array<out String>?): Int {
        val id = ContentUris.parseId(uri).toInt()
        runBlocking(Dispatchers.IO) {
            dao.deleteNote(id.toLong())
        }
        context?.contentResolver?.notifyChange(URI, null)
        return id
    }

    override fun update(
        uri: Uri,
        vals: ContentValues?,
        p2: String?,
        p3: Array<out String>?,
    ): Int {
        val id = ContentUris.parseId(uri).toInt()
        val note = NoteEntity(
            id = id.toLong(),
            note = vals?.getAsString("note").orEmpty()
        )
        runBlocking(Dispatchers.IO) {
            dao.addNote(note)
        }
        context?.contentResolver?.notifyChange(URI, null)
        return id
    }
}