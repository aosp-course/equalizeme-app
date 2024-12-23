package com.example.equalizeme.model

data class Profile (
    val id: Int,
    var mName: String,
    var mEqualizer: Equalizer) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Profile) return false

        return id == other.id
    }
}
