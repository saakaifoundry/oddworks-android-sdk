package io.oddworks.device.model

import java.util.HashMap

import io.oddworks.device.Util
import io.oddworks.device.model.common.Identifier

class OddView : OddObject {

    var title: String? = null
        private set

    constructor(identifier: Identifier) : super(identifier) {
    }

    constructor(id: String, type: String) : super(id, type) {
    }

    override fun setAttributes(attributes: Map<String, Any>) {
        title = Util.getString(attributes, "title", null)
    }

    override fun getAttributes(): Map<String, Any> {
        val attributes = HashMap<String, Any>()

        attributes.put("title", title)

        return attributes
    }


    override fun toString(): String {
        return TAG + "(" +
                "id='" + getId() + "', " +
                "title='" + title + "', " +
                "relationships='" + getRelationships().size + "', " +
                "included='" + getIncluded().size + "')"
    }

    companion object {
        val TAG = OddView::class.java.simpleName
    }
}
