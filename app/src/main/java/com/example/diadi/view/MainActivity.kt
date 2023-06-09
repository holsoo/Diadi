package com.example.diadi.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.diadi.R

import com.example.diadi.databinding.ActivityMainBinding
import com.example.diadi.domain.Place
import com.example.diadi.dto.SavePlaceDto
import com.example.diadi.viewmodel.UserViewModel

import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.text.Typography.dagger


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val ACCESS_FINE_LOCATION = 1000
    lateinit var binding : ActivityMainBinding
    lateinit var mapView : MapView
    lateinit var SavePlaceDto : SavePlaceDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // 일기 작성 화면으로 가기
        val addButton : ImageView = findViewById(R.id.main_addDiary)
        addButton.setOnClickListener {
            val goToAddIntent = Intent(this, AddActivity::class.java)
            startActivity(goToAddIntent)
        }


        // 사용자가 gps 기능을 활성화했는지 체크
        activateGPS()

        mapView = binding.mapView

        val marker = MapPOIItem()
        val saveToken =  intent.getStringExtra("save")

        if (saveToken!=null) {
            val longitude = SavePlaceDto.x
            val latitude = SavePlaceDto.y
            val placeName = SavePlaceDto.placeName
            marker.apply{
                itemName = placeName
                mapPoint = MapPoint.mapPointWithGeoCoord(longitude, latitude)
                markerType = MapPOIItem.MarkerType.CustomImage
                customImageResourceId = R.drawable.black
                selectedMarkerType = MapPOIItem.MarkerType.CustomImage
                customSelectedImageResourceId = R.drawable.purple
                isCustomImageAutoscale = false
                setCustomImageAnchor(0.5f, 1.0f)
            }
            mapView.addPOIItem(marker)
        }

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        val curX = df.format(SavePlaceDto.x)
        val curY = df.format(SavePlaceDto.y)


        if (returnLat() == curX && returnLon() == curY) {
            val revisitBackground: View = findViewById(R.id.revisitMessageBackground)
            val revisitMessage: TextView = findViewById(R.id.revisitMessage)
            val addNowButton: Button = findViewById(R.id.addNow)
            val noAddNowButton: Button = findViewById(R.id.noAddNow)

            revisitBackground.visibility = View.VISIBLE
            revisitMessage.visibility = View.VISIBLE
            addNowButton.visibility = View.VISIBLE
            noAddNowButton.visibility = View.VISIBLE

            addNowButton.setOnClickListener {
                val addDiaryIntent = Intent(this, AddActivity::class.java)
                startActivity(addDiaryIntent)
            }

            noAddNowButton.setOnClickListener {
                revisitBackground.visibility = View.INVISIBLE
                revisitMessage.visibility = View.INVISIBLE
                addNowButton.visibility = View.INVISIBLE
                noAddNowButton.visibility = View.INVISIBLE
            }
        }
    }

    // 커스텀 말풍선 클래스
    class CustomBalloonAdapter(inflater: LayoutInflater): CalloutBalloonAdapter {
        val mCalloutBalloon: View = inflater.inflate(R.layout.balloon_layout, null)
        val name: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_name)
        val address: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_address)

        override fun getCalloutBalloon(poiItem: MapPOIItem?): View {
            // 마커 클릭 시 나오는 말풍선
            name.text = poiItem?.itemName
            address.text = "getCalloutBalloon"
            return mCalloutBalloon
        }

        override fun getPressedCalloutBalloon(poiItem: MapPOIItem?): View {
            // 말풍선 클릭 시
            address.text = "getPressedCalloutBalloon"
            return mCalloutBalloon
        }
    }

    // 마커 클릭 이벤트 리스너
    class MarkerEventListener(val context: Context): MapView.POIItemEventListener {
        override fun onPOIItemSelected(mapView: MapView?, poiItem: MapPOIItem?) {
            val listIntent = Intent(context, ListActivity::class.java)
            listIntent.putExtra("Place", poiItem?.itemName)
            listIntent.putExtra("x", poiItem?.mapPoint.toString())
            listIntent.putExtra("y", poiItem?.mapPoint.toString())
            context.startActivity(listIntent)
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
        }
    }



    // 2. 추적 시작 코드가 아래 코드인데요,
    // 버튼 만드시고 추적 시작때는 아래 if문 실행해주시면 됩니다.
    // 반대로 추적 중지때는 맨 아래의 stopTracking() 함수 실행시켜 주시면 됩니다.
    private fun activateGPS() {
        if (checkLocationService()) {
            permissionCheck()
        } else {
            Toast.makeText(this, "GPS 기능을 활성화해야 합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun permissionCheck() {
        val preference = getPreferences(MODE_PRIVATE)
        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 상태
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // 권한 거절
                val builder = AlertDialog.Builder(this)
                builder.setMessage("현재 위치를 확인하시려면 설정에서 위치 권한을 허용해주세요.")
                builder.setPositiveButton("확인") { dialog, which ->
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                }
                builder.setNegativeButton("취소") { dialog, which ->

                }
                builder.show()
            } else {
                if (isFirstCheck) {
                    // 최초 권한 요청
                    preference.edit().putBoolean("isFirstPermissionCheck", false).apply()
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("현재 위치를 확인하시려면 설정에서 위치 권한을 허용해주세요.")
                    builder.setPositiveButton("설정으로 이동") { dialog, which ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
                        startActivity(intent)
                    }
                    builder.setNegativeButton("취소") { dialog, which ->

                    }
                    builder.show()
                }
            }
        } else {
            // 모든 권한이 있으면, 추적 시작
            startTracking()
        }
    }


    // 권한 요청
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한 요청, 승인됨.
                Toast.makeText(this, "위치 권한 승인", Toast.LENGTH_SHORT).show()
                startTracking()
            } else {
                // 권한 요청 했으나, 거절됨.
                Toast.makeText(this, "위치 권한 거부", Toast.LENGTH_SHORT).show()
                permissionCheck()
            }
        }
    }

    // GPS가 켜져있는지 확인
    private fun checkLocationService(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    // 위치 트래킹 시작
    private fun startTracking() {
        binding.mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // 최소 업데이트 시간 간격 (1초)
        val MIN_TIME_BETWEEN_UPDATES: Long = 1000

        // 최소 거리 변화 (10미터)
        val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f

        // 위치 제공자(GPS_PROVIDER)로부터 위치 업데이트 요청
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_TIME_BETWEEN_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES,
            locationListener
        )
    }

    // 위치 트래킹 중지
    private fun stopTracking() {
        binding.mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
    }

    var currentLatitude: Double = 0.0
    var currentLongitude: Double = 0.0

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // 위치가 변경되었을 때 호출됩니다.
            currentLatitude = location.latitude
            currentLongitude = location.longitude
            returnLat()
            returnLon()
        }
    }

    fun returnLat(): String {
        val lat = currentLatitude
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        val curLat = df.format(lat)
        return curLat
    }

    fun returnLon(): String {
        val lon = currentLongitude
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        val curLon = df.format(lon)
        return curLon
    }
}


