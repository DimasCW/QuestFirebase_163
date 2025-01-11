package com.example.kotlin_firebase.ui.ViewModel

import androidx.lifecycle.ViewModel
import com.example.kotlin_firebase.repository.MahasiswaRepository

class InsertViewModel(
    private val mhs: MahasiswaRepository
) : ViewModel() {
    //Menyimpan input form ke dalam entity
    fun MahasiswaEvent.toMhsModel() : Mahasiswa = Mahasiswa(
        nim = nim,
        nama = nama,
        jenisKelamin = jenisKelamin,
        alamat = alamat,
        kelas = kelas,
        angkatan = angkatan
    )
}