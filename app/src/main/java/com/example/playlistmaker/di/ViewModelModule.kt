package com.example.playlistmaker.di
import com.example.playlistmaker.ui.mediateka.view_model.FavoritesFragmentViewModel
import com.example.playlistmaker.ui.mediateka.view_model.MediatekaViewModel
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistCreationViewModel
import com.example.playlistmaker.ui.mediateka.view_model.PlaylistsFragmentViewModel
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    viewModel {
        PlayerViewModel(get(), get(), get())
    }
    viewModel{
        SearchViewModel(get(), get())
    }
    viewModel{
        SettingsViewModel(get())
    }
    viewModel {
        FavoritesFragmentViewModel(get())
    }
    viewModel {
        PlaylistsFragmentViewModel(get())
    }
    viewModel{
        MediatekaViewModel()
    }
    viewModel{
        PlaylistCreationViewModel(get())
    }
}