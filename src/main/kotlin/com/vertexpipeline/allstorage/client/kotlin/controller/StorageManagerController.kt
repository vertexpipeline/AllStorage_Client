package com.vertexpipeline.allstorage.client.kotlin.controller

import com.vertexpipeline.allstorage.client.kotlin.model.NodeInfo
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue
import java.net.HttpURLConnection
import java.net.Inet4Address
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.*

class StorageManagerController : Controller() {
    val rest: Rest by inject()
    val appController:AppController by inject()

    val nodesProperty = SimpleListProperty<NodeInfo>(ArrayList<NodeInfo>().observable())
    var nodes by nodesProperty

    fun ping(n: Int): Int {
        try {
            val con = URL("http://192.168.1.$n:600/api/ping").openConnection() as HttpURLConnection
            con.connectTimeout = 1000
            con.connect()
            if (con.responseCode == 200) {
                return n
            } else {
                return -1
            }
        } catch (ex: Exception) {
            return -1
        }
    }

    fun encode(str: String):String {
        return URLEncoder.encode(
                Base64.getEncoder().encode(
                        MessageDigest.getInstance("SHA-256").digest(
                                str.toByteArray(Charset.defaultCharset())
                        )
                ).toString(Charset.defaultCharset()),
                "UTF-8"
        )
    }

    fun tryConnect(ip: String, key: String):Boolean {
        try {
            rest.baseURI = "http://${ip}:600"
            val response = rest.get("api/info?accessKey=${encode(key)}").one()
            if (response.string("state") == "success") {
                val info = response.getJsonObject("result").toModel<NodeInfo>()
                if (info.name != null) {
                    appController.connectedNode = info
                    appController.enableControls()
                    appController.onConnected()
                    return true
                }
            } else {
                return false
            }
        }catch (ex:Exception){
            print(ex)
        }
        appController.disableControls()
        return false
    }

    fun scan() {
        nodes.clear()
        runAsync {
            for (n in 0..255) {
                runAsync {
                    ping(n)
                } ui {
                    if (it != -1) {
                        val info = NodeInfo()
                        info.address = "192.168.1.$n"
                        info.name = Inet4Address.getByAddress(byteArrayOf(192.toByte(),168.toByte(),1.toByte(),n.toByte())).hostName
                        nodes.add(info)
                    }
                }
            }
        }
    }
}