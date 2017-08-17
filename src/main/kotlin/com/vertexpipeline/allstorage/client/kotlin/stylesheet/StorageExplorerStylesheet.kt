package com.vertexpipeline.allstorage.client.kotlin.stylesheet

import javafx.scene.paint.Color
import tornadofx.*

class StorageExplorerStylesheet:Stylesheet(){
    companion object {
        val menuButton by cssclass()
    }
    init {
        menuButton{
             backgroundColor += Color.TRANSPARENT
             borderColor += box(Color.TRANSPARENT)
             opacity = 1.0
             hover {
                 opacity = 0.8
             }

        }
    }
}