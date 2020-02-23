package com.hr.kurtovic.tomislav.familyboard

import android.app.Application
import com.hr.kurtovic.tomislav.familyboard.auth.AuthManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class FamilyBoardApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@FamilyBoardApp)
            modules(
                listOf(
                    firebaseAuth
                )
            )
        }
    }
}

val firebaseAuth = module {
    single { AuthManager() }
}