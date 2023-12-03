package com.example.presentacion

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.srodenas.buttondescribe.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    companion object {
        private const val KEY_NUMBER_OF_THROWS = "numberOfThrows"
        private const val DEFAULT_NUMBER_OF_THROWS = 5
    }

    private lateinit var bindingSettings: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSettings = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(bindingSettings.root)
        initSharedPreferences()

        bindingSettings.btnSaveSettings.setOnClickListener {
            val numberOfThrows = bindingSettings.edtNumberOfThrows.text.toString().toInt()
            saveNumberOfThrows(numberOfThrows)
            showToast("Configuración guardada")
        }
    }

    private fun initSharedPreferences() {
        sharedPreferences = getPreferences(MODE_PRIVATE)
        val numberOfThrows = getNumberOfThrows()
        bindingSettings.edtNumberOfThrows.setText(numberOfThrows.toString())
    }

    private fun saveNumberOfThrows(numberOfThrows: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_NUMBER_OF_THROWS, numberOfThrows)
        editor.apply()
    }

    private fun getNumberOfThrows(): Int {
        return sharedPreferences.getInt(KEY_NUMBER_OF_THROWS, DEFAULT_NUMBER_OF_THROWS)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
