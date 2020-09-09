package com.masuwes.tugasfirebase.recyclerview

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.masuwes.tugasfirebase.AddDataActivity
import com.masuwes.tugasfirebase.databinding.ItemDataBinding
import com.masuwes.tugasfirebase.model.ModelData

class ItemDataAdapter : RecyclerView.Adapter<ItemDataViewHolder>() {
    private var listData = arrayListOf<ModelData>()

    fun addData(data: List<ModelData>) {
        listData.clear()
        listData.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDataBinding.inflate(inflater, parent, false)
        return ItemDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemDataViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)

        holder.itemView.setOnClickListener { view ->
            val intent = Intent(view.context, AddDataActivity::class.java )
            intent.putExtra("DATA", data)
            view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listData.size
}