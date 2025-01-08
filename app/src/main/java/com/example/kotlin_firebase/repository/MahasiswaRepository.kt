package com.example.kotlin_firebase.repository

import com.example.kotlin_firebase.model.Mahasiswa
import kotlinx.coroutines.flow.Flow

// Step 3 buat interface
interface MahasiswaRepository {
    suspend fun getAllMahasiswa(): Flow<List<Mahasiswa>>
    suspend fun insertMahasiswa(mahasiswa: Mahasiswa)
    suspend fun updateMahasiswa(nim: String, mahasiswa: Mahasiswa)
    suspend fun deleteMahasiswa(nim: String)
    suspend fun getMahasiswaByNim(nim: String): Flow<Mahasiswa>
}