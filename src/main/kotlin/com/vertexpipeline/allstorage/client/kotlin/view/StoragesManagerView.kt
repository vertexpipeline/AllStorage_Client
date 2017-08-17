package com.vertexpipeline.allstorage.client.kotlin.view

import com.vertexpipeline.allstorage.client.kotlin.controller.StorageManagerController
import com.vertexpipeline.allstorage.client.kotlin.model.NodeInfo
import javafx.geometry.Orientation
import javafx.scene.Parent
import tornadofx.*

class StoragesManagerView : View("Storages") {
    val controller: StorageManagerController by inject()

    override fun onDock() {
        controller.scan()
        super.onDock()
    }

    override val root = anchorpane {
        button(messages["refresh"]) {
            anchorpaneConstraints {
                topAnchor = 10.0
                rightAnchor = 10.0
            }
            setOnAction {
                controller.scan()
            }
        }
        button(messages["connect"]) {
            anchorpaneConstraints {
                topAnchor = 10.0
                rightAnchor = 90.0
            }
        }
        listview<NodeInfo> {
            itemsProperty().bindBidirectional(controller.nodesProperty)
            anchorpaneConstraints {
                topAnchor = 48.0
                rightAnchor = 0.0
                leftAnchor = 0.0
                bottomAnchor = 0.0
            }
            cellFormat { item ->
                graphic = vbox{
                    borderpane{
                        left{
                            label(item.name){
                                style{
                                    fontSize = 14.px
                                }
                            }
                        }
                        right{
                            label(item.address){
                                style{
                                    fontSize = 14.px
                                }
                            }
                        }
                    }
                    separator(Orientation.HORIZONTAL)
                    borderpane{
                        val keyField = textfield {
                            promptText = "key"
                        }
                        left {
                           this += keyField
                        }
                        right{
                            button(messages["connect"]){
                                setOnAction {
                                    if(controller.tryConnect(item.address, keyField.text)){
                                        currentStage?.close()
                                    } else {
                                        keyField.text = ""
                                        keyField.promptText = messages["wrong_text"]
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