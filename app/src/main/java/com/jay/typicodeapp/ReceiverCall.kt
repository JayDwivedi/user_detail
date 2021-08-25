package com.jay.typicodeapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.jay.typicodeapp.services.VoiceRecognitionService

class ReceiverCall : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.i("Service Stops", "Ohhhhhhh")
        p0?.startService(Intent(p0, VoiceRecognitionService::class.java))
    }
}