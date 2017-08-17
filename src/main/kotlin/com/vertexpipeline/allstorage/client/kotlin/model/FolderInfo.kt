package com.vertexpipeline.allstorage.client.kotlin.model

import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue
import javax.json.JsonObject

class FolderInfo : JsonModel {
    val foldersProperty = SimpleListProperty<FolderInfo>()
    var folders by foldersProperty

    val filesProperty = SimpleListProperty<FileInfo>()
    var files by filesProperty

    val pathProperty = SimpleStringProperty("")
    var path by pathProperty

    val nameProperty = SimpleStringProperty("")
    var name by nameProperty

    val fullNameProperty = SimpleStringProperty("")
    var fullName by fullNameProperty

    override fun toJSON(json: JsonBuilder) {
        with(json) {
            add("name", name)
            add("path", path)

            try {
                add("files", files.asIterable().toJSON())
            } catch (ex: Exception) {
                files = ArrayList<FileInfo>().observable()
            }
            try {
                add("folders", folders.asIterable().toJSON())
            } catch (ex: Exception) {
                folders = ArrayList<FolderInfo>().observable()
            }
        }
    }

    override fun updateModel(json: JsonObject) {
        with(json) {
            name = string("name")
            path = string("path")
            fullName = path + "/" + name
            files = getJsonArray("files").toModel<FileInfo>()
            folders = getJsonArray("folders").toModel<FolderInfo>()
        }
    }
}
