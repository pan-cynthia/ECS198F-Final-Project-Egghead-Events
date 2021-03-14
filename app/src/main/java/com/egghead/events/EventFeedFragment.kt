package com.egghead.events

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_event_feed.*

class EventFeedFragment : Fragment() {

    private lateinit var firebase : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event_feed, container, false)
        firebase = FirebaseAuth.getInstance()
        val user = FirebaseAuth.getInstance().currentUser.isAnonymous
        if (user) {
            view.findViewById<FloatingActionButton>(R.id.favorite_button).hide()
            view.findViewById<FloatingActionButton>(R.id.create_event_button).hide()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

       create_event_button.setOnClickListener {
            val action = R.id.action_eventFeedFragment_to_createEventFragment
            findNavController().navigate(action)
        }
    }
}