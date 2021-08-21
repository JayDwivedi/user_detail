package com.jay.typicodeapp.features.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jay.typicodeapp.DroidSpeechActivity
import com.jay.typicodeapp.MainApplication
import com.jay.typicodeapp.databinding.ActivityMainBinding
import com.jay.typicodeapp.features.KontinuousSpeechRecognitionActivity
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        fetchNewUsers()

    }

    private fun initUI() {

        binding.tvGreeting.setOnClickListener {

           // startActivity( Intent(this, SakaVoiceRecognitionActivity::class.java))

            startActivity( Intent(this, DroidSpeechActivity::class.java))
        }
        binding.rvUsersList.adapter = usersListAdapter

        binding.root.setOnRefreshListener {
            binding.root.isRefreshing = false
            fetchNewUsers()
        }
    }



    private fun fetchNewUsers() {
        binding.progressIndicator.visibility = View.VISIBLE
        mainViewModel.getUsers().observe(this, {
            if (it.isNotEmpty()) {
                usersListAdapter.updateUsers(it)

            }
            binding.progressIndicator.visibility = View.GONE
        })
    }

    private fun onUserSelected(userData: UserData) {
        startActivity(UserDetailsActivity.createIntent(userData, callbackInterface, this))
    }
    private val callbackInterface = object : CallbackInterface {
        override fun onFetchImage(imageView: ImageView) {
            imageView.visibility=View.VISIBLE
            Glide.with(MainApplication.getApplicationContext())
                .load("https:picsum.photos/200")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView)
        }
    }
}
