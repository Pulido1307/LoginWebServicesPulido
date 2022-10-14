package com.polar.industries.loginwebservicespulido

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.polar.industries.loginwebservicespulido.adapters.AdapterPokemon
import com.polar.industries.loginwebservicespulido.models.Pokemon
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class BienvenidoActivity : AppCompatActivity() {

    private val HOST: String = "https://pruebapulido.000webhostapp.com/pokedex/pokedex.php"
    private lateinit var recyclerViewPokemon: RecyclerView
    private lateinit var pokemonList: ArrayList<Pokemon>
    private var swipeRefreshLayout:SwipeRefreshLayout? = null
    private var progressAsyncTask: BienvenidoActivity.ProgressAsyncTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenido)
        supportActionBar!!.hide()

        swipeRefreshLayout = findViewById(R.id.swipeToRefreshLayout)
        recyclerViewPokemon = findViewById(R.id.recyclerViewPokemon)
        recyclerViewPokemon!!.setHasFixedSize(true)
        recyclerViewPokemon.layoutManager = LinearLayoutManager(this@BienvenidoActivity)


       cargarData()

        swipeRefreshLayout!!.setOnRefreshListener {
            cargarData()
        }
    }


    private fun isOnline(): Boolean {
        val on = (applicationContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

        val activityNetwork: NetworkInfo? = on.activeNetworkInfo

        return activityNetwork != null && activityNetwork.isConnected
    }

    private fun cargarData() {
        if(isOnline()){
            try {
                val json: JSONObject = JSONObject()
                json.put("Button", "ConsultarPeoductos")

                progressAsyncTask = ProgressAsyncTask()
                progressAsyncTask!!.execute("POST", HOST,  json.toString())

            }catch (e:Exception){
                swipeRefreshLayout!!.isRefreshing = false
                Toast.makeText(this@BienvenidoActivity, "Ha ocurrido un problema", Toast.LENGTH_SHORT).show()
            }

            swipeRefreshLayout!!.isRefreshing = false
        }else{
            Toast.makeText(this@BienvenidoActivity, "No existe conexión a Internet", Toast.LENGTH_SHORT).show()
            swipeRefreshLayout!!.isRefreshing = false
        }
    }


    inner class ProgressAsyncTask : AsyncTask<String, Unit, String>() {
        val TIME_OUT = 50000

        //Antes de ejecutar
        override fun onPreExecute() {
            super.onPreExecute()
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
            val stringBuilder = java.lang.StringBuilder()

            bufferedReader.forEachLine { stringBuilder.append(it) }
            Log.e("StringBuider", "${stringBuilder.toString()}")

            return stringBuilder.toString()
        }
        //Cuando llegan los datos del servidor
        override fun onProgressUpdate(vararg values: Unit?) {
            super.onProgressUpdate(*values)

        }

        //Despues de la ejecuión
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)


            if (!result.isNullOrBlank() && !result.isNullOrEmpty() ) {
                val parser: Parser = Parser()
                val stringBuilder: java.lang.StringBuilder = StringBuilder(result)
                val json2: JsonObject = parser.parse(stringBuilder) as JsonObject
                pokemonList = ArrayList<Pokemon>()

                if(json2.int("succes") == 1){
                    val jsonInformationFinal = JSONObject(result)
                    val arrayPokemones = jsonInformationFinal.getJSONArray("datosUsuario")

                    for(i in 0 until arrayPokemones.length()){
                        pokemonList.add(
                            Pokemon(
                                arrayPokemones.getJSONObject(i).getInt("idPokedex"),
                                arrayPokemones.getJSONObject(i).getString("nombrePokemon"),
                                arrayPokemones.getJSONObject(i).getString("tipoPokemon"),
                                arrayPokemones.getJSONObject(i).getString("imagenPokemon")
                            )
                        )
                    }

                    val adapter = AdapterPokemon(this@BienvenidoActivity, pokemonList, this@BienvenidoActivity)
                    recyclerViewPokemon!!.adapter = adapter
                } else {
                    val mensaje: AlertDialogPersonalized = AlertDialogPersonalized(
                        "¡Credenciales inválidas! \u274C",
                        "El usuario o contraseña es incorrecto",
                        this@BienvenidoActivity
                    )
                }
            } else if (result.isNullOrBlank() && result.isNullOrEmpty()) {
                val mensaje: AlertDialogPersonalized = AlertDialogPersonalized(
                    "¡Advertencia! \u26A0\uFE0F",
                    "No hubó respuesta del servidor",
                    this@BienvenidoActivity
                )
            }
        }

        override fun onCancelled() {
            super.onCancelled()
        }
    }


}