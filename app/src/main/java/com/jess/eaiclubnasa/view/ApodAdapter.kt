package com.jess.eaiclubnasa.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jess.eaiclubnasa.R
import com.jess.eaiclubnasa.model.ApodResult
import com.squareup.picasso.Picasso

class ApodAdapter(var listApod: MutableList<ApodResult>) :
    RecyclerView.Adapter<ApodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_apod_recyclerview, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount() = listApod.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(listApod[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var apodImage = itemView.findViewById<ImageView>(R.id.iv_apod)
        private var apodTitle = itemView.findViewById<TextView>(R.id.tv_apod)

        fun onBind(apodResult: ApodResult) {
            Picasso.get().load(apodResult.url).into(apodImage)
            apodTitle.text = apodResult.title
        }
    }

    fun update(list: MutableList<ApodResult>) {

        if (this.listApod.isEmpty()) {
            this.listApod = list as ArrayList<ApodResult>
        } else {
            this.listApod.addAll(list)
        }
        notifyDataSetChanged()
    }
}