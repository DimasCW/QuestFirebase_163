package com.example.kotlin_firebase.ui.ViewModel

import androidx.lifecycle.ViewModel
import com.example.kotlin_firebase.model.Mahasiswa
import com.example.kotlin_firebase.repository.MahasiswaRepository

class InsertViewModel(
    private val mhs: MahasiswaRepository
) : ViewModel() {
    //data class variable yang menyimpan data input form
    data class MahasiswaEvent(
        val nim: String = "",
        val nama: String = "",
        val jenisKelamin: String = "",
        val alamat: String = "",
        val kelas: String = "",
        val angkatan: String = ""
    )

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