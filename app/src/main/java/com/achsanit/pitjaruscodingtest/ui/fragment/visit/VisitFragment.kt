package com.achsanit.pitjaruscodingtest.ui.fragment.visit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.achsanit.pitjaruscodingtest.R
import com.achsanit.pitjaruscodingtest.databinding.FragmentVisitBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class VisitFragment : Fragment() {
    private var _binding: FragmentVisitBinding? = null
    private val binding get() = _binding!!

    private val visitViewModel: VisitViewModel by viewModel()
    private val args: VisitFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVisitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            tvStoreName.text = args.storeData.storeName

            btnFinish.setOnClickListener {
                visitViewModel.updateVisitStatus(true, args.storeData.id)

                findNavController().popBackStack(R.id.storeVerificationFragment, true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}