package com.polar.industries.loginwebservicespulido

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.polar.industries.loginwebservicespulido.models.Pokemon

class BienvenidoActivity : AppCompatActivity() {

    private val HOST: String = "pruebapulido.000webhostapp.com/pokedex/pokedex.php"
    private lateinit var recyclerViewPokemon: RecyclerView
    private lateinit var pokemonList: ArrayList<Pokemon>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenido)
        supportActionBar!!.hide()

        recyclerViewPokemon = findViewById(R.id.recyclerViewPokemon)
        recyclerViewPokemon!!.setHasFixedSize(true)
        recyclerViewPokemon.layoutManager = LinearLayoutManager(this@BienvenidoActivity)

        cargarData()
    }

    private fun cargarData() {
        TODO("Not yet implemented")
    }


}