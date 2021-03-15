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
            startTimestampView.text = item.start.toString()
            endTimestampView.text = item.end.toString()
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
}

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val fullView: LinearLayout = view.findViewById(R.id.event_item)
    val titleView: TextView = view.findViewById(R.id.event_title)
    val descriptionView: TextView = view.findViewById(R.id.event_description)
    val favoriteButton: Button = view.findViewById(R.id.event_favorite_button)
    val startTimestampView: TextView = view.findViewById(R.id.event_start_timestamp)
    val endTimestampView: TextView = view.findViewById(R.id.event_end_timestamp)
}