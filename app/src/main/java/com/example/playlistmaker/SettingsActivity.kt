package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val backButton = findViewById<ImageButton>(R.id.back_button)
        val shareButton = findViewById<TextView>(R.id.share_button)
        val techSupportButton = findViewById<TextView>(R.id.tech_support_button)
        val termsOfServiceButton = findViewById<TextView>(R.id.terms_of_service_button)
        val nightModeSwitch = findViewById<SwitchMaterial>(R.id.night_mode_switch)
        if((applicationContext as App).darkTheme){
            nightModeSwitch.isChecked = true
        }
        nightModeSwitch.setOnCheckedChangeListener { switcher, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
            if (isChecked){
                sharedPrefs
                    .edit()
                    .putString(THEME_MODE_KEY, DARK_MODE_VALUE)
                    .apply()
            }
            else{
                sharedPrefs
                    .edit()
                    .putString(THEME_MODE_KEY, LIGHT_MODE_VALUE)
                    .apply()
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
            val techSupportIntent = Intent(Intent.ACTION_SENDTO)
            techSupportIntent.data = Uri.parse("mailto:")
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
    companion object {

        const val THEME_MODE_KEY = "key_for_theme_mode"
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val DARK_MODE_VALUE = "dark"
        const val LIGHT_MODE_VALUE = "light"
    }
}