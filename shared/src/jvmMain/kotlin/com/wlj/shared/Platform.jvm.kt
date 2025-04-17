package com.wlj.shared

import io.ktor.client.engine.HttpClientEngineConfig
import java.util.UUID
import javax.swing.JOptionPane

class JVMPlatform: Platform {

    override val name: String = "Java ${System.getProperty("java.version")}"

    override val randomUUID: String
        get() = UUID.randomUUID().toString()

    override fun log(str: String) {
         println(str)
    }

    override fun toast(str: String) {
        JOptionPane.showMessageDialog(null, str, "Notification", JOptionPane.INFORMATION_MESSAGE)
    }

    override fun configEngine(config: HttpClientEngineConfig) {
    }
}

actual fun getPlatform(): Platform = JVMPlatform()