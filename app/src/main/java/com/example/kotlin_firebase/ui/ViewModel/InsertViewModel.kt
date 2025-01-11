package com.example.kotlin_firebase.ui.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_firebase.model.Mahasiswa
import com.example.kotlin_firebase.repository.MahasiswaRepository
import kotlinx.coroutines.launch

class InsertViewModel(
    private val mhs: MahasiswaRepository
) : ViewModel() {
    //data class variable yang menyimpan data input form
    var uiEvent: InsertUiState by mutableStateOf(InsertUiState())
        private set

    var uiState: FormState by mutableStateOf(FormState.Idle)
        private set

    //Memperbarui state berdasarkan input pengguna
    fun updateState(mahasiswaEvent : MahasiswaEvent){
        uiEvent = uiEvent.copy(
            insertUiEvent = mahasiswaEvent,
        )
    }

    //Validasi data input pengguna
    fun validateFields() : Boolean{
        val event = uiEvent.insertUiEvent
        val errorState = FormErrorState(
            nim = if (event.nim.isNotEmpty()) null else "NIM tidak boleh kosong",
            nama = if (event.nama.isNotEmpty()) null else "Nama tidak boleh kosong",
            jenisKelamin = if (event.jenisKelamin.isNotEmpty()) null else "Jenis Kelamin tidak boleh kosong",
            alamat = if (event.alamat.isNotEmpty()) null else "Alamat tidak boleh kosong",
            kelas = if (event.kelas.isNotEmpty()) null else "Kelas tidak boleh kosong",
            angkatan = if (event.angkatan.isNotEmpty()) null else "Angkatan tidak boleh kosong"

        )
        uiEvent = uiEvent.copy(
            isEntryValid = errorState
        )
        return errorState.isValid()

    }

    fun insertMahasiswa(){
        if(validateFields()){
            viewModelScope.launch {
                uiState = FormState.Loading
                try {
                    mhs.insertMahasiswa(uiEvent.insertUiEvent.toMhsModel())
                    uiState = FormState.Success("Data berhasil disimpan")
                }catch (e : Exception){
                    uiState = FormState.Error("Data gagal disimpan")
                }

            }
        }else{
            uiState = FormState.Error("Data tidak valid")
        }
    }

    fun resetForm(){
        uiEvent = InsertUiState()
        uiState = FormState.Idle
    }

    fun resetSnackBarMessage(){
        uiState = FormState.Idle
    }
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
    data class Success(val message: String) : FormState()
    data class Error(val message: String) : FormState()
}