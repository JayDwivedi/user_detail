package com.jay.typicodeapp.features.details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jay.typicodeapp.databinding.ActivityUserDetailsBinding
import com.jay.typicodeapp.features.home.CallbackInterface
import com.jay.typicodeapp.features.home.MainActivity
import com.jay.typicodeapp.services.data.UserData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailsBinding


    private val userData: UserData? by lazy {
        (intent.getParcelableExtra(USER_DATA_KEY) as? UserData)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    @SuppressLint("SetTextI18n")
    private fun initUI() {
        userData?.let {
            binding.run {
                tvUname.text = "Username: ${it.username}"
                tvUID.text = "UID: ${it.id}"
                tvLocation.text =
                    "Latitude: ${it.address.geo.lat}, Longitude: ${it.address.geo.lng}"
                btnShowImage.setOnClickListener {
                    (intent.getSerializableExtra(CALLBACK_INTERFACE) as? CallbackInterface)?.onFetchImage(
                        binding.ivMain
                    )
                }
            }
        } ?: finish()
    }

    companion object {

        private const val USER_DATA_KEY = "user_data_key"

        private const val CALLBACK_INTERFACE = "interface"

        fun createIntent(userData: UserData, callback: CallbackInterface, context: Context) =
            Intent(context, UserDetailsActivity::class.java).apply {
                putExtra(USER_DATA_KEY, userData)
                putExtra(CALLBACK_INTERFACE, callback)
            }
    }
}
