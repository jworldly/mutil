package com.wlj.shared

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.datastore.DataStoreSettings
import okio.Path.Companion.toPath


fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

actual val settings: FlowSettings = DataStoreSettings(
    datastore = createDataStore({
        // 示例：将文件保存在用户主目录下
        val appName = System.getProperty("app.name", "defaultAppName")
        tools.log(appName)
        System.getProperty("user.home") + "/${Config.appName}/$dataStoreFileName"
    })
)

fun ss(){

}