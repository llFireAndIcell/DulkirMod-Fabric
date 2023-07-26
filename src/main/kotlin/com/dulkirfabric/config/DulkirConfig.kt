/**
 *   This Source Code Form is subject to the terms of the Mozilla Public
 *   License, v. 2.0. If a copy of the MPL was not distributed with this
 *   file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * If it is not possible or desirable to put the notice in a particular
 * file, then You may include the notice in a location (such as a LICENSE
 * file in a relevant directory) where a recipient would be likely to look
 * for such a notice.
 *
 * You may add additional accurate notices of copyright ownership.
 */
@file:UseSerializers(com.dulkirfabric.config.serializations.KeySerializer::class)
package com.dulkirfabric.config

import com.dulkirfabric.DulkirModFabric
import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.dsl.*
import com.dulkirfabric.util.render.AnimationPreset
import com.dulkirfabric.util.render.HudElement
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import moe.nea.jarvis.api.Point
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.InputUtil
import net.minecraft.client.util.InputUtil.UNKNOWN_KEY
import net.minecraft.text.LiteralTextContent
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import java.io.File

class DulkirConfig {

    private val buttonText: Text =
        MutableText.of(LiteralTextContent("Dulkir")).formatted(Formatting.BOLD, Formatting.YELLOW)
    var screen: Screen

    init {
        configBuilder {
            defaultBackgroundTexture = Identifier("minecraft:textures/block/oak_planks.png")
            setGlobalized(true)
            setGlobalizedExpanded(false)
            parentScreen = mc.currentScreen
            setSavingRunnable(::saveConfig)

            ConfigHelper.entryBuilder = entryBuilder()

            category(Text.literal("General")) {
                makeToggle(
                    text = Text.literal("Inventory Scale Toggle"),
                    property = configOptions::invScaleBool,
                    tooltip = Text.literal("This is a tooltip")
                )
                makeFloat(
                    text = Text.literal("Inventory Scale"),
                    property = configOptions::inventoryScale,
                    tooltip = Text.literal("Size of GUI whenever you're in an inventory screen")
                )
                makeFloat(
                    text = Text.literal("Tooltip Scale"),
                    property = configOptions::tooltipScale,
                    tooltip = Text.literal("Default Value for Scaling a particular tooltip without scroll input")
                )
                makeToggle(
                    text = Text.literal("Ignore Reverse Third Person"),
                    property = configOptions::ignoreReverseThirdPerson
                )
                makeToggle(
                    text = Text.literal("Disable Status Effect Rendering"),
                    property = configOptions::statusEffectHidden
                )
                makeToggle(
                    text = Text.literal("Custom Block outlines"),
                    property = configOptions::customBlockOutlines
                )
                makeIntSlider(
                    text = Text.literal("Line Thickness"),
                    property = configOptions::blockOutlineThickness,
                    min = 1,
                    max = 5
                )
                makeColor(
                    text = Text.literal("Outline Color"),
                    property = configOptions::blockOutlineColor
                )
                makeToggle(
                    text = Text.literal("Abiphone DND"),
                    property = configOptions::abiPhoneDND
                )
                makeToggle(
                    text = Text.literal("Inactive Effigy Waypoints"),
                    property = configOptions::inactiveEffigyDisplay
                )
                makeToggle(
                    text = Text.literal("Disable Explosion Particles"),
                    property = configOptions::disableExplosionParticles
                )
                makeToggle(
                    text = Text.literal("Durability-Based Cooldown Display"),
                    property = configOptions::duraCooldown
                )
                makeToggle(
                    text = Text.literal("Hide Armor Overlay in Skyblock"),
                    property = configOptions::hideArmorOverlay
                )
                makeToggle(
                    text = Text.literal("Hide Hunger Overlay in Skyblock"),
                    property = configOptions::hideHungerOverlay
                )
                makeIntSlider(
                    text = Text.literal("Anti Downtime Alarm"),
                    property = configOptions::alarmTimeout,
                    min = 0,
                    max = 1000,
                    tooltip = Text.literal("Set to 0 to disable. (Time in seconds)")
                )
                makeToggle(
                    text = Text.literal("Arachne Keeper Waypoints"),
                    property = configOptions::arachneKeeperWaypoints
                )
                makeToggle(
                    text = Text.literal("Arachne Boss Spawn Timer"),
                    property = configOptions::arachneSpawnTimer
                )
                makeToggle(
                    text = Text.literal("Convert Action Bar to HUD elements"),
                    property = configOptions::hudifyActionBar,
                    tooltip = Text.literal("This converts Mana/Health/Def/Stacks as HUD elements")
                )
                makeToggle(
                    text = Text.literal("Include EHP in def HUD element"),
                    property = configOptions::showEHP,
                    tooltip = Text.literal("Must have Action Bar HUD elements Enabled")
                )
                makeToggle(
                    text = Text.literal("Hide Held Item Tooltips"),
                    property = configOptions::hideHeldItemTooltip,
                    tooltip = Text.literal("This is for the pesky overlay that pops up on switching items")
                )
            }

            category(Text.literal("Shortcuts")) {
                makeKeybind(
                    text = Text.literal("Dynamic Key"),
                    property = configOptions::dynamicKey
                )
                makeConfigList(
                    name = Text.literal("Macros"),
                    property = configOptions::macrosList,
                    newT = { Macro(UNKNOWN_KEY, "") },
                    elementName = Text.literal("Macro"),
                    render = { value ->
                        listOf(
                            makeString(Text.literal("Command"), value::command),
                            makeKeybind(Text.literal("KeyBinding"), value::keyBinding)
                        )
                    }
                )
            }

            category(Text.literal("Aliases")) {
                makeConfigList(
                    Text.literal("Aliases (do not include '/')"),
                    configOptions::aliasList,
                    { Alias("", "") },
                    Text.literal("Alias"),
                    { value ->
                        listOf(
                            makeString(Text.literal("Command"), value::command),
                            makeString(Text.literal("Alias"), value::alias)
                        )
                    }
                )
            }

            category(Text.literal("Animations")) {
                makeIntSlider(
                    text = Text.literal("posX"),
                    property = configOptions.animationPreset::posX,
                    min = -150,
                    max = 150
                )
                makeIntSlider(
                    text = Text.literal("posY"),
                    property = configOptions.animationPreset::posY,
                    min = -150,
                    max = 150
                )
                makeIntSlider(
                    text = Text.literal("posZ"),
                    property = configOptions.animationPreset::posZ,
                    min = -150, max = 50
                )
                makeIntSlider(
                    text = Text.literal("rotationX"),
                    property = configOptions.animationPreset::rotX,
                    min = -180,
                    max = 180
                )
                makeIntSlider(
                    text = Text.literal("rotationY"),
                    property = configOptions.animationPreset::rotY,
                    min = -180,
                    max = 180
                )
                makeIntSlider(
                    text = Text.literal("rotationZ"),
                    property = configOptions.animationPreset::rotZ,
                    min = -180,
                    max = 180
                )
                makeFloat(
                    text = Text.literal("Held Item Scale"),
                    property = configOptions.animationPreset::scale,
                    tooltip = Text.literal("Recommended range of .1 - 2")
                )
                makeIntSlider(
                    text = Text.literal("Swing Speed"),
                    property = configOptions.animationPreset::swingDuration,
                    min = 2,
                    max = 20
                )
                makeToggle(
                    Text.literal("Cancel Re-Equip Animation"),
                    configOptions.animationPreset::cancelReEquip
                )
            }

            category(Text.literal("Bridge Features")) {
                makeToggle(
                    text = Text.literal("Format Bridge Messages"),
                    property = configOptions::bridgeFormatter
                )
                makeString(
                    text = Text.literal("Bridge Bot IGN"),
                    property = configOptions::bridgeBotName
                )
                makeColor(
                    text = Text.literal("Bridge User Color"),
                    property = configOptions::bridgeNameColor
                ) { setDefaultValue(Formatting.GOLD.colorValue!!) }
            }

            transparentBackground()
        }.build().let { screen = it }
    }

    @Serializable
    data class ConfigOptions(
        var invScaleBool: Boolean = false,
        var inventoryScale: Float = 1f,
        var macrosList: List<Macro> = listOf(Macro(UNKNOWN_KEY, "")),
        var aliasList: List<Alias> = listOf(Alias("", "")),
        var ignoreReverseThirdPerson: Boolean = false,
        var dynamicKey: InputUtil.Key = UNKNOWN_KEY,
        var customBlockOutlines: Boolean = false,
        var blockOutlineThickness: Int = 3,
        var blockOutlineColor: Int = 0xFFFFFF,
        var abiPhoneDND: Boolean = false,
        var abiPhoneCallerID: Boolean = false,
        var tooltipScale: Float = 1f,
        var statusEffectHidden: Boolean = false,
        var inactiveEffigyDisplay: Boolean = false,
        var disableExplosionParticles: Boolean = false,
        var hideArmorOverlay: Boolean = false,
        var hideHungerOverlay: Boolean = false,
        var animationPreset: AnimationPreset = AnimationPreset(),
        var duraCooldown: Boolean = false,
        var alarmTimeout: Int = 300,
        var arachneKeeperWaypoints: Boolean = false,
        var arachneSpawnTimer: Boolean = false,
        var bridgeFormatter: Boolean = false,
        var bridgeBotName: String = "Dilkur",
        var bridgeNameColor: Int = Formatting.GOLD.colorValue!!,
        val positions: MutableMap<String, HudElement.Positioning> = mutableMapOf(),
        var hudifyActionBar: Boolean = true,
        var showEHP: Boolean = false,
        var hideHeldItemTooltip: Boolean = false
    )

    @Serializable
    data class Macro(
        var keyBinding: InputUtil.Key,
        var command: String,
    )

    @Serializable
    data class Alias(
        var alias: String,
        var command: String
    )

    /**
     * Object for storing all the actual config values that will be used in doing useful stuff with the config
     */
    companion object ConfigVars {

        var configOptions = ConfigOptions()

        val huds = mutableListOf<Pair<HudElement, Point>>()

        fun hudElement(
            id: String, label: Text, width: Int, height: Int,
            defaultPosition: Point
        ): HudElement {
            val element = HudElement(
                configOptions.positions.getOrPut(
                    id
                ) { HudElement.Positioning(defaultPosition.x(), defaultPosition.y(), 1F) },
                id,
                label, width, height,
            )
            huds.add(Pair(element, defaultPosition))
            return element
        }

        private fun saveConfig() {
            val json = Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                encodeDefaults = true
            }

            val configDirectory = File(mc.runDirectory, "config")
            if (!configDirectory.exists()) {
                configDirectory.mkdir()
            }
            val configFile = File(configDirectory, "dulkirConfig.json")
            configFile.writeText(json.encodeToString(configOptions))
        }

        fun loadConfig() {
            val configDir = File(mc.runDirectory, "config")
            if (!configDir.exists()) return
            val configFile = File(configDir, "dulkirConfig.json")
            if (configFile.exists()) {
                val json = Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                }
                configOptions = json.decodeFromString<ConfigOptions>(configFile.readText())
            }
            huds.forEach { (element, defaultPosition) ->
                element.positioning = configOptions.positions.getOrPut(element.key) {
                    HudElement.Positioning(defaultPosition.x(), defaultPosition.y(), 1F)
                }
            }
        }
    }
}