package com.vertexpipeline.allstorage.client.kotlin.app

import com.vertexpipeline.allstorage.client.kotlin.stylesheet.AppStylesheet
import com.vertexpipeline.allstorage.client.kotlin.view.AppView
import tornadofx.*

class ClientApp : App(AppView::class, AppStylesheet::class){
    init {
        reloadStylesheetsOnFocus()
    }
}