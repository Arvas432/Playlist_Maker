package com.example.playlistmaker.ui.mediateka.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.ui.mediateka.view_model.FavoritesFragmentViewModel
import com.example.playlistmaker.utils.BindingFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {
    private val favoritesViewModel: FavoritesFragmentViewModel by viewModel{
        parametersOf()
    }
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            FavoritesFragment().apply {}
    }
}