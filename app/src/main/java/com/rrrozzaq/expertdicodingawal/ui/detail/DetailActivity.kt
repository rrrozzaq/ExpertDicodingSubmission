package com.rrrozzaq.expertdicodingawal.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import com.rrrozzaq.core.domain.model.DetailUser
import com.rrrozzaq.core.utils.Async
import com.rrrozzaq.expertdicodingawal.R
import com.rrrozzaq.expertdicodingawal.databinding.ActivityDetailBinding
import com.rrrozzaq.expertdicodingawal.model.DetailModel
import com.rrrozzaq.expertdicodingawal.model.toModel
import com.rrrozzaq.expertdicodingawal.model.toUser
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nickname = intent.getStringExtra(EXTRA_NICKNAME).toString()

        detailViewModel.showUser(nickname).toString()
        lifecycleScope.launch {
            detailViewModel.detailUser.collect {
                updateUI(it)
            }
        }

        //follower following
        val sectionsPagerAdapter = FollowAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun updateUI(result: Async<DetailUser>) {
        when (result) {
            is Async.Loading -> showLoading(true)
            is Async.Success -> {
                displayUser(result.data.toModel())
                showLoading(false)

                detailViewModel.checkUser(result.data.username)
                detailViewModel.showFollowers(result.data.username)
                detailViewModel.showFollowing(result.data.username)
            }

            is Async.Error -> {
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun displayUser(detailUser: DetailModel) {
        with(binding) {
            Glide.with(avatarView).load(detailUser.avatarUrl)
                .error(R.drawable.ic_baseline_broken_image_24).into(avatarView)
            name.text = detailUser.fullName ?: "-"
            usernameView.text = detailUser.username
            bio.text = detailUser.bio ?: "-"
            val foll = String.format(
                resources.getString(
                    R.string.follower,
                    detailUser.followers.toString()
                )
            )
            val fill = String.format(
                resources.getString(
                    R.string.following,
                    detailUser.following.toString()
                )
            )
            follower.text = foll
            following.text = fill
        }
        lifecycleScope.launch {
            detailViewModel.isFavorite.collect {
                onFavorite(detailUser, it)
            }
        }
    }

    private fun onFavorite(detailUser: DetailModel, isFavorite: Int) {
        var clicked: Boolean
        clicked = isFavorite > 0
        binding.fab.setImageResource(
            if (clicked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
        )
        binding.fab.setOnClickListener {
            clicked = !clicked
            if (clicked) {
                detailViewModel.favUser(detailUser.toUser())
                binding.fab.setImageResource(R.drawable.ic_baseline_favorite_24)
                Toast.makeText(
                    this@DetailActivity,
                    "Disimpan ke favorit",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                detailViewModel.unFavUser(detailUser.id)
                binding.fab.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                Toast.makeText(
                    this@DetailActivity,
                    "Daftar favorit diperbarui",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_NICKNAME = "extra_nickname"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.foll,
            R.string.fill
        )
    }

}