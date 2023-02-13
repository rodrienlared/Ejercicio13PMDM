package com.example.ejercicio13

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio13.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cambiarImagenSuperiorConTiempo()
        cambiarImagenInferiorConTiempo()

        funcionDado()
        crearPersonaje()
    }

    private fun crearPersonaje() {
        val personaje = Personaje()

        val sharedPreference = getSharedPreferences("PERSONAJE_APLICACION", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val gson = Gson()
        val jsonPersonajeCheck = sharedPreference.getString("PERSONAJE", null)
        if(jsonPersonajeCheck == null){
            val json = gson.toJson(personaje)
            editor.putString("PERSONAJE", json)
            editor.apply()
        }
    }

    private fun funcionDado() {
        binding.imagenInferior.setOnClickListener {
            when((0..4).random()){
                1 -> startActivity(Intent(this, Objeto::class.java))
                2 -> startActivity(Intent(this, Ciudad::class.java))
                3 -> startActivity(Intent(this, Mercader::class.java))
                4 -> startActivity(Intent(this, Enemigo::class.java))
            }
        }
    }

    private fun cambiarImagenInferiorConTiempo() {
        var i = 0
        val imagenes = arrayOf(R.drawable.dado_normal, R.drawable.dado_invertido)
        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                binding.imagenInferior.setImageResource(imagenes[i++ % imagenes.size])
                handler.postDelayed(this, 500) // delay de 500ms
            }
        }
        handler.post(runnable)
    }

    private fun cambiarImagenSuperiorConTiempo() {
        var i = 0
        val imagenes = arrayOf(R.drawable.campo, R.drawable.montana, R.drawable.cueva)
        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                binding.imagenSuperior.setImageResource(imagenes[i++ % imagenes.size])
                handler.postDelayed(this, 2000) // delay de 1 segundo
            }
        }
        handler.post(runnable)
    }
}