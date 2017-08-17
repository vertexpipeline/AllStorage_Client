package com.vertexpipeline.allstorage.client.kotlin.model

import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import javax.json.JsonObject

class ScanResult:JsonModel{
    val foldersProperty = SimpleListProperty<FolderInfo>()
    var folders by foldersProperty
    val filesProperty = SimpleListProperty<FileInfo>()
    var files by filesProperty

    override fun toJSON(json: JsonBuilder) {
        with(json){
            add("folders", folders.asIterable().toJSON())
            add("files", files.asIterable().toJSON())
        }
    }
    override fun updateModel(json: JsonObject) {
        with(json){
            files = jsonArray("files")?.toModel<FileInfo>()
            folders = jsonArray("folders")?.toModel<FolderInfo>()
        }
    }
}
