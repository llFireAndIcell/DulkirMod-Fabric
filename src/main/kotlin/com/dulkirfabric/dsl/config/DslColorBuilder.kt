package com.dulkirfabric.dsl.config

import com.dulkirfabric.dsl.literal
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.gui.entries.ColorEntry
import me.shedaniel.clothconfig2.impl.builders.ColorFieldBuilder
import net.minecraft.text.Text
import java.awt.Color
import kotlin.reflect.KMutableProperty0

class DslColorBuilder(name: Text) : ColorFieldBuilder(resetButtonKey, name, Color.WHITE.rgb) {

    var default: Int = Color.WHITE.rgb
    var property: KMutableProperty0<Int>? = null
    var tooltip: Text? = null

    override fun build(): ColorEntry {
        setDefaultValue(default)

        val propertyCached = property
        if (propertyCached != null) {
            value = propertyCached.get()
            setSaveConsumer { propertyCached.set(it) }
        }
        if (tooltip != null) setTooltip(tooltip)
        return super.build()
    }
}

inline fun ConfigCategory.color(
    name: Text,
    block: DslColorBuilder.() -> Unit
): ColorEntry = DslColorBuilder(name).run {
    block()
    build()
}.also { this.addEntry(it) }

fun ConfigCategory.color(
    name: String,
    block: DslColorBuilder.() -> Unit
) = color(name.literal(), block)