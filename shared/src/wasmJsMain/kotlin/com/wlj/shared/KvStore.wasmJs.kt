package com.wlj.shared

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.StorageSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.SuspendSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.russhwolf.settings.coroutines.toSuspendSettings
import com.russhwolf.settings.observable.makeObservable

@OptIn(markerClass = [ExperimentalSettingsApi::class])
actual val settings: FlowSettings = StorageSettings().makeObservable().toFlowSettings()
