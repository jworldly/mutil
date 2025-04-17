package com.wlj.shared

import io.ktor.client.engine.HttpClientEngineConfig
import kotlinx.cinterop.ObjCMethod
import platform.Foundation.NSLog
import platform.Foundation.NSString
import platform.Foundation.NSUUID
import platform.Foundation.performSelectorOnMainThread
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIView
import platform.UIKit.UIWindow
import platform.darwin.NSObject
import kotlin.experimental.ExperimentalObjCName


class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val randomUUID: String
        get() = NSUUID().UUIDString()

    override fun log(str: String) {
        NSLog(str)
    }

    override fun toast(str: String) {
        log(str)
        showToast(str)
    }

    override fun configEngine(config: HttpClientEngineConfig) {
    }
}

// 新增Toast Helper类
@OptIn(ExperimentalObjCName::class)
@ObjCName("ToastHelper")
class ToastHelper : NSObject() {

    fun showToast(message: NSString) {
//        val window = UIApplication.sharedApplication.keyWindow ?: return
//        val toastView = createToastView(message as String, window)
//
//        window.addSubview(toastView)

    }

}


actual fun getPlatform(): Platform = IOSPlatform()