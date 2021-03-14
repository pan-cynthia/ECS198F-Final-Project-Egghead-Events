package com.egghead.events

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Event(
        var title: String = "",
        var description: String = "",
        var tags: ArrayList<String> = arrayListOf(),
        var start: Timestamp = Timestamp(Date()),
        var end: Timestamp = Timestamp(Date()),
        var location: String = ""
) : Parcelable
