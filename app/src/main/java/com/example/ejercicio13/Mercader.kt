package com.example.ejercicio13

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio13.databinding.ActivityMercaderBinding
import com.google.gson.Gson

class Mercader : AppCompatActivity() {

    private lateinit var binding: ActivityMercaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMercaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        launchInitial()
    }

    private fun launchInitial() {
        binding.comerciar.visibility = View.VISIBLE
        binding.continuarMercader.visibility = View.VISIBLE

        binding.vender.visibility = View.GONE
        binding.cantidad.visibility = View.GONE
        binding.explicacion.visibility = View.GONE
        binding.finalCompra.visibility = View.GONE
        binding.cancelar.visibility = View.GONE

        binding.imagen.setImageResource(R.drawable.mercader)

        binding.comerciar.setOnClickListener {
            showComerciarButtons()
            comerciarAhora()
        }

        binding.continuarMercader.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun showComerciarButtons() {
        binding.cantidad.visibility = View.VISIBLE
        binding.vender.visibility = View.VISIBLE
        binding.cancelar.visibility = View.VISIBLE
        binding.explicacion.visibility = View.VISIBLE
        binding.finalCompra.visibility = View.VISIBLE

        binding.comerciar.visibility = View.GONE
        binding.continuarMercader.visibility = View.GONE
    }

    private fun comerciarAhora() {
        binding.imagen.setImageResource(R.drawable.objeto)

        binding.finalCompra.setOnClickListener {
            if (binding.cantidad.text.toString().isNotEmpty() && binding.cantidad.text.toString()
                    .toInt() > 0
            ) {
                val personaje = recuperarPersonaje()
                val tamanoRestanteMochilaPersonaje =
                    personaje.LIMITE_MOCHILA - personaje.getTamanoTotal()
                val tamanoObjetosTotales =
                    ObjetoJuego().getPeso() * binding.cantidad.text.toString().toInt()
                val precioObjetosTotales =
                    ObjetoJuego().getValor() * binding.cantidad.text.toString().toInt()
                val monedasPersonajeTotales = personaje.monedero

                if (tamanoRestanteMochilaPersonaje != 0)
                    if (tamanoRestanteMochilaPersonaje >= tamanoObjetosTotales)
                        if (monedasPersonajeTotales >= precioObjetosTotales) {
                            for (i in 1..binding.cantidad.text.toString().toInt())
                                personaje.mochila.add(ObjetoJuego())

                            aplicarCambiosPersonaje(personaje)
                            alerta("Has comprado ${binding.cantidad.text} objetos")
                        } else
                            alertaWithNoIntent("No tienes suficientes monedas")
                    else
                        alertaWithNoIntent("No tienes espacio suficiente en tu mochila")
                else
                    alertaWithNoIntent("No tienes espacio suficiente en tu mochila")
            } else
                alertaWithNoIntent("No puedes comprar 0 objetos")
        }

        binding.vender.setOnClickListener {
            if (binding.cantidad.text.toString().isNotEmpty() && binding.cantidad.text.toString()
                    .toInt() > 0
            ) {
                val personaje = recuperarPersonaje()
                val precioObjetosTotales =
                    ObjetoJuego().getValor() * binding.cantidad.text.toString().toInt()
                val cantidadObjetosPersonaje = personaje.mochila.size
                if (cantidadObjetosPersonaje >= binding.cantidad.text.toString().toInt()) {
                    for (i in 1..binding.cantidad.text.toString().toInt())
                        personaje.mochila.removeAt(0)

                    personaje.monedero += precioObjetosTotales
                    aplicarCambiosPersonaje(personaje)
                    alerta("Has vendido ${binding.cantidad.text} objetos")
                } else
                    alertaWithNoIntent("No tienes suficientes objetos en tu mochila")
            } else
                alertaWithNoIntent("No puedes vender 0 objetos")
        }

        binding.cancelar.setOnClickListener {
            launchInitial()
        }
    }

    fun alerta(mensaje: String) {
        val builder = AlertDialog.Builder(this)

        with(builder) {
            setTitle("Alerta")
            setMessage(mensaje)
            setPositiveButton("Continuar") { dialog, which ->
                startActivity(Intent(this@Mercader, MainActivity::class.java))
            }
            show()
        }
    }

    fun alertaWithNoIntent(mensaje: String) {
        val builder = AlertDialog.Builder(this)

        with(builder) {
            setTitle("Alerta")
            setMessage(mensaje)
            setPositiveButton("Continuar") { dialog, which -> }
            show()
        }
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
}