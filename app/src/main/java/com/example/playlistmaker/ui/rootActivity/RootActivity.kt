package com.example.playlistmaker.ui.rootActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.playlistCreationFragment, R.id.playerFragment -> {
                    binding.bottomNavigationView.isVisible = false
                }
                else -> {
                    binding.bottomNavigationView.isVisible = true
                }
            }
        }
        binding.bottomNavigationView.setupWithNavController(navController)
    }
    //Кастомный тост тк. Google запретил кастомные view для обычных тостов
    fun showFakeToast(message: String){
        binding.fakeToastTv.text = message
        val enterAnimation = AnimationUtils.loadAnimation(this, R.anim.fake_toast_animation_up)
        binding.fakeToastTv.isVisible = true
        binding.fakeToastTv.startAnimation(enterAnimation)
        binding.fakeToastTv.postDelayed({
            val exitAnimation = AnimationUtils.loadAnimation(this, R.anim.fake_toast_animation_down)
            binding.fakeToastTv.startAnimation(exitAnimation)
            exitAnimation.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(animation: Animation?) = Unit
                override fun onAnimationEnd(animation: Animation?) {
                    binding.fakeToastTv.isVisible = false
                }

                override fun onAnimationRepeat(animation: Animation?) = Unit
            })
        }, fakeToastDuration)
    }
    companion object{
        const val fakeToastDuration = 2000L
    }
}