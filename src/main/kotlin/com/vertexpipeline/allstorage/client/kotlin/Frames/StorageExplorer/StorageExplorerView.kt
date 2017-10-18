package com.vertexpipeline.allstorage.client.kotlin.Frames.StorageExplorer

import com.vertexpipeline.allstorage.client.kotlin.Frames.App.AppController
import com.vertexpipeline.allstorage.client.kotlin.Models.ExplorerItem
import com.vertexpipeline.allstorage.client.kotlin.Models.ExplorerItemType
import com.vertexpipeline.allstorage.client.kotlin.Stylesheets.GlobalStylesheet
import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.layout.Border
import tornadofx.*
import javafx.collections.FXCollections
import javafx.scene.control.TableRow
import javafx.scene.input.KeyCode
import javafx.scene.input.TransferMode
import java.util.Comparator

class StorageExplorerView : View() {
    val controller: StorageExplorerController by inject()
    val appController: AppController by inject()

    val searchField = textfield {
        // TODO search field
        disableProperty().bindBidirectional(appController.isControlDisabledProperty)
        anchorpaneConstraints {
            leftAnchor = 0.0
            topAnchor = 4.0
            bottomAnchor = 4.0
        }
        style {
            opacity = 0.3
        }
        setOnMouseEntered {
            style {
                opacity = 1.0
            }
        }
        setOnMouseExited {
            style {
                if (text == "" && !isFocused)
                    opacity = 0.3
            }
        }
        focusedProperty().addListener { observable, oldValue, newValue ->
            if (!newValue)
                style {
                    if (text == "" && !isFocused)
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
            anchorpane {
                toolbar {
                    disableProperty().bindBidirectional(appController.isControlDisabledProperty)
                    anchorpaneConstraints {
                        leftAnchor = 0.0
                        topAnchor = 0.0
                        bottomAnchor = 0.0
                        rightAnchor = 0.0
                    }
                    button {
                        addClass(GlobalStylesheet.menuButton)
                        graphic = imageview(Image("MenuIcons/back.png"))
                        setOnAction {
                            controller.moveBack()
                        }
                        disableProperty().bindBidirectional(controller.cannotMoveBackProperty)
                    }
                    button {
                        addClass(GlobalStylesheet.menuButton)
                        graphic = imageview(Image("MenuIcons/refresh.png"))
                    }
                    button {
                        addClass(GlobalStylesheet.menuButton)
                        graphic = imageview(Image("MenuIcons/recycling.png"))
                    }
                    button {
                        addClass(GlobalStylesheet.menuButton)
                        graphic = imageview(Image("MenuIcons/fast-forward.png")) {
                            rotate = 90.0
                        }
                    }
                }
                anchorpane {
                    prefWidth = 160.0
                    minWidth = 160.0
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
            tableview<ExplorerItem>() {
                itemsProperty().bindBidirectional(controller.explorerItems)
                disableProperty().bindBidirectional(appController.isControlDisabledProperty)
                fixedCellSize = 36.0
                setRowFactory {
                    val row = TableRow<ExplorerItem>()
                    row.onDoubleClick {
                        val item = this@tableview.selectedItem
                        if(item != null){
                            if(item.type == ExplorerItemType.Directory){
                                controller.openDir(item.name)
                            }
                        }
                    }
                    row
                }
                column("", ExplorerItem::iconProperty).cellFormat {
                    this.tableColumn.prefWidth = 40.0
                    graphic = imageview(it)
                }
                column(messages["name"], ExplorerItem::nameProperty).cellFormat {
                    this.tableColumn.prefWidth = 160.0
                    text = it
                    style {
                        fontSize = 14.px
                        alignment = Pos.CENTER
                    }
                }
                /*column(messages["extension"], ExplorerItem::extensionProperty).cellFormat {
                    text = if (it != "<storage>") it else messages["folder"]
                    style {
                        fontSize = 14.px
                        alignment = Pos.CENTER
                    }
                }*/
                column(messages["modified_date"], ExplorerItem::lastModifiedProperty).cellFormat { it ->
                    this.tableColumn.prefWidth = 160.0
                    if (this.rowItem.extension != "<folder>") {
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
                    if (this.rowItem.extension != "<folder>") {
                        val s = it.toDouble()
                        text = if (s < 1024) {
                            "${s.toInt()} ${messages["bytes"]}"
                        } else if (s < 1048576) {
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
                        fun makeScore(name: String, req: String): Int { // TODO make searching
                            return 0
                        }

                        val s1 = makeScore(o1.name, str) + if (o1.extension == "<folder>") 1 else 0
                        val s2 = makeScore(o2.name, str) + if (o2.extension == "<folder>") 1 else 0
                        if (s1 < s2)
                            1
                        else if (s1 == s2)
                            0
                        else
                            -1
                    }
                    FXCollections.sort(items.observable(), comp)
                    true
                }
                contextmenu {
                    menu("Create folder"){
                        setOnAction {
                            controller.createFolder()
                            this@contextmenu.hide()
                        }
                    }
                }
                setOnDragOver {
                    if(it.dragboard.hasFiles()){
                        it.acceptTransferModes(TransferMode.LINK)
                    }
                }
                setOnDragDropped {
                    with(it.dragboard) {
                        if (hasFiles()) {
                           controller.AddPackage(files.toTypedArray())
                        }
                    }
                }
            }
        }
        bottom {
            textfield {
                disableProperty().bindBidirectional(appController.isControlDisabledProperty)
                border = Border.EMPTY
                isEditable = true
                isFocusTraversable = false
                textProperty().bindBidirectional(controller.pathProperty)
                setOnKeyPressed {
                    if(it.code == KeyCode.ENTER){
                        controller.scanDir()
                    }
                }
            }
        }
    }
}