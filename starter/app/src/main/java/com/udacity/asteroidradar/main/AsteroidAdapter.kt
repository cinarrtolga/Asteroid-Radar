package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding
import com.udacity.asteroidradar.domain.AsteroidModel

class AsteroidAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<AsteroidModel, AsteroidAdapter.AsteroidViewHolder>(DiffCallBak) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AsteroidAdapter.AsteroidViewHolder {
        return AsteroidViewHolder(AsteroidListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: AsteroidAdapter.AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(asteroid)
        }
        holder.bind(asteroid)
    }

    class AsteroidViewHolder(private var binding: AsteroidListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: AsteroidModel) {
            binding.asteroid = asteroid

            binding.executePendingBindings()
        }
    }

    companion object DiffCallBak : DiffUtil.ItemCallback<AsteroidModel>() {
        override fun areItemsTheSame(oldItem: AsteroidModel, newItem: AsteroidModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: AsteroidModel, newItem: AsteroidModel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class OnClickListener(val clickListener: (asteroid: AsteroidModel) -> Unit) {
        fun onClick(asteroid: AsteroidModel) = clickListener(asteroid)
    }
}