package com.example.kotlin_firebase.ui.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_firebase.model.Mahasiswa
import com.example.kotlin_firebase.repository.MahasiswaRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

// Step 8
sealed class DetailUiState {
    data class Success(val mahasiswa: List<Mahasiswa>) : DetailUiState()
    data class Error(val exception: Throwable) : DetailUiState()
    object Loading : DetailUiState()
}

class DetailViewModel (
    private val mhs: MahasiswaRepository
):ViewModel(){
    var mhsUIState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set

    init {
        getMhs()
    }

    fun getMhs() {
        viewModelScope.launch {
            mhs.getAllMahasiswa()
                .onStart{
                    mhsUIState = DetailUiState.Loading
                }
                .catch{
                    mhsUIState = DetailUiState.Error(it)
                }
                .collect{
                    mhsUIState = if (it.isEmpty()) {
                        DetailUiState.Error(Exception("Belum ada daftar mahasiswa"))
                    }else{
                        DetailUiState.Success(it)
                    }
                }
        }
    }
    fun deleteMahasiswa(mahasiswa: Mahasiswa){
        viewModelScope.launch {
            try {
                mhs.deleteMahasiswa(mahasiswa)
            }catch (e: Exception){
                mhsUIState = DetailUiState.Error(e)
            }
        }

    }
}

