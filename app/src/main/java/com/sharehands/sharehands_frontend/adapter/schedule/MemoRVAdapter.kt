package com.sharehands.sharehands_frontend.adapter.schedule

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.DialogAddMemoBinding
import com.sharehands.sharehands_frontend.databinding.ItemMemoBinding
import com.sharehands.sharehands_frontend.repository.MemoItem
import com.sharehands.sharehands_frontend.repository.ScheduleDatabase
import com.sharehands.sharehands_frontend.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemoRVAdapter(private val context: MainActivity, private val memoList: MutableList<MemoItem>?)
    : RecyclerView.Adapter<MemoRVAdapter.MemoViewHolder>() {
        inner class MemoViewHolder(private val binding: ItemMemoBinding)
            : RecyclerView.ViewHolder(binding.root) {
                fun bind(context: Context, current: MemoItem) {
                    val currentId = current.id
                    val currentMemo = current.memo
                    val scheduleDatabase = ScheduleDatabase.getInstance(context)
                    val noMemos = (context as MainActivity).findViewById<TextView>(R.id.tv_no_memos)
                    val editBtn = binding.btnEditMemo
                    binding.tvItem.text = currentMemo
                    binding.btnDelete.setOnClickListener {
                        memoList?.removeAt(adapterPosition)
                        CoroutineScope(Dispatchers.Main).launch {
                            withContext(Dispatchers.IO) {
                                scheduleDatabase?.memoItemDao()
                                    ?.deleteMemo(MemoItem(currentId, currentMemo))
                            }
                        }
                        notifyItemRemoved(adapterPosition)
                        notifyItemRangeChanged(adapterPosition, memoList!!.size)
                        if (memoList.isEmpty()) {
                            noMemos.visibility = View.VISIBLE
                        }
                    }

                    editBtn.setOnClickListener {
                        val builder = AlertDialog.Builder(context)
                        val builderItem = DialogAddMemoBinding.inflate(LayoutInflater.from(context))
                        val editText = builderItem.dialogEditMemo
                        editText.setText(current.memo)

                        with(builder) {
                            setTitle("메모 수정하기")
                            setView(builderItem.root)
                            setPositiveButton("수정") { dialogInterface: DialogInterface, num: Int ->
                                if (scheduleDatabase != null) {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        withContext(Dispatchers.IO) {
                                            scheduleDatabase.memoItemDao().updateMemo(MemoItem(current.id, editText.text.toString()))
                                        }
                                        current.memo = editText.text.toString()
                                        notifyItemChanged(adapterPosition)
                                    }

                                }
                            }
                            setNegativeButton("취소") { dialogInterface: DialogInterface, num: Int ->
                                dialogInterface.dismiss()
                            }
                        }

                        builder.show()
                    }


                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val binding = ItemMemoBinding.inflate(LayoutInflater.from(context), parent, false)
        return MemoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        holder.bind(context, memoList!![position])
    }

    override fun getItemCount(): Int {
        return memoList!!.size
    }
}