package com.example.kotlin_firebase.model
// Step 1 buat class
data class Mahasiswa(
    val nim: String,
    val nama: String,
    val alamat : String,
    val dosen1 : String,
    val dosen2 : String,
    val judul_skripsi : String,
    val jenis_kelamin: String,
    val kelas : String,
    val angkatan : String

)

// Step 2 bikin constructor untuk memberi nilai default pada class
{
    constructor(

    ):this(nim = "", nama = "", alamat = "",dosen1 = "", dosen2 = "", judul_skripsi = "", jenis_kelamin = "", kelas = "", angkatan = "")
}