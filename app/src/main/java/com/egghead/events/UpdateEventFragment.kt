package com.egghead.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import io.grpc.okhttp.internal.Platform
import java.text.SimpleDateFormat
import java.util.*

class UpdateEventFragment : Fragment() {

    val args: UpdateEventFragmentArgs by navArgs()

    var startTimeInMilliseconds : Long = 0
    var endTimeInMilliseconds : Long = 0

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
        val locationView : TextView = view.findViewById(R.id.event_location)

        titleView.text = event.title
        descriptionView.text = event.description
        locationView.text = event.location

        val formatter = SimpleDateFormat("MMM dd yyyy HH:mm", Locale.US)
        startTimestampView.text = formatter.format(event.start.toDate())
        endTimestampView.text = formatter.format(event.end.toDate())

        startTimestampView.setOnClickListener {
            pickDateTime(startTimestampView)
        }

        endTimestampView.setOnClickListener {
            pickDateTime(endTimestampView)
        }

        view.findViewById<Button>(R.id.submit_button).setOnClickListener {
            event.title = titleView.text.toString()
            event.description = descriptionView.text.toString()
            event.location = locationView.text.toString()
            event.start = Timestamp(Date(startTimeInMilliseconds))
            event.end = Timestamp(Date(endTimeInMilliseconds))

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

    private fun pickDateTime(textView: TextView) {
        val textView = textView
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                TimePickerDialog(
                    requireContext(),
                    TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                        val pickedDateTime = Calendar.getInstance()
                        pickedDateTime.set(year, month, day, hour, minute)
                        if (textView == view?.findViewById<TextView>(R.id.event_start)) {
                            textView.text = SimpleDateFormat("MMM dd yyyy HH:mm", Locale.US).format(
                                pickedDateTime.time
                            )
                            startTimeInMilliseconds = pickedDateTime.timeInMillis
                        } else {
                            textView.text = SimpleDateFormat("MMM dd yyyy HH:mm", Locale.US).format(
                                pickedDateTime.time
                            )
                            endTimeInMilliseconds = pickedDateTime.timeInMillis
                        }
                    },
                    startHour,
                    startMinute,
                    true
                ).show()
            },
            startYear,
            startMonth,
            startDay
        ).show()
    }
}