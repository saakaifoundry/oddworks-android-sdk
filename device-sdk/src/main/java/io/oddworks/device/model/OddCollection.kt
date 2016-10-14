package io.oddworks.device.model

import io.oddworks.device.model.common.OddIdentifier
import io.oddworks.device.model.common.OddImage
import io.oddworks.device.model.common.OddRelationship
import io.oddworks.device.model.common.OddResource
import org.joda.time.DateTime
import org.json.JSONObject

class OddCollection(identifier: OddIdentifier,
                    relationships: Set<OddRelationship>,
                    included: MutableSet<OddResource>,
                    meta: JSONObject?,
                    val title: String,
                    val description: String,
                    val images: Set<OddImage>,
                    val genres: Set<String>,
                    val releaseDate: DateTime?) : OddResource(identifier, relationships, included, meta)
