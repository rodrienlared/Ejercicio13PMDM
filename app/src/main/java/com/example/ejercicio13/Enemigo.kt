package com.example.ejercicio13

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ejercicio13.databinding.ActivityEnemigoBinding

class Enemigo : AppCompatActivity() {

    private lateinit var binding: ActivityEnemigoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnemigoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.luchar.setOnClickListener {
            startActivity(Intent(this, Lucha::class.java))
        }

        binding.huir.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}