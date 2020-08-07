package com.hr.kurtovic.tomislav.familyboard

import android.app.Application
import com.google.firebase.messaging.FirebaseMessagingService
import com.hr.kurtovic.tomislav.familyboard.api.*
import com.hr.kurtovic.tomislav.familyboard.auth.AuthService
import com.hr.kurtovic.tomislav.familyboard.auth.AuthServiceImpl
import com.hr.kurtovic.tomislav.familyboard.auth.AuthViewModel
import com.hr.kurtovic.tomislav.familyboard.family_list.FamilyListViewModel
import com.hr.kurtovic.tomislav.familyboard.main_board.MainBoardViewModel
import com.hr.kurtovic.tomislav.familyboard.main_board.input.event.EventViewModel
import com.hr.kurtovic.tomislav.familyboard.main_board.input.pets.PetsViewModel
import com.hr.kurtovic.tomislav.familyboard.main_board.input.store.StoreViewModel
import com.hr.kurtovic.tomislav.familyboard.main_board.message_display.MessageDisplayViewModel
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
                    api, authentication, mainBoard, input, familyList, profile, appModels
                )
            )
        }
    }
}

val api = module {
    single<FamilyMemberService> { FamilyMemberServiceImpl() }
    single<FamilyMessageService> { FamilyMessageServiceImpl(androidApplication().applicationContext) }
    single<FamilyService> { FamilyServiceImpl() }
    single<FirebaseMessagingService> { FirebaseMessagingServiceImpl() }
}

val authentication = module {
    single<AuthService> { AuthServiceImpl() }
    viewModel { AuthViewModel(get(), get(), get()) }
}

val input = module {
    viewModel { PetsViewModel(androidApplication().applicationContext, get(), get()) }
    viewModel { EventViewModel(get(), get()) }
    viewModel { StoreViewModel(androidApplication().resources, get(), get()) }
}

val mainBoard = module {
    viewModel { MainBoardViewModel(get(), get()) }
    viewModel { (messageId: String) -> MessageDisplayViewModel(messageId, get(), get()) }
}

val familyList = module {
    viewModel { FamilyListViewModel(get(), get()) }
}

val profile = module {
    viewModel { ProfileViewModel(get(), get(), get()) }
}

val appModels = module {
    viewModel { SharedViewModel(androidApplication().applicationContext) }
    viewModel { FragmentHolderViewModel(androidApplication().applicationContext, get()) }
}
