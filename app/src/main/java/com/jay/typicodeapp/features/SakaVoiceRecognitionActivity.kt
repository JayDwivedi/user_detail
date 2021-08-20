package com.jay.typicodeapp.features

import android.Manifest
import android.R.attr
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.stephenvinouze.core.interfaces.RecognitionCallback
import com.github.stephenvinouze.core.managers.KontinuousRecognitionManager
import com.github.stephenvinouze.core.models.RecognitionStatus
import com.jay.typicodeapp.R
import kotlinx.android.synthetic.main.activity_saka_voice_recognition.*
import java.util.*


class SakaVoiceRecognitionActivity : AppCompatActivity() , RecognitionCallback {

    var speakButton: Button? = null
    var textView: TextView? = null



    private val recognitionManager: KontinuousRecognitionManager by lazy {
        KontinuousRecognitionManager(this, activationKeyword = ACTIVATION_KEYWORD, shouldMute = false, callback = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saka_voice_recognition)
        recognitionManager.createRecognizer()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_REQUEST_CODE)
        }
        progressBar.visibility = View.INVISIBLE
        progressBar.max = 10
        textView = findViewById(R.id.textView)

        speakButton = findViewById(R.id.button)

        speakButton?.setOnClickListener {


                startVoiceRecognitionActivity()
            }

        }


        private fun startVoiceRecognitionActivity() {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            //Specify language
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
            // Specify language model
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Speech recognition demo"
            )
            // Specify how many results to receive
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
            // Start listening
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode === VOICE_RECOGNITION_REQUEST_CODE && resultCode === RESULT_OK) {
                // Fill the list view with the strings the recognizer thought it
                // could have heard
                val spokenText: String? =
                    data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                        results?.get(0)
                    }

                if (spokenText?.contains(ACTIVATION_KEYWORD) == true) {
                    informationMenu()
                }
                else
                    Toast.makeText(this,"Your text is not recognized.",Toast.LENGTH_LONG).show()

            }
        }

        private fun informationMenu() {

            Toast.makeText(this,"Your text is recognized and proceeding to next",Toast.LENGTH_LONG).show()
           startActivity(Intent("android.intent.action.INFOSCREEN"))
        }

    override fun onDestroy() {
        recognitionManager.destroyRecognizer()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startRecognition()
        }
    }

    override fun onPause() {
        stopRecognition()
        super.onPause()
    }

    private fun startRecognition() {
        progressBar.isIndeterminate = false
        progressBar.visibility = View.VISIBLE
        recognitionManager.startRecognition()
    }

    private fun stopRecognition() {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.INVISIBLE
        recognitionManager.stopRecognition()
    }

    private fun getErrorText(errorCode: Int): String = when (errorCode) {
        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
        SpeechRecognizer.ERROR_CLIENT -> "Client side error"
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
        SpeechRecognizer.ERROR_NETWORK -> "Network error"
        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
        SpeechRecognizer.ERROR_NO_MATCH -> "No match"
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
        SpeechRecognizer.ERROR_SERVER -> "Error from server"
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
        else -> "Didn't understand, please try again."
    }

    override fun onBeginningOfSpeech() {
        Log.i("Recognition","onBeginningOfSpeech")
    }

    override fun onBufferReceived(buffer: ByteArray) {
        Log.i("Recognition", "onBufferReceived: $buffer")
    }

    override fun onEndOfSpeech() {
        Log.i("Recognition","onEndOfSpeech")
    }

    override fun onError(errorCode: Int) {
        val errorMessage = getErrorText(errorCode)
        Log.i("Recognition","onError: $errorMessage")
        textView?.text = errorMessage
    }

    override fun onEvent(eventType: Int, params: Bundle) {
        Log.i("Recognition","onEvent")
    }

    override fun onReadyForSpeech(params: Bundle) {
        Log.i("Recognition","onReadyForSpeech")
    }

    override fun onRmsChanged(rmsdB: Float) {
        progressBar.progress = rmsdB.toInt()
    }

    override fun onPrepared(status: RecognitionStatus) {
        when (status) {
            RecognitionStatus.SUCCESS -> {
                Log.i("Recognition","onPrepared: Success")
                textView?.text  = "Recognition ready"
            }
            RecognitionStatus.UNAVAILABLE -> {
                Log.i("Recognition", "onPrepared: Failure or unavailable")
                AlertDialog.Builder(this)
                    .setTitle("Speech Recognizer unavailable")
                    .setMessage("Your device does not support Speech Recognition. Sorry!")
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            }
        }
    }

    override fun onKeywordDetected() {
        Log.i("Recognition","keyword detected !!!")
        textView?.text = "Keyword detected"
    }

    override fun onPartialResults(results: List<String>) {}

    override fun onResults(results: List<String>, scores: FloatArray?) {
        val text = results.joinToString(separator = "\n")
        Log.i("Recognition","onResults : $text")
        textView?.text = text
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_AUDIO_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startRecognition()
                }
            }
        }
    }





    companion object {
        /**
         * Put any keyword that will trigger the speech recognition
         */
        private const val ACTIVATION_KEYWORD = "tunna tunna"
        private const val RECORD_AUDIO_REQUEST_CODE = 101
        private const  val VOICE_RECOGNITION_REQUEST_CODE = 1234
    }

    }
