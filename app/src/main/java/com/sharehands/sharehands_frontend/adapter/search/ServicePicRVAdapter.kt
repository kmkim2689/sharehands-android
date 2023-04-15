package com.sharehands.sharehands_frontend.adapter.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ItemPicUploadBinding
import com.sharehands.sharehands_frontend.model.search.ServicePic
import com.sharehands.sharehands_frontend.model.search.ServicePicPart
import com.sharehands.sharehands_frontend.view.search.ServiceWriteActivity
import com.sharehands.sharehands_frontend.viewmodel.search.ServiceUploadViewModel
import okhttp3.MultipartBody
import retrofit2.http.Multipart

class ServicePicRVAdapter(private val context: ServiceWriteActivity, private var pictureList: ArrayList<String>, private val viewModel: ServiceUploadViewModel): RecyclerView.Adapter<ServicePicRVAdapter.ServicePicViewHolder>() {

    inner class ServicePicViewHolder(private val binding: ItemPicUploadBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, currentPic: String, position: Int) {

            Glide.with(context)
                .load(currentPic)
                .into(binding.ivUploadedPic)
            Log.d("image 글라이드", "추가")
            binding.ivUploadedPic.clipToOutline = true
            binding.ivUploadedPic.scaleType = ImageView.ScaleType.CENTER_CROP

            // 삭제 시 리스트에서 빼기
            binding.btnDeletePic.setOnClickListener {
                Log.d("image position", position.toString())
                pictureList.removeAt(position)
                viewModel.deleteImage(position)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicePicViewHolder {
        val binding = ItemPicUploadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServicePicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServicePicViewHolder, position: Int) {
        holder.bind(context, pictureList[position], position)
    }

    override fun getItemCount(): Int {
        return pictureList.size
    }

}