package com.example.kotlin_firebase

import android.app.Application
import com.example.kotlin_firebase.di.AppContainer
import com.example.kotlin_firebase.di.MahasiswaContainer
//Step 6
//step 7 di manifest tambahin android name dan access internet
class MahasiswaApplications: Application(){
    lateinit var container: AppContainer

    override fun onCreate(){
        super.onCreate()
        container =  MahasiswaContainer()
    }
}