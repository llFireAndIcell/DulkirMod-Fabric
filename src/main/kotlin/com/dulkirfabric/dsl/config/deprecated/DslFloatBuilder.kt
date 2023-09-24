package com.dulkirfabric.dsl.config.deprecated

import com.dulkirfabric.dsl.literal
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.gui.entries.FloatListEntry
import me.shedaniel.clothconfig2.impl.builders.FloatFieldBuilder
import net.minecraft.text.Text
import kotlin.reflect.KMutableProperty0

class DslFloatBuilder(name: Text) : FloatFieldBuilder(resetButtonKey, name, 0f) {

    var default: Float = 0f
    var property: KMutableProperty0<Float>? = null
    var tooltip: Text? = null

    override fun build(): FloatListEntry {
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

inline fun ConfigCategory.float(
    name: Text,
    block: DslFloatBuilder.() -> Unit
): FloatListEntry = DslFloatBuilder(name).run {
    block()
    build()
}.also { this.addEntry(it) }

fun ConfigCategory.float(
    name: String,
    block: DslFloatBuilder.() -> Unit
) = float(name.literal(), block)