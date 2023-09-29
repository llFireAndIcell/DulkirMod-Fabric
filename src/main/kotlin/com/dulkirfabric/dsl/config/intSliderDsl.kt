package com.dulkirfabric.dsl.config

import com.dulkirfabric.dsl.config.deprecated.resetButtonKey
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry
import net.minecraft.text.Text
import java.util.*
import kotlin.reflect.KMutableProperty0

@ConfigDsl
class IntSliderScope : ConfigEntryScope<Int>() {

    override lateinit var name: Text
    override lateinit var property: KMutableProperty0<Int>
    override var default: Int = 0
    var range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE

    @Suppress("DEPRECATION", "UnstableApiUsage")
    override fun build(): AbstractConfigListEntry<Int> {
        return IntegerSliderEntry(
            name,
            range.first,
            range.last,
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
fun CategoryScope.intSlider(scope: IntSliderScope.() -> Unit): CategoryScope {
    val intSliderScope = IntSliderScope()
    intSliderScope.scope()
    add(intSliderScope.build() as AbstractConfigListEntry<Any>)
    return this
}