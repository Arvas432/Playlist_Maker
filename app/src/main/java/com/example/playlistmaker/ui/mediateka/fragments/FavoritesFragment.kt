package com.example.playlistmaker.ui.mediateka.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.ui.mediateka.view_model.FavoritesFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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