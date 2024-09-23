package com.mohaberabi.contentproviderapp

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mohaberabi.contentproviderapp.data.di.viewmodelFactory
import com.mohaberabi.contentproviderapp.presentation.screen.NotesScreen
import com.mohaberabi.contentproviderapp.presentation.viewmodel.NotesViewModel
import com.mohaberabi.contentproviderapp.ui.theme.ContentProviderAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContentProviderAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->

                    val factory = viewmodelFactory {
                        NotesViewModel(NotesApplication.noteDi.noteRepository)
                    }

                    val viewmodel = viewModel<NotesViewModel>(factory = factory)
                    NotesScreen(
                        viewModel = viewmodel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

