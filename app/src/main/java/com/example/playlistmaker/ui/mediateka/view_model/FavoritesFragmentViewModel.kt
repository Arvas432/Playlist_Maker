package com.example.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.ui.mediateka.states.FavoritesState
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(private val favoritesInteractor: FavoritesInteractor): ViewModel() {
    private var screenStateLiveData = MutableLiveData<FavoritesState>(FavoritesState.Default)
    fun getScreenStateLiveData(): LiveData<FavoritesState> = screenStateLiveData
    fun getFavorites(){
        viewModelScope.launch{
            favoritesInteractor.getFavorites().collect{
                if (it.isEmpty()){
                    screenStateLiveData.postValue(FavoritesState.Empty)
                }else{
                    screenStateLiveData.postValue(FavoritesState.Content(it))
                }
            }
        }
    }
}