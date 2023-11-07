package com.achsanit.pitjaruscodingtest.di

import com.achsanit.pitjaruscodingtest.data.Repository
import com.achsanit.pitjaruscodingtest.data.local.DbDao
import com.achsanit.pitjaruscodingtest.domain.IRepository
import com.achsanit.pitjaruscodingtest.domain.Interactor
import com.achsanit.pitjaruscodingtest.domain.UseCase
import com.achsanit.pitjaruscodingtest.ui.fragment.login.LoginViewModel
import com.achsanit.pitjaruscodingtest.ui.fragment.mainmenu.MainMenuViewModel
import com.achsanit.pitjaruscodingtest.ui.fragment.splash.SplashViewModel
import com.achsanit.pitjaruscodingtest.ui.fragment.store.StoreViewModel
import com.achsanit.pitjaruscodingtest.ui.fragment.visit.VisitViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repoModule = module {
    single<IRepository> {
        Repository(get(),get())
    }
    single<UseCase> {
        Interactor(get())
    }
}

val viewModelModule = module {
    viewModel {
        LoginViewModel(get(), get())
    }
    viewModel {
        StoreViewModel(get())
    }
    viewModel {
        SplashViewModel(get())
    }
    viewModel {
        MainMenuViewModel(get())
    }
    viewModel {
        VisitViewModel(get())
    }
}