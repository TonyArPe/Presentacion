package com.example.presentacion

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ImageButton
import com.example.srodenas.buttondescribe.R
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnMusica = findViewById<ImageButton>(R.id.btnMusica)
        val botonLLamar = findViewById<ImageButton>(R.id.imageButton)
        val botonInternet = findViewById<ImageButton>(R.id.botonInternet)
        val botonSalir = findViewById<ImageButton>(R.id.botonSalir)
        val botonFavoritos = findViewById<ImageButton>(R.id.botonFavoritos)
        val botonJuego = findViewById<ImageButton>(R.id.btnJuego)

        botonLLamar.setOnClickListener {
            val intent = Intent(this, LLamada::class.java)
            startActivity(intent)
        }
        btnMusica.setOnClickListener {
            val intent = Intent(this, Musica::class.java)
            startActivity(intent)
        }

        botonJuego.setOnClickListener{
            val intent = Intent(this, Juego::class.java)
            startActivity(intent)
        }

        botonInternet.setOnClickListener {
            openChrome()
        }

        botonSalir.setOnClickListener {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            exitProcess(0)
        }
        botonFavoritos.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            val uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + File.separator + "Favoritos" + File.separator)
            intent.setDataAndType(uri, "*/*")
            startActivity(Intent.createChooser(intent, "Abrir carpeta"))
        }
    }
    private fun openChrome() {
        val url = "https://www.google.com"
        val chromePackage = "com.android.chrome" // Paquete de Google Chrome

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage(chromePackage) // Establece el paquete de Chrome

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // Si Chrome no está instalado, abre en el navegador predeterminado
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }
}