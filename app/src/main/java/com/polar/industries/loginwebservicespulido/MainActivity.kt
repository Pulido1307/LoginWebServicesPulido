package com.polar.industries.loginwebservicespulido

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var textInputLayoutUserName: TextInputLayout
    private lateinit var textInputLayoutPassword: TextInputLayout
    private lateinit var buttonIniciarSesión: Button
    private lateinit var buttonDialogRegistrate: Button
    private lateinit var progressBar: ProgressBar
    private var progressAsyncTask: ProgressAsyncTask? = null
    private val host: String = "https://pruebapulido.000webhostapp.com/Servicios/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        textInputLayoutUserName = findViewById(R.id.textInputLayoutUserName)
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword)
        buttonIniciarSesión = findViewById(R.id.buttonIniciarSesión)
        buttonDialogRegistrate = findViewById(R.id.buttonDialogRegistrate)
        progressBar = findViewById(R.id.progressBar)


        buttonIniciarSesión.setOnClickListener {
            if (!checkField(textInputLayoutUserName) && !checkField(textInputLayoutPassword)){
                val json = JSONObject()
                json.put("Login", true)
                json.put("usuario", textInputLayoutUserName.editText?.text.toString().trim())
                json.put("contrasena", textInputLayoutPassword.editText?.text.toString().trim())
                progressAsyncTask = ProgressAsyncTask()
                progressAsyncTask!!.execute("POST", host+"login.php", json.toString())
            }
        }

    }

    fun disableView(view: View, isDisabled: Boolean) {
        if (isDisabled) {
            view.alpha = 0.5f
            view.isEnabled = false
        } else {
            view.alpha = 1f
            view.isEnabled = true
        }
    }

    fun checkField(textInputLayout: TextInputLayout): Boolean {
        if (textInputLayout.editText?.text.toString().isNullOrEmpty()) {
            Toast.makeText(applicationContext, "${textInputLayout.hint} Es un campo requerido.", Toast.LENGTH_SHORT).show()
            textInputLayout.error = "Campo requerido"
        }
        return textInputLayout.editText?.text.toString().isNullOrEmpty()

    }


    inner class ProgressAsyncTask : AsyncTask<String, Unit, String>() {
        val TIME_OUT = 50000

        //Antes de ejecutar
        override fun onPreExecute() {
            super.onPreExecute()
            Toast.makeText(applicationContext, "Todo está blanco", Toast.LENGTH_SHORT).show()
            disableView(buttonIniciarSesión!!, true)
            disableView(buttonIniciarSesión!!, true)
            disableView(textInputLayoutPassword!!, true)
            disableView(textInputLayoutUserName!!, true)
            progressBar!!.visibility = View.VISIBLE
        }

        //En ejecución en segundo plano
        override fun doInBackground(vararg params: String?): String {
            val url = URL(params[1])
            val httpClient = url.openConnection() as HttpURLConnection
            httpClient.readTimeout = TIME_OUT
            httpClient.connectTimeout = TIME_OUT
            httpClient.requestMethod = params[0]

            if (params[0] == "POST") {
                httpClient.instanceFollowRedirects = false
                httpClient.doOutput = true
                httpClient.doInput = true
                httpClient.useCaches = false
                httpClient.setRequestProperty("Content-Type", "application/json; charset-utf-8")
            }

            try {
                if (params[0] == "POST") {
                    httpClient.connect()
                    val os = httpClient.outputStream
                    val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                    writer.write(params[2])
                    writer.flush()
                    writer.close()
                    os.close()
                }
                if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                    val stream = BufferedInputStream(httpClient.inputStream)
                    val data: String = readStream(inputStream = stream)
                    Log.e("Data:", data)
                    return data
                } else if (httpClient.responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
                    Log.e("ERROR:", httpClient.responseCode.toString())
                } else {
                    Log.e("ERROR:", httpClient.responseCode.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpClient.disconnect()
            }
            return null.toString()
        }

        fun readStream(inputStream: BufferedInputStream): String {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()

            bufferedReader.forEachLine { stringBuilder.append(it) }
            Log.e("StringBuider", "${stringBuilder.toString()}")

            return stringBuilder.toString()
        }
        //Cuando llegan los datos del servidor
        override fun onProgressUpdate(vararg values: Unit?) {
            super.onProgressUpdate(*values)
            if (!values.isNotEmpty() && values[0] != null)
                progressBar!!.visibility = View.VISIBLE
        }

        //Despues de la ejecuión
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            disableView(buttonIniciarSesión!!, false)
            disableView(buttonDialogRegistrate!!, false)
            disableView(textInputLayoutUserName!!, false)
            disableView(textInputLayoutPassword!!, false)
            Log.e("Resultado:", "$result")
            progressBar!!.visibility = View.INVISIBLE

            if (!result.isNullOrBlank() && !result.isNullOrEmpty() && !checkField(textInputLayoutPassword) && !checkField(textInputLayoutUserName)
            ) {
                val parser: Parser = Parser()
                val stringBuilder: StringBuilder = StringBuilder(result)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject

                if (json.int("succes") == 1) {
                    val jsonFinal = JSONObject(result)
                    val dataUser = jsonFinal.getJSONArray("datosUsuario")
                    val idUser = dataUser.getJSONObject(0).getInt("idUsuario")
                    val nameUser = dataUser.getJSONObject(0).getString("nombreUsuario")
                    val intent = Intent(applicationContext, BienvenidoActivity::class.java).apply {
                        putExtra("nameUser", "$nameUser")
                        putExtra("idUser", "$idUser")
                    }
                    startActivity(intent)
                    finish()
                } else {
                    val mensaje: AlertDialogPersonalized = AlertDialogPersonalized(
                        "¡Credenciales inválidas! \u274C",
                        "El usuario o contraseña es incorrecto",
                        this@MainActivity
                    )
                }
            } else if (result.isNullOrBlank() && result.isNullOrEmpty() && !checkField(textInputLayoutUserName) && !checkField(textInputLayoutPassword)
            ) {


                val mensaje: AlertDialogPersonalized = AlertDialogPersonalized(
                    "¡Advertencia! \u26A0\uFE0F",
                    "No hubó respuesta del servidor",
                    this@MainActivity
                )
            }
        }

        override fun onCancelled() {
            super.onCancelled()
            disableView(buttonIniciarSesión!!, false)
            disableView(buttonDialogRegistrate!!, false)
            disableView(textInputLayoutUserName!!, false)
            disableView(textInputLayoutPassword!!, false)
            progressBar!!.visibility = View.INVISIBLE
        }
    }
}
