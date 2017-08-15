package com.vertexpipeline.allstorage.client.kotlin.model

import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import tornadofx.getValue
import tornadofx.setValue
import java.time.LocalDateTime

enum class ExplorerItemType{ File, Directory}

class ExplorerItem(val itemName:String, val itemType: ExplorerItemType, val itemSize:Long, val itemModDateTime: LocalDateTime){
    val nameProperty = SimpleStringProperty("")
    var name by nameProperty

    val typeProperty = SimpleObjectProperty<ExplorerItemType>(ExplorerItemType.File)
    var type by typeProperty

    val modDateProperty = SimpleObjectProperty<LocalDateTime>(LocalDateTime.now())
    var modDate by modDateProperty

    val sizeProperty = SimpleLongProperty(0)
    var size by sizeProperty

    val iconProperty = SimpleObjectProperty<Image>()
    var icon by iconProperty

    init{
        name = itemName
        type = itemType
        size = itemSize
        modDate = itemModDateTime
        when (type) {
            ExplorerItemType.File -> {
                try{
                    var ext = name.removeRange(0, name.lastIndexOf('.')).toLowerCase().removeRange(0,1)
                    icon = Image("fileIcons/$ext.png")
                }catch (ex:Exception){
                    icon = Image("fileIcons/file.png")
                }
            }
            ExplorerItemType.Directory -> icon = Image("fileIcons/folder.png")
        }
    }
}