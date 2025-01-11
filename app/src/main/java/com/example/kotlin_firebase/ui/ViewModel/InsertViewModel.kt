package com.example.kotlin_firebase.ui.ViewModel

import androidx.lifecycle.ViewModel
import com.example.kotlin_firebase.model.Mahasiswa
import com.example.kotlin_firebase.repository.MahasiswaRepository

class InsertViewModel(
    private val mhs: MahasiswaRepository
) : ViewModel() {
    //data class variable yang menyimpan data input form

}

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

data class FormErrorState(
    val nim: String? = null,
    val nama: String? = null,
    val jenisKelamin: String? = null,
    val alamat: String? = null,
    val kelas: String? = null,
    val angkatan: String? = null
){
    fun isValid() : Boolean{
        return nim == null && nama == null && jenisKelamin == null && alamat == null && kelas == null && angkatan == null
    }
}

data class InsertUiState(
    val insertUiEvent: MahasiswaEvent = MahasiswaEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
)

sealed class FormState{
    object Idle : FormState()
    object Loading : FormState()
    data class Success(val mahasiswa: Mahasiswa) : FormState()
    data class Error(val throwable: Throwable) : FormState()
}