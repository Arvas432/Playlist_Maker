package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backButton = findViewById<ImageButton>(R.id.back_button)
        val shareButton = findViewById<FrameLayout>(R.id.share_button)
        val techSupportButton = findViewById<FrameLayout>(R.id.tech_support_button)
        val termsOfServiceButton = findViewById<FrameLayout>(R.id.terms_of_service_button)
        val nightModeSwitch = findViewById<SwitchCompat>(R.id.night_mode_switch)
        nightModeSwitch.setOnClickListener {

        }

        nightModeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView?.isChecked == true) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        backButton.setOnClickListener {
            finish()
        }
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
            shareIntent.type = "text/plain"
            startActivity(shareIntent)
        }
        techSupportButton.setOnClickListener {
            val techSupportIntent = Intent(Intent.ACTION_SEND)
            techSupportIntent.data = Uri.parse("mailto:")
            techSupportIntent.type = "message/rfc822"
            techSupportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
            techSupportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            techSupportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_contents))
            startActivity(Intent.createChooser(techSupportIntent, "Send Email"))
        }
        termsOfServiceButton.setOnClickListener {
            val termsOfServiceIntent = Intent(Intent.ACTION_VIEW)
            termsOfServiceIntent.data = Uri.parse(getString(R.string.terms_of_service_link))
            startActivity(termsOfServiceIntent)
        }
    }
}
