package com.mohaberabi.contentproviderapp.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mohaberabi.contentproviderapp.presentation.viewmodel.NotesViewModel


@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel
) {

    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        item {
            TextField(
                value = state.name,
                onValueChange = {
                    viewModel.nameChanged(it)
                },
            )


        }
        item {
            Button(
                onClick = { viewModel.addNote() },
            ) {

                Text(text = "Save Note")
            }
        }
        items(state.notes) { note ->

            Text(
                text = note.note,
            )

        }
    }
}