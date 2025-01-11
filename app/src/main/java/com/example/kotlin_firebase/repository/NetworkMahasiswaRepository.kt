package com.example.kotlin_firebase.repository

import com.example.kotlin_firebase.model.Mahasiswa
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

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
        try {
            firestore.collection("mahasiswa").add(mahasiswa).await()
        }catch (e: Exception){
            throw Exception ("Gagal menambahkan data mahasiswa: ${e.message}")
        }
    }

    override suspend fun updateMahasiswa(nim: String, mahasiswa: Mahasiswa) {
        try {
            firestore.collection("mahasiswa")
                .document(mahasiswa.nim)
                .set(mahasiswa)
                .await()
        }catch (e:Exception){
            throw Exception("Gagal mengupdate data mahasiswa: ${e.message}")
        }
    }

    override suspend fun deleteMahasiswa(mahasiswa: Mahasiswa) {
        try{
            firestore.collection("mahasiswa")
                .document(mahasiswa.nim)
                .delete()
                .await()
        }catch (e : Exception){
            throw Exception("Gagal menghapus data mahasiswa: ${e.message}")
        }
    }

    override suspend fun getMahasiswaByNim(nim: String): Flow<Mahasiswa> = callbackFlow {
        val mhsDocument = firestore.collection("mahasiswa")
            .document(nim)
            .addSnapshotListener{value, error ->
                if(value != null){
                    val mhs = value.toObject(Mahasiswa::class.java)!!
                    trySend(mhs)

                }
            }
        awaitClose{
            mhsDocument.remove()
        }
    }

}