package com.polar.industries.loginwebservicespulido

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class BienvenidoActivity : AppCompatActivity() {

    private lateinit var textViewUserName: TextView
    private lateinit var textViewID: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenido)
        supportActionBar!!.hide()

        textViewUserName = findViewById(R.id.textViewUserName)
        textViewID = findViewById(R.id.textViewID)

        textViewUserName.text = "Nombre de Usuario: ${intent.getStringExtra("nameUser")}"
        textViewID.text = "Id del usuario: ${intent.getStringExtra("idUser")}"
    }
}