package com.vertexpipeline.allstorage.client.kotlin.Frames.StorageManager

import com.vertexpipeline.allstorage.client.kotlin.Frames.App.AppController
import com.vertexpipeline.allstorage.client.kotlin.Models.NodeInfo
import com.vertexpipeline.allstorage.client.kotlin.Stylesheets.GlobalStylesheet
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.ProgressBar
import javafx.scene.control.TextField
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.paint.Color
import tornadofx.*

class StoragesManagerView : View("Storages") {
    val controller: StorageManagerController by inject()
    val appController: AppController by inject()
    val fromIP1 = TextField("192")
    val fromIP2 = TextField("168")
    val fromIP3 = TextField("1")
    val fromIP4 = TextField("0")

    val toIP1 = TextField("192")
    val toIP2 = TextField("168")
    val toIP3 = TextField("1")
    val toIP4 = TextField("255")

    var progressBar = ProgressBar()

    override fun onDock() {
        super.onDock()
    }

    override val root = borderpane {
        center {
            listview<NodeInfo> {
                itemsProperty().bindBidirectional(controller.nodesProperty)
                addClass(GlobalStylesheet.storageManagerCell)
                anchorpaneConstraints {
                    topAnchor = 0.0
                    rightAnchor = 0.0
                    leftAnchor = 0.0
                    bottomAnchor = 0.0
                }
                cellFormat { item ->
                    addClass(GlobalStylesheet.storageManagerCell)
                    style {
                        padding = box(4.px, 4.px, 4.px, 4.px)
                    }
                    graphic = borderpane {
                        style {
                            backgroundColor += c("f5f5f5")
                            effect = DropShadow(4.0, Color.BLACK.brighter().brighter())
                        }
                        center {
                            vbox {
                                style {
                                    padding = box(8.px, 8.px, 8.px, 8.px)
                                }
                                borderpane {
                                    left {
                                        label(item.name) {
                                            textFill = Color.BLACK
                                            style {
                                                fontSize = 18.px
                                            }
                                        }
                                    }
                                    bottom {
                                        label(item.address) {
                                            textFill = Color.BLACK
                                            style {
                                                fontSize = 12.px
                                            }
                                        }
                                    }
                                }
                                borderpane {
                                    val keyField = textfield {
                                        promptText = "key"
                                    }
                                    left {
                                        if (item.needKey) this += keyField
                                    }
                                    right {
                                        button(messages["connect"]) {
                                            addClass(GlobalStylesheet.menuButton)
                                            style {
                                                backgroundColor += c("673ab7")
                                                textFill = Color.WHITE
                                                padding = box(6.px, 6.px, 6.px, 6.px)
                                            }
                                            setOnAction {
                                                val info = controller.tryConnect(item.address, keyField.text)
                                                if (info != null) {
                                                    appController.enableControls()
                                                    appController.onConnected()
                                                } else {
                                                    keyField.text = ""
                                                    keyField.promptText = messages["wrong_text"]
                                                    appController.disableControls()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        bottom {
            borderpane {
                center {
                    vbox {
                        padding = Insets(4.0)
                        vbox {
                            hbox {
                                label("From") {
                                    minWidth = 60.0
                                    padding = Insets(0.0, 8.0, 0.0, 8.0)
                                    style {
                                        fontSize = 12.px
                                    }
                                }
                                this += fromIP1
                                label(".")
                                this += fromIP2
                                label(".")
                                this += fromIP3
                                label(".")
                                this += fromIP4
                            }
                            hbox {
                                label("To") {
                                    minWidth = 60.0
                                    padding = Insets(0.0, 8.0, 0.0, 8.0)
                                    style {
                                        fontSize = 12.px
                                    }
                                }
                                this += toIP1
                                label(".")
                                this += toIP2
                                label(".")
                                this += toIP3
                                label(".")
                                this += toIP4
                            }
                        }
                        hbox{
                            progressBar = progressbar {
                                padding = Insets(8.0)
                                prefHeight = 44.0
                                prefWidth = 260.0
                                progressProperty().bindBidirectional(controller.scanProgressProperty)
                            }; this += progressBar
                            button {
                                padding = Insets(8.0)
                                addClass(GlobalStylesheet.menuButton)
                                graphic = imageview(Image("MenuIcons/search.png"))
                                setOnAction {
                                    controller.scan(
                                            fromIP1.text.toInt(),
                                            fromIP2.text.toInt(),
                                            fromIP3.text.toInt(),
                                            fromIP4.text.toInt(),
                                            toIP1.text.toInt(),
                                            toIP2.text.toInt(),
                                            toIP3.text.toInt(),
                                            toIP4.text.toInt()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}