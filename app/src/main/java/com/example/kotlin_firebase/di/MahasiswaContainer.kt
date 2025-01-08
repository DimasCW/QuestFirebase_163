package com.example.kotlin_firebase.di

import com.example.kotlin_firebase.repository.MahasiswaRepository
import com.example.kotlin_firebase.repository.NetworkMahasiswaRepository
import com.google.firebase.firestore.FirebaseFirestore
// Step 5
interface AppContainer{
    val mahasiswaRepository : MahasiswaRepository
}

class MahasiswaContainer : AppContainer{

    private val firebase: FirebaseFirestore = FirebaseFirestore.getInstance()

    override val mahasiswaRepository: MahasiswaRepository by lazy {
        NetworkMahasiswaRepository(firebase)
    }
}