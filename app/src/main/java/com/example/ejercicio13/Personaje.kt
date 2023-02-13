package com.example.ejercicio13

class Personaje() {
    val LIMITE_MOCHILA = 100

    var mochila = arrayListOf<ObjetoJuego>()
    var fuerza: Int = 0
    var vida : Int = 200
    var defensa: Int = 0
    var monedero: Int = 0
        get() = field

    // Crea el constructor por defecto para esta clase
    init {
        fuerza = generateFuerza()
        defensa = generateDefensa()
    }

    fun generateFuerza(): Int{
        return (10..15).random()
    }

    fun generateDefensa(): Int{
        return (1..5).random()
    }

    fun getTamanoTotal(): Int{
        var tamanoTotal = 0
        mochila.forEach {
            tamanoTotal += it.getPeso()
        }
        return tamanoTotal
    }

    fun getValorTotal(): Int{
        var valorTotal = 0
        mochila.forEach {
            valorTotal += it.getValor()
        }
        return valorTotal
    }

    fun canIStore(objeto: ObjetoJuego): Boolean{
        val tamanoTotal = getTamanoTotal()

        val espacioLibre = LIMITE_MOCHILA - tamanoTotal
        return espacioLibre >= objeto.getPeso()
    }
}