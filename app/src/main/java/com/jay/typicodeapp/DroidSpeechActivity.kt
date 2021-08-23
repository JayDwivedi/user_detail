package com.jay.typicodeapp

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.vikramezhil.droidspeech.DroidSpeech
import com.vikramezhil.droidspeech.OnDSListener
import kotlinx.android.synthetic.main.activity_droid_speech.*
import java.util.*


class DroidSpeechActivity : AppCompatActivity(), OnDSListener {
    private lateinit var droidSpeech: DroidSpeech
    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_droid_speech)
        droidSpeech = DroidSpeech(this, null)
        droidSpeech.setOnDroidSpeechListener(this)
        droidSpeech.setShowRecognitionProgressView(true)
        droidSpeech.setOneStepResultVerify(true)
        droidSpeech.setRecognitionProgressMsgColor(Color.WHITE)
        droidSpeech.setOneStepVerifyConfirmTextColor(Color.WHITE)
        droidSpeech.setOneStepVerifyRetryTextColor(Color.WHITE)
        // Starting droid speech



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

        // Starting droid speech
        droidSpeech.startDroidSpeechRecognition()


    }

    override fun onDestroy() {
        super.onDestroy()
        // Closing droid speech
        droidSpeech.closeDroidSpeechOperations()
    }

    override fun onDroidSpeechSupportedLanguages(
        currentSpeechLanguage: String?,
        supportedSpeechLanguages: MutableList<String>?
    ) {
        Log.i(TAG, "Current speech language = $currentSpeechLanguage")
        Log.i(TAG, "Supported speech languages = " + supportedSpeechLanguages.toString())

        if (supportedSpeechLanguages!!.contains("en-IN")) {
            // Setting the droid speech preferred language as en if found
            droidSpeech.setPreferredLanguage("en-IN")

            // Setting the confirm and retry text in en
            droidSpeech.setOneStepVerifyConfirmText("tunna")
            droidSpeech.setOneStepVerifyRetryText("tunna")
        }
    }

    override fun onDroidSpeechRmsChanged(rmsChangedValue: Float) {
        // Log.i(TAG, "Live speech result = $liveSpeechResult")
    }

    override fun onDroidSpeechLiveResult(liveSpeechResult: String?) {
        Log.i(TAG, "Live speech result = $liveSpeechResult")
        if (liveSpeechResult?.contains("hey monster") == true) {
            Toast.makeText(this, liveSpeechResult, Toast.LENGTH_LONG).show()
            droidSpeech.closeDroidSpeechOperations()
            startActivity(Intent("android.intent.action.INFOSCREEN"))


        }



    }

    override fun onDroidSpeechFinalResult(finalSpeechResult: String?) {
        // Setting the final speech result
        this.finalSpeechResult.text = finalSpeechResult.toString()
        if (finalSpeechResult?.contains("ok monster") == true) {
            Toast.makeText(this, finalSpeechResult, Toast.LENGTH_LONG).show()
        }

        if (droidSpeech.continuousSpeechRecognition) {
            val colorPallets1 =
                intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA)
            val colorPallets2 =
                intArrayOf(Color.YELLOW, Color.RED, Color.CYAN, Color.BLUE, Color.GREEN)

            // Setting random color pallets to the recognition progress view
            droidSpeech.setRecognitionProgressViewColors(if (Random().nextInt(2) == 0) colorPallets1 else colorPallets2)
        }
    }

    override fun onDroidSpeechClosedByUser() {

    }

    override fun onDroidSpeechError(errorMsg: String?) {

        // Speech error
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()

        // droidSpeech.closeDroidSpeechOperations()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Starting droid speech
                droidSpeech.startDroidSpeechRecognition()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}