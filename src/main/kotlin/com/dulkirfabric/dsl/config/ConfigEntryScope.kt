package com.dulkirfabric.dsl.config

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import net.minecraft.text.Text
import kotlin.reflect.KMutableProperty0

abstract class ConfigEntryScope<T> {

    abstract var name: Text
    abstract var property: KMutableProperty0<T>
    abstract var default: T
    open var tooltip: Array<Text>? = null
    open var requiresRestart: Boolean = false

    abstract fun build(): AbstractConfigListEntry<T>
}