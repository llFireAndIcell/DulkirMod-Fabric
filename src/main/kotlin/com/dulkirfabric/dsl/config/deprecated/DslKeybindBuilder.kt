package com.dulkirfabric.dsl.config.deprecated

import com.dulkirfabric.dsl.literal
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.Modifier
import me.shedaniel.clothconfig2.api.ModifierKeyCode
import me.shedaniel.clothconfig2.gui.entries.KeyCodeEntry
import me.shedaniel.clothconfig2.impl.builders.KeyCodeBuilder
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text
import kotlin.reflect.KMutableProperty0

class DslKeybindBuilder(var name: Text) {

    var default: InputUtil.Key = InputUtil.UNKNOWN_KEY
    lateinit var property: KMutableProperty0<InputUtil.Key>
    var tooltip: Text? = null

    fun build(): KeyCodeEntry =
        KeyCodeBuilder(resetButtonKey, name, ModifierKeyCode.of(property.get(), Modifier.none())).run {
            setDefaultValue(default)
            setKeySaveConsumer { property.set(it) }
            setTooltip(tooltip)
            build()
        }
}

inline fun ConfigCategory.keybind(
    name: Text,
    block: DslKeybindBuilder.() -> Unit
): KeyCodeEntry = DslKeybindBuilder(name).run {
    block()
    build()
}.also { this.addEntry(it) }

fun ConfigCategory.keybind(
    name: String,
    block: DslKeybindBuilder.() -> Unit
) = keybind(name.literal(), block)