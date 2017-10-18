package com.vertexpipeline.allstorage.client.kotlin.Frames.StorageManager

import com.vertexpipeline.allstorage.client.kotlin.Frames.App.AppController
import com.vertexpipeline.allstorage.client.kotlin.Models.NodeInfo
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleListProperty
import tornadofx.*
import java.net.HttpURLConnection
import java.net.Inet4Address
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.*
import tornadofx.getValue
import tornadofx.setValue

data class PingResult(val a: Int, val b: Int, val c: Int, val d: Int)

class StorageManagerController : Controller() {
    val rest: Rest by inject()
    val appController: AppController by inject()

    val nodesProperty = SimpleListProperty<NodeInfo>(ArrayList<NodeInfo>().observable())
    var nodes by nodesProperty

    val scanProgressProperty = SimpleDoubleProperty(0.0)
    var scanProgress by scanProgressProperty

    val isConnectedProperty = SimpleBooleanProperty()
    var isConnected by isConnectedProperty


    var scanSize = 0.0
    var scanStep = 0.0
    fun ping(a: Int, b: Int, c: Int, d: Int): PingResult? {
        try {
            val con = URL("http://$a.$b.$c.$d:600/api/ping").openConnection() as HttpURLConnection
            con.connectTimeout = 1000
            con.connect()
            if (con.responseCode == 200) {
                return PingResult(a, b, c, d)
            } else {
                return null
            }
        } catch (ex: Exception) {
            return null
        }
    }

    fun encode(str: String): String {
        return URLEncoder.encode(
                Base64.getEncoder().encode(
                        MessageDigest.getInstance("SHA-256").digest(
                                str.toByteArray(Charset.defaultCharset())
                        )
                ).toString(Charset.defaultCharset()),
                "UTF-8"
        )
    }

    fun tryConnect(ip: String, key: String = ""): NodeInfo? {
        try {
            rest.baseURI = "http://${ip}"
            val url = "/api/info${(if (key != "") "?accessKey=${encode(key)}" else "")}"
            val response = rest.get(url).one()
            if (response.string("state") == "success") {
                val info = response.getJsonObject("result").toModel<NodeInfo>()
                if (info.name != null) {
                    appController.connectedNode = info
                    return info
                }
            } else {
                return null
            }
        } catch (ex: Exception) {
            print(ex)
        }
        appController.disableControls()
        return null
    }

    fun scan(a: Int, b: Int, c: Int, d: Int, a1: Int, b1: Int, c1: Int, d1: Int) {
        nodes.clear()
        runAsync {
            scanSize += (Math.abs(a - a1 + 1) * Math.abs(b - b1 + 1) * Math.abs(c - c1 + 1) * Math.abs(d - d1 + 1))
            scanStep = 1 / scanSize
            scanProgress = 0.0
            for (i in a..a1) for (j in b..b1) for (k in c..c1) for (l in d..d1) {
                runAsync {
                    ping(i, j, k, l)
                } ui {
                    scanProgress += scanStep
                    if (it != null) {
                        val (i1, j1, k1, l1) = it
                        val info = tryConnect("$i1.$j1.$k1.$l1:600")
                        if (info != null) {
                            nodes.add(info)
                        }
                    }
                }
            }
        }
    }
}