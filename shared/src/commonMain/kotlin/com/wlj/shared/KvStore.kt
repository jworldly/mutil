package com.wlj.shared

import com.russhwolf.settings.coroutines.FlowSettings

/**
 * @Author: wlj
 * @Date: 2025/4/21
 * @Description: 用settings库实现k-v存储
 */

internal const val dataStoreFileName = "dice.preferences_pb"

// Common
expect val settings: FlowSettings
