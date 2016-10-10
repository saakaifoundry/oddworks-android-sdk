package io.oddworks.device.model.common

import io.oddworks.device.model.common.OddIdentifier
import io.oddworks.device.model.common.OddRelationship
import org.json.JSONObject

open class OddResource(val identifier: OddIdentifier, var relationships: MutableSet<OddRelationship>, val included: MutableSet<OddResource>, val meta: JSONObject?) {
    fun getRelationship(name: String): OddRelationship? {
        return relationships.find {
            it.name == name
        }
    }

    fun getIdentifiersByRelationship(name: String): Set<OddIdentifier> {
        val relationship = getRelationship(name) ?: return emptySet()

        return relationship.identifiers
    }

    fun getIncludedByRelationship(name: String): Set<OddResource> {
        val relationship = getRelationship(name) ?: return emptySet()

        return relationship.identifiers.map { identifier ->
            included.find { it.identifier == identifier }
        }.filterNotNull().toSet()
    }

    fun isMissingIncluded(): Boolean {
        var isMissing = false
        relationships.forEach {
            it.identifiers.forEach { identifier ->
                val included = included.find { it.identifier == identifier }
                if (included == null) {
                    isMissing = true
                }
            }
        }
        return isMissing
    }

    fun addIncluded(resource: OddResource) {
        included.add(resource)
    }
}