package com.dulkirfabric.dsl.config

import com.dulkirfabric.dsl.config.deprecated.resetButtonKey
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.api.Modifier
import me.shedaniel.clothconfig2.api.ModifierKeyCode
import me.shedaniel.clothconfig2.gui.entries.KeyCodeEntry
import me.shedaniel.clothconfig2.gui.entries.StringListEntry
import net.minecraft.client.util.InputUtil.UNKNOWN_KEY
import net.minecraft.text.Text
import java.util.*
import kotlin.reflect.KMutableProperty0

val EMPTY_KEYBIND = ModifierKeyCode.of(UNKNOWN_KEY, Modifier.none())

@ConfigDsl
class KeybindScope : ConfigEntryScope<ModifierKeyCode>() {

    override lateinit var name: Text
    override lateinit var property: KMutableProperty0<ModifierKeyCode>
    override var default: ModifierKeyCode = ModifierKeyCode.of(UNKNOWN_KEY, Modifier.none())

    @Suppress("DEPRECATION", "UnstableApiUsage")
    override fun build(): AbstractConfigListEntry<ModifierKeyCode> {
        return KeyCodeEntry(
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
fun CategoryScope.keybind(scope: KeybindScope.() -> Unit): CategoryScope {
    val keybindScope = KeybindScope()
    keybindScope.scope()
    add(keybindScope.build() as AbstractConfigListEntry<Any>)
    return this
}

/**
 * Used to build a string config entry without immediately adding it to a config
 * @return The [StringListEntry] that is built
 */
fun keybindEntry(scope: KeybindScope.() -> Unit): AbstractConfigListEntry<ModifierKeyCode> {
    val keybindScope = KeybindScope()
    keybindScope.scope()
    return keybindScope.build()
}