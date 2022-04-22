package com.example.myapplication.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.PlantModel
import com.example.myapplication.PlantPopup
import com.example.myapplication.R
import com.example.myapplication.fragments.PlantRepository

class PlantAdapter(
        val context: MainActivity,
        private val plantList: List<PlantModel>,
        private val layoutId: Int
): RecyclerView.Adapter<PlantAdapter.ViewHolder>()
{

    // boite pour ranger tout les composants à controler
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val plantImage= view.findViewById<ImageView>(R.id.image_item)
        val plantName:TextView? = view.findViewById(R.id.name_item)
        val plantDescription:TextView? = view.findViewById(R.id.description_item)
        val starIcon = view.findViewById<ImageView>(R.id.star_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        //recuperer les informations de la plante
        val currentPlant = plantList[position]

        //recuperer le repository
        val repo = PlantRepository()

        //utiliser glide pour recuperer l'image à partir de son lien
        Glide.with(context).load(Uri.parse(currentPlant.imageUrl)).into(holder.plantImage)

        //mettre à jour le nom de la plante
        holder.plantName?.text = currentPlant.name

        //mettre à jour la description de la plante
        holder.plantDescription?.text = currentPlant.description

        //verifier si la plante est liké ou non
        if (currentPlant.liked)
        {
            holder.starIcon.setImageResource(R.drawable.ic_like)
        }
        else
        {
            holder.starIcon.setImageResource(R.drawable.ic_unlike)

        }
        // rajouter une interaction sur cette etoiles
        holder.starIcon.setOnClickListener{
            //inverser si le buton est like ou non
            currentPlant.liked= ! currentPlant.liked
            //mettre ajour l'objet plant
            repo.updatePlant(currentPlant)
        }

       // interaction lors du clic sur une plante
        holder.itemView.setOnClickListener {
            PlantPopup(this, currentPlant).show()
        }
    }

    override fun getItemCount(): Int
    {
        return plantList.size
    }

}

