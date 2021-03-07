package com.egghead.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

class PreloadFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_preload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser != null) {
            val action = R.id.action_preloadFragment_to_homeFragment
            findNavController().navigate(action)
        } else {
            val action = R.id.action_preloadFragment_to_signinFragment
            findNavController().navigate(action)
        }
    }
}