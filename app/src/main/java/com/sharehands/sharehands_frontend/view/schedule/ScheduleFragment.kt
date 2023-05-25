package com.sharehands.sharehands_frontend.view.schedule

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.adapter.schedule.BelongingsRVAdapter
import com.sharehands.sharehands_frontend.adapter.schedule.TodayServiceVPAdapter
import com.sharehands.sharehands_frontend.databinding.DialogAddItemBinding
import com.sharehands.sharehands_frontend.databinding.FragmentScheduleBinding
import com.sharehands.sharehands_frontend.model.schedule.CheckListItem
import com.sharehands.sharehands_frontend.network.RetrofitClient
import com.sharehands.sharehands_frontend.network.schedule.TodayServices
import com.sharehands.sharehands_frontend.repository.Equipment
import com.sharehands.sharehands_frontend.repository.MemoItem
import com.sharehands.sharehands_frontend.repository.ScheduleDatabase
import com.sharehands.sharehands_frontend.repository.SharedPreferencesManager
import com.sharehands.sharehands_frontend.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar


class ScheduleFragment : Fragment() {
    lateinit var binding: FragmentScheduleBinding
    private lateinit var equipmentRVAdapter: BelongingsRVAdapter
    private lateinit var equipmentItems: MutableList<Equipment>
    private lateinit var memoItems: MutableList<MemoItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScheduleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scheduleDatabase = ScheduleDatabase.getInstance(requireContext())

        val token = SharedPreferencesManager.getInstance(context as MainActivity).getString("token", "null")
//        val recyclerView = binding.rvServiceToday
        val tvNone = binding.tvTodayNone
        val viewPagerToday = binding.vpTodayServices



        val equipmentRecyclerView = binding.rvCheckList
        val memoRecyclerView = binding.rvMemo
        CoroutineScope(Dispatchers.Main).launch {
            if (scheduleDatabase != null) {
                getItems(scheduleDatabase)
                val equipmentAdapter = BelongingsRVAdapter(requireContext() as MainActivity, equipmentItems)
                equipmentRecyclerView.adapter = equipmentAdapter
                equipmentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                equipmentRVAdapter = equipmentAdapter
            }

        }




        Log.d("schedule fragment", "onViewCreated")

        val today = Calendar.getInstance()
        val month = today.get(Calendar.MONTH) + 1
        val day = today.get(Calendar.DAY_OF_MONTH)
        val weekday = when(today.get(Calendar.DAY_OF_WEEK)) {
            1 -> "일"
            2 -> "월"
            3 -> "화"
            4 -> "수"
            5 -> "목"
            6 -> "금"
            7 -> "토"
            else -> ""
        }

        // 오늘의 봉사 불러오기
        // ViewPager로 구현...
        RetrofitClient.createRetorfitClient().getTodayService(token)
            .enqueue(object : Callback<TodayServices> {
                override fun onResponse(
                    call: Call<TodayServices>,
                    response: Response<TodayServices>
                ) {
                    val result = response.body()
                    Log.d("오늘 봉사 불러오기 코드", "${response.code()}")
                    Log.d("오늘 봉사 불러오기 성공", "${result}")
                    if (result?.workList?.isNotEmpty() == true) {
                        // recyclerview
                        val vpTodayAdapter = TodayServiceVPAdapter(activity!!, result?.workList)
                        viewPagerToday.adapter = vpTodayAdapter
                        // 페이지 간 간격 설정
                        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
                        val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pageWidth)
                        val screenWidth = resources.displayMetrics.widthPixels
                        val offsetPx = screenWidth - pageMarginPx - pagerWidth
                        viewPagerToday.setPageTransformer { page, position ->
                            page.translationX = position * -offsetPx
                        }
//                        val layoutManager = LinearLayoutManager(requireContext())
//                        recyclerView.adapter = adapter
//                        recyclerView.layoutManager = layoutManager

                    } else {
//                        recyclerView.visibility = View.INVISIBLE
                        viewPagerToday.visibility = View.INVISIBLE
                        binding.layoutTodayServices.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<TodayServices>, t: Throwable) {
                    Log.d("오늘 봉사 불러오기 실패", "${t.message}")
                }

            })

        val todayText = "${month} / ${day} (${weekday})"
        binding.tvToday.text = todayText

        val monthlyCalendarIntent = Intent(context as MainActivity, MonthlyCalendarActivity::class.java)
        binding.ivGotoCalendar.setOnClickListener {
            startActivity(monthlyCalendarIntent)
        }

        binding.tvServiceToday.setOnClickListener {
            startActivity(monthlyCalendarIntent)
        }

        binding.ivServiceToday.setOnClickListener {
            startActivity(monthlyCalendarIntent)
        }

        binding.tvAddItem.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val builderItem = DialogAddItemBinding.inflate(layoutInflater)
            val editText = builderItem.dialogEdit

            with(builder) {
                setTitle("준비물 추가")
                setView(builderItem.root)
                setPositiveButton("추가") { dialogInterface: DialogInterface, num: Int ->
                    if (scheduleDatabase != null) {
                        CoroutineScope(Dispatchers.Main).launch {
                            getNewItems(scheduleDatabase, editText.text.toString())
                            // 어댑터에 반영하기. 만약 초기화된 경우
                            if (::equipmentRVAdapter.isInitialized) {
                                CoroutineScope(Dispatchers.Main).launch {

                                    val equipmentAdapter = BelongingsRVAdapter(requireContext() as MainActivity, equipmentItems)
                                    equipmentRecyclerView.adapter = equipmentAdapter
                                    equipmentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                                    equipmentRVAdapter = equipmentAdapter
                                }

                            }
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

    suspend fun getItems(scheduleDatabase: ScheduleDatabase) {
        withContext(Dispatchers.IO) {
            equipmentItems = scheduleDatabase?.equipmentDao()?.getAllEquipments()!!
            memoItems = scheduleDatabase?.memoItemDao()?.getAllMemos()!!
        }
    }

    suspend fun getNewItems(scheduleDatabase: ScheduleDatabase, text: String) {
        withContext(Dispatchers.IO) {
            scheduleDatabase.equipmentDao().addEquipment(Equipment(null, false, text))
            equipmentItems = scheduleDatabase?.equipmentDao()?.getAllEquipments()!!

        }
    }


}