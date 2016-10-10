package io.oddworks.device.model.common

import io.oddworks.device.model.common.Identifier
import io.oddworks.device.model.common.Relationship
import org.json.JSONObject

open class OddResource(val identifier: Identifier, var relationships: MutableSet<Relationship>, val included: MutableSet<OddResource>, val meta: JSONObject?) {
    fun getRelationship(name: String): Relationship? {
        return relationships.find {
            it.name == name
        }
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
}