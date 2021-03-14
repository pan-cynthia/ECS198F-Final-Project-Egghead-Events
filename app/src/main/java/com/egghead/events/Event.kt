package com.egghead.events

import com.google.firebase.Timestamp
import java.util.*
import kotlin.collections.ArrayList

data class Event(
        var title: String = "",
        var description: String = "",
        var tags: ArrayList<String> = arrayListOf(),
        var start: Timestamp = Timestamp(Date()),
        var end: Timestamp = Timestamp(Date()),
        var location: String = ""
)
