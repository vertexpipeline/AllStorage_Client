package com.vertexpipeline.allstorage.client.kotlin.Stylesheets

import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import tornadofx.*

class GlobalStylesheet : Stylesheet(){
    companion object {
        val menuButton by cssclass()
        val storageManagerCell by cssclass()
    }
    init {
        menuButton{
            backgroundColor += Color.TRANSPARENT
            opacity = 1.0
            and(hover){
                opacity = 0.8
            }
            and(disabled){
                opacity = 0.3
            }
            and(pressed){
                effect = DropShadow(4.0, Color.BLACK.brighter())
            }
        }
        storageManagerCell {
            and(selected){
                backgroundColor += c("ffffff", 0.0)
                textFill = Color.BLACK
            }
        }
    }
}