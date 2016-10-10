package io.oddworks.device.model

import java.util.HashMap

import io.oddworks.device.model.common.OddIdentifier

class OddPromotion : OddObject {

    var title: String? = null
        private set
    var description: String? = null
        private set
    var mediaImage: MediaImage? = null
        private set
    var url: String? = null
        private set

    constructor(identifier: OddIdentifier) : super(identifier) {
    }

    constructor(id: String, type: String) : super(id, type) {
    }

    override fun setAttributes(attributes: Map<String, Any>) {
        this.title = Util.INSTANCE.getString(attributes, "title", null)
        this.description = Util.INSTANCE.getString(attributes, "description", null)
        this.mediaImage = attributes["mediaImage"] as MediaImage
        this.url = Util.INSTANCE.getString(attributes, "url", null)
    }

    override fun getAttributes(): Map<String, Any> {
        val attributes = HashMap<String, Any>()
        attributes.put("title", title)
        attributes.put("description", description)
        attributes.put("mediaImage", mediaImage)
        attributes.put("url", url)

        return attributes
    }

    val isPresentable: Boolean
        get() = true

    fun toPresentable(): Presentable {
        return Presentable(title, description, mediaImage)
    }

    override fun toString(): String {
        return TAG + "(" +
                "id='" + getId() + "', " +
                "type='" + getType() + "', " +
                "title='" + title + "', " +
                "description='" + description + "', " +
                "mediaImage='" + mediaImage + "', " +
                "url='" + url + "')"
    }

    companion object {
        val TAG = OddPromotion::class.java.simpleName
    }
}
