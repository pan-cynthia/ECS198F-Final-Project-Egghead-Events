package com.egghead.events

import com.google.firebase.firestore.PropertyName

data class User(
    @PropertyName("favorites")
    var tags: ArrayList<String> = arrayListOf()
)
