package com.sharehands.sharehands_frontend.view.search

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sharehands.sharehands_frontend.R
import com.sharehands.sharehands_frontend.databinding.ActivityLocationSearchBinding
import com.sharehands.sharehands_frontend.network.search.location.KakaoMapClient.API_KEY
import net.daum.android.map.MapViewEventListener
import net.daum.android.map.MapViewTouchEventListener
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPOIItem.MarkerType
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.MapView.POIItemEventListener
import java.util.*

class LocationSearchActivity: AppCompatActivity() {
    lateinit var binding: ActivityLocationSearchBinding
    lateinit var address: String
    private lateinit var mapView: MapView
    private val eventListener = MarkerEventListener()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location_search)

        val API_KEY = "b03a4ceae5d55bdc92ecfbe00ddaf2c1"
        // 외대 위치 위도경도 기본값으로 설정
        val hufs = MapPoint.mapPointWithGeoCoord(37.5964763, 127.0588839)
        // 확대 수준
        val zoomLevel = 2

        // MapView 객체 생성 후 설정
        val mapView = binding.mapView
        mapView.setMapCenterPointAndZoomLevel(hufs, zoomLevel, true)

        val marker = MapPOIItem()
        marker.apply {
            itemName = "봉사 장소"
            // tag : 식별자
            tag = 1
            // 기본 마커 위치를 외대로 설정
            mapPoint = hufs
            Log.d("mapPoint", "${mapPoint}")
            // 마커 타입
            markerType = MapPOIItem.MarkerType.BluePin
            // 마커 드래그 가능하도록록
            isDraggable = true
        }

        // 마커 모양 커스터마이징


        // 외대에 마커 추가
        mapView.addPOIItem(marker)

        // 지도 좌표를 주소 정보로 변환하는 객체(geoCoder)
        val reverseGeoCodingResultListener = object : MapReverseGeoCoder.ReverseGeoCodingResultListener {
            override fun onReverseGeoCoderFoundAddress(mapReverseGeoCoder: MapReverseGeoCoder, addressString: String) {
                // 주소 검색 성공
                Log.d("ReverseGeoCoder", "주소 : $addressString")
                address = addressString
                marker.itemName = address
            }

            override fun onReverseGeoCoderFailedToFindAddress(mapReverseGeoCoder: MapReverseGeoCoder) {
                // 주소 검색 실패
                Log.d("ReverseGeoCoder", "주소 검색 실패")
            }
        }


        val geoCoder = MapReverseGeoCoder(
            API_KEY,
            hufs,
            reverseGeoCodingResultListener,
            this
        )

        geoCoder.startFindingAddress()

        mapView.setPOIItemEventListener(eventListener)
    }

    // onCreate 내부에서 리스너를 정의하면, 동작하지 않는다는 것에 유의
    class MarkerEventListener(): MapView.POIItemEventListener {


        val marker = MapPOIItem()


        val reverseGeoCodingResultListener = object : MapReverseGeoCoder.ReverseGeoCodingResultListener {
            override fun onReverseGeoCoderFoundAddress(mapReverseGeoCoder: MapReverseGeoCoder, addressString: String) {
                // 주소 검색 성공
                Log.d("ReverseGeoCoder", "주소 : $addressString")
                marker.itemName = addressString
            }

            override fun onReverseGeoCoderFailedToFindAddress(mapReverseGeoCoder: MapReverseGeoCoder) {
                // 주소 검색 실패
                Log.d("ReverseGeoCoder", "주소 검색 실패")
            }
        }

        override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {

        }

        override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {

        }

        override fun onCalloutBalloonOfPOIItemTouched(
            p0: MapView?,
            p1: MapPOIItem?,
            p2: MapPOIItem.CalloutBalloonButtonType?
        ) {

        }

        override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
            Log.d("reverse marker", "location changed")
            Log.d("reverse address changed", "${p2}")
            val API_KEY = "b03a4ceae5d55bdc92ecfbe00ddaf2c1"
            val geoCoder = MapReverseGeoCoder(
                API_KEY,
                p2,
                reverseGeoCodingResultListener,
                LocationSearchActivity()
            )

            geoCoder.startFindingAddress()

        }

    }

}