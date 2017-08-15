package com.vertexpipeline.allstorage.client.kotlin.model

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

enum class FSItemType{
    File,
    Directory
}

class FSItem(nm:String, t:FSItemType, p:String){
    val pathProperty = SimpleStringProperty()
    var path by pathProperty
    val typeProperty = SimpleObjectProperty<FSItemType>()
    var type by typeProperty
    val nameProperty = SimpleStringProperty("name")
    var name by nameProperty
    init {
        path = p
        name = nm
        type = t
    }
}
