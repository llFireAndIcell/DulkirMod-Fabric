package com.dulkirfabric.dsl.config.deprecated

import com.dulkirfabric.dsl.literal
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder
import net.minecraft.text.Text
import kotlin.reflect.KMutableProperty0

class DslToggleBuilder(name: Text) : BooleanToggleBuilder(resetButtonKey, name, false) {

    var default: Boolean = false
    var property: KMutableProperty0<Boolean>? = null
    var tooltip: Text? = null

    override fun build(): BooleanListEntry {
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

inline fun ConfigCategory.toggle(
    name: Text,
    block: DslToggleBuilder.() -> Unit
): BooleanListEntry = DslToggleBuilder(name).run {
    block()
    build()
}.also { this.addEntry(it) }

fun ConfigCategory.toggle(
    name: String,
    block: DslToggleBuilder.() -> Unit
) = toggle(name.literal(), block)