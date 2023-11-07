package com.achsanit.pitjaruscodingtest.ui.fragment.store

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.achsanit.pitjaruscodingtest.databinding.FragmentStoreVerificationBinding
import com.achsanit.pitjaruscodingtest.ui.activity.MainActivity
import com.achsanit.pitjaruscodingtest.util.extension.getAddress
import com.achsanit.pitjaruscodingtest.util.extension.getDistanceLocation
import com.achsanit.pitjaruscodingtest.util.extension.makeGone
import com.achsanit.pitjaruscodingtest.util.extension.makeVisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class StoreVerificationFragment : Fragment() {
    private var _binding: FragmentStoreVerificationBinding? = null
    private val binding get() = _binding!!

    private val args: StoreVerificationFragmentArgs by navArgs()
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userLocation: LatLng? = null
    private var isInArea: Boolean = false
    private var isTakePhoto: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStoreVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val argument = args.storeEntityData

        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        with(binding) {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }

            ibTagLocation.setOnClickListener {
                pbLoadLocation.makeVisible()
                getCurrentLocation()
            }
            ibCamera.setOnClickListener {
                val action =
                    StoreVerificationFragmentDirections
                        .actionStoreVerificationFragmentToCameraFragment()
                findNavController().navigate(action)
            }
            btnVisit.setOnClickListener {
                if (isInArea && isTakePhoto) {
                    val action = StoreVerificationFragmentDirections
                            .actionStoreVerificationFragmentToVisitFragment(args.storeEntityData)
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Silahkan foto dan tagging lokasi dalam radius kurang dari 100m",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            tvStoreName.text = argument.storeName
            tvStoreAddress.text =
                getAddress(argument.latitude.toDouble(), argument.longitude.toDouble())

            layoutLocationNotSuitable.btnReset.setOnClickListener {
                pbLoadLocation.makeVisible()
                getCurrentLocation()
            }
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
            ONE_MINUTES,
            0f,
            locationListener
        )

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                ONE_MINUTES * 2L,
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

            pbLoadLocation.makeGone()

            val distance = getDistanceLocation(
                LatLng(location.latitude, location.longitude),
                LatLng(
                    args.storeEntityData.latitude.toDouble(),
                    args.storeEntityData.longitude.toDouble()
                )
            )
            if (distance <= 100) {
                isInArea = true

                layoutLocationNotSuitable.apply {
                    tvLocationNotSuitable.text = "Lokasi telah sesuai"
                    btnReset.makeGone()
                }

                Snackbar.make(
                    binding.root,
                    "Lokasi anda telah sesuai...",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                layoutLocationNotSuitable.btnReset.makeVisible()

                Snackbar.make(
                    binding.root,
                    "Jarak terlalu jauh...",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Uri>("imageUri")
            ?.observe(viewLifecycleOwner) {
                binding.ivCameraResult.setImageURI(it)
                isTakePhoto = true
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "STORE VERIFICATION"
        private const val ONE_MINUTES = 60000L
    }
}