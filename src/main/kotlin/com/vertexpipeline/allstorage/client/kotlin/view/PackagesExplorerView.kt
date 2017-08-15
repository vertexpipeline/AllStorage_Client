package com.vertexpipeline.allstorage.client.kotlin.view

import com.vertexpipeline.allstorage.client.kotlin.controller.AppController
import com.vertexpipeline.allstorage.client.kotlin.controller.PackagesExplorerController
import tornadofx.*
import javafx.scene.image.Image


class PackagesExplorerView : View() {
    val controller: PackagesExplorerController by inject()
    val appController : AppController by inject()
    override val root = anchorpane {
        anchorpaneConstraints {
            topAnchor = 0.0
            leftAnchor = 0.0
            rightAnchor = 0.0
            bottomAnchor = 0.0
        }
        menubar {
            disableProperty().bindBidirectional(appController.isControlDisabledProperty)
            anchorpaneConstraints {
                leftAnchor = 0.0
                rightAnchor = 0.0
            }
            menu {
                graphic = imageview(Image("MenuIcons/refresh.png"))
            }
            menu {
                graphic = imageview(Image("MenuIcons/fast-forward.png")){
                    rotate = 90.0
                }
            }
        }
        listview<String>{
            disableProperty().bindBidirectional(appController.isControlDisabledProperty)
            anchorpaneConstraints {
                leftAnchor = 0.0
                rightAnchor = 0.0
                topAnchor = 32.0
                bottomAnchor = 0.0
            }
        }

        /*treeview<String> {
            anchorpaneConstraints {
                bottomAnchor = 0.0
                leftAnchor = 0.0
                rightAnchor = 0.0
                topAnchor = 25.0
            }
            root = TreeItem("")

            val fsView = FileSystemView.getFileSystemView()
            *//*val listener = object:ChangeListener<Boolean>{
                override fun changed(observable: ObservableValue<out Boolean>?, oldValue: Boolean?, newValue: Boolean?) {
                    if(newValue == true){
                        val bb = observable as BooleanProperty
                        val t = bb.bean as TreeItem<FSItem>
                        t.children.forEach{
                            val ch = it
                            File(it.value.path + it.value.name).listFiles().forEach {
                                val item = TreeItem(FSItem(it.name, if(it.isDirectory)FSItemType.Directory else FSItemType.File, it.path))
                                item.expandedProperty().addListener(this)
                                ch.getChildren().add(item)
                            }
                        }
                    }
                }
            }*//*

            cellFormat {
                text = it
            }

            lazyPopulate {
                try {
                    val text = if (it.value == "") "/" else it.value
                    val file = File(text)
                    val isDir = file.isDirectory
                    if (isDir) {
                        file.listFiles().map {
                            val new = text + it.name + (if (file.isDirectory) "\\" else "")
                            new
                        }
                    } else {
                        listOf()
                    }
                }
                catch (ex:Exception){
                    listOf()
                }
            }
        }*/
    }
}
