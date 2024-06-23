package com.rrrozzaq.expertdicodingawal.ui.main

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import com.rrrozzaq.core.domain.model.User
import com.rrrozzaq.core.utils.Async
import com.rrrozzaq.expertdicodingawal.R
import com.rrrozzaq.expertdicodingawal.databinding.ActivityMainBinding
import com.rrrozzaq.expertdicodingawal.model.UserModel
import com.rrrozzaq.expertdicodingawal.model.toModel
import com.rrrozzaq.expertdicodingawal.ui.detail.DetailActivity
import com.rrrozzaq.expertdicodingawal.ui.setting.SettingActivity
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.users.collect {
                    Log.d("Async", "onCreate: $it")
                    updateUI(it)
                }
            }
        }

        mainViewModel.getThemeSettings().observe(this) {
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
        binding.bottomNavBar.setItemSelected(R.id.home)

        binding.bottomNavBar.setOnItemSelectedListener {
            when (it) {
                R.id.home -> {
                    if (!isCurrentlyOnMainActivity()) {
                        startActivity(Intent(this@MainActivity, MainActivity::class.java))
                    }
                }
                R.id.favorite -> {
                    val uri = Uri.parse("githubuser://favorite")
                    startActivity(Intent(Intent.ACTION_VIEW, uri))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
                R.id.setting -> {
                    startActivity(Intent(this@MainActivity, SettingActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
        }
    }

    private fun isCurrentlyOnMainActivity(): Boolean {
        return true
    }


    private fun updateUI(list: Async<List<User>>) {
        when (list) {
            is Async.Loading -> binding.progressBar.visibility = View.VISIBLE
            is Async.Success -> {
                val items = list.data.map { it.toModel() }
                if (items.isNotEmpty()) {
                    setItemData(items)
                    binding.tvEmpty.visibility = View.GONE
                    binding.recycleView.visibility = View.VISIBLE
                } else {
                    binding.recycleView.visibility = View.GONE
                    binding.tvEmpty.visibility = View.VISIBLE
                }
                binding.progressBar.visibility = View.GONE
            }

            is Async.Error -> {
                Toast.makeText(this, list.message, Toast.LENGTH_SHORT).show()
                binding.tvEmpty.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_item, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.findUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun setItemData(itemsItem: List<UserModel>) {
        val adapter = MainAdapter(itemsItem)
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        adapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserModel) {
                showSelected(data.username)
            }
        })
    }

    private fun showSelected(nickname: String) {
        val moveWithObjectIntent = Intent(this@MainActivity, DetailActivity::class.java)
        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_NICKNAME, nickname)
        startActivity(moveWithObjectIntent)
    }

}