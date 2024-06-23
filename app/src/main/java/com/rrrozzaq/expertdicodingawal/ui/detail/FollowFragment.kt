package com.rrrozzaq.expertdicodingawal.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rrrozzaq.core.domain.model.User
import com.rrrozzaq.core.utils.Async
import com.rrrozzaq.expertdicodingawal.databinding.FragmentFollowBinding
import com.rrrozzaq.expertdicodingawal.model.UserModel
import com.rrrozzaq.expertdicodingawal.model.toModel
import com.rrrozzaq.expertdicodingawal.ui.main.MainAdapter
import kotlinx.coroutines.launch

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private lateinit var detailViewModel: DetailViewModel
    private var position: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        arguments?.let {
            position = it.getInt(ARG_POSITION)
        }

        updateUI()
    }

    private fun updateUI() {
        if (position == 1) {
            lifecycleScope.launch {
                detailViewModel.listFollowers.collect {
                    showList(it)
                }
            }
        } else {
            lifecycleScope.launch {
                detailViewModel.listFollowing.collect {
                    showList(it)
                }
            }
        }
    }

    private fun showList(result: Async<List<User>>) {
        when (result) {
            is Async.Loading -> showLoading(true)
            is Async.Success -> {
                val items = result.data.map { it.toModel() }
                if (items.isNotEmpty()) {
                    setItemData(items)
                    binding.tvEmpty.visibility = View.GONE
                } else {
                    binding.tvEmpty.visibility = View.VISIBLE
                }
                showLoading(false)
            }

            is Async.Error -> {
                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        }
    }

    private fun setItemData(itemsItem: List<UserModel>) {
        val adapter = MainAdapter(itemsItem)
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_POSITION = "section_number"
    }

}