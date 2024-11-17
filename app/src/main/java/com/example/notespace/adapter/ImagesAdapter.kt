package com.example.notespace.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notespace.databinding.FragmentTrashBinding
import com.example.notespace.databinding.ImageItemLayoutBinding

class ImagesAdapter(private val images: MutableList<Uri>):
    RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: ImageItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(imageUri: Uri) {
            binding.imageItemView.setImageURI(imageUri)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImageItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    fun addImage(imageUri: Uri) {
        images.add(imageUri)
        notifyItemInserted(images.size - 1)
    }


}