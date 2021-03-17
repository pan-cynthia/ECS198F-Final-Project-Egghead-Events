package com.egghead.events

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class EventListAdapter(private var events: List<Event>, private val mNavController: NavController) : RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = events[position]
        holder.apply {
            titleView.text = item.title
            descriptionView.text = item.description
            locationView.text = item.location

            val formatter = SimpleDateFormat("MMM dd yyyy HH:mm", Locale.US)
            startTimestampView.text = formatter.format(item.start.toDate())
            endTimestampView.text = formatter.format(item.end.toDate())

            favoriteButton.setOnClickListener {
                // First update the singleton
                EventsSingleton.events[position].favorited = !EventsSingleton.events[position].favorited

                // then update the actual list on the way back
                EventFirestore.postFavorites {
                    setData(EventsSingleton.events)

                    if (events[position].favorited) {
                        favoriteButton.setBackgroundResource(R.drawable.ic_star_filled_24px)
                    } else {
                        favoriteButton.setBackgroundResource(R.drawable.ic_star_unfilled_24px)
                    }
                }
            }

            if (item.favorited) {
                favoriteButton.setBackgroundResource(R.drawable.ic_star_filled_24px)
            } else {
                favoriteButton.setBackgroundResource(R.drawable.ic_star_unfilled_24px)
            }
        }

        holder.fullView.setOnClickListener{
            Log.d("click", "event item clicked on")
            val action: NavDirections = EventFeedFragmentDirections.actionEventFeedFragmentToDisplayEventFragment(item)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount() = events.size

    fun setData(newEvents: List<Event>) {
        events = newEvents
        Log.d("EventListAdapter", "Got new posts")
        this.notifyDataSetChanged()
    }

    fun setDataWithSearch(newEvents: List<Event>, search: String){
        val tempevents = mutableListOf<Event>()
        for (anEvent in newEvents){
            if (anEvent.title.contains(search, ignoreCase = true) || anEvent.description.contains(search, ignoreCase = true)){
                tempevents.add(anEvent)
            }
        }
        events = tempevents
        Log.d("EventListAdapter", "Got new posts")
        this.notifyDataSetChanged()
    }

    fun setDataWithFilter(newEvents: List<Event>, search: String, searchlocation: String, starttime: Long , endtime: Long, favoriteFilter: Boolean){
        val tempevents = mutableListOf<Event>()
        for (anEvent in newEvents){
            if (anEvent.title.contains(search, ignoreCase = true) || anEvent.description.contains(search, ignoreCase = true)){
                if (anEvent.location.contains(searchlocation, ignoreCase = true)) {
                    if (anEvent.start > Timestamp(Date(starttime)) || starttime == 0L && anEvent.end < Timestamp(Date(starttime))) {
                        if (anEvent.end < Timestamp(Date(endtime)) || endtime == 0L) {
                            if (anEvent.favorited == true || favoriteFilter == false) {
                                tempevents.add(anEvent)
                            }
                        }
                    }
                }
            }
        }
        events = tempevents
        Log.d("EventListAdapter", "Got new posts")
        this.notifyDataSetChanged()
    }
}

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val fullView: LinearLayout = view.findViewById(R.id.event_item)
    val titleView: TextView = view.findViewById(R.id.event_title)
    val descriptionView: TextView = view.findViewById(R.id.event_description)
    val favoriteButton: Button = view.findViewById(R.id.event_favorite_button)
    val startTimestampView: TextView = view.findViewById(R.id.event_start_timestamp)
    val endTimestampView: TextView = view.findViewById(R.id.event_end_timestamp)
    val locationView : TextView = view.findViewById(R.id.event_location)
}