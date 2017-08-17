package com.vertexpipeline.allstorage.client.kotlin.model

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.json.JsonObject
import javax.json.JsonWriter
import tornadofx.getValue
import tornadofx.setValue
import javax.json.JsonArray

class NodeInfo : JsonModel {
    val nameProperty = SimpleStringProperty("")
    var name by nameProperty

    val addressProperty = SimpleStringProperty("")
    var address by addressProperty

    val nodesProperty = SimpleObjectProperty<Array<NodeInfo>>(arrayOf())
    var nodes by nodesProperty

    val accessKeyProperty = SimpleStringProperty("")
    var accessKey by accessKeyProperty

    val IDProperty = SimpleStringProperty("")
    var ID by IDProperty

    override fun updateModel(json: JsonObject) {
        with(json){
            name = string("name")
            address = string("address")
            nodes = jsonArray("nodes")?.toModel<NodeInfo>()?.toTypedArray()
            accessKey = string("accessKey")
            ID = string("ID")
        }
    }

    override fun toJSON(json: JsonBuilder) {
        with(json){
            add("name", name)
            add("address", address)
            add("accessKey", accessKey)
            add("ID", ID)
        }
    }
}