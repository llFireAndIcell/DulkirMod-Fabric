package com.dulkirfabric.dsl.config

import com.dulkirfabric.dsl.config.deprecated.resetButtonKey
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.gui.entries.FloatListEntry
import net.minecraft.text.Text
import java.util.*
import kotlin.reflect.KMutableProperty0

@ConfigDsl
class FloatScope : ConfigEntryScope<Float>() {

    override lateinit var name: Text
    override lateinit var property: KMutableProperty0<Float>
    override var default: Float = 0f
    var min: Float? = null
    var max: Float? = null

    @Suppress("DEPRECATION", "UnstableApiUsage")
    override fun build(): FloatListEntry {
        return FloatListEntry(
            name,
            property.get(),
            resetButtonKey,
            { default },
            { property.set(it) },
            { tooltip?.let { Optional.of(it) } },
            requiresRestart
        ).apply {
            min?.let { setMinimum(it) }
            max?.let { setMaximum(it) }
        }
    }
}

@Suppress("UNCHECKED_CAST")
@ConfigDsl
fun CategoryScope.float(scope: FloatScope.() -> Unit): CategoryScope {
    val floatScope = FloatScope()
    floatScope.scope()
    add(floatScope.build() as AbstractConfigListEntry<Any>)
    return this
}