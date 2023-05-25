package com.sharehands.sharehands_frontend.adapter.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sharehands.sharehands_frontend.databinding.ItemChecklistBinding
import com.sharehands.sharehands_frontend.model.schedule.CheckListItem
import com.sharehands.sharehands_frontend.repository.Equipment
import com.sharehands.sharehands_frontend.repository.ScheduleDatabase
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BelongingsRVAdapter(private val context: MainActivity, private val list: MutableList<Equipment>?)
    :RecyclerView.Adapter<BelongingsRVAdapter.BelongingsViewHolder>() {
        inner class BelongingsViewHolder(val binding: ItemChecklistBinding): RecyclerView.ViewHolder(binding.root) {
            fun bind(context: Context, current: Equipment, position: Int) {
                val scheduleDatabase = ScheduleDatabase.getInstance(context)
                binding.chkItem.isChecked = current.isChecked
                binding.tvItem.text = current.equipment
                binding.btnDelete.setOnClickListener {
                    list?.removeAt(adapterPosition)
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            scheduleDatabase?.equipmentDao()?.deleteEquipment(current)
                        }
                        notifyItemRemoved(adapterPosition)
                        notifyItemRangeChanged(adapterPosition, list!!.size)
                    }


                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BelongingsViewHolder {
        val binding = ItemChecklistBinding.inflate(LayoutInflater.from(context), parent, false)
        return BelongingsViewHolder(binding)

    }

    override fun onBindViewHolder(holder: BelongingsViewHolder, position: Int) {
        holder.bind(context, list!![position], position)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
}