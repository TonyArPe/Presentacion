package com.example.presentacion

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.srodenas.buttondescribe.R
import com.example.srodenas.buttondescribe.databinding.JuegoActivityBinding
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class Juego : AppCompatActivity() {

    companion object {
        private const val KEY_NUMBER_OF_THROWS = "numberOfThrows"
        private const val DEFAULT_NUMBER_OF_THROWS = 5
    }

    private lateinit var bindingDados: JuegoActivityBinding
    private val results = mutableListOf<List<Int>>()
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var btnSettings: ImageButton
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingDados = JuegoActivityBinding.inflate(layoutInflater)
        setContentView(bindingDados.root)
        bindingDados.botonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        initEvent()
        initTextToSpeech()
        initSharedPreferences()
    }

    private fun initSharedPreferences() {
        sharedPreferences = getPreferences(MODE_PRIVATE)
        val numberOfThrows = getNumberOfThrows()
        bindingDados.txtResultado.text = "Numero de tiradas: $numberOfThrows\n"
    }

    private fun saveNumberOfThrows(numberOfThrows: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_NUMBER_OF_THROWS, numberOfThrows)
        editor.apply()
    }

    private fun getNumberOfThrows(): Int {
        return sharedPreferences.getInt(KEY_NUMBER_OF_THROWS, DEFAULT_NUMBER_OF_THROWS)
    }

    private fun initEvent() {
        bindingDados.txtResultado.visibility = View.INVISIBLE
        bindingDados.imageButton.setOnClickListener {
            bindingDados.txtResultado.visibility = View.VISIBLE
            startGame()
        }
    }

    private fun initTextToSpeech() {
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = Locale.getDefault()
                if (textToSpeech.isLanguageAvailable(locale) == TextToSpeech.LANG_AVAILABLE) {
                    textToSpeech.language = locale
                }
            }
        }

        textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onDone(utteranceId: String?) {
                // Acciones después de que la síntesis de voz ha finalizado
                runOnUiThread {
                    // Puedes realizar acciones específicas después de que la síntesis de voz ha finalizado
                }
            }

            override fun onError(utteranceId: String?) {
                // Manejar errores si ocurren durante la síntesis de voz
                runOnUiThread {
                    // Puedes realizar acciones específicas si hay un error
                }
            }

            override fun onStart(utteranceId: String?) {
                // Acciones al comenzar la síntesis de voz
                runOnUiThread {
                    // Puedes realizar acciones específicas al comenzar la síntesis de voz
                }
            }
        })
    }

    private fun startGame() {
        results.clear()

        val numberOfThrows = getNumberOfThrows()
        val schedulerExecutor = Executors.newSingleThreadScheduledExecutor()
        val msc = 1000
        for (i in 1..numberOfThrows) {
            schedulerExecutor.schedule(
                {
                    throwDadoInTime()
                    displayResults()
                },
                msc * i.toLong(), TimeUnit.MILLISECONDS
            )
        }

        schedulerExecutor.schedule({
            displayFinalResult()
            schedulerExecutor.shutdown()
        }, msc * (numberOfThrows + 1).toLong(), TimeUnit.MILLISECONDS)
    }

    private fun throwDadoInTime() {
        val numDados = Array(3) { Random.nextInt(1, 6) }
        results.add(numDados.toList())

        val imageViews: Array<ImageView> = arrayOf(
            bindingDados.imagviewDado1,
            bindingDados.imagviewDado2,
            bindingDados.imagviewDado3
        )

        for (i in 0 until 3) {
            selectView(imageViews[i], numDados[i])
        }
    }

    private fun displayResults() {
        runOnUiThread {
            val currentResult = results.last()
            val currentTotal = currentResult.sum()
            val equation = currentResult.joinToString(" + ")
            bindingDados.txtResultado.append("$equation = $currentTotal\n")
        }
    }

    private fun displayFinalResult() {
        Handler(Looper.getMainLooper()).postDelayed({
            val lastResult = results.last()
            val lastTotal = lastResult.sum()
            val chiste = contarChiste(lastTotal)

            bindingDados.txtResultado.append("\nTotal: $lastTotal\n$chiste")
            decirChiste(chiste)
        }, 1000)
    }

    private fun contarChiste(numero: Int): String {
        val chistes = listOf(
            "—A mí me gustaría vivir en una isla desierta.\n" +
                    "—A mí también.\n" +
                    "—¡Joder! ¡Ya empezamos a llenarla!",
            "—Mamá, en el cole me llaman despistado.\n" +
                    "—Niño, ¡que esta no es tu casa!",
            "—Pues entre pitos y flautas me he gastado 10.000 euros.\n" +
                    "—¿Y eso?    \n" +
                    "—Pues ya ves, cuatro mil en pitos y seis mil en flautas.",
            "—Cariño, creo que estás obsesionado con el fútbol y me haces falta.\n" +
                    "—¡¿Qué falta?! ¡¿Qué falta?! ¡¡Si no te he tocado!!",
            "—Hola, soy paraguayo y quiero pedirle la mano de su hija para casarme con ella.\n" +
                    "—¿Para qué?\n" +
                    "—Paraguayo",
            "Dos tontos en un tren:\n" +
                    "—¿Ves qué rápido pasan los postes?\n" +
                    "—Sí, en el viaje de vuelta volvemos en poste.",
            "—Soy celíaca.\n" +
                    "—Encantado, yo Antoniaco.",
            "—Acabo de escribir un libro.\n" +
                    "—¿Y por qué has dibujado un dedo en la primera página?\n" +
                    "—Es el índice.",
            "—Parece que su tos ha mejorado.\n" +
                    "—Es que estuve practicando toda la noche.",
            "¿Sabes cuánta leche da una vaca en su vida? Pues la misma que en bajada.",
            "¿Cómo queda un mago después de comer? Magordito.",
            "Se abre el telón. Acto 1: una piedra. Acto 2: la misma piedra. Acto 3: sigue siendo la misma piedra. Se cierra el telón. ¿Nombre de la obra? Rocky 3.",
            "¿Por qué le dio un paro cardiaco a la impresora? Parece que tuvo una impresión muy fuerte.",
            "¿De dónde vienen los hámster? De Hamsterdam.",
            "¡Estás obsesionado con la comida! No sé a que te refieres croquetamente.",
            "Una familia ocupó un terreno en Hawaii. Ahora a ver quién la desaloha.",
            "¿Qué pasa si tiras un pato al agua? Nada.",
            "¿Qué le dice un huevo a una sartén? Me tienes frito.",
            "Ayer me caí y pensé que me había roto el peroné. Peronó.",
            "¿Por qué no se puede discutir con un DJ? Porque siempre cambia de tema.",
            "Me han dado planton. ¿Como a las ballenas?",
            "—¡Me acaba de picar una serpiente!\n" +
                    "—¿Cobra?\n" +
                    "—No, gratis.",
            "Una pareja de ancianos está en la cama y ella le dice a su marido:\n" +
                    "—Pareces un teléfono móvil.\n" +
                    "—¿Por qué? ¿Vibro mucho?\n" +
                    "—Porque cuando entras en el túnel te quedas sin cobertura.",
            "—Papá, ayer empecé a salir con mi nuevo novio. Es mecánico, y me dijo que tengo 2 bellos amortiguadores además de 2 magníficos parachoques.\n" +
                    "—¡Dile a tu novio que si abre el capó y mide el aceite del motor le rompo el tubo de escape!",
            "Un gnomo va a una farmacia.\n" +
                    "\n" +
                    "Gnomo: ¿Me da una caja de condones?\n" +
                    "\n" +
                    "Farmacéutico: ¿Control?\n" +
                    "\n" +
                    "Gnomo: ¡No, no! ¡Sin troll! ",
            "—Estoy saliendo con una chica que podría ser mi hija.\n" +
                    "—¿De verdad? ¡Qué grande eres! ¡Estás hecho un tigre! ¡Un titán! ¡Un seductor! Y dime, ¿quién es?\n" +
                    "—Tu hija.",
            "Pues la postura favorita de mi mujer en la cama es la del pez.",
            "¿La del pez? Pues esa no la conozco.",
            "Sí hombre. Se da la vuelta y... ¡nada!\n" +
                    "\n" +
                    "—Ahora tengo que tener mucho cuidado y no quedarme embarazada.\n" +
                    "—¡Pero si tu marido se ha hecho la vasectomía!\n" +
                    "—Por eso mismo.",
            "—A mí me gustaría vivir en una isla desierta.\n" +
                    "—A mí también.\n" +
                    "—¡Joder! ¡Ya empezamos a llenarla!",
            "—Mamá, en el cole me llaman despistado.\n" +
                    "—Niño, ¡que esta no es tu casa!",
            "—Pues entre pitos y flautas me he gastado 10.000 euros.\n" +
                    "—¿Y eso?    \n" +
                    "—Pues ya ves, cuatro mil en pitos y seis mil en flautas.",
            "—Cariño, creo que estás obsesionado con el fútbol y me haces falta.\n" +
                    "—¡¿Qué falta?! ¡¿Qué falta?! ¡¡Si no te he tocado!!",
            "—Hola, soy paraguayo y quiero pedirle la mano de su hija para casarme con ella.\n" +
                    "—¿Para qué?\n" +
                    "—Paraguayo",
            "Dos tontos en un tren:\n" +
                    "—¿Ves qué rápido pasan los postes?\n" +
                    "—Sí, en el viaje de vuelta volvemos en poste.",
            "—Soy celíaca.\n" +
                    "—Encantado, yo Antoniaco.",
            "—Acabo de escribir un libro.\n" +
                    "—¿Y por qué has dibujado un dedo en la primera página?\n" +
                    "—Es el índice.",
            "—Parece que su tos ha mejorado.\n" +
                    "—Es que estuve practicando toda la noche.",
            "¿Sabes cuánta leche da una vaca en su vida? Pues la misma que en bajada.",
            "¿Cómo queda un mago después de comer? Magordito.",
            "Se abre el telón. Acto 1: una piedra. Acto 2: la misma piedra. Acto 3: sigue siendo la misma piedra. Se cierra el telón. ¿Nombre de la obra? Rocky 3.",
            "¿Por qué le dio un paro cardiaco a la impresora? Parece que tuvo una impresión muy fuerte.",
            "¿De dónde vienen los hámster? De Hamsterdam.",
            "¡Estás obsesionado con la comida! No sé a que te refieres croquetamente.",
            "Una familia ocupó un terreno en Hawaii. Ahora a ver quién la desaloha.",
            "¿Qué pasa si tiras un pato al agua? Nada.",
            "¿Qué le dice un huevo a una sartén? Me tienes frito.",
            "Ayer me caí y pensé que me había roto el peroné. Peronó.",
            "¿Por qué no se puede discutir con un DJ? Porque siempre cambia de tema.",
            "Me han dado planton. ¿Como a las ballenas?",
            "—¡Me acaba de picar una serpiente!\n" +
                    "—¿Cobra?\n" +
                    "—No, gratis.",
            "Una pareja de ancianos está en la cama y ella le dice a su marido:\n" +
                    "—Pareces un teléfono móvil.\n" +
                    "—¿Por qué? ¿Vibro mucho?\n" +
                    "—Porque cuando entras en el túnel te quedas sin cobertura.",
            "—Papá, ayer empecé a salir con mi nuevo novio. Es mecánico, y me dijo que tengo 2 bellos amortiguadores además de 2 magníficos parachoques.\n" +
                    "—¡Dile a tu novio que si abre el capó y mide el aceite del motor le rompo el tubo de escape!",
            "Un gnomo va a una farmacia.\n" +
                    "\n" +
                    "Gnomo: ¿Me da una caja de condones?\n" +
                    "\n" +
                    "Farmacéutico: ¿Control?\n" +
                    "\n" +
                    "Gnomo: ¡No, no! ¡Sin troll! ",
            "—Estoy saliendo con una chica que podría ser mi hija.\n" +
                    "—¿De verdad? ¡Qué grande eres! ¡Estás hecho un tigre! ¡Un titán! ¡Un seductor! Y dime, ¿quién es?\n" +
                    "—Tu hija.",
            "Pues la postura favorita de mi mujer en la cama es la del pez.",
            "¿La del pez? Pues esa no la conozco.",
            "Sí hombre. Se da la vuelta y... ¡nada!\n" +
                    "\n" +
                    "—Ahora tengo que tener mucho cuidado y no quedarme embarazada.\n" +
                    "—¡Pero si tu marido se ha hecho la vasectomía!\n" +
                    "—Por eso mismo."
        )

        return if (numero in 1..chistes.size) {
            chistes[numero - 1]
        } else {
            "No hay chiste disponible para el número $numero"
        }
    }

    private fun decirChiste(chiste: String) {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }

        if (results.isNotEmpty()) {
            val lastResult = results.last()
            val lastTotal = lastResult.sum()
            textToSpeech.speak("Total: ${lastTotal.toString()}", TextToSpeech.QUEUE_FLUSH, null, null)
        }

        textToSpeech.speak(chiste, TextToSpeech.QUEUE_ADD, null, null)
    }

    private fun selectView(imgV: ImageView, v: Int) {
        when (v) {
            1 -> imgV.setImageResource(R.drawable.dado1)
            2 -> imgV.setImageResource(R.drawable.dado2)
            3 -> imgV.setImageResource(R.drawable.dado3)
            4 -> imgV.setImageResource(R.drawable.dado4)
            5 -> imgV.setImageResource(R.drawable.dado5)
            6 -> imgV.setImageResource(R.drawable.dado6)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}
