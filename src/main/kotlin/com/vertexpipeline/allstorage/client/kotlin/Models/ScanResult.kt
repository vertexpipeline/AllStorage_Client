package com.vertexpipeline.allstorage.client.kotlin.Models

import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.json.JsonObject
import tornadofx.getValue
import tornadofx.setValue

class ScanResult : JsonModel {
    val foldersProperty = SimpleListProperty<FolderInfo>(listOf<FolderInfo>().observable())
    var folders by foldersProperty

    val filesProperty = SimpleListProperty<FileInfo>(listOf<FileInfo>().observable())
    var files by filesProperty

    val pathProperty = SimpleStringProperty("")
    var path by pathProperty

    override fun toJSON(json: JsonBuilder) {
        with(json) {
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
            try {
                files = getJsonArray("files").toModel<FileInfo>()
            } catch (ex: Exception) {
            }
            try {
                folders = getJsonArray("folders").toModel<FolderInfo>()
            } catch (ex: Exception) {
            }
            path = string("path")
        }
    }
}