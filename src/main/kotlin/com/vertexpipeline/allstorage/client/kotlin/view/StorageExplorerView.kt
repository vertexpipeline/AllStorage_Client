package com.vertexpipeline.allstorage.client.kotlin.view

import com.vertexpipeline.allstorage.client.kotlin.model.ExplorerItem
import com.vertexpipeline.allstorage.client.kotlin.model.ExplorerItemType
import com.vertexpipeline.allstorage.client.kotlin.controller.AppController
import com.vertexpipeline.allstorage.client.kotlin.controller.StorageExplorerController
import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.layout.Border
import tornadofx.*
import javafx.collections.FXCollections
import java.util.Comparator

class StorageExplorerView : View() {
    val controller: StorageExplorerController by inject()
    val appController : AppController by inject()
    val searchField = textfield { // TODO search field
        disableProperty().bindBidirectional(appController.isControlDisabledProperty)
        anchorpaneConstraints {
            leftAnchor = 0.0
            topAnchor = 4.0
            bottomAnchor = 4.0
        }
        style{
            opacity = 0.3
        }
        setOnMouseEntered {
            style{
                opacity = 1.0
            }
        }

        setOnMouseExited {
            style {
                if(text == "" && !isFocused)
                    opacity = 0.3
            }
        }
        focusedProperty().addListener { observable, oldValue, newValue ->
            if(!newValue)
                style{
                    if(text == "" && !isFocused)
                        opacity = 0.3
                }
        }
    }

    override val root = borderpane {
        anchorpaneConstraints {
            topAnchor = 0.0
            leftAnchor = 0.0
            rightAnchor = 0.0
            bottomAnchor = 0.0
        }
        top {
            anchorpane{
                menubar {
                    disableProperty().bindBidirectional(appController.isControlDisabledProperty)
                    anchorpaneConstraints {
                        leftAnchor = 0.0
                        topAnchor = 0.0
                        bottomAnchor = 0.0
                        rightAnchor = 0.0
                    }
                    menu {
                        graphic = imageview(Image("MenuIcons/back.png"))
                    }
                    menu {
                        graphic = imageview(Image("MenuIcons/refresh.png"))
                    }
                    menu {
                        graphic = imageview(Image("MenuIcons/recycling.png"))
                    }
                    menu {
                        graphic = imageview(Image("MenuIcons/fast-forward.png")){
                            rotate = 90.0
                        }
                    }
                }
                anchorpane{
                    prefWidth = 200.0
                    minWidth = 200.0
                    anchorpaneConstraints {
                        rightAnchor = 0.0
                        topAnchor = 0.0
                        bottomAnchor = 0.0
                    }
                    this += searchField
                }
            }
        }
        center {
            tableview<ExplorerItem>(controller.files.observable()) {
                disableProperty().bindBidirectional(appController.isControlDisabledProperty)
                fixedCellSize = 36.0
                column(messages["icon"], ExplorerItem::icon).cellFormat {
                    graphic = imageview(it)
                }
                column(messages["name"], ExplorerItem::nameProperty).cellFormat {
                    prefWidth = 120.0
                    text = it
                    style {
                        fontSize = 14.px
                        alignment = Pos.CENTER
                    }
                }
                column(messages["modified_date"], ExplorerItem::modDateProperty).cellFormat { it ->
                    if (this.rowItem.itemType != ExplorerItemType.Directory) {
                        text = it.toString()
                        style {
                            fontSize = 14.px
                            alignment = Pos.CENTER
                        }
                    } else {
                        text = ""
                    }
                }
                column(messages["size"], ExplorerItem::sizeProperty).cellFormat {
                    if (this.rowItem.itemType != ExplorerItemType.Directory) {
                        val s = it.toDouble()
                        text = if(s<1024){
                            "${s.toInt()} ${messages["bytes"]}"
                        } else if(s<1048576) {
                            "${(s / 1024).toInt()} ${messages["kilob"]}"
                        } else {
                            "${(s / 1048576).toInt()} ${messages["mbytes"]}"
                        }
                        style {
                            fontSize = 14.px
                            alignment = Pos.CENTER
                        }
                    } else {
                        text = ""
                    }

                }
                setSortPolicy {
                    val comp = Comparator { o1: ExplorerItem, o2: ExplorerItem ->
                        val str = searchField.text
                        fun makeScore(name:String, req:String):Int{ // TODO make searching
                              return 0
                        }
                        val s1 = makeScore(o1.name, str) + if(o1.type == ExplorerItemType.Directory) 1 else 0
                        val s2 = makeScore(o2.name, str)+ if(o2.type == ExplorerItemType.Directory) 1 else 0
                        if(s1 < s2)
                            1
                        else if (s1 == s2)
                            0
                        else
                            -1
                    }
                    FXCollections.sort(items.observable(), comp)
                    true
                }
            }
        }
        bottom {
            textfield{
                disableProperty().bindBidirectional(appController.isControlDisabledProperty)
                border = Border.EMPTY
                isEditable = false
                isFocusTraversable = false
                textProperty().bindBidirectional(controller.pathProperty)
            }
        }
    }
}