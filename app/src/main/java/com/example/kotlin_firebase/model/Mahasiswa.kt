package com.example.kotlin_firebase.model
// Step 1 buat class
data class Mahasiswa(
    val nim: String,
    val nama: String,
    val alamat : String,

    val jenis_kelamin: String,
    val kelas : String,
    val angkatan : String

)

// Step 2 bikin constructor untuk memberi nilai default pada class
//{
//    constructor(
//
//    ):this(nim = "", nama = "", alamat = "", jenis_kelamin = "", kelas = "", angkatan = "")
//}