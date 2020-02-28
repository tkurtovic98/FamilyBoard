package com.hr.kurtovic.tomislav.familyboard

import android.app.Application
import com.hr.kurtovic.tomislav.familyboard.auth.AuthManager
import com.hr.kurtovic.tomislav.familyboard.auth.AuthService
import com.hr.kurtovic.tomislav.familyboard.auth.AuthServiceImpl
import com.hr.kurtovic.tomislav.familyboard.auth.AuthViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
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
                    firebaseAuth, authentication
                )
            )
        }
    }
}

val firebaseAuth = module {
    single { AuthManager() }
}

val authentication = module {

    single<AuthService> { AuthServiceImpl() }

    viewModel { AuthViewModel(get()) }
}