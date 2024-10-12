package com.example.webtoon

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import room.Manhwa
import room.ManhwaViewModel

class FavRecyclerViewAdapter(private val context:Context,private var itemList: List<Manhwa>,private var manhwaViewModel: ManhwaViewModel) :
    RecyclerView.Adapter<FavRecyclerViewAdapter.MyViewHolder>() {

    // ViewHolder holds references to the views for each item
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgView: ImageView = itemView.findViewById(R.id.img_view)
        val nametext : TextView = itemView.findViewById(R.id.name_fav)
        val reads : TextView = itemView.findViewById(R.id.reads_fav)
        val removefav : ImageButton = itemView.findViewById(R.id.removefav)
    }

    // Inflate the item layout and create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav, parent, false)
        return MyViewHolder(itemView)
    }

    // Bind the data to the views
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val manhwa = itemList[position]
        val imguri = Uri.parse(manhwa.img)

        Picasso.get()
            .load(imguri)
            .into(holder.imgView)

        holder.nametext.text = manhwa.name
        holder.reads.text = manhwa.reads



        // Set click listener on the ImageView
        holder.imgView.setOnClickListener {
            val intent = Intent(context,InfoActivity::class.java)
            intent.putExtra("MANHWA_ID", manhwa.id) // Pass the manhwa ID
            context.startActivity(intent)
        }

        holder.removefav.setOnClickListener(){
            manhwa.isfav = false
           manhwaViewModel.updateManhwa(manhwa)
            Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT).show()
        }
    }

    // Return the total number of items
    override fun getItemCount() = itemList.size

    fun setManhwaList(newList: List<Manhwa>) {
        itemList = newList
        notifyDataSetChanged()
    }
}
