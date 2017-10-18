package com.vertexpipeline.allstorage.client.kotlin.Frames.App

import com.vertexpipeline.allstorage.client.kotlin.Frames.StorageExplorer.StorageExplorerController
import com.vertexpipeline.allstorage.client.kotlin.Models.FileInfo
import com.vertexpipeline.allstorage.client.kotlin.Models.NodeInfo
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleListProperty
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue
import java.io.InputStream

class AppController : Controller(){
    val explorerController: StorageExplorerController by inject()
    val rest = Rest()

    val isControlDisabledProperty = SimpleBooleanProperty(true)
    var isMenuDisabled by isControlDisabledProperty

    var connectedNode: NodeInfo? = null

    val isConnectedProperty = SimpleBooleanProperty()
    var isConnected by isConnectedProperty

    val uploadQueueProperty = SimpleListProperty<FileInfo>(ArrayList<FileInfo>().observable())
    var uploadQueue by uploadQueueProperty

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

    fun operationFault(){
        isConnected = false
        disableControls()
    }

    fun uploadFiles(){
        rest.baseURI = "http://"+connectedNode?.address
        while(uploadQueue.count() != 0){
            val file = uploadQueue.component1()
            rest.post("/api/upload?fileID=${file.fileID}&size=${file.size}", file.fileLocal?.inputStream() as InputStream)
            print("${file.name} uploaded")
        }
    }
}