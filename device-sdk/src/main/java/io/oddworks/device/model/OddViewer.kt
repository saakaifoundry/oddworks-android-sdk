package io.oddworks.device.model

import android.os.Build
import io.oddworks.device.exception.OddResourceException
import io.oddworks.device.model.common.OddIdentifier
import io.oddworks.device.model.common.OddRelationship
import io.oddworks.device.model.common.OddResource
import io.oddworks.device.model.common.OddResourceType
import org.json.JSONObject

class OddViewer(identifier: OddIdentifier,
                relationships: MutableSet<OddRelationship>,
                included: MutableSet<OddResource>,
                meta: JSONObject?,
                val email: String,
                val entitlements: Set<String>,
                val jwt: String): OddResource(identifier, relationships, included, meta) {

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
}
