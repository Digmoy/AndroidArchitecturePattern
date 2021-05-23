package com.example.androidarchitecturepatterns.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidarchitecturepatterns.databinding.UpcomingLayoutBinding
import com.example.androidarchitecturepatterns.model.UpcomingProduct
import com.squareup.picasso.Picasso

class UpComingAdapter(private var context: Context, private var modelList: MutableList<UpcomingProduct>) : RecyclerView.Adapter<UpComingAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: UpcomingLayoutBinding) : RecyclerView.ViewHolder(binding.root){

       fun setDataToViews(position: Int){

           binding.productName.setText(modelList.get(position).product_name)
           Picasso.with(context).load(modelList.get(position).product_image).into(binding.productImage)

       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(UpcomingLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: UpComingAdapter.ViewHolder, position: Int) {
        holder.setDataToViews(position)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}