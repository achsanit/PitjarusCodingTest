package com.achsanit.pitjaruscodingtest.ui.fragment.login

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.achsanit.pitjaruscodingtest.R
import com.achsanit.pitjaruscodingtest.databinding.FragmentLoginBinding
import com.achsanit.pitjaruscodingtest.util.Resource
import com.achsanit.pitjaruscodingtest.util.extension.disable
import com.achsanit.pitjaruscodingtest.util.extension.enable
import com.achsanit.pitjaruscodingtest.util.extension.makeGone
import com.achsanit.pitjaruscodingtest.util.extension.makeVisible
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton(false)
        with(binding) {
            btnLogin.setOnClickListener {
                loginViewModel.login(edtUsername.text.toString(), edtPassword.text.toString())
            }
        }

        loginViewModel.loginStatus.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    binding.pbLoading.makeGone()

                    result.data?.let {
                        if (it) {
                            loginViewModel.setLoginStatus(true)

                            if (binding.cbRememberMe.isChecked) {
                                loginViewModel.saveUsername(binding.edtUsername.text.toString())
                            }

                            val action =
                                LoginFragmentDirections.actionLoginFragmentToMainMenuFragment()
                            findNavController().navigate(action)
                        } else {
                            Toast.makeText(requireContext(), "gagal login", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                is Resource.Loading -> {
                    binding.pbLoading.makeVisible()
                }
                is Resource.Error -> {
                    binding.pbLoading.makeGone()
                    Toast.makeText(requireContext(), "gagal login", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "gagal login", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginViewModel.getUsername().observe(viewLifecycleOwner) {
            binding.edtUsername.setText(it)
        }

        val usernameStream = RxTextView.textChanges(binding.edtUsername)
            .skipInitialValue()
            .map { username ->
                username.trim().isEmpty()
            }
        usernameStream.subscribe {
            showUsernameError(it)
        }

        val passwordStream = RxTextView.textChanges(binding.edtPassword)
            .skipInitialValue()
            .map { password ->
                password.trim().isEmpty()
            }
        passwordStream.subscribe {
            showPasswordError(it)
        }

        val btnLoginStream = Observable.combineLatest(
            usernameStream,
            passwordStream
        ) { usernameInvalid, passwordInvalid ->
            !usernameInvalid && !passwordInvalid
        }
        btnLoginStream.subscribe {
            setButton(it)
        }

    }

    private fun showPasswordError(isNotValid: Boolean) {
        if (isNotValid) {
            binding.tilPassword.error = getString(R.string.text_error_empty_field)
        } else {
            binding.tilPassword.apply {
                error = null
                isErrorEnabled = false
            }
        }
    }

    private fun showUsernameError(isNotValid: Boolean) {
        if (isNotValid) {
            binding.tilUsername.error = getString(R.string.text_error_empty_field)
        } else {
            binding.tilUsername.apply {
                error = null
                isErrorEnabled = false
            }
        }
    }

    private fun setButton(isEnable: Boolean) {
        if (isEnable) {
            binding.btnLogin.apply {
                enable()
                setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.blue_foundation
                    )
                )
                setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.white
                    )
                )
            }
        } else {
            binding.btnLogin.apply {
                disable()
                setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey_700
                    )
                )
                setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.white
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}