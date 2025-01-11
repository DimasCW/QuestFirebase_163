package com.example.kotlin_firebase.ui.ViewModel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kotlin_firebase.MahasiswaApplications

object PenyediaViewModel{
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                aplikasiMahasiswa().container.mahasiswaRepository
            )
        }
        initializer {
            InsertViewModel(
                mhs().container.MahasiswaRepository
            )
        }
    }
    fun CreationExtras.aplikasiMahasiswa(): MahasiswaApplications =
        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]as MahasiswaApplications)
}