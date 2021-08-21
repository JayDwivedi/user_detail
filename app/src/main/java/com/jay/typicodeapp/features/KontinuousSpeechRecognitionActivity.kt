package com.jay.typicodeapp.features

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jay.typicodeapp.R

class KontinuousSpeechRecognitionActivity : AppCompatActivity(), RecognitionListener {

    companion object {
        /**
         * Put any keyword that will trigger the speech recognition
         */
        private const val ACTIVATION_KEYWORD = "OK"
        private const val RECORD_AUDIO_REQUEST_CODE = 101
    }

    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 1
    private var returnedText: TextView? = null
    private var returnedError: TextView? = null
    private var progressBar: ProgressBar? = null
    private var speech: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null
    private val LOG_TAG = "VoiceRecognitionActivity"

    private fun resetSpeechRecognizer() {
        if (speech != null) speech!!.destroy()
        speech = SpeechRecognizer.createSpeechRecognizer(this)
        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this))
        if (SpeechRecognizer.isRecognitionAvailable(this)) speech?.setRecognitionListener(this) else finish()
    }

    private fun setRecogniserIntent() {
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent!!.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
            "en"
        )
        recognizerIntent!!.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_kontinuous_speech_recognition2)

        // UI initialisation

        // UI initialisation
        returnedText = findViewById(R.id.textView1)
        returnedError = findViewById(R.id.errorView1)
        progressBar = findViewById(R.id.progressBar1)
        progressBar?.setVisibility(View.INVISIBLE)


        // start speech recogniser


        // start speech recogniser
        resetSpeechRecognizer()

        // start progress bar

        // start progress bar
        progressBar?.setVisibility(View.VISIBLE)
        progressBar?.setIndeterminate(true)

        // check for permission

        // check for permission
        val permissionCheck: Int =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSIONS_REQUEST_RECORD_AUDIO
            )
            return
        }

        setRecogniserIntent()
        speech!!.startListening(recognizerIntent)
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                speech!!.startListening(recognizerIntent)
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onResume() {
        Log.i(LOG_TAG, "resume")
        super.onResume()
        resetSpeechRecognizer()
        if(speech!=null)
        speech?.startListening(recognizerIntent)
    }

    override fun onPause() {
        Log.i(LOG_TAG, "pause")
        super.onPause()
        speech?.stopListening()
    }

    override fun onStop() {
        Log.i(LOG_TAG, "stop")
        super.onStop()
        if (speech != null) {
            speech?.destroy()
        }
    }


    override fun onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech")
        progressBar!!.isIndeterminate = false
        progressBar!!.max = 10
    }

    override fun onBufferReceived(buffer: ByteArray) {
        Log.i(LOG_TAG, "onBufferReceived: $buffer")
    }

    override fun onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech")
        progressBar!!.isIndeterminate = true
        speech!!.stopListening()
    }

    override fun onResults(results: Bundle) {
        Log.i(LOG_TAG, "onResults")
        val matches = results
            .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        var text = ""
        for (result in matches!!) text += """
     $result
     
     """.trimIndent()
        returnedText!!.text = text
        if (matches.contains("ok monster"))
        {Toast.makeText(
            this,
            "Word matched:-$text",
            Toast.LENGTH_SHORT
        ).show()

            startActivity(Intent("android.intent.action.INFOSCREEN"))
        }else Toast.makeText(
            this,
            "Word not matched:-$text", Toast.LENGTH_SHORT
        ).show()
        speech!!.startListening(recognizerIntent)
    }

    override fun onError(errorCode: Int) {
        val errorMessage = getErrorText(errorCode)
        Log.i(LOG_TAG, "FAILED $errorMessage")
        returnedError!!.text = errorMessage

        // rest voice recogniser
        resetSpeechRecognizer()
        speech!!.startListening(recognizerIntent)
    }

    override fun onEvent(arg0: Int, arg1: Bundle?) {
        Log.i(LOG_TAG, "onEvent")
    }

    override fun onPartialResults(arg0: Bundle?) {
        Log.i(LOG_TAG, "onPartialResults")
    }

    override fun onReadyForSpeech(arg0: Bundle?) {
        Log.i(LOG_TAG, "onReadyForSpeech")
    }

    override fun onRmsChanged(rmsdB: Float) {
        //Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar!!.progress = rmsdB.toInt()
    }

    fun getErrorText(errorCode: Int): String {
        val message: String
        message = when (errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No match"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
            SpeechRecognizer.ERROR_SERVER -> "error from server"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Didn't understand, please try again."
        }
        return message
    }

}
