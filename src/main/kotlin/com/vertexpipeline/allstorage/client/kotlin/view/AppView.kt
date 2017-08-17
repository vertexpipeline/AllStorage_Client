package com.vertexpipeline.allstorage.client.kotlin.view

import javafx.scene.image.Image
import tornadofx.*
import java.util.*

class AppView : View("All Storage") {
    val rest:Rest by inject()
    val fileExplorer: StorageExplorerView by inject()
    val storagesManager: PackagesExplorerView by inject()

    init {
        FX.messages = ResourceBundle.getBundle("Messages")
        reloadStylesheetsOnFocus()
    }

    override fun onDock() {
        currentStage?.width = 800.0
        currentStage?.height = 600.0
    }

    override val root = anchorpane {
        menubar {
            style{
                backgroundColor += c("#ffffff", 0.0)
            }
            anchorpaneConstraints {
                rightAnchor = 0.0
                leftAnchor = 0.0
                topAnchor = 0.0
            }
            menu(messages["storages"]) {
                graphic = imageview(Image("MenuIcons/database.png"))
                menu(messages["connections"]){
                    setOnAction {
                        find(StoragesManagerView::class).openModal()
                    }
                }
            }
            menu(messages["settings"]) {
                graphic = imageview(Image("MenuIcons/settings-gears.png"))
            }
        }
        splitpane {
            anchorpaneConstraints {
                topAnchor = 25.0
                leftAnchor = 0.0
                rightAnchor = 0.0
                bottomAnchor = 0.0
            }
            anchorpane {
                this += fileExplorer
            }
            anchorpane {
                anchorpaneConstraints {
                    topAnchor = 0.0
                    leftAnchor = 0.0
                    rightAnchor = 0.0
                    bottomAnchor = 0.0
                }
                maxWidth = 200.0
                minWidth = 200.0
                this += storagesManager
            }
        }
    }
}
