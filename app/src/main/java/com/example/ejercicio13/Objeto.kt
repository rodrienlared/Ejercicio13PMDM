package com.example.ejercicio13

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio13.databinding.ActivityObjetoBinding
import com.google.gson.Gson

class Objeto : AppCompatActivity() {

    private lateinit var binding: ActivityObjetoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjetoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val personaje: Personaje = recuperarPersonaje()
        val objeto = ObjetoJuego()


        binding.recoger.setOnClickListener {
            if(personaje.canIStore(objeto)){
                personaje.mochila.add(objeto)
                alerta("El objeto se ha almacenado correctamente. \n\nEspacio restante en la mochila: ${personaje.LIMITE_MOCHILA - personaje.getTamanoTotal()}\n\nPulsa \"Continuar\" para proseguir.", personaje)
            }
            else{
                alerta(
                    "No se ha podido guardar el objeto.\n\nMotivo: No hay suficiente espacio en la mochila.\n\nPulsa \"Continuar\" para volver a inicio.", null)
            }
        }

        binding.continuar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
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

    private fun alerta(mensaje: String, personaje: Personaje?) {
        val builder = AlertDialog.Builder(this)

        with(builder){
            setTitle("Alerta")
            setMessage(mensaje)
            setPositiveButton("Continuar"){dialog, which ->
                if(personaje != null)
                    aplicarCambiosPersonaje(personaje)
                startActivity(Intent(this@Objeto, MainActivity::class.java))
            }
            show()
        }
    }

}