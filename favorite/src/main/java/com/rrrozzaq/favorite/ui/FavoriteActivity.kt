package com.rrrozzaq.favorite.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.EntryPointAccessors
import com.rrrozzaq.core.domain.model.User
import com.rrrozzaq.core.utils.Async
import com.rrrozzaq.expertdicodingawal.R
import com.rrrozzaq.favorite.databinding.ActivityFavoriteBinding
import com.rrrozzaq.favorite.di.DaggerUseCaseComponent
import com.rrrozzaq.expertdicodingawal.di.UseCaseDependency
import com.rrrozzaq.expertdicodingawal.model.UserModel
import com.rrrozzaq.expertdicodingawal.model.toModel
import com.rrrozzaq.expertdicodingawal.ui.detail.DetailActivity
import com.rrrozzaq.expertdicodingawal.ui.main.MainActivity
import com.rrrozzaq.expertdicodingawal.ui.main.MainAdapter
import com.rrrozzaq.expertdicodingawal.ui.setting.SettingActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    @Inject
    lateinit var factory: ViewModelFactory
    private val favoriteViewModel: FavoriteViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerUseCaseComponent.builder()
            .context(this)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    applicationContext,
                    UseCaseDependency::class.java
                )
            )
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteViewModel.getAllFavUser()
        lifecycleScope.launch {
            favoriteViewModel.listUsers.collect {
                updateUI(it)
            }
        }

        favoriteViewModel.getThemeSettings().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.bottomNavBar.setBackgroundResource(R.drawable.background_navbar_dark)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.bottomNavBar.setBackgroundResource(R.drawable.background_navbar_light)
            }
        }

        setUpTabBar()
    }

    private fun setUpTabBar() {
        binding.bottomNavBar.setItemSelected(R.id.favorite)

        binding.bottomNavBar.setOnItemSelectedListener {
            when (it) {
                R.id.home -> {
                    startActivity(Intent(this@FavoriteActivity, MainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

                }
                R.id.favorite -> {
                    val uri = Uri.parse("githubuser://favorite")
                    startActivity(Intent(Intent.ACTION_VIEW, uri))
                }
                R.id.setting -> {
                    startActivity(Intent(this@FavoriteActivity, SettingActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
        }
    }

    private fun updateUI(result: Async<List<User>>) {
        when (result) {
            is Async.Loading -> binding.progressBar.visibility = View.VISIBLE
            is Async.Success -> {
                Log.d("FavoriteActivity", "Successfully loaded favorite users: ${result.data}")
                val data = result.data.map { it.toModel() }
                if (data.isNotEmpty()) {
                    setItemData(data)
                    binding.tvEmpty.visibility = View.GONE
                    binding.recycleViewFav.visibility = View.VISIBLE
                } else {
                    binding.recycleViewFav.visibility = View.GONE
                    binding.tvEmpty.visibility = View.VISIBLE
                }
                binding.progressBar.visibility = View.GONE
            }
            is Async.Error -> {
                Log.d("FavoriteActivity", "Error loading favorite users: ${result.message}")
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setItemData(list: List<UserModel>) {
        val adapter = MainAdapter(list)
        binding.recycleViewFav.adapter = adapter
        binding.recycleViewFav.layoutManager = LinearLayoutManager(this)
        binding.recycleViewFav.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        adapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserModel) {
                showSelected(data.username)
            }
        })
    }

    private fun showSelected(nickname: String) {
        val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_NICKNAME, nickname)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.getAllFavUser()
    }
}