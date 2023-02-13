package com.example.ejercicio13

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio13.databinding.ActivityCiudadBinding

class Ciudad : AppCompatActivity() {

    private lateinit var binding: ActivityCiudadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCiudadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.entrarCiudad.setOnClickListener {
            startActivity(Intent(this, ActivityEnBlanco::class.java))
        }

        binding.continuarCiudad.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}