package com.dulkirfabric.dsl.config

import com.dulkirfabric.dsl.config.deprecated.resetButtonKey
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.gui.entries.StringListEntry
import net.minecraft.text.Text
import java.util.*
import kotlin.reflect.KMutableProperty0

@ConfigDsl
class StringScope : ConfigEntryScope<String>() {

    override lateinit var name: Text
    override lateinit var property: KMutableProperty0<String>
    override var default: String = ""

    @Suppress("DEPRECATION", "UnstableApiUsage")
    override fun build(): AbstractConfigListEntry<String> {
        return StringListEntry(
            name,
            property.get(),
            resetButtonKey,
            { default },
            { property.set(it) },
            { tooltip?.let { Optional.of(it) } },
            requiresRestart
        )
    }
}

@Suppress("UNCHECKED_CAST")
@ConfigDsl
fun CategoryScope.string(scope: StringScope.() -> Unit): CategoryScope {
    val stringScope = StringScope()
    stringScope.scope()
    add(stringScope.build() as AbstractConfigListEntry<Any>)
    return this
}

/**
 * Used to build a string config entry without immediately adding it to a config
 * @return The [StringListEntry] that is built
 */
fun stringEntry(scope: StringScope.() -> Unit): AbstractConfigListEntry<String> {
    val stringScope = StringScope()
    stringScope.scope()
    return stringScope.build()
}