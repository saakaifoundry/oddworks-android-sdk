package io.oddworks.device.model.common

import android.os.Parcel
import android.os.Parcelable

open class OddIdentifier(val id: String, val type: OddResourceType) : Parcelable {

    constructor(parcel: Parcel): this(parcel.readString(), OddResourceType.valueOf(parcel.readString()))

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(type.toString())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as OddIdentifier?

        if (id != that!!.id) return false
        return type == that.type

    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<OddIdentifier> = object : Parcelable.Creator<OddIdentifier> {
            override fun createFromParcel(`in`: Parcel): OddIdentifier {
                return OddIdentifier(`in`)
            }

            override fun newArray(size: Int): Array<OddIdentifier?> {
                return arrayOfNulls(size)
            }
        }
    }
}
