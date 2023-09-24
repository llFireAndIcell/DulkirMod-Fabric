@file:Suppress("FunctionName")

package com.dulkirfabric.dsl.config

import com.dulkirfabric.dsl.literal
import com.dulkirfabric.dsl.translatable
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.minecraft.util.Identifier

@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
@DslMarker
annotation class ConfigDsl

@ConfigDsl
class ConfigScope {

    // these must be initialized before calling build
    lateinit var saveRunnable: () -> Unit
    lateinit var parent: Screen

    var title: Text = "text.cloth-config.config".translatable
    var fallbackCategory: String? = null
    var globalized = false
    var globalizedExpanded = true
    var editable = true
    var tabsSmoothScroll = true
    var listSmoothScroll = true
    var doesConfirmSave = true
    var transparentBackground = true
    var defaultBackground: Identifier = Screen.OPTIONS_BACKGROUND_TEXTURE
    var alwaysShowTabs = true

    private val categoryScopes: MutableList<CategoryScope> = mutableListOf()

    internal fun addCategory(categoryScope: CategoryScope) {
        categoryScopes.add(categoryScope)
    }

    /**
     * Builds the config and returns it as a [Screen]
     * The following properties must be initialized before calling [build]:
     * - [saveRunnable]
     * - [parent]
     * - [fallbackCategory]
     */
    @Suppress("UnstableApiUsage")
    fun build(): Screen {
        val builder = ConfigBuilder.create().also {
            it.parentScreen = parent
            it.setSavingRunnable(saveRunnable)
            it.setTitle(title)
            it.setFallbackCategory(it.getOrCreateCategory(
                if (fallbackCategory == null) categoryScopes[0].name.literal()
                else fallbackCategory.literal()
            ))
            it.setGlobalized(globalized)
            it.setGlobalizedExpanded(globalizedExpanded)
            it.isEditable = editable
            it.setShouldTabsSmoothScroll(tabsSmoothScroll)
            it.setShouldListSmoothScroll(listSmoothScroll)
            it.setDoesConfirmSave(doesConfirmSave)
            it.setTransparentBackground(transparentBackground)
            it.defaultBackgroundTexture = defaultBackground
            it.setAlwaysShowTabs(alwaysShowTabs)
        }

        for (categoryScope in categoryScopes) {
            builder.getOrCreateCategory(categoryScope.name.literal).apply {
                for (entry in categoryScope.entries) addEntry(entry)
            }
        }

        return builder.build()
    }
}

@ConfigDsl
fun config(scope: @ConfigDsl ConfigScope.() -> Unit): Screen {
    val configScope = ConfigScope()
    configScope.scope()
    return configScope.build()
}
