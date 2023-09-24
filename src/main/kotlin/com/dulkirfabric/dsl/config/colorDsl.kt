package com.dulkirfabric.dsl.config

import com.dulkirfabric.dsl.config.deprecated.resetButtonKey
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.gui.entries.ColorEntry
import net.minecraft.text.Text
import java.awt.Color
import java.util.Optional
import kotlin.reflect.KMutableProperty0

@ConfigDsl
class ColorScope : ConfigEntryScope<Int>() {

    override lateinit var name: Text
    override lateinit var property: KMutableProperty0<Int>
    override var default: Int = Color.WHITE.rgb

    override fun build(): AbstractConfigListEntry<Int> {
        return ColorEntry(
            name,
            property.get(),
            resetButtonKey,
            { default },
            { property.set(it) },
            { Optional.ofNullable(arrayOf(tooltip)) },
            requiresRestart
        )
    }
}

@Suppress("UNCHECKED_CAST")
@ConfigDsl
fun CategoryScope.color(scope: ColorScope.() -> Unit): CategoryScope {
    val colorScope = ColorScope()
    colorScope.scope()
    add(colorScope.build() as AbstractConfigListEntry<Any>)
    return this
}