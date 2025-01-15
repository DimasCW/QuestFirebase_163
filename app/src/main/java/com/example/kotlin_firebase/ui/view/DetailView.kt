package com.example.kotlin_firebase.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlin_firebase.model.Mahasiswa
import com.example.kotlin_firebase.ui.ViewModel.DetailUiState
import com.example.kotlin_firebase.ui.ViewModel.DetailViewModel
import com.example.kotlin_firebase.ui.ViewModel.PenyediaViewModel


//STEP 9
//Detailscreen menampilkan loading message
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navigateToItemDetail: ()-> Unit,
    modifier: Modifier=Modifier,
    onDetailClick: (String) -> Unit={},
    viewModel : DetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {Text("Detail")}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemDetail,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Mahasiswa")
            }
        }
    ){ innerPadding ->
        DetailStatus(
            detailUiState = viewModel.mhsUIState,
            retryAction = { viewModel.getMhs() }, modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,
            onDeleteClick = {
                viewModel.deleteMahasiswa(it)
            }
        )
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier){
    Column (
        modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CircularProgressIndicator()
    }
}

//Detailscreen menampilkan error message
@Composable
fun OnError(retryAction: ()->Unit, modifier: Modifier = Modifier,message:String){
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = message,modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text("Retry")
        }
    }
}

@Composable
fun MhsLayout(
    mahasiswa: List<Mahasiswa>,
    modifier: Modifier = Modifier,
    onDetailClick: (Mahasiswa) -> Unit,
    onDeleteClick: (String) -> Unit = {}
){
    LazyColumn (
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        items(mahasiswa){mahasiswa ->
            MhsCard(
                mahasiswa = mahasiswa,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(mahasiswa) },
                onDeleteClick = {
                    onDeleteClick(mahasiswa.nim)
                }
            )
        }
    }
}

@Composable
fun MhsCard(
    mahasiswa: Mahasiswa,
    modifier: Modifier = Modifier,
    onDeleteClick: (String) -> Unit ={}
){
    Card (
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        Column (
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = mahasiswa.nama,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick =  { onDeleteClick(mahasiswa.nim)}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                    )
                }
                Text(
                    text = mahasiswa.nim,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = mahasiswa.kelas,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = mahasiswa.alamat,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = mahasiswa.judul_skripsi,
                style = MaterialTheme.typography.titleMedium
            )

        }
    }
}

@Composable
fun DetailStatus(
    detailUiState: DetailUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Mahasiswa) -> Unit={},
    onDetailClick: (String) -> Unit
){
    when(detailUiState) {
        is DetailUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is DetailUiState.Success ->
            MhsLayout(
                mahasiswa = detailUiState.mahasiswa, modifier = modifier.fillMaxWidth(),
                onDetailClick = {
                    onDetailClick(it.nim)
                },
                onDeleteClick = {
                    onDetailClick(it)
                }
            )
        is DetailUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize(), message = detailUiState.exception.
        message ?: "Error")
    }
}

