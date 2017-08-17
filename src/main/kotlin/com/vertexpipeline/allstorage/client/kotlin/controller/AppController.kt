package com.vertexpipeline.allstorage.client.kotlin.controller

import com.vertexpipeline.allstorage.client.kotlin.model.NodeInfo
import javafx.beans.property.SimpleBooleanProperty
import tornadofx.*

class AppController : Controller(){
    val explorerController:StorageExplorerController by inject()

    val isControlDisabledProperty = SimpleBooleanProperty(true)
    var isMenuDisabled by isControlDisabledProperty

    var connectedNode: NodeInfo? = null

    fun onConnected(){
        try{
            explorerController.scanDir()
        }catch (ex:Exception){
            print(ex.localizedMessage)
            disableControls()
        }
    }

    fun enableControls(){
        isMenuDisabled = false
    }

    fun disableControls(){
        isMenuDisabled = true
    }
}