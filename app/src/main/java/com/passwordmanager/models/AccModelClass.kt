package com.passwordmanager.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Password account model class
 */
class AccModelClass(val id: Int, val accName: String?, val username: String?, val passwd: String?, val profile: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(accName)
        parcel.writeString(username)
        parcel.writeString(passwd)
        parcel.writeString(profile)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AccModelClass> {
        override fun createFromParcel(parcel: Parcel): AccModelClass {
            return AccModelClass(parcel)
        }

        override fun newArray(size: Int): Array<AccModelClass?> {
            return arrayOfNulls(size)
        }
    }
}