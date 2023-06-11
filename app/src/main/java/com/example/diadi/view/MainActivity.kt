package com.example.diadi.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider

import com.example.diadi.databinding.ActivityMainBinding
import com.example.diadi.viewmodel.UserViewModel

import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapView
import kotlin.text.Typography.dagger


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val ACCESS_FINE_LOCATION = 1000
    lateinit var binding : ActivityMainBinding
    lateinit var mapView : MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // 사용자가 gps 기능을 활성화했는지 체크
        activateGPS()

        mapView = binding.mapView

        val marker = MapPOIItem()
        // diary에 저장된 위치명, 위치 좌표 (longitude, latitude) 불러와서 저장하는 코드 필요
        // 위치명, 좌표, 해당 위치의 일기 수
        // 이렇게 3개의 column으로 해서 2차원 배열에 저장하면 될듯

        marker.apply {
            // itemName = 위치명
            // mapPoint = MapPoint.mapPointWithGeoCoord(longitude, latitude)
            // markerType = MapPOIItem.MarkerType.CustomImage
            // selectedMarkerType = MapPOIItem.MarkerType.CustomImage
            // when (해당 위치의 일기 수) {
            //     1 -> {
            //         customImageResourceId = R.drawable.마커1
            //         customSelectedImageResourceId = R.drawable.보라마커1
            //     2 -> {
            //         customImageResourceId = R.drawable.마커2
            //         customSelectedImageResourceId = R.drawable.보라마커2
            //     .
            //     .
            //     9 -> {
            //         customImageResourceId = R.drawable.마커9
            //         customSelectedImageResourceId = R.drawable.보라마커9
            //     else -> {
            //         customImageResourceId = R.drawable.마커10
            //         customSelectedImageResourceId = R.drawable.보라마커10
            // isCustomImageAutoscale = false
            // setCustomImageAnchor(0.5f, 1.0f)

            // 이거랑 marker에 onClickListener 넣어서 누르면 일기목록 뜨게 해야되는데
        }
        mapView.addPOIItem(marker)
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
    }

    // 위치 트래킹 중지
    private fun stopTracking() {
        binding.mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
    }
}


