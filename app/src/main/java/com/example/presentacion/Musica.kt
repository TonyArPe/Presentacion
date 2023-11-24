package com.example.presentacion

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class Musica: AppCompatActivity() {
    private lateinit var botonPlay: Button
    private lateinit var  botonPause: Button
    private lateinit var botonStop: Button
    private lateinit var botonHome: Button
    private lateinit var botonAnterior: Button
    private lateinit var botonSiguiente: Button
    private lateinit var cancionActual: Uri
    private lateinit var listaCanciones: ArrayList<Uri>
    private var posicionCancionActual: Int = 0
    var mMediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musica)

        val botonplay = findViewById<Button>(R.id.botonPlay)
        val botonpause = findViewById<Button>(R.id.botonPause)
        val botonstop = findViewById<Button>(R.id.botonStop)
        val botonhome = findViewById<Button>(R.id.botonHome)
        val botonAnterior = findViewById<Button>(R.id.botonAnterior)
        val botonSiguiente = findViewById<Button>(R.id.botonSiguiente)



        // 1. Plays the water sound

        fun playSound() {
            if (mMediaPlayer == null) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.water)
                mMediaPlayer!!.isLooping = true
                mMediaPlayer!!.start()
            } else mMediaPlayer!!.start()
        }
        botonplay.setOnClickListener { playSound() }

        // 2. Pause playback
        fun pauseSound() {
            if (mMediaPlayer?.isPlaying == true) mMediaPlayer?.pause()
        }
        botonpause.setOnClickListener { pauseSound() }

        // 3. Stops playback
        fun stopSound() {
            if (mMediaPlayer != null) {
                mMediaPlayer!!.stop()
                mMediaPlayer!!.release()
                mMediaPlayer = null
            }
        }
        botonstop.setOnClickListener { stopSound() }

        botonAnterior.setOnClickListener {
            if (posicionCancionActual > 0) {
                posicionCancionActual--
                cancionActual = listaCanciones[posicionCancionActual]
                mMediaPlayer?.reset()
                mMediaPlayer?.setDataSource(this, cancionActual)
                mMediaPlayer?.prepare()
                mMediaPlayer?.start()
            }
        }

        botonSiguiente.setOnClickListener {
            if (posicionCancionActual < listaCanciones.size - 1) {
                posicionCancionActual++
                cancionActual = listaCanciones[posicionCancionActual]
                mMediaPlayer?.reset()
                mMediaPlayer?.setDataSource(this, cancionActual)
                mMediaPlayer?.prepare()
                mMediaPlayer?.start()
            }
        }



        botonhome.setOnClickListener {
            super.onBackPressedDispatcher.onBackPressed()
        }

        fun onStop() {
            super.onStop()
            if (mMediaPlayer != null) {
                mMediaPlayer!!.release()
                mMediaPlayer = null
            }
        }

        // Set listeners for buttons to trigger actions




    }


}