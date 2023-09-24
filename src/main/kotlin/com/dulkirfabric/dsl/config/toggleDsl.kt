package com.dulkirfabric.dsl.config

import com.dulkirfabric.dsl.config.deprecated.resetButtonKey
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry
import net.minecraft.text.Text
import java.util.*
import kotlin.reflect.KMutableProperty0

@ConfigDsl
class ToggleScope : ConfigEntryScope<Boolean> {

    override lateinit var name: Text
    override lateinit var property: KMutableProperty0<Boolean>
    override var default = false
    override var tooltip: Text? = null
    override var requiresRestart = false
    var yesNoTextSupplier: ((Boolean) -> Text)? = null

    override fun build(): BooleanListEntry = object : BooleanListEntry(
        name,
        property.get(),
        resetButtonKey,
        { default },
        { property.set(it) },
        { Optional.ofNullable(arrayOf(tooltip)) },
        requiresRestart
    ) {
        override fun getYesNoText(bool: Boolean): Text =
            if (yesNoTextSupplier == null) super.getYesNoText(bool)
            else yesNoTextSupplier!!(bool)
    }
}

@Suppress("UNCHECKED_CAST")
@ConfigDsl
fun CategoryScope.toggle(scope: ToggleScope.() -> Unit): CategoryScope {
    val toggleScope = ToggleScope()
    toggleScope.scope()
    add(toggleScope.build() as AbstractConfigListEntry<Any>)
    return this
}