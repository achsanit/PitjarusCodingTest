package com.achsanit.pitjaruscodingtest.ui.fragment.mainmenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.achsanit.pitjaruscodingtest.databinding.FragmentMainMenuBinding
import com.achsanit.pitjaruscodingtest.ui.activity.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainMenuFragment : Fragment() {
    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    private val mainMenuViewModel: MainMenuViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermission()

        with(binding) {
            ibMenuVisit.setOnClickListener {
                val action = MainMenuFragmentDirections.actionMainMenuFragmentToListStoreFragment()
                findNavController().navigate(action)
            }
            ibMenuLogout.setOnClickListener {
                mainMenuViewModel.setStatusLogin(false)

                val action = MainMenuFragmentDirections.actionMainMenuFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }

    }

    private fun checkPermission() {
        if (!(requireActivity() as MainActivity).hasAllPermission()) {
            (requireActivity() as MainActivity).requestAllPermission()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}