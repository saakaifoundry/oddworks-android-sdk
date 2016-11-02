package io.oddworks.device.model

import android.os.Parcel
import android.os.Parcelable
import io.oddworks.device.exception.OddResourceException
import io.oddworks.device.model.common.*
import org.json.JSONObject

class OddViewer(identifier: OddIdentifier,
                relationships: Set<OddRelationship>,
                included: MutableSet<OddResource>,
                meta: JSONObject?,
                val email: String,
                val entitlements: Set<String>,
                val jwt: String): OddResource(identifier, relationships, included, meta), Parcelable {

    protected constructor(source: Parcel): this(
            OddIdentifier(source.readString(), OddResourceType.VIEWER),
            emptySet(),
            mutableSetOf<OddResource>(),
            null,
            source.readString(),
            mutableListOf<String>().apply {
                source.readStringList(this)
            }.toSet(),
            source.readString()
    )


    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(identifier.id)
        dest.writeString(email)
        dest.writeStringList(entitlements.toList())
        dest.writeString(jwt)
    }

    override fun describeContents() = 0

    init {
        if (identifier.type != OddResourceType.VIEWER) {
            throw OddResourceException("Mismatched OddResourceType identifier: $identifier")
        }
    }

    fun isEntitledBy(entitlement: String): Boolean {
        return entitlements.contains(entitlement)
    }

    override fun toString(): String {
        return "OddViewer(identifier: $identifier, email: $email, entitlements: $entitlements, jwt: ${jwt.subSequence(0, 10)}...)"
    }

    object RELATIONSHIPS {
        @JvmField val WATCHLIST = "watchlist"
    }

    companion object {
        @JvmField
        @Suppress("unused")
        val CREATOR = createParcel(::OddViewer)
    }
}
