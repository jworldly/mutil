package com.wlj.shared

import java.util.UUID
import javax.swing.JOptionPane

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val randomUUID: String
        get() = UUID.randomUUID().toString()

    override fun log(str: String) {
         System.out.println(str)
    }

    override fun toast(str: String) {
        JOptionPane.showMessageDialog(null, str, "Notification", JOptionPane.INFORMATION_MESSAGE)
    }
}

actual fun getPlatform(): Platform = JVMPlatform()