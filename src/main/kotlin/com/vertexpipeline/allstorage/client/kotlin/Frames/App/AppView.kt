package com.vertexpipeline.allstorage.client.kotlin.Frames.App

import com.vertexpipeline.allstorage.client.kotlin.Frames.PackagesExplorer.PackagesExplorerView
import com.vertexpipeline.allstorage.client.kotlin.Frames.StorageExplorer.StorageExplorerView
import com.vertexpipeline.allstorage.client.kotlin.Frames.StorageManager.StoragesManagerView
import javafx.scene.control.Label
import tornadofx.*
import java.util.*

class AppView : View("All Storage") {
    val rest: Rest by inject()
    val storageExplorer: StorageExplorerView by inject()
    val packagesExplorer: PackagesExplorerView by inject()
    val storageManager: StoragesManagerView by inject()
    val controller: AppController by inject()

    val uploadCount = Label()

    init {
        FX.messages = ResourceBundle.getBundle("Messages")
    }

    override fun onDock() {
        currentStage?.width = 1000.0
        currentStage?.height = 600.0
    }

    override val root = anchorpane {
        splitpane {
            anchorpaneConstraints {
                topAnchor = 0.0
                leftAnchor = 0.0
                rightAnchor = 0.0
                bottomAnchor = 20.0
            }
            anchorpane {
                this += storageExplorer
            }
            anchorpane {
                anchorpaneConstraints {
                    topAnchor = 0.0
                    leftAnchor = 0.0
                    rightAnchor = 0.0
                    bottomAnchor = 0.0
                }
                maxWidth = 300.0
                minWidth = 300.0
                tabpane {
                    anchorpaneConstraints {
                        topAnchor = 0.0
                        leftAnchor = 0.0
                        rightAnchor = 0.0
                        bottomAnchor = 0.0
                    }
                    tab(messages["packages"]) {
                        isClosable = false
                        this += packagesExplorer
                    }
                    tab(messages["uploadQueue"]) {
                        isClosable = false
                    }
                    tab(messages["connecting"]) {
                        isClosable = false
                        this += storageManager
                    }
                }
            }
        }
        hbox {
            anchorpaneConstraints {
                leftAnchor = 0.0
                rightAnchor = 0.0
                bottomAnchor = 0.0
            }
            prefHeight = 20.0

            /*hbox{
                text("Файлов отгружается:")
                this += uploadCount
            }*/
        }
    }
}
