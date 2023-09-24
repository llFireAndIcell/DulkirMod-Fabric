package com.dulkirfabric.dsl.config

import com.dulkirfabric.dsl.literal
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry
import net.minecraft.text.Text

@ConfigDsl
class CategoryScope(val name: Text) {

    internal val entries: MutableList<AbstractConfigListEntry<Any>> = mutableListOf()

    fun add(entry: AbstractConfigListEntry<Any>) {
        entries.add(entry)
    }
}

@ConfigDsl
fun ConfigScope.category(
    name: Text,
    scope: @ConfigDsl CategoryScope.() -> Unit
): ConfigScope {
    val categoryScope = CategoryScope(name)
    categoryScope.scope()
    this.addCategory(categoryScope)
    return this
}

@ConfigDsl
fun ConfigScope.category(
    name: String,
    scope: @ConfigDsl CategoryScope.() -> Unit
): ConfigScope =
    category(name.literal, scope)