package com.vertexpipeline.allstorage.client.kotlin.model

import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import javax.json.Json
import javax.json.JsonObject

enum class FileState{
    Uploaded,
    Created
}

class FileInfo :JsonModel{
    val stateProperty = SimpleObjectProperty<FileState>()
    var state by stateProperty

    val dataHashProperty = SimpleStringProperty()
    var dataHash by dataHashProperty

    val fileIDProperty = SimpleStringProperty()
    var fileID by fileIDProperty

    val sizeProperty = SimpleLongProperty()
    var size by sizeProperty

    val extensionProperty = SimpleStringProperty()
    var extension by extensionProperty

    val pathProperty = SimpleStringProperty()
    var path by pathProperty

    val modDateProperty = SimpleObjectProperty<LocalDateTime>(LocalDateTime.now())
    var modDate by pathProperty

    val nameProperty = SimpleStringProperty()
    var name by nameProperty

    override fun toJSON(json: JsonBuilder) {
        with(json){
            add("name", name)
            add("path", path)
            add("extension", extension)
            add("size", size)
            add("fileID", fileID)
            add("dataHash", dataHash)
            add("state", when(state?:FileState.Created){ FileState.Created -> "created"; FileState.Uploaded -> "uploaded"})
        }
    }

    override fun updateModel(json: JsonObject) {
        with(json){
            name = string("name")
            path = string("path")
            extension = string("extension")
            fileID = string("fileID")
            dataHash = string("dataHash")
            state = when(string("state")){
                "uploaded" -> FileState.Uploaded
                "created" -> FileState.Created
                else -> FileState.Created
            }
        }
        size = json.long("size") ?: 0
    }
}
