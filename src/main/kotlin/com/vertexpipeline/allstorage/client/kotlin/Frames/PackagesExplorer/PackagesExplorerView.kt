package com.vertexpipeline.allstorage.client.kotlin.Frames.PackagesExplorer

import com.vertexpipeline.allstorage.client.kotlin.Frames.App.AppController
import com.vertexpipeline.allstorage.client.kotlin.Stylesheets.GlobalStylesheet
import com.vertexpipeline.allstorage.client.kotlin.Frames.StorageManager.StoragesManagerView
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
        toolbar {
            anchorpaneConstraints {
                leftAnchor = 0.0
                rightAnchor = 0.0
            }
            button {
                addClass(GlobalStylesheet.menuButton)
                disableProperty().bindBidirectional(appController.isControlDisabledProperty)
                graphic = imageview(Image("MenuIcons/refresh.png"))
            }
            button {
                addClass(GlobalStylesheet.menuButton)
                disableProperty().bindBidirectional(appController.isControlDisabledProperty)
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
    }
}
