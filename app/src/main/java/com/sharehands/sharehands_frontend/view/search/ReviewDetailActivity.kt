package com.sharehands.sharehands_frontend.view.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.search.ReviewDetailRVAdapter
import com.sharehands.sharehands_frontend.databinding.ActivityReviewDetailBinding
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.viewmodel.search.ReviewDetailViewModel

class ReviewDetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityReviewDetailBinding
    var isLoading = false
    var page = 1
    var pageLen = 10
    private lateinit var adapter: ReviewDetailRVAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_detail)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_detail)

        val token = SharedPreferencesManager.getInstance(this).getString("token", "null")
        val viewModel = ViewModelProvider(this).get(ReviewDetailViewModel::class.java)
        val serviceId = intent.getIntExtra("serviceId", 0)
        Log.d("serviceId", "${serviceId}")

        binding.btnWriteService.setOnClickListener {
            val intent = Intent(this, ReviewWriteActivity::class.java)
            intent.putExtra("serviceId", serviceId)
            startActivity(intent)
        }

        binding.ivGoBack.setOnClickListener {
            finish()
        }

        getReviews(token, serviceId, 0, viewModel)

        val recyclerView = binding.rvReviews
        adapter = ReviewDetailRVAdapter(this, viewModel.reviewList.value, token, serviceId, viewModel)
        layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (pageLen == 10) {
                    if (dy > 50) {
                        val total = adapter.itemCount
//                    Log.d("total", "${total}")
                        if (page == 1) {
                            viewModel.isInitialSuccessful.observe(this@ReviewDetailActivity) {
                                if (viewModel.isInitialSuccessful.value == true) {
                                    if (!isLoading) {
                                        pageLen = viewModel.response.value!!.size
                                        page++
                                        Log.d("review len", "${viewModel.response.value!!.size.toInt()}")
                                        Log.d("review last id", "${viewModel.response.value!!.last().reviewId.toInt()}")
                                        getReviews(token, serviceId, viewModel.response.value!!.last().reviewId.toInt(), viewModel)
                                        Log.d("page after cnt", "${page}")
                                    }
                                }


                            }
                        } else {
                            viewModel.isAdditionalSuccessful.observe(this@ReviewDetailActivity) {
                                if (viewModel.isAdditionalSuccessful.value == true) {
                                    if (!isLoading) {
                                        pageLen = viewModel.response.value!!.size
                                        page++
                                        Log.d("review len", "${viewModel.response.value!!.size.toInt()}")
                                        Log.d("review last id", "${viewModel.response.value!!.last().reviewId.toInt()}")
                                        getReviews(token, serviceId, viewModel.response.value!!.last().reviewId.toInt(), viewModel)
                                        Log.d("page after cnt", "${page}")
                                    }
                                }


                            }
                        }

                    }
                }

            }
        })

    }

    override fun onResume() {
        super.onResume()

    }

    fun getReviews(token: String, serviceId: Int, last: Int, viewModel: ReviewDetailViewModel) {
        if (token != "null") {
            isLoading = true
            binding.progressReview.visibility = View.VISIBLE
            if (page == 1) {
                viewModel.getReviews(token, serviceId.toLong())
                viewModel.isInitialSuccessful.observe(this) {
                    val result = viewModel.result.value
                    if (viewModel.result.value?.isPermission == true) {
                        binding.btnWriteService.visibility = View.VISIBLE
                    } else {
                        binding.btnWriteService.visibility = View.GONE
                    }

                    binding.tvServiceTitle.text = result?.workTitle
                    binding.tvUserNumContent.text = "${result?.reviewAmount}명"
                    binding.tvUserRatingNum.text = String.format("%.2f", result?.rateAvg)

                    binding.ratingAverage.rating = result?.rateAvg!!.toFloat()
                    binding.ratingAverage1.rating = result?.rate1Avg!!.toFloat()
                    binding.ratingAverage2.rating = result?.rate2Avg!!.toFloat()
                    binding.ratingAverage3.rating = result?.rate3Avg!!.toFloat()



                    Handler().postDelayed({
                        if (::adapter.isInitialized) {
                            adapter.notifyDataSetChanged()
                        } else {
                            adapter = ReviewDetailRVAdapter(this@ReviewDetailActivity, viewModel.reviewList.value, token, serviceId, viewModel)
                            layoutManager = LinearLayoutManager(this@ReviewDetailActivity)
                            binding.rvReviews.adapter = adapter
                            binding.rvReviews.layoutManager = layoutManager
                        }
                        isLoading = false
                        binding.progressReview.visibility = View.GONE
                    }, 500)



                }
            } else {
                viewModel.getReviewsAdditional(token, serviceId.toLong(), last)
                viewModel.isAdditionalSuccessful.observe(this@ReviewDetailActivity) {
                    Handler().postDelayed({
                        if (::adapter.isInitialized) {
                            adapter.notifyDataSetChanged()
                        } else {
                            adapter = ReviewDetailRVAdapter(this@ReviewDetailActivity, viewModel.reviewList.value, token, serviceId, viewModel)
                            layoutManager = LinearLayoutManager(this@ReviewDetailActivity)
                            binding.rvReviews.adapter = adapter
                            binding.rvReviews.layoutManager = layoutManager
                        }
                        isLoading = false
                        binding.progressReview.visibility = View.GONE
                    }, 500)

                }
            }
        }
    }
}