package com.egghead.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.type.DateTime
import java.text.SimpleDateFormat
import java.util.*


class CreateEventFragment : Fragment() {

    lateinit var firebase : FirebaseAuth
    var startTimeInMilliseconds : Long = 0
    var endTimeInMilliseconds : Long = 0

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

        val start = view.findViewById<TextView>(R.id.event_start)
        start.setOnClickListener {
            pickDateTime(start)
        }

        val end = view.findViewById<TextView>(R.id.event_end)
        end.setOnClickListener {
            pickDateTime(end)
        }

        view.findViewById<Button>(R.id.submit_button).setOnClickListener {
            val title = view.findViewById<TextView>(R.id.event_title).text.toString()
            val description = view.findViewById<TextView>(R.id.event_description).text.toString()
            val location = view.findViewById<TextView>(R.id.event_location).text.toString()

            val event: Event = Event(
                title = title,
                description = description,
                location = location, start = Timestamp(Date(startTimeInMilliseconds)),
                end = Timestamp(Date(endTimeInMilliseconds)),
                uid = user.uid
            )

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