package com.mohaberabi.contentproviderapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohaberabi.contentproviderapp.domain.model.NoteModel
import com.mohaberabi.contentproviderapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(
    private val notesRepository: NoteRepository
) : ViewModel() {


    private val _state = MutableStateFlow(NotesState())

    val state = _state.asStateFlow()


    init {

        notesRepository.getAllNotes()
            .onEach { notes -> _state.update { it.copy(notes = notes) } }
            .launchIn(viewModelScope)
    }


    fun nameChanged(name: String) = _state.update { it.copy(name = name) }


    fun addNote() {
        viewModelScope.launch {
            val note = NoteModel(note = _state.value.name)
            notesRepository.addNote(note)
        }
    }

}