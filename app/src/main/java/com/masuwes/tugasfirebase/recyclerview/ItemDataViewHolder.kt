package com.masuwes.tugasfirebase.recyclerview

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.masuwes.tugasfirebase.R
import com.masuwes.tugasfirebase.databinding.ItemDataBinding
import com.masuwes.tugasfirebase.model.ModelData

class ItemDataViewHolder(private val binding: ItemDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(data: ModelData) {
        Glide.with(binding.root.context)
            .load(data.profile_image)
            .centerCrop()
            .placeholder(R.drawable.ic_person)
            .into(binding.imgProfile)

        binding.run {
            txtNama.text = data.profile_name
            txtKelas.text = data.profile_class
            txtAlamat.text = data.profile_address
        }

    }
}