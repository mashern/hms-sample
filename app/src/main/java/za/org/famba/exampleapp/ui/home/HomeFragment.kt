package za.org.famba.exampleapp.ui.home

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_WIFI_STATE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.huawei.agconnect.crash.AGConnectCrash
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationServices
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.CameraPosition
import com.huawei.hms.maps.model.LatLng
import za.org.famba.exampleapp.databinding.FragmentHomeBinding
import com.huawei.hms.maps.CameraUpdateFactory

import com.huawei.hms.maps.CameraUpdate




class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val TAG = "Maps"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    private var huaweiMap: HuaweiMap? = null

    private var mapView: MapView? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        checkPermissions()
        checkPermissions1()

        val root: View = binding.root
        mapView = binding.mapView

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }

        mapView?.apply {
            onCreate(mapViewBundle)
            getMapAsync(this@HomeFragment)
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)


        getLatestLocation()


        return root
    }

    private fun getLatestLocation() {
        try {

            val lastlocaton = fusedLocationProviderClient?.lastLocation
            lastlocaton?.addOnSuccessListener { location ->
                if (location == null) {

                } else {
                    val build = CameraPosition.Builder()
                        .target(LatLng(location.latitude, location.longitude))
                        .zoom(10f)
                        .bearing(90f)
                        .tilt(20f)
                        .build()
                    val cameraUpdate = CameraUpdateFactory.newCameraPosition(build)
                    huaweiMap?.animateCamera(cameraUpdate);
                }

            }
            lastlocaton?.addOnFailureListener { e: Exception ->
                AGConnectCrash.getInstance().recordException(e)
            }
        } catch (e: Exception) {
            AGConnectCrash.getInstance().recordException(e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresPermission(allOf = [ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE])
    override fun onMapReady(map: HuaweiMap?) {
        huaweiMap = map

        huaweiMap?.apply {
            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isCompassEnabled = true
            uiSettings.isRotateGesturesEnabled = true
            uiSettings.isScrollGesturesEnabled = true
            uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = true
            uiSettings.isTiltGesturesEnabled = true
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isIndoorLevelPickerEnabled = true
            uiSettings.isMapToolbarEnabled = true
            uiSettings.setAllGesturesEnabled(true)
            uiSettings.isZoomGesturesEnabled = false
        }
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "sdk >= 23 M")

            context?.let {
                if (ActivityCompat.checkSelfPermission(
                        it,
                        ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Request permissions for your app.
                    val strings = arrayOf(
                        ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                    // Request permissions.
                    activity?.let {
                        ActivityCompat.requestPermissions(it, strings, 1)
                    }

                }
            }
        }
    }

    private fun checkPermissions1() {
        context?.let {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                Log.i(TAG, "sdk < 28 Q")
                if (ActivityCompat.checkSelfPermission(
                        it,
                        ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    val strings =
                        arrayOf(ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    activity?.let { act -> ActivityCompat.requestPermissions(act, strings, 1) }
                }
            } else {
                if (ActivityCompat.checkSelfPermission(
                        it,
                        ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        it,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    val strings = arrayOf(
                        ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"
                    )
                    activity?.let { act -> ActivityCompat.requestPermissions(act, strings, 2) }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }
}