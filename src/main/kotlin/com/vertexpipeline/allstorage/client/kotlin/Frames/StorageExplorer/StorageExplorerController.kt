package com.vertexpipeline.allstorage.client.kotlin.Frames.StorageExplorer

import com.vertexpipeline.allstorage.client.kotlin.Frames.App.AppController
import com.vertexpipeline.allstorage.client.kotlin.Models.*
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.io.File
import java.lang.Package
import java.net.URLEncoder
import java.util.*
import kotlin.collections.ArrayList

class StorageExplorerController : Controller() {
    val rest = Rest()
    val appController: AppController by inject()

    val explorerItems = SimpleListProperty<ExplorerItem>(ArrayList<ExplorerItem>().observable())
    var items by explorerItems

    val pathProperty = SimpleStringProperty("")
    var path by pathProperty

    val cannotMoveBackProperty = SimpleBooleanProperty(true)
    var cannotMoveBack by cannotMoveBackProperty

    val movingHistory: Stack<String> = Stack()

    fun encode(str: String?): String {
        return URLEncoder.encode(str ?: "", "UTF-8")
    }

    fun AddPackage(files:Array<File>){
        rest.baseURI = "http://"+appController.connectedNode?.address
        val root = Package()
        root.packageName = "package"
        root.path = path

        val rootFiles = ArrayList<FileInfo>()
        val rootFolders = ArrayList<FolderInfo>()

        fun getFileInfo(file:File):FileInfo{
            val fileInfo = FileInfo()
            fileInfo.name = file.name
            fileInfo.extension = file.extension
            fileInfo.size = file.length()
            return fileInfo
        }

        fun getFolderInfo(folder:File):FolderInfo{
            val folderInfo = FolderInfo()
            val fileInfos = ArrayList<FileInfo>()
            val folderInfos = ArrayList<FolderInfo>()
            folder.listFiles().forEach {
                if(it.isFile)
                    fileInfos.add(getFileInfo(it))
                else
                    folderInfos.add(getFolderInfo(it))
            }
            folderInfo.name = folder.name
            folderInfo.folders = folderInfos.observable()
            folderInfo.files = fileInfos.observable()
            return folderInfo
        }

        for (file in files){
            if(file.isFile) {
                rootFiles.add(getFileInfo(file))
            } else {
                rootFolders.add(getFolderInfo(file))
            }
        }

        root.files = rootFiles.observable()
        root.folders = rootFolders.observable()
        val name = "new"
        try {
            val res = rest.post("api/addPackage?accessKey=${encode(appController.connectedNode?.accessKey)}", root).one().toModel<OperationResult>()
            print(res.state)
        }
        catch (ex:Exception) {
            print(ex)
        }
    }

    fun addFile(file: File): Boolean {
        val resp = rest.get("api/createFile?accessKey=${encode(appController.connectedNode?.accessKey)}" +
                "&path=${encode(path)}" +
                "&name=${encode(file.name)}" +
                "&size=${file.length()}" +
                "&extension=${encode(file.extension)}").one()
        if (resp.string("state") == "success") {
            scanDir()
            return true
        } else {
            print(resp.string("state"))
            return false
        }
    }

    fun scanDir(): Boolean {
        rest.baseURI = "http://"+appController.connectedNode?.address
        items.clear()
        val response = rest.get("/api/scan?accessKey=${encode(appController.connectedNode?.accessKey)}" +
                "&path=${encode(path)}").one().toModel<OperationResult>()
        if (response.state == "success") {
            val res = response.result.toModel<ScanResult>()
            res.folders.forEach {
                items.add(ExplorerItem(ExplorerItemType.Directory, it))
            }
            res.files.forEach {
                items.add(ExplorerItem(ExplorerItemType.File, it))
            }
            return true
        }
        return false
    }

    fun openDir(folder: String): Boolean {
        val lastPath = path
        path += "/" + folder
        if (scanDir()) {
            cannotMoveBack = false
            movingHistory.push(lastPath)
            return true
        } else {
            return false
        }
    }

    fun createFolder() {
        val resp = rest.get("api/createFolder?accessKey=${encode(appController.connectedNode?.accessKey)}" +
                "&path=${encode(path)}" +
                "&name=${encode("New folder")}").one()
        if (resp.string("state") == "success") {
            scanDir()
        } else {
            print(resp.string("state"))
        }
    }

    fun moveBack() {
        if (!movingHistory.empty()) {
            path = movingHistory.pop()
            scanDir()
            if (movingHistory.empty())
                cannotMoveBack = true
        }
    }
}