package com.vertexpipeline.allstorage.client.kotlin.controller

import com.vertexpipeline.allstorage.client.kotlin.model.ExplorerItem
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class StorageExplorerController :Controller(){
    val filesProperty = SimpleObjectProperty<ArrayList<ExplorerItem>>(ArrayList<ExplorerItem>())
    var files by filesProperty

    val pathProperty = SimpleStringProperty("/")
    var path by pathProperty

    fun updateFiles(){

    }
}