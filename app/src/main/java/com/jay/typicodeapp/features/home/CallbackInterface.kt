package com.jay.typicodeapp.features.home

import android.widget.ImageView
import java.io.Serializable

interface CallbackInterface : Serializable {
    fun onFetchImage(imageView: ImageView)
}
