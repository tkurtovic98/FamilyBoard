package com.hr.kurtovic.tomislav.familyboard

import android.app.Application
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberService
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMemberServiceImpl
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMessageService
import com.hr.kurtovic.tomislav.familyboard.api.FamilyMessageServiceImpl
import com.hr.kurtovic.tomislav.familyboard.auth.AuthManager
import com.hr.kurtovic.tomislav.familyboard.auth.AuthService
import com.hr.kurtovic.tomislav.familyboard.auth.AuthServiceImpl
import com.hr.kurtovic.tomislav.familyboard.auth.AuthViewModel
import com.hr.kurtovic.tomislav.familyboard.main_board.MainBoardViewModel
import com.hr.kurtovic.tomislav.familyboard.main_board.input.pets.PetsService
import com.hr.kurtovic.tomislav.familyboard.main_board.input.pets.PetsServiceImpl
import com.hr.kurtovic.tomislav.familyboard.main_board.input.pets.PetsViewModel
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
                    api, authentication, mainBoard, pets
                )
            )
        }
    }
}

val api = module {
    single { AuthManager(get()) }
    single<FamilyMemberService> { FamilyMemberServiceImpl() }
    single<FamilyMessageService> { FamilyMessageServiceImpl() }
}

val authentication = module {

    single<AuthService> { AuthServiceImpl() }

    viewModel { AuthViewModel(get()) }
}

val pets = module {
    single<PetsService> { PetsServiceImpl() }

    viewModel { PetsViewModel(get(), get()) }
}

val mainBoard = module {

    viewModel { MainBoardViewModel() }
}