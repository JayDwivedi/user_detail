package com.jay.typicodeapp.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast


class VoiceRecognitionService : Service(), RecognitionListener {

    private lateinit var audioManager: AudioManager
    private var current_volume: Int=5
    private var speechRecognizer: SpeechRecognizer? = null


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "start Service.", Toast.LENGTH_SHORT).show()
        startListening()
        return START_STICKY
    }

    private fun startListening() {

         audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
       // current_volume = audioManager.getStreamVolume(AudioManager.STREAM_RING)
        audioManager.setStreamMute(AudioManager.STREAM_RING, true);
       // audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(applicationContext)
            speechRecognizer?.setRecognitionListener(this)

        }

        val voice = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        voice.putExtra(
            RecognizerIntent.EXTRA_CALLING_PACKAGE, javaClass
                .getPackage().name
        )
        voice.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        voice.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10)
        speechRecognizer?.startListening(voice)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        // TODO Auto-generated method stub
        val restartService = Intent(
            applicationContext,
            this.javaClass
        )
        restartService.setPackage(packageName)
        val restartServicePI = PendingIntent.getService(
            applicationContext, 1, restartService,
            PendingIntent.FLAG_ONE_SHOT
        )

        //Restart the service once it has been killed android
        val alarmService =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmService[AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 100] =
            restartServicePI
    }

    override fun onReadyForSpeech(bundle: Bundle?) {}

    override fun onBeginningOfSpeech() {}

    override fun onRmsChanged(v: Float) {}

    override fun onBufferReceived(bytes: ByteArray?) {}

    override fun onEndOfSpeech() {}

    override fun onError(i: Int) {

        startListening()

    }

    override fun onResults(results: Bundle) {

        val matches = results
            .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)


        if (matches?.contains("ok monster") == true) {

            // do your action, for now i am stopping the service
//            audioManager.setStreamVolume(AudioManager.STREAM_RING, current_volume,
//                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
            audioManager.setStreamMute(AudioManager.STREAM_RING, false);
                this.stopSelf()

            Toast.makeText(
                this,
                "word matched:- " + (matches?.get(0)?.toString() ?: "" + "Service will stop"),
                Toast.LENGTH_LONG
            ).show()

        } else
            Toast.makeText(
                this,
                "word not matched:- " + (matches?.get(0)?.toString() ?: ""),
                Toast.LENGTH_LONG
            ).show()
        // end of open app code

        startListening()
    }

    override fun onPartialResults(bundle: Bundle?) {}

    override fun onEvent(i: Int, bundle: Bundle?) {}

    override fun onDestroy() {
        speechRecognizer?.destroy()
    }

}