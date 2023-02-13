package com.example.ejercicio13

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.ejercicio13.databinding.ActivityLuchaBinding
import com.google.gson.Gson
import kotlin.properties.Delegates

private lateinit var binding: ActivityLuchaBinding
private var VIDA_TOTAL_ENEMIGO by Delegates.notNull<Int>()

class Lucha : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLuchaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val personaje = recuperarPersonaje()
        setVidasTotales(personaje)
        botonAtacar(personaje)
        botonHuir()
        botonCurarse(personaje)

    }

    private fun botonCurarse(personaje: Personaje) {
        binding.curarse.setOnClickListener {
            val vidaPersonaje = binding.vidaRestanteTotalPersonaje.text.toString().toInt()
            if(personaje.mochila.size > 0)
                if(binding.vidaRestanteTotalPersonaje.text.toString().toInt() + 20 <= 200){
                    binding.vidaRestanteTotalPersonaje.text = (vidaPersonaje + 20).toString()
                    alertaWithNoIntent("Has curado 20 puntos de vida. Te quedan ${personaje.mochila.size} objetos para curarte.")
                    personaje.mochila.removeAt(0)
                    setAndCheckImagesForHealth()
                    aplicarCambiosPersonaje(personaje)
                } else{
                    if(binding.vidaRestanteTotalPersonaje.text.toString().toInt() < 200){
                        binding.vidaRestanteTotalPersonaje.text = "200"
                        personaje.mochila.removeAt(0)
                        setAndCheckImagesForHealth()
                        aplicarCambiosPersonaje(personaje)
                    }
                    else
                        alertaWithNoIntent("Tienes la vida máxima. No puedes curarte más. Intenta otra cosa.")
                } else
                alertaWithNoIntent("No tienes objetos para curarte. Intenta otra cosa.")
        }
    }

    private fun botonHuir() {
        binding.huirBatalla.setOnClickListener {
            val dado = (1..6).random()

            if(dado == 5 || dado == 6)
                alerta("Has huido. Pulsa \"Continuar\" para volver a inicio.")
            else
                alertaWithNoIntent("No has podido huir. Elige otra cosa")
        }
    }

    private fun botonAtacar(personaje: Personaje) {
        binding.atacar.setOnClickListener {
            val vidaEnemigo = binding.vidaRestanteTotalEnemigo.text.toString().toInt()
            val ataque = (1..6).random()

            if (ataque == 4 || ataque == 5 || ataque == 6){
                binding.vidaRestanteTotalEnemigo.text = (vidaEnemigo - (20/personaje.defensa)).toString()

                if(vidaEnemigo > 0)
                    ataqueEnemigo(personaje)
                else{
                    binding.vidaRestanteTotalEnemigo.text = "0"

                    if(personaje.mochila.size + 3 <= personaje.LIMITE_MOCHILA){
                        for(i in 1..3)
                            personaje.mochila.add(ObjetoJuego())
                        alerta("¡Has ganado!\n\nLas recompensas no han podido ser guardadas. No hay suficiente espacio de almacenamiento en la mochila.\n\nLas monedas han sido guardadas correctamente en tu monedero. Pulsa \"Continuar\" para volver a inicio.")
                    } else
                        alerta("¡Has ganado!\n\nLas recompensas han sido guardadas correctamente en tu mochila. Pulsa \"Continuar\" para volver a inicio.")

                    personaje.monedero += 100
                }
            }
            else {
                alertaWithEnemyAttack("El ataque ha fallado. Es el turno del enemigo", personaje)
            }

            setAndCheckImagesForHealth()
            aplicarCambiosPersonaje(personaje)
        }
    }

    private fun ataqueEnemigo(personaje: Personaje) {
        if(binding.vidaRestanteTotalEnemigo.text.toString().toInt() > 0){
            if(VIDA_TOTAL_ENEMIGO == 100)
                binding.vidaRestanteTotalPersonaje.text = (binding.vidaRestanteTotalPersonaje.text.toString().toInt() - (20/personaje.defensa)).toString()
            else
                binding.vidaRestanteTotalPersonaje.text = (binding.vidaRestanteTotalPersonaje.text.toString().toInt() - (30/personaje.defensa)).toString()

            if(binding.vidaRestanteTotalPersonaje.text.toString().toInt() <= 0){
                binding.vidaRestanteTotalPersonaje.text = "0"
                alertaEOG("Has muerto. \n\nFin del juego.")
            }
        } else {
            binding.vidaRestanteTotalEnemigo.text = "0"

            if(personaje.mochila.size + 3 <= personaje.LIMITE_MOCHILA){
                for(i in 1..3)
                    personaje.mochila.add(ObjetoJuego())
                alerta("¡Has ganado!\n\nLas recompensas han sido guardadas correctamente en tu mochila.\n\nLas monedas han sido guardadas correctamente en tu monedero. Pulsa \"Continuar\" para volver a inicio.")
            } else
                alerta("¡Has ganado!\n\nLas recompensas no han podido ser guardadas, pero las monedas ya se encuentran en tu monedero. Pulsa \"Continuar\" para volver a inicio.")

            personaje.monedero += 100
        }
    }

    private fun setAndCheckImagesForHealth() {
        val vidaPersonaje = binding.vidaRestanteTotalPersonaje.text.toString().toInt()
        val vidaEnemigo = binding.vidaRestanteTotalEnemigo.text.toString().toInt()

        if(vidaEnemigo >= VIDA_TOTAL_ENEMIGO / 1)
            binding.imagenVidaEnemigo.setImageResource(R.drawable.vida_completa)
        else
            if(vidaEnemigo >= VIDA_TOTAL_ENEMIGO / 2)
                binding.imagenVidaEnemigo.setImageResource(R.drawable.vida_casi_completa)
            else
                if(vidaEnemigo >= VIDA_TOTAL_ENEMIGO / 3)
                    binding.imagenVidaEnemigo.setImageResource(R.drawable.vida_mitad)
                else
                    if(vidaEnemigo >= VIDA_TOTAL_ENEMIGO / 4)
                        binding.imagenVidaEnemigo.setImageResource(R.drawable.vida_casi_vacia)
                    else
                        if(vidaEnemigo >= VIDA_TOTAL_ENEMIGO / 5)
                            binding.imagenVidaEnemigo.setImageResource(R.drawable.vida_casi_casi_vacia)

        if(vidaPersonaje >= 200 / 1)
            binding.imagenVidaPersonaje.setImageResource(R.drawable.vida_completa)
        else
            if(vidaPersonaje > 200 / 2)
                binding.imagenVidaPersonaje.setImageResource(R.drawable.vida_casi_completa)
            else
                if(vidaPersonaje > 200 / 3)
                    binding.imagenVidaPersonaje.setImageResource(R.drawable.vida_mitad)
                else
                    if(vidaPersonaje > 200 / 4)
                        binding.imagenVidaPersonaje.setImageResource(R.drawable.vida_casi_vacia)
                    else
                        if(vidaPersonaje > 200 / 5)
                            binding.imagenVidaPersonaje.setImageResource(R.drawable.vida_casi_casi_vacia)
                        else
                            if(vidaPersonaje == 0)
                                binding.imagenVidaPersonaje.setImageResource(R.drawable.vida_vacia)
    }

    private fun setVidasTotales(personaje: Personaje) {
        val vidaTotalEnemigo = (0..1).random()
        if(vidaTotalEnemigo == 0){
            binding.vidaRestanteTotalEnemigo.text = "100"
            VIDA_TOTAL_ENEMIGO = 100
        } else{
            binding.vidaRestanteTotalEnemigo.text = "200"
            VIDA_TOTAL_ENEMIGO = 200
        }

        binding.vidaRestanteTotalPersonaje.text = personaje.vida.toString()
    }

    private fun recuperarPersonaje(): Personaje {
        val sharedPreferences = getSharedPreferences("PERSONAJE_APLICACION", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("PERSONAJE", "")
        return gson.fromJson(json, Personaje::class.java)
    }

    private fun aplicarCambiosPersonaje(personaje: Personaje) {
        val sharedPreference = getSharedPreferences("PERSONAJE_APLICACION", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.clear()
        val gson = Gson()
        val json = gson.toJson(personaje)
        editor.putString("PERSONAJE", json)
        editor.apply()
    }

    private fun alertaWithNoIntent(mensaje: String) {
        val builder = AlertDialog.Builder(this)

        with(builder){
            setTitle("Alerta")
            setMessage(mensaje)
            setPositiveButton("Continuar"){dialog, which ->}
            show()
        }
    }

    private fun alertaWithEnemyAttack(mensaje: String, personaje: Personaje) {
        val builder = AlertDialog.Builder(this)

        with(builder){
            setTitle("Alerta")
            setMessage(mensaje)
            setPositiveButton("Continuar"){dialog, which ->
                ataqueEnemigo(personaje)
            }
            show()
        }
    }

    fun alerta(mensaje: String) {
        val builder = AlertDialog.Builder(this)

        with(builder) {
            setTitle("Alerta")
            setMessage(mensaje)
            setPositiveButton("Continuar") { dialog, which ->
                startActivity(Intent(this@Lucha, MainActivity::class.java))
            }
            show()
        }
    }

    fun alertaEOG(mensaje: String) {
        val builder = AlertDialog.Builder(this)

        with(builder) {
            setTitle("Alerta")
            setMessage(mensaje)
            setPositiveButton("Continuar") { dialog, which ->
                startActivity(Intent(this@Lucha, ActivityEnNegro::class.java))
            }
            show()
        }
    }
}
