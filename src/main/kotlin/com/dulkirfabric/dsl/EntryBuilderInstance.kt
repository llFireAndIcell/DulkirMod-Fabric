package com.dulkirfabric.dsl

import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.clothconfig2.api.ConfigBuilder

/**
 * An object to store the [ConfigBuilder]'s entry builder instance. This is because:
 * - Only one entry builder is needed
 * - Several files need access to it
 * - The files don't necessarily have access to the [ConfigBuilder] itself, so the value has to be stored here
 */
object EntryBuilderInstance {

    private var entryBuilder: ConfigEntryBuilder? = null

    fun get() = entryBuilder ?: throw IllegalAccessException("Must initialize EntryBuilderInstance before accessing it")
    fun set(entryBuilder: ConfigEntryBuilder) { this.entryBuilder = entryBuilder }
}