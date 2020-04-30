package com.hr.kurtovic.tomislav.familyboard

import android.app.Application
import com.hr.kurtovic.tomislav.familyboard.api.*
import com.hr.kurtovic.tomislav.familyboard.auth.AuthService
import com.hr.kurtovic.tomislav.familyboard.auth.AuthServiceImpl
import com.hr.kurtovic.tomislav.familyboard.auth.AuthViewModel
import com.hr.kurtovic.tomislav.familyboard.family_list.FamilyListViewModel
import com.hr.kurtovic.tomislav.familyboard.main_board.MainBoardViewModel
import com.hr.kurtovic.tomislav.familyboard.main_board.input.pets.PetsViewModel
import com.hr.kurtovic.tomislav.familyboard.profile.ProfileViewModel
import org.koin.android.ext.koin.androidApplication
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
                    api, authentication, mainBoard, pets, familyList, profileFragment, sharedModel
                )
            )
        }
    }
}

val api = module {
    single<FamilyMemberService> { FamilyMemberServiceImpl() }
    single<FamilyMessageService> { FamilyMessageServiceImpl(androidApplication().applicationContext) }
    single<FamilyService> { FamilyServiceImpl() }
}

val authentication = module {
    single<AuthService> { AuthServiceImpl() }
    viewModel { AuthViewModel(get(), get()) }
}

val pets = module {
    viewModel { PetsViewModel(get(), get()) }
}

val mainBoard = module {
    viewModel { MainBoardViewModel() }
}

val familyList = module {
    viewModel { FamilyListViewModel(get(), get()) }
}

val profileFragment = module {
    viewModel { ProfileViewModel(get(), get()) }
}

val sharedModel = module {
    viewModel { SharedViewModel(androidApplication().applicationContext) }
}