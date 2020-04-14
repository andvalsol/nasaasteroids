package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.SingleAsteroidLayoutBinding
import com.udacity.asteroidradar.models.Asteroid

interface OnAsteroidClick {
    fun onAsteroidClick(asteroid: Asteroid)
}

class AsteroidsAdapter(private val onAsteroidClick: OnAsteroidClick) :
    ListAdapter<Asteroid, AsteroidsAdapter.AsteroidViewHolder>(DiffCallback()) {

    class AsteroidViewHolder(private val binding: SingleAsteroidLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid, asteroidClick: OnAsteroidClick) {
            with(binding) {
                this.asteroid = asteroid
                this.clickListener = asteroidClick

                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val binding = DataBindingUtil.inflate<SingleAsteroidLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.single_asteroid_layout,
            parent,
            false
        )

        return AsteroidViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.bind(getItem(position), onAsteroidClick)
    }
}

class DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid) = true
}