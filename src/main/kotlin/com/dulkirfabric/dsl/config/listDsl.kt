package com.dulkirfabric.dsl.config

import com.dulkirfabric.dsl.config.deprecated.resetButtonKey
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.gui.entries.MultiElementListEntry
import me.shedaniel.clothconfig2.gui.entries.NestedListListEntry
import net.minecraft.text.Text
import java.util.*
import kotlin.reflect.KMutableProperty0

@ConfigDsl
class ListScope<T> : ConfigEntryScope<List<T>>() {

    override lateinit var name: Text
    override lateinit var property: KMutableProperty0<List<T>>
    override var default: List<T> = listOf()

    var expandByDefault = false
    var deletableElements = true
    var insertInFront = false

    lateinit var entryName: Text
    lateinit var entry: (T) -> List<AbstractConfigListEntry<*>>

    @Suppress("UnstableApiUsage")
    override fun build(): AbstractConfigListEntry<List<T>> {
        return NestedListListEntry<T, MultiElementListEntry<T>>(
            name,
            property.get(),
            expandByDefault,
            { tooltip?.let { Optional.ofNullable(it) } },
            { property.set(it) },
            { default },
            resetButtonKey,
            deletableElements,
            insertInFront,
            { value, _ ->
                MultiElementListEntry<T>(entryName, value, entry(value), expandByDefault)
            }
        )
    }
}

@Suppress("UNCHECKED_CAST")
@ConfigDsl
fun <T> CategoryScope.list(scope: ListScope<T>.() -> Unit): CategoryScope {
    val listScope = ListScope<T>()
    listScope.scope()
    add(listScope.build() as AbstractConfigListEntry<Any>)
    return this
}