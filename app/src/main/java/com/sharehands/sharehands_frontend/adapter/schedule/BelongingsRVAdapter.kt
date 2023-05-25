package com.sharehands.sharehands_frontend.adapter.schedule

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ItemChecklistBinding
import com.sharehands.sharehands_frontend.repository.Equipment
import com.sharehands.sharehands_frontend.repository.ScheduleDatabase
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
                val currentId = current.id
                val currentChecked = current.isChecked
                if (currentChecked) {
                    binding.tvItem.paintFlags = binding.tvItem.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    binding.tvItem.setTextColor(Color.GRAY)
                }
                val noItems = (context as MainActivity).findViewById<TextView>(R.id.tv_no_equipments)
                val currentItem = current.equipment
                binding.chkItem.isChecked = current.isChecked
                binding.tvItem.text = current.equipment
                binding.chkItem.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        binding.tvItem.paintFlags = binding.tvItem.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        binding.tvItem.setTextColor(Color.GRAY)
                    } else {
                        binding.tvItem.paintFlags = 0
                        binding.tvItem.setTextColor(ContextCompat.getColor(context, R.color.text_color))
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            scheduleDatabase?.equipmentDao()
                                ?.updateChecked(Equipment(currentId, isChecked, currentItem))
                        }
                    }

                }
                binding.btnDelete.setOnClickListener {
                    list?.removeAt(adapterPosition)
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            scheduleDatabase?.equipmentDao()?.deleteEquipment(current)
                        }
                        notifyItemRemoved(adapterPosition)
                        notifyItemRangeChanged(adapterPosition, list!!.size)
                        if (list.isEmpty()) {
                            noItems.visibility = View.VISIBLE
                        }
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