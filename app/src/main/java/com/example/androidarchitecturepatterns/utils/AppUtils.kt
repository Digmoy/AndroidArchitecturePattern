package com.example.androidarchitecturepatterns.utils

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.androidarchitecturepatterns.R

fun Activity.nextFragment(id: Int, bundle: Bundle? = null) {
    Navigation.findNavController(this, R.id.hostFragment).navigate(id, bundle)
}

fun Activity.popFragment() {
    Navigation.findNavController(this, R.id.hostFragment).popBackStack()
}

fun Fragment.showToast(message: String?) {
    if (message != null) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}
