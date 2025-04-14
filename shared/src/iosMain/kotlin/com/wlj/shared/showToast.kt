package com.wlj.shared

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSinceNow
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertAction
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.darwin.DISPATCH_TIME_NOW
import platform.darwin.NSObject
import platform.darwin.dispatch_after
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_time
import platform.darwin.dispatch_time_t

fun showToast(message: String, duration: Long = 2000) {
    val rootViewController = getRootViewController()
    rootViewController?.let { viewController ->
        val alertController = UIAlertController.alertControllerWithTitle(
            title = null,
            message = message,
            preferredStyle = 0
        )
        val okAction = UIAlertAction.actionWithTitle(
            title = "OK",
            style = 0,
            handler = null
        )
        alertController.addAction(okAction)
        viewController.presentViewController(
            alertController,
            animated = true,
            completion = null
        )
        // 使用 DispatchQueue 实现延迟关闭
        val delayTime = duration * 1_000_000 // 将毫秒转换为纳秒
        val dispatchTime = dispatch_time(DISPATCH_TIME_NOW, delayTime)
        dispatch_after(dispatchTime, queue = dispatch_get_main_queue()) {
            alertController.dismissViewControllerAnimated(true, completion = null)
        }
    }
}

private fun getRootViewController(): UIViewController? {
    val keyWindow = UIApplication.sharedApplication.keyWindow
    return keyWindow?.rootViewController
}
    