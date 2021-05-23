package com.example.androidarchitecturepatterns.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.androidarchitecturepatterns.R
import com.example.androidarchitecturepatterns.databinding.FragmentLoginBinding
import com.example.androidarchitecturepatterns.repository.AppRepository
import com.example.androidarchitecturepatterns.utils.Resource
import com.example.androidarchitecturepatterns.utils.SharedPreferencesUtil
import com.example.androidarchitecturepatterns.utils.nextFragment
import com.example.androidarchitecturepatterns.viewmodel.LoginViewModel
import com.example.androidarchitecturepatterns.viewmodel.ViewModelProviderFactory


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginViewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(requireActivity().application, repository)
        loginViewModel = ViewModelProvider(requireActivity(), factory).get(LoginViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_login, container, false)

        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.phone.addTextChangedListener {
            if (binding.phoneLayout.error != null)
            {
                binding.phoneLayout.error = null
            }
        }

        binding.password.addTextChangedListener {
            if (binding.passwordLayout.error != null){
                binding.passwordLayout.error = null
            }
        }

        binding.loginButton.setOnClickListener{
            val phone =binding.phone.text.toString()
            val password = binding.password.text.toString()
            val device_token = "slghldfhg"
            userLogin(phone,password,device_token)
        }
    }

    private fun userLogin(phone: String, password: String, deviceToken: String) {
        Log.e("phone",""+phone)
        loginViewModel.userLoginFun(phone,password,deviceToken)

        loginViewModel.loginResponse.observe(requireActivity(),{ event ->
            event.getContentIfNotHandled()?.let { response ->
                when(response)
                {
                    is Resource.Success ->{
                        response.data?.let { loginResponse ->

                            if(loginResponse.status == true)
                            {
                                Toast.makeText(requireActivity(),loginResponse.message, Toast.LENGTH_LONG).show()
                                SharedPreferencesUtil().setUserId(loginResponse.customer_details.response.customer_id,requireContext())
                                SharedPreferencesUtil().setUserName(loginResponse.customer_details.response.customer_name,requireContext())
                                SharedPreferencesUtil().setPhone(loginResponse.customer_details.response.customer_phone,requireContext())
                                SharedPreferencesUtil().setEmail(loginResponse.customer_details.response.customer_email,requireContext())
                                requireActivity().nextFragment(R.id.action_loginFragment_to_listFragment)
                            }
                            else
                            {
                                Toast.makeText(requireActivity(),loginResponse.message, Toast.LENGTH_LONG).show()
                            }

                        }
                    }

                    is Resource.Error -> {
                        response.message?.let { message ->

                        }
                    }

                    is Resource.Loading -> {
                    }

                }
            }
        })
    }


}