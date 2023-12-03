package com.example.presentacion

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.srodenas.buttondescribe.R

class LLamada : AppCompatActivity() {
    private lateinit var btnCall: Button
    private lateinit var btnHome: Button
    // ...

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_llamada)
        btnCall = findViewById(R.id.btnCall)
        btnHome = findViewById(R.id.btnHome)
        // ...

        // Acción al hacer clic en el botón de llamar
        btnCall.setOnClickListener {
            if (checkPermission()) {
                val phoneNumber = "tel:611496339" // Reemplaza con el número al que deseas llamar
                val callIntent = Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber))
                startActivity(callIntent)
            } else {
                requestPermission()
            }
        }

        // Acción al hacer clic en el botón de Home
        btnHome.setOnClickListener {
            val homeIntent = Intent(this, MainActivity::class.java)
            startActivity(homeIntent)
        }

        // ...
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CALL_PHONE),
            REQUEST_CALL_PHONE_PERMISSION
        )
    }

    companion object {
        private const val REQUEST_CALL_PHONE_PERMISSION = 1
    }

}



