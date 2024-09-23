package com.mohaberabi.contentproviderapp.presentation.viewmodel

import com.mohaberabi.contentproviderapp.domain.model.NoteModel

data class NotesState(

    val name: String = "",
    val notes: List<NoteModel> = emptyList()
)
