package com.mohaberabi.contentproviderapp.data.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


fun <VM : ViewModel> viewmodelFactory(init: () -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return init() as T
        }
    }
}