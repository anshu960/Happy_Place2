package com.example.happy_place.Activity.Activity.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happy_place.Activity.Activity.models.HappyPlaceModel
import com.example.happy_place.R
import kotlinx.android.synthetic.main.item_happy_places.view.*

open class HappyPlacesAdapter (
    private val context: Context,
    private var list: ArrayList<HappyPlaceModel>
        ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener: OnclickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_happy_places,
                parent,
                false
            )
        )
    }

    fun setOnClickListener(onclickListener: OnclickListener){
        this.onClickListener = onclickListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            holder.itemView.iv_place_image.setImageURI(Uri.parse(model.image))
            holder.itemView.tvTitle.text = model.title
            holder.itemView.tvDescription.text = model.description
            holder.itemView.tvAddress.text = model.location
            holder.itemView.tvDate.text = model.date

            holder.itemView.setOnClickListener {
                if (onClickListener != null){
                    onClickListener!!.onClick(position, model)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnclickListener{
        fun onClick(position: Int, model: HappyPlaceModel)
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}