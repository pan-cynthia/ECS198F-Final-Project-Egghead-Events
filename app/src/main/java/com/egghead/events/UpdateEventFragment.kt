package com.egghead.events

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class UpdateEventFragment : Fragment() {

    val args: UpdateEventFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_event, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val event : Event = args.event

        val titleView: TextView = view.findViewById(R.id.event_title)
        val descriptionView: TextView = view.findViewById(R.id.event_description)
        val startTimestampView: TextView = view.findViewById(R.id.event_start)
        val endTimestampView: TextView = view.findViewById(R.id.event_end)

        titleView.text = event.title
        descriptionView.text = event.description
        startTimestampView.text = event.start.toString()
        endTimestampView.text = event.end.toString()

        view.findViewById<Button>(R.id.submit_button).setOnClickListener {
            event.title = titleView.text.toString()
            event.description = descriptionView.text.toString()

            EventFirestore.updateEvent(event) { response ->
                if (response == ResponseType.SUCCESS) {
                    Log.d("post", "successfully updated event")
                    val action : NavDirections = UpdateEventFragmentDirections.actionUpdateEventFragmentToDisplayEventFragment(event)
                    findNavController().navigate(action)
                } else {
                    Log.d("post", "could not update event")
                }
            }
        }
    }
}