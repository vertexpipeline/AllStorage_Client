package com.vertexpipeline.allstorage.client.kotlin.model

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.json.JsonObject

class OperationResult():JsonModel{
    val stateProperty = SimpleStringProperty()
    var state by stateProperty
    val resultProperty = SimpleObjectProperty<JsonObject>()
    var result by resultProperty

    override fun updateModel(json: JsonObject) {
        with(json){
            state = string("state")
            result = getJsonObject("result")
        }
    }
}
