package com.example.closetcompanion.models

import android.os.Parcel
import android.os.Parcelable

data class User(
    val username: String,
    val password: String,
    val first_name: String,
    val last_name: String,
    val email_address: String,
    val dob: String,
    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(first_name)
        parcel.writeString(last_name)
        parcel.writeString(email_address)
        parcel.writeString(dob)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
