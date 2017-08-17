package com.vertexpipeline.allstorage.client.kotlin.model

import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import tornadofx.getValue
import tornadofx.setValue
import java.text.SimpleDateFormat
import java.time.LocalDateTime

enum class ExplorerItemType { File, Directory }

class ExplorerItem(itemType: ExplorerItemType, it: Any) {
    val typeProperty = SimpleObjectProperty<ExplorerItemType>(ExplorerItemType.File)
    var type by typeProperty

    val itemProperty = SimpleObjectProperty<Any>()
    var item by itemProperty

    val iconProperty = SimpleObjectProperty<Image>()
    var icon by iconProperty

    val sizeProperty = SimpleLongProperty(0)
    var size by sizeProperty

    val extensionProperty = SimpleStringProperty("")
    var extension by extensionProperty

    val nameProperty = SimpleStringProperty("")
    var name by nameProperty

    val lastModifiedProperty = SimpleObjectProperty<LocalDateTime>(LocalDateTime.MAX)
    val lastModified by lastModifiedProperty

    init {
        type = itemType
        item = it
        when (type) {
            ExplorerItemType.File -> {
                val item = it as FileInfo
                name = item.name
                extension = item.extension
                size = item.size
                try {
                    var ext = item.name.removeRange(0, item.name.lastIndexOf('.')).toLowerCase().removeRange(0, 1)
                    icon = Image("fileIcons/$ext.png")
                } catch (ex: Exception) {
                    icon = Image("fileIcons/file.png")
                }
            }
            ExplorerItemType.Directory -> {
                val item = it as FolderInfo
                name = item.name
                extension = "<folder>"
                icon = Image("fileIcons/folder.png")
            }
        }
    }
}