package com.polar.industries.loginwebservicespulido

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide

class QuienEsEsePokemonActivity : AppCompatActivity() {
    private lateinit var imageViewPushImage: ImageView
    private lateinit var textViewNumeroPush: TextView
    private lateinit var textViewNombrePush: TextView
    private lateinit var textViewTipoPush: TextView
    private lateinit var cardViewFondoQEEP: CardView

    var circualarProgressDrawable: CircularProgressDrawable? = null
    private val URLIMAGES: String = "https://pruebapulido.000webhostapp.com/pokedex/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quien_es_ese_pokemon)

        imageViewPushImage = findViewById(R.id.imageViewPushImage)
        textViewNumeroPush = findViewById(R.id.textViewNumeroPush)
        textViewNombrePush = findViewById(R.id.textViewNombrePush)
        textViewTipoPush = findViewById(R.id.textViewTipoPush)
        cardViewFondoQEEP = findViewById(R.id.cardViewFondoQEEP)

        circualarProgressDrawable = CircularProgressDrawable(this@QuienEsEsePokemonActivity)

        var numero = intent.getStringExtra("no")
        var tipo = intent.getStringExtra("tipo")
        var nombre = intent.getStringExtra("nombre")
        var imagen = intent.getStringExtra("imagen")



        circualarProgressDrawable!!.strokeWidth = 5f
        circualarProgressDrawable!!.centerRadius = 30f
        circualarProgressDrawable!!.start()
        Glide.with(this@QuienEsEsePokemonActivity).load("$URLIMAGES${imagen}")
            .centerCrop().placeholder(circualarProgressDrawable)
            .error(R.drawable.bulbasaur)
            .into(imageViewPushImage)

        textViewNumeroPush.text = "N. º$numero"
        textViewNombrePush.text = nombre
        textViewTipoPush.text = tipo



    }
}