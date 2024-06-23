package com.rrrozzaq.expertdicodingawal.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import com.rrrozzaq.expertdicodingawal.R
import com.rrrozzaq.expertdicodingawal.databinding.ActivitySettingBinding
import com.rrrozzaq.expertdicodingawal.ui.main.MainActivity

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
                binding.bottomNavBar.setBackgroundResource(R.drawable.background_navbar_dark)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
                binding.bottomNavBar.setBackgroundResource(R.drawable.background_navbar_light)
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }

        setUpTabBar()
    }

    private fun setUpTabBar() {
        binding.bottomNavBar.setItemSelected(R.id.setting)

        binding.bottomNavBar.setOnItemSelectedListener {
            when (it) {
                R.id.home -> {
                    startActivity(Intent(this@SettingActivity, MainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

                }
                R.id.favorite -> {
                    val uri = Uri.parse("githubuser://favorite")
                    startActivity(Intent(Intent.ACTION_VIEW, uri))
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                }
            }
        }
    }
}