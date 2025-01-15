package com.example.kotlin_firebase.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kotlin_firebase.ui.ViewModel.FormState
import com.example.kotlin_firebase.ui.ViewModel.InsertUiState
import com.example.kotlin_firebase.ui.ViewModel.InsertViewModel
import com.example.kotlin_firebase.ui.ViewModel.MahasiswaEvent
import com.example.kotlin_firebase.ui.ViewModel.PenyediaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlin_firebase.ui.ViewModel.FormErrorState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertMhsView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertViewModel = viewModel(factory = PenyediaViewModel.Factory)
){

    val uiState = viewModel.uiState //state utama unutk loading, success,

    val uiEvent = viewModel.uiEvent // Stat untuk form dan navigasi
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    //Observasi perubahan state untuk snackbar dan navigasi
    LaunchedEffect (uiState){
        when(uiState){
            is FormState.Success -> {
                println(
                    "InsertMhsView: uiState is FormState.success, navigate" +
                            "to home " + uiState.message
                )
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message) //Tampilkan snackbar
                }
                delay(700)
                // Navigasi Lnagsung
                onNavigate()

                viewModel.resetSnackBarMessage()//ResetSnackbarState
            }

            is FormState.Error -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }

            else -> Unit
        }
    }
    Scaffold (
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text("Tambah Mahasiswa")},
                navigationIcon = {
                    Button(onClick = onBack){
                        Text("Back")
                    }
                }
            )
        }
    ){
        padding ->
        Column (
            modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
        ){
            InsertBodyMhs(
                uiState = uiEvent,
                homeUiState = uiState,
                onValueChange = { updateEvent ->
                    viewModel.updateState(updateEvent)
                },
                onClick = {
                    if (viewModel.validateFields()){
                        viewModel.insertMahasiswa()
                    }
                }
            )
        }
    }
}

@Composable
fun InsertBodyMhs(
    modifier: Modifier = Modifier,
    onValueChange: (MahasiswaEvent) -> Unit,
    uiState: InsertUiState,
    onClick: ()-> Unit,
    homeUiState: FormState

){
    Column (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        FormMahasiswa(
            mahasiswaEvent = uiState.insertUiEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = homeUiState !is FormState.Loading,
        ) {
            if(homeUiState is FormState.Loading){
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                        .padding(end = 8.dp)
                )
                Text("Loading")
            }else{
                Text("Add")
            }
        }
    }
}

@Composable
fun FormMahasiswa (
    mahasiswaEvent: MahasiswaEvent = MahasiswaEvent(),
    onValueChange: (MahasiswaEvent) -> Unit,
    errorState: FormErrorState = FormErrorState(),
    modifier: Modifier = Modifier
){
    val jenisKelalmin = listOf("Laki-laki", "Perempuan")
    val kelas = listOf("A","B","C","D","E")

    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.nama,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(nama = it))
            },
            label = {
                Text("Nama")

            },
            isError = errorState.nama != null,
            placeholder = {
                Text("Masukkan Nama")

            }
        )
        Text(
            text = errorState.nama ?: "",
            color = Color.Red,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.nim,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(nim = it))
            },
            label = {
                Text("NIM")
            },
            isError =  errorState.nim != null,
            placeholder = { Text("Masukkan NIM") },
            keyboardOptions = KeyboardOptions(keyboardType =
            KeyboardType.Number)
            )
        Text(text = errorState.nim ?: "",color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Jenis Kelamin")
        Row (
            modifier = Modifier.fillMaxWidth(),
        ){
            jenisKelalmin.forEach {
                jk->
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    RadioButton(
                        selected = mahasiswaEvent.jenisKelamin == jk,
                        onClick = {
                            onValueChange(mahasiswaEvent.copy(jenisKelamin = jk))
                        },

                    )
                    Text(text = jk)
                }
            }
        }
        Text(
            text = errorState.jenisKelamin ?: "",
            color = Color.Red,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.alamat,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(alamat = it))
            },
            label = { Text("Alamat") },
            isError = errorState.alamat != null,
            placeholder = { Text("Masukkan alamat")}
        )
        Text(text = errorState.alamat ?: "",color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.dosen1,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(dosen1 = it))
            },
            label = { Text("Dosen 1") },
            isError = errorState.dosen1 != null,
            placeholder = { Text("Masukkan Nama Dosen 1")}
        )
        Text(text = errorState.dosen1 ?: "",color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.dosen2,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(dosen2 = it))
            },
            label = { Text("Dosen 2") },
            isError = errorState.dosen2 != null,
            placeholder = { Text("Masukkan Nama Dosen 2")}
        )
        Text(text = errorState.dosen2 ?: "",color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.judul_skripsi,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(judul_skripsi = it))
            },
            label = { Text("Judul Skripsi") },
            isError = errorState.judul_skripsi != null,
            placeholder = { Text("Masukkan Judul Skripsi")}
        )
        Text(text = errorState.judul_skripsi ?: "",color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Kelas")
        Row{
            kelas.forEach{kelas ->
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    RadioButton(
                        selected = mahasiswaEvent.kelas == kelas,
                        onClick = {
                            onValueChange(mahasiswaEvent.copy(kelas = kelas))
                        },
                    )
                    Text(text = kelas)
                }
            }
        }
        Text(
            text = errorState.kelas ?: "",
            color = Color.Red,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = mahasiswaEvent.angkatan,
            onValueChange = {
                onValueChange(mahasiswaEvent.copy(angkatan = it))
            },
            label = { Text("Angkatan")},
            isError = errorState.angkatan != null,
            placeholder = { Text("Masukkan Angkatan")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

        )
        Text(text = errorState.angkatan ?: "",color = Color.Red)
    }

}