package com.vertexpipeline.allstorage.client.kotlin.controller

import javafx.beans.property.SimpleBooleanProperty
import tornadofx.*

class AppController : Controller(){
    val isControlDisabledProperty = SimpleBooleanProperty(true)
    var isMenuDisabled by isControlDisabledProperty
}