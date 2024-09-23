package com.mohaberabi.contentproviderappclient

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val NOTES_TABLE = "notes"
private const val AUTH = "mohaberabi.notesapp.provider"
private val URI = Uri.parse("content://${AUTH}/${NOTES_TABLE}")

@Composable
fun NoteClientScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current


    var notes by remember {
        mutableStateOf(listOf<NoteModel>())
    }
    LaunchedEffect(
        key1 = Unit,
    ) {

        notes = queryNotes(context)
    }



    LaunchedEffect(
        key1 = context,
    ) {
        context.contentResolver.observeNotes(context)
            .onEach {
                notes = it
            }
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        item {
            Button(onClick = {
                context.contentResolver.addNote()
            }) {
                Text(text = "Add")
            }
        }
        items(notes) { note ->
            Text(text = note.note)
        }
    }
}

fun queryNotes(context: Context): List<NoteModel> {
    val resolver = context.contentResolver

    val list = mutableListOf<NoteModel>()
    resolver.query(
        URI,
        null,
        null,
        null,
        null,
    ).use {
        it?.let { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val note = cursor.getString(cursor.getColumnIndexOrThrow("note"))
                val noteModel = NoteModel(id.toLong(), note)
                list.add(noteModel)
            }
        }


    }
    return list
}


fun ContentResolver.addNote() {
    val values = ContentValues().apply {
        put("note", "testClient")
    }
    insert(URI, values)
}

fun ContentResolver.updateNote(id: Long) {
    val values = ContentValues().apply {
        put("note", "testClient")
    }
    val uri = Uri.parse("content://$AUTH/$NOTES_TABLE/$id")
    update(uri, values, null, null)
}

fun ContentResolver.deleteNote(id: Long) {
    val uri = Uri.parse("content://$AUTH/$NOTES_TABLE/$id")
    delete(uri, null, null)
}

fun ContentResolver.observeNotes(context: Context) = callbackFlow<List<NoteModel>> {
    val observer = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            val notes = queryNotes(context = context)
            trySend(notes)
        }
    }
    registerContentObserver(URI, false, observer)
    awaitClose {
        unregisterContentObserver(observer)
    }
}