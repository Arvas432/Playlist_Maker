package com.example.playlistmaker.ui.settings.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.mediateka.fragments.BindingFragment
import com.example.playlistmaker.ui.settings.states.SettingsState
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BindingFragment<FragmentSettingsBinding>(){
    private val viewModel by viewModel<SettingsViewModel>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner){
            when(it){
                SettingsState.switchOff -> binding.nightModeSwitch.isChecked = false
                SettingsState.switchOn -> binding.nightModeSwitch.isChecked = true
                SettingsState.switchActive -> binding.nightModeSwitch.isEnabled = true
                SettingsState.switchInactive -> binding.nightModeSwitch.isEnabled = false
            }
        }
        binding.nightModeSwitch.setOnCheckedChangeListener { switcher, isChecked ->
            viewModel.switchTheme(isChecked)
        }
        binding.shareButton.setOnClickListener {
            val shareLink = getString(R.string.share_link)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareLink)
            shareIntent.type = "text/plain"
            startActivity(shareIntent)
        }
        binding.techSupportButton.setOnClickListener {
            val techSupportIntent = Intent(Intent.ACTION_SENDTO)
            techSupportIntent.data = Uri.parse("mailto:")
            techSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
            techSupportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            techSupportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_contents))
            startActivity(Intent.createChooser(techSupportIntent, "Send Email"))
        }
        binding.termsOfServiceButton.setOnClickListener {
            val termsOfServiceLink = getString(R.string.terms_of_service_link)
            val termsOfServiceIntent = Intent(Intent.ACTION_VIEW)
            termsOfServiceIntent.data = Uri.parse(termsOfServiceLink)
            startActivity(termsOfServiceIntent)
        }
    }
    companion object{
        fun newInstance() = SettingsFragment.apply {  }
    }
}