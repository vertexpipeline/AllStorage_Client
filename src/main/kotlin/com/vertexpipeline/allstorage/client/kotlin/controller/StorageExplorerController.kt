package com.vertexpipeline.allstorage.client.kotlin.controller

import com.vertexpipeline.allstorage.client.kotlin.model.*
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue
import java.io.File
import java.net.URLEncoder
import java.util.*

class StorageExplorerController :Controller(){
    val rest:Rest by inject()
    val appController:AppController by inject()

    val explorerItems = SimpleListProperty<ExplorerItem>(ArrayList<ExplorerItem>().observable())
    var items by explorerItems

    val pathProperty = SimpleStringProperty("root")
    var path by pathProperty

    val cannotMoveBackProperty = SimpleBooleanProperty(true)
    var cannotMoveBack by cannotMoveBackProperty

    val movingHistory: Stack<String> = Stack()

    fun encode(str:String?):String{
        return URLEncoder.encode(str ?: "", "UTF-8")
    }

    fun addFile(file:File):Boolean{
        val resp = rest.get("api/createFile?accessKey=${encode(appController.connectedNode?.accessKey)}" +
                "&path=${encode(path)}" +
                "&name=${encode(file.name)}" +
                "&size=${file.length()}" +
                "&extension=${encode(file.extension)}").one()
        if(resp.string("state") == "success"){
            scanDir()
            return true
        } else {
            print(resp.string("state"))
            return false
        }
    }

    fun scanDir():Boolean{
        items.clear()
        val response = rest.get("api/scan?accessKey=${encode(appController.connectedNode?.accessKey)}" +
                "&path=${URLEncoder.encode(path, "UTF-8")}").one().toModel<OperationResult>()
        if(response.state == "success") {
            val res = response.result.toModel<ScanResult>()
            res.folders.forEach {
                items.add(ExplorerItem(ExplorerItemType.Directory, it))
            }
            res.files.forEach {
                items.add(ExplorerItem(ExplorerItemType.File, it))
            }
            return  true
        }
        return false
    }

    fun openDir(folder: String):Boolean {
        val lastPath = path
        path += "/" + folder
        if(scanDir())
        {
            cannotMoveBack = false
            movingHistory.push(lastPath)
            return true
        } else {
            return false
        }
    }

    fun createFolder(){
        val resp = rest.get("api/createFolder?accessKey=${encode(appController.connectedNode?.accessKey)}" +
                "&path=${encode(path)}" +
                "&name=${encode("New folder")}").one()
        if(resp.string("state") == "success"){
            scanDir()
        } else {
            print(resp.string("state"))
        }
    }

    fun moveBack(){
        if(!movingHistory.empty()){
            path = movingHistory.pop()
            scanDir()
            if(movingHistory.empty())
                cannotMoveBack = true
        }
    }
}