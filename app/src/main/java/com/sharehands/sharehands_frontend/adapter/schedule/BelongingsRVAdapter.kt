package com.sharehands.sharehands_frontend.adapter.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sharehands.sharehands_frontend.databinding.ItemChecklistBinding
import com.sharehands.sharehands_frontend.model.schedule.CheckListItem
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity

class BelongingsRVAdapter(private val context: MainActivity, private val list: MutableList<CheckListItem>)
    :RecyclerView.Adapter<BelongingsRVAdapter.BelongingsViewHolder>() {
        inner class BelongingsViewHolder(val binding: ItemChecklistBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(context: Context, current: CheckListItem, position: Int) {
                binding.chkItem.isChecked = current.isChecked
                binding.tvItem.text = current.item
                binding.btnDelete.setOnClickListener {
                    list.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                    notifyItemRangeChanged(adapterPosition, list.size)
                    SharedPreferencesManager.getInstance(context).deleteArray("items", list)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BelongingsViewHolder {
        val binding = ItemChecklistBinding.inflate(LayoutInflater.from(context), parent, false)
        return BelongingsViewHolder(binding)

    }

    override fun onBindViewHolder(holder: BelongingsViewHolder, position: Int) {
        holder.bind(context, list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}