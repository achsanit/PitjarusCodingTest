package com.achsanit.pitjaruscodingtest.ui.fragment.store

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.achsanit.pitjaruscodingtest.R
import com.achsanit.pitjaruscodingtest.databinding.FragmentListStoreBinding
import com.achsanit.pitjaruscodingtest.ui.activity.MainActivity
import com.achsanit.pitjaruscodingtest.ui.adapter.ListStoreAdapter
import com.achsanit.pitjaruscodingtest.util.extension.makeGone
import com.achsanit.pitjaruscodingtest.util.mapper.mapToListLatLang
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ListStoreFragment : Fragment() {
    private var _binding: FragmentListStoreBinding? = null
    private val binding get() = _binding!!

    private val storeViewModel: StoreViewModel by viewModel()
    private val listStoreAdapter: ListStoreAdapter by lazy {
        ListStoreAdapter {
            val action =
                ListStoreFragmentDirections.actionListStoreFragmentToStoreVerificationFragment(it)
            findNavController().navigate(action)
        }
    }
    private lateinit var map: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userLocation: LatLng? = null
    private val listLatLngStore: MutableList<LatLng> = mutableListOf()

    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap

        val indonesiaLatLng = LatLng(-6.200000, 106.816666)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                indonesiaLatLng, 1F
            )
        )
        binding.pbMap.visibility = View.VISIBLE
        getCurrentLocation()
    }

    private fun setUpMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.f_map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListStoreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        setUpMap()
        obsListStore()

        with(binding) {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            rvListStore.layoutManager = LinearLayoutManager(requireContext())
            rvListStore.adapter = listStoreAdapter
        }
    }

    private fun obsListStore() {
        storeViewModel.getListStore().observe(viewLifecycleOwner) { result ->
            listStoreAdapter.submitData(result)
            listLatLngStore.addAll(result.mapToListLatLang())
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return (requireActivity() as MainActivity).requestAllPermission()
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            60000L,
            0f,
            locationListener
        )

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                60000L * 2L,
                0f, locationListener
            )
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    setLocation(location)
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "failed to get location", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private val locationListener = LocationListener {
        try {
            setLocation(it)
        } catch (e: Throwable) {
            Timber.tag(TAG).e(e)
        }
    }

    private fun setLocation(location: Location) {
        with(binding) {
            userLocation = LatLng(location.latitude, location.longitude)

            pbMap.makeGone()

            map.clear()

            userLocation?.let { loc ->
                map.addCircle(
                    CircleOptions().center(loc).radius(100.0).fillColor(R.color.light_foundation).strokeWidth(0F)
                )
                map.addCircle(
                    CircleOptions().center(loc).radius(10.0).fillColor(R.color.blue_foundation)
                        .strokeColor(R.color.white).strokeWidth(1F)
                )
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        loc, 16.5F
                    )
                )
            }

            listLatLngStore.forEach { latLng ->
                map.addMarker(MarkerOptions().position(latLng))
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "List Store Fragment"
    }
}