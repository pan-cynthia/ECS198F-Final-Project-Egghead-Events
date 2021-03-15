package com.egghead.events

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class CreateEventFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_create_event, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser

        view.findViewById<Button>(R.id.submit_button).setOnClickListener {
            val title = view.findViewById<TextView>(R.id.event_title).text.toString()
            val description = view.findViewById<TextView>(R.id.event_description).text.toString()

            val event: Event = Event(title=title, description=description, location="Cool location", start= Timestamp(
                Date(1615760000000)), end= Timestamp(Date(1615760000000)), uid = user.uid)

            EventFirestore.postEvent(event) { response ->
                if (response == ResponseType.SUCCESS) {
                    Log.d("post", "successfully posted event")
                    val action = R.id.action_createEventFragment_to_eventFeedFragment
                    findNavController().navigate(action)
                } else {
                    Log.d("post", "could not post event")
                }
            }
        }
    }
}