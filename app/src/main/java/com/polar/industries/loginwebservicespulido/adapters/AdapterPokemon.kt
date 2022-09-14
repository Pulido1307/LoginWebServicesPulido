package com.polar.industries.loginwebservicespulido.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.polar.industries.loginwebservicespulido.R
import com.polar.industries.loginwebservicespulido.models.Pokemon

class AdapterPokemon(private val context: Context, private val pokeList:List<Pokemon>):
    RecyclerView.Adapter<AdapterPokemon.pokeHolder>() {

    var circualarProgressDrawable: CircularProgressDrawable? = null
    private val URLIMAGES: String = "pruebapulido.000webhostapp.com/pokedex/"

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
            .error(R.drawable.background)
            .into(holder.imageViewPokemon!!)


        holder.cardViewContainer.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return pokeList.size
    }

    inner class pokeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardViewContainer: CardView = itemView.findViewById(R.id.cardViewContainer)
        val imageViewPokemon: ImageView = itemView.findViewById(R.id.imageViewPokemon)
        val imageViewItem: ImageView = itemView.findViewById(R.id.imageViewItem)
        val textViewNumeroPokItem: TextView = itemView.findViewById(R.id.textViewNumeroPokItem)
        val textViewNombrePokItem: TextView = itemView.findViewById(R.id.textViewNombrePokItem)
        val textViewTipoPokItem: TextView = itemView.findViewById(R.id.textViewTipoPokItem)

    }
}