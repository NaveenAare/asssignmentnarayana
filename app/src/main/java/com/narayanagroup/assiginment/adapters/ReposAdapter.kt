package com.narayanagroup.assiginment.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.narayanagroup.assiginment.R
import com.narayanagroup.assiginment.databinding.AdapterItemBinding

import com.narayanagroup.assiginment.models.items
import com.narayanagroup.assiginment.ui.repodetails

class ReposAdapter(private val clicked: (String) -> Unit) :
    PagingDataAdapter<items, ReposAdapter.PlayersViewHolder>(PlayersDiffCallback()) {


   //
    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {

        val data = getItem(position)
        holder.bind(data)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {

        return PlayersViewHolder(
            AdapterItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }



    inner class PlayersViewHolder(
        private val binding: AdapterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: items?) {

            binding.let {

                val name = it.root.context.getString(
                    R.string.name,
                    data?.name, data?.full_name
                )
                it.root.setOnClickListener {
                    //clicked.invoke(name)
                    val intent = Intent(itemView.context, repodetails::class.java)
                    intent.putExtra("name",data?.name)
                    intent.putExtra("fullname",data?.full_name)
                    intent.putExtra("url",data?.html_url)
                    intent.putExtra("avatr_url",data?.owner?.avatar_url)
                    intent.putExtra("des",data?.description)

                    itemView.context.startActivity(intent)
                }
                it.name.text = data?.name
                it.fullname.text=data?.full_name
                it.upadatedon.text="Last Updated on"+data?.updated_at

            }

        }
    }

    private class PlayersDiffCallback : DiffUtil.ItemCallback<items>() {
        override fun areItemsTheSame(oldItem: items, newItem: items): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: items, newItem: items): Boolean {
            return oldItem == newItem
        }
    }

}