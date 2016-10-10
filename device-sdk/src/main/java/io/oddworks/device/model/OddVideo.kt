package io.oddworks.device.model

import org.joda.time.DateTime

import java.util.HashMap

import io.oddworks.device.model.common.OddIdentifier

import io.oddworks.device.Util

class OddVideo : OddObject {
    var title: String? = null
        private set
    var description: String? = null
        private set
    var releaseDate: DateTime? = null
        private set
    private var duration: Int? = null
    var url: String? = null
        private set
    /** returns true if this OddVideo stream is broadcasting now.  */
    var isCurrentlyLive: Boolean = false
        private set

    constructor(identifier: OddIdentifier) : super(identifier) {
    }

    constructor(id: String, type: String) : super(id, type) {
    }

    fun getDuration(): Int {
        if (duration == null) {
            duration = 0
        }

        return duration!!
    }


    /** returns true if this OddVideo is a liveStream object in the api's catalog  */
    val isLive: Boolean?
        get() = getType() == OddObject.TYPE_LIVE_STREAM

    override fun setAttributes(attributes: Map<String, Any>) {
        this.title = Util.getString(attributes, "title", null)
        this.description = Util.getString(attributes, "description", null)
        this.releaseDate = Util.getDateTime(attributes, "releaseDate", null)
        this.duration = Util.getInteger(attributes, "duration", null)
        this.url = Util.getString(attributes, "url", null)
        this.isCurrentlyLive = Util.getBoolean(attributes, "isLive")
    }

    override fun getAttributes(): Map<String, Any> {
        val attributes = HashMap<String, Any>()
        attributes.put("title", title)
        attributes.put("description", description)
        attributes.put("releaseDate", releaseDate)
        attributes.put("duration", getDuration())
        attributes.put("url", url)
        attributes.put("isLive", isCurrentlyLive)
        return attributes
    }


    override fun toString(): String {
        return "OddVideo{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                ", url='" + url + '\'' +
                '}'
    }

    companion object {
        val TAG = OddVideo::class.java.simpleName
    }
}
