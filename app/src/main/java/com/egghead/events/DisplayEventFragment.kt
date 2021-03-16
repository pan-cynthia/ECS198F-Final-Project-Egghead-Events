package com.egghead.events

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class DisplayEventFragment : Fragment() {

    val args: DisplayEventFragmentArgs by navArgs()
    lateinit var firebase : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebase = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_display_event, container, false)
        view.findViewById<FloatingActionButton>(R.id.edit_button).hide()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val event = args.event

        val user = FirebaseAuth.getInstance().currentUser
        if (event.uid == user.uid) {
            view.findViewById<FloatingActionButton>(R.id.edit_button).show()
        }

        Log.d("title", event.title)

        val titleView: TextView = view.findViewById(R.id.event_title)
        val descriptionView: TextView = view.findViewById(R.id.event_description)
        val startTimestampView: TextView = view.findViewById(R.id.event_start_timestamp)
        val endTimestampView: TextView = view.findViewById(R.id.event_end_timestamp)
        val locationView : TextView = view.findViewById(R.id.event_location)

        titleView.text = event.title
        descriptionView.text = event.description
        locationView.text = event.location

        val formatter = SimpleDateFormat("MMM dd yyyy HH:mm", Locale.US)
        startTimestampView.text = formatter.format(event.start.toDate())
        endTimestampView.text = formatter.format(event.end.toDate())

        view.findViewById<FloatingActionButton>(R.id.edit_button).setOnClickListener {
            val action : NavDirections = DisplayEventFragmentDirections.actionDisplayEventFragmentToUpdateEventFragment(event)
            findNavController().navigate(action)
        }

        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            val action = R.id.action_displayEventFragment_to_eventFeedFragment
            findNavController().navigate(action)
        }
    }
}