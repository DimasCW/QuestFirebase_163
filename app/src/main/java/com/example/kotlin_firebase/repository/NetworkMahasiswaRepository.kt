package com.example.kotlin_firebase.repository

import com.example.kotlin_firebase.model.Mahasiswa
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

// Step 4
//mendukung realtime dengan callbackflow
//mendukung realtime dengan .addsnapshotlister bakal realtime
class NetworkMahasiswaRepository(
    private val firestore: FirebaseFirestore
):MahasiswaRepository{
    override suspend fun getAllMahasiswa(): Flow<List<Mahasiswa>> = callbackFlow {
        val mhsCollection = firestore.collection("mahasiswa")
            .orderBy("nim", Query.Direction.DESCENDING)
            .addSnapshotListener{
                value, error ->
                if (value != null) {
                    val mhsList = value.documents.mapNotNull {
                        it.toObject(Mahasiswa::class.java)!!
                    }
                    trySend(mhsList)
                }
            }
        awaitClose{
            mhsCollection.remove()
        }
    }

    override suspend fun insertMahasiswa(mahasiswa: Mahasiswa) {
        TODO("Not yet implemented")
    }

    override suspend fun updateMahasiswa(nim: String, mahasiswa: Mahasiswa) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMahasiswa(nim: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getMahasiswaByNim(nim: String): Flow<Mahasiswa> {
        TODO("Not yet implemented")
    }

}