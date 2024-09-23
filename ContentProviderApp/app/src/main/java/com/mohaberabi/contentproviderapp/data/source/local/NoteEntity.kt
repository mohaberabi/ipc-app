package com.mohaberabi.contentproviderapp.data.source.local

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mohaberabi.contentproviderapp.domain.model.NoteModel


@Entity(tableName = "notes")
data class NoteEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val note: String

)

fun NoteModel.toNoteEntity() = NoteEntity(id, note)


fun NoteEntity.toNoteModel() = NoteModel(id, note)
