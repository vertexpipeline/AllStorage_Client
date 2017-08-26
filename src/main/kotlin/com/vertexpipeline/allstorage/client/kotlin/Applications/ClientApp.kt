package com.vertexpipeline.allstorage.client.kotlin.Applications

import com.vertexpipeline.allstorage.client.kotlin.Stylesheets.GlobalStylesheet
import com.vertexpipeline.allstorage.client.kotlin.Frames.App.AppView
import tornadofx.*

class ClientApp : App(AppView::class, GlobalStylesheet::class){
    init {
        reloadStylesheetsOnFocus()
    }
}