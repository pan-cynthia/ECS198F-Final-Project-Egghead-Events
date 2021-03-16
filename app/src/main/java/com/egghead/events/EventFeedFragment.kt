package com.egghead.events

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.login.LoginManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_event_feed.*
import java.util.*

class EventFeedFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private var favoriteFilter: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        auth = FirebaseAuth.getInstance()

        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.standard_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                auth.signOut()
                LoginManager.getInstance().logOut()

                val action = R.id.action_eventFeedFragment_to_signinFragment
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event_feed, container, false)
        val user = FirebaseAuth.getInstance().currentUser.isAnonymous
        if (user) {
            view.findViewById<FloatingActionButton>(R.id.favorite_button).hide()
            view.findViewById<FloatingActionButton>(R.id.create_event_button).hide()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventsRecyclerView: RecyclerView = view.findViewById(R.id.events_recycler_view)
        eventsRecyclerView.layoutManager = LinearLayoutManager(this.context)

        val eventListAdapter = EventListAdapter(arrayListOf(), findNavController())
        eventsRecyclerView.adapter = eventListAdapter

        EventFirestore.getAllEvents {
            eventListAdapter.setData(it)
        }

        val createEventButton = view.findViewById<FloatingActionButton>(R.id.create_event_button)
        createEventButton.setOnClickListener {
            val action = R.id.action_eventFeedFragment_to_createEventFragment
            findNavController().navigate(action)
        }

        val favoriteButton = view.findViewById<FloatingActionButton>(R.id.favorite_button)
        favoriteButton.setOnClickListener {
            favoriteFilter = !favoriteFilter
            if (favoriteFilter) {
                eventListAdapter.setData(EventsSingleton.events.filter {
                    it.favorited
                })
                favoriteButton.setImageResource(R.drawable.ic_star_filled_24px)
            } else {
                eventListAdapter.setData(EventsSingleton.events)
                favoriteButton.setImageResource(R.drawable.ic_star_unfilled_24px)
            }
        }
    }
}