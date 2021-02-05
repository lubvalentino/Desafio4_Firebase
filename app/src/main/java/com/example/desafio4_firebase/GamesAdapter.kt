package com.example.desafio4_firebase


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GamesAdapter (var listGames: MutableList <GamesItem>) :RecyclerView.Adapter<GamesAdapter.GamesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GamesAdapter.GamesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_single_game, parent, false)
        return GamesViewHolder(view)
    }

    override fun onBindViewHolder(holder: GamesAdapter.GamesViewHolder, position: Int) {
        holder.bind(listGames[position])
    }

    override fun getItemCount(): Int {
        return listGames.size
    }

    class GamesViewHolder (itemView:View):RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.ivPoster)
        val name : TextView = itemView.findViewById(R.id.tvMainItemMovie)
        val created : TextView = itemView.findViewById(R.id.tvMainCreatedMovie)

        fun bind (games: GamesItem):Unit = with(itemView){
            Glide.with(itemView.context).load(games.foto).into(image)
            name.text = games.name
            created.text = games.created
        }
    }


}
