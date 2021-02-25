package com.jay.typicodeapp.features.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jay.typicodeapp.MainApplication
import com.jay.typicodeapp.databinding.ActivityMainBinding
import com.jay.typicodeapp.features.details.UserDetailsActivity
import com.jay.typicodeapp.services.data.UserData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    private val usersListAdapter: UsersListAdapter by lazy {
        UsersListAdapter(::onUserSelected)
    }

    companion object {
        fun loadImage(imageView: ImageView) = Glide.with(MainApplication.getApplicationContext())
            .load("https://picsum.photos/200")
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(imageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        initVM()
    }

    private fun initUI() {
        binding.rvUsersList.adapter = usersListAdapter

        binding.root.setOnRefreshListener {
            binding.root.isRefreshing = false
            fetchNewUsers()
        }
    }

    private fun initVM() {
        mainViewModel.loaderState.observe(this, {
            reactOnLoader(it)
        })

        fetchNewUsers()
    }

    private fun reactOnLoader(it: Int) {
        if (it == 0) binding.progressIndicator.visibility = View.GONE
        else {
            binding.rvUsersList.visibility = View.GONE
            binding.progressIndicator.visibility = View.VISIBLE
        }
    }

    private fun fetchNewUsers() {
        mainViewModel.getUsers().observe(this, {
            if (it.isNotEmpty()) {
                usersListAdapter.updateUsers(it)
                binding.rvUsersList.visibility = View.VISIBLE
            }
        })
    }

    private fun onUserSelected(userData: UserData) {
        startActivity(UserDetailsActivity.createIntent(userData, this))
    }
}
