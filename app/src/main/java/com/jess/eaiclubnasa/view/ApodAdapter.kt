package com.jess.eaiclubnasa.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jess.eaiclubnasa.ApodUtils.formatDate
import com.jess.eaiclubnasa.R
import com.jess.eaiclubnasa.model.ApodResult

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
        private var apodDate = itemView.findViewById<TextView>(R.id.tv_apod_date)

        fun onBind(apodResult: ApodResult) {
            Glide.with(itemView.context).load(apodResult.url).placeholder(R.drawable.logo)
                .into(apodImage)
            apodTitle.text = apodResult.title
            apodDate.text = formatDate(apodResult.date)
        }
    }

    fun update(list: MutableList<ApodResult>) {

        if (this.listApod.isEmpty()) {
            this.listApod = list
        } else {
            this.listApod.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun getLastItem() = listApod.last()
}