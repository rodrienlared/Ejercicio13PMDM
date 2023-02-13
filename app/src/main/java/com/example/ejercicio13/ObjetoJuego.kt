package com.example.ejercicio13

class ObjetoJuego() {
    private var peso: Int = 5
    private var valor: Int = 125
    private var vida: Int = 20

    constructor(peso: Int, valor: Int, vida: Int): this(){
        this.peso = peso
        this.valor = valor
        this.vida = vida
    }

    /* GETTER & SETTER SETTINGS */

    fun getPeso(): Int{
        return this.peso
    }

    fun getValor(): Int{
        return this.valor
    }

    fun getVida(): Int{
        return this.vida
    }

    fun setPeso(peso: Int){
        this.peso = peso
    }

    fun setValor(valor: Int){
        this.valor = valor
    }

    fun setVida(vida: Int){
        this.vida = vida
    }

    /* GETTER & SETTER SETTINGS */
}