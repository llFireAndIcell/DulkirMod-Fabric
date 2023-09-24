package com.dulkirfabric.dsl.config.deprecated

import com.dulkirfabric.dsl.literal
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.gui.entries.MultiElementListEntry
import me.shedaniel.clothconfig2.gui.entries.NestedListListEntry
import net.minecraft.text.Text
import java.util.*
import kotlin.reflect.KMutableProperty0

class DslListBuilder<T>(var name: Text) {

    lateinit var property: KMutableProperty0<List<T>>
    lateinit var defaultElementSupplier: (() -> T)
    lateinit var elementBuilder: (T) -> List<AbstractConfigListEntry<*>>
    var elementName: Text? = null
    var expandByDefault = false
    var deletableElements = true
    var insertInFront = false

    @Suppress("UnstableApiUsage")
    fun build(): NestedListListEntry<T, MultiElementListEntry<T>> {
        val listEntry = NestedListListEntry<T, MultiElementListEntry<T>>(
            name,
            property.get(),
            expandByDefault,
            { Optional.empty() },
            { property.set(it) },
            { mutableListOf() },
            resetButtonKey,
            deletableElements,
            insertInFront,
            { value, _ ->
                val nonnullValue: T = value ?: defaultElementSupplier()
                MultiElementListEntry<T>(elementName, nonnullValue, elementBuilder(nonnullValue), true)
            }
        )

        return listEntry
    }
}

inline fun <T> ConfigCategory.list(
    name: Text,
    block: DslListBuilder<T>.() -> Unit
): NestedListListEntry<T, MultiElementListEntry<T>> = DslListBuilder<T>(name).run {
    block()
    build()
}.also { this.addEntry(it) }

fun <T> ConfigCategory.list(
    name: String,
    block: DslListBuilder<T>.() -> Unit
): NestedListListEntry<T, MultiElementListEntry<T>> = list<T>(name.literal(), block)
