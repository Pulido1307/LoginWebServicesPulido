package com.polar.industries.loginwebservicespulido.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.polar.industries.loginwebservicespulido.QuienEsEsePokemonActivity
import com.polar.industries.loginwebservicespulido.R
import com.polar.industries.loginwebservicespulido.models.Pokemon

class AdapterPokemon(private val context: Context, private val pokeList:List<Pokemon>,private val activity: Activity):
    RecyclerView.Adapter<AdapterPokemon.pokeHolder>() {

    var circualarProgressDrawable: CircularProgressDrawable? = null
    private val URLIMAGES: String = "https://pruebapulido.000webhostapp.com/pokedex/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pokeHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_pokedex, parent, false)
        circualarProgressDrawable = CircularProgressDrawable(context)
        return pokeHolder(itemView)
    }

    override fun onBindViewHolder(holder: pokeHolder, position: Int) {
        val currentPokemon: Pokemon = pokeList[position]

        holder.textViewNumeroPokItem?.text = "N. º${currentPokemon.idPokedex}"
        holder.textViewNombrePokItem?.text = currentPokemon.nombrePokemon
        holder.textViewTipoPokItem?.text = currentPokemon.tipoPokemon

        circualarProgressDrawable!!.strokeWidth = 5f
        circualarProgressDrawable!!.centerRadius = 30f
        circualarProgressDrawable!!.start()
        Glide.with(context).load("$URLIMAGES${currentPokemon.imagenPokemon}")
            .centerCrop().placeholder(circualarProgressDrawable)
            .error(R.drawable.bulbasaur)
            .into(holder.imageViewPokemon!!)

        if(currentPokemon.tipoPokemon.equals("Agua"))
           holder.cardViewContainer.setBackgroundColor(Color.BLUE)
        else if(currentPokemon.tipoPokemon.equals("Electrico")){
            holder.cardViewContainer.setBackgroundColor(Color.YELLOW)
            holder.textViewTipoPokItem.setTextColor(Color.BLACK)
            holder.textViewNombrePokItem.setTextColor(Color.BLACK)
            holder.textViewNumeroPokItem.setTextColor(Color.BLACK)
        }
        else if (currentPokemon.tipoPokemon.equals("Dragón")){
            holder.cardViewContainer.setCardBackgroundColor(Color.CYAN)
            holder.textViewTipoPokItem.setTextColor(Color.BLACK)
            holder.textViewNombrePokItem.setTextColor(Color.BLACK)
            holder.textViewNumeroPokItem.setTextColor(Color.BLACK)
        }
        else if(currentPokemon.tipoPokemon.equals("Tierra"))
            holder.cardViewContainer.setCardBackgroundColor(Color.DKGRAY)

        holder.cardViewContainer.setOnClickListener {
            moveToActivity(
                currentPokemon.nombrePokemon!!,
                currentPokemon.idPokedex.toString(),
                currentPokemon.imagenPokemon!!,
                currentPokemon.tipoPokemon!!
            )
        }

    }

    private fun moveToActivity(nombre: String, numero:String, imagen:String, tipo:String){
        val intent: Intent = Intent(activity, QuienEsEsePokemonActivity::class.java).apply {
            putExtra("no", numero)
            putExtra("tipo", tipo)
            putExtra("nombre", nombre)
            putExtra("imagen", imagen)

            activity.startActivity(this)
        }
    }

    override fun getItemCount(): Int {
        return pokeList.size
    }

    override fun getItemViewType(position: Int): Int {

        Toast.makeText(context, pokeList[position].nombrePokemon, Toast.LENGTH_LONG).show()

        return super.getItemViewType(position)
    }


    inner class pokeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardViewContainer: CardView = itemView.findViewById(R.id.cardViewContainer)
        val imageViewPokemon: ImageView = itemView.findViewById(R.id.imageViewPokemon)
        val textViewNumeroPokItem: TextView = itemView.findViewById(R.id.textViewNumeroPokItem)
        val textViewNombrePokItem: TextView = itemView.findViewById(R.id.textViewNombrePokItem)
        val textViewTipoPokItem: TextView = itemView.findViewById(R.id.textViewTipoPokItem)

    }
}