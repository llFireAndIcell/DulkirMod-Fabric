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

import com.dulkirfabric.DulkirModFabric.mc
import com.dulkirfabric.dsl.*
import com.dulkirfabric.dsl.config.*
import com.dulkirfabric.util.render.AnimationPreset
import com.dulkirfabric.util.render.HudElement
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import moe.nea.jarvis.api.Point
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.InputUtil.*
import net.minecraft.text.*
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import java.io.File

class DulkirConfig {

    @Suppress("unused")
    private val buttonText: Text =
        MutableText.of(LiteralTextContent("Dulkir")).formatted(Formatting.BOLD, Formatting.YELLOW)
    var screen: Screen

    init {
        configBuilder {
            // general config settings
            defaultBackgroundTexture = Identifier("minecraft:textures/block/oak_planks.png")
            setGlobalized(true)
            setGlobalizedExpanded(false)
            parentScreen = mc.currentScreen
            setSavingRunnable(::saveConfig)
            transparentBackground()

            // making categories and adding config options
            category("General") {
                toggle("Inventory Scale Toggle") {
                    property = configOptions::invScaleBool
                    tooltip = "This is a tooltip".literal()
                }
                float("Inventory Scale") {
                    property = configOptions::inventoryScale
                    tooltip = "Size of GUI whenever you're in an inventory screen".literal()
                }
                float("Tooltip Scale") {
                    property = configOptions::tooltipScale
                    tooltip = "Default Value for Scaling a particular tooltip without scroll input".literal()
                }
                toggle("Ignore Reverse Third Person") { property = configOptions::ignoreReverseThirdPerson }
                toggle("Disable Status Effect Rendering") { property = configOptions::statusEffectHidden }
                toggle("Custom Block outlines") { property = configOptions::customBlockOutlines }
                intSlider("Line Thickness") {
                    property = configOptions::blockOutlineThickness
                    range = 1..5
                }
                color("Outline Color") { property = configOptions::blockOutlineColor }
                toggle("Abiphone DND") { property = configOptions::abiPhoneDND }
                toggle("Abiphone Caller ID") { property = configOptions::abiPhoneCallerID }
                toggle("Inactive Effigy Waypoints") { property = configOptions::inactiveEffigyDisplay }
                toggle("Disable Explosion Particles") { property = configOptions::disableExplosionParticles }
                toggle("Durability-Based Cooldown Display") { property = configOptions::duraCooldown }
                toggle("Hide Armor Overlay in Skyblock") { property = configOptions::hideArmorOverlay }
                toggle("Hide Hunger Overlay in Skyblock") { property = configOptions::hideHungerOverlay }
                toggle("Hide Fire Overlay") { property = configOptions::hideFireOverlay }
                toggle("Hide Lightning (SkyBlock only)") { property = configOptions::hideLightning }
                toggle("Hide Non-Crits") { property = configOptions::hideNonCrits }
                toggle("Hide Crits") { property = configOptions::hideCrits }
                toggle("Truncate Crits") { property = configOptions::truncateDamage }
                intSlider("Anti Downtime Alarm") {
                    property = configOptions::alarmTimeout
                    tooltip = "Set to 0 to disable. (Time in seconds)".literal()
                    range = 0..1000
                }
                toggle("Arachne Keeper Waypoints") { property = configOptions::arachneKeeperWaypoints }
                toggle("Arachne Boss Spawn Timer") { property = configOptions::arachneSpawnTimer }
                toggle("Convert Action Bar to HUD elements") {
                    property = configOptions::hudifyActionBar
                    tooltip = "This converts Mana/Health/Def/Stacks as HUD elements".literal()
                }
                toggle("Show Speed in HUD") { property = configOptions::speedHud }
                toggle("Include EHP in def HUD element") {
                    property = configOptions::showEHP
                    tooltip = "Must have Action Bar HUD elements Enabled".literal()
                }
                toggle("Hide Held Item Tooltips") {
                    property = configOptions::hideHeldItemTooltip
                    tooltip = "This is for the pesky overlay that pops up on switching items".literal()
                }
                toggle("Etherwarp Preview") {
                    property = configOptions::showEtherwarpPreview
                    tooltip = "Highlights the targeted block when shifting with a aotv".literal()
                }
                color("Etherwarp Preview Color") {
                    property = configOptions::etherwarpPreviewColor
                    default = 0x99FFFFFF.toInt()
                }
                toggle("Broken Hype Notification") { property = configOptions::brokenHypNotif }
            }

            category("Shortcuts") {
                keybind("Dynamic Key") { property = configOptions::dynamicKey }
                toggle("Only Register Shortcuts in Skyblock") {
                    property = configOptions::macrosSkyBlockOnly
                    tooltip = "Useful if you want to use some of these binds elsewhere for non-skyblock specific stuff".literal()
                }
                list<Macro>("Macros") {
                    property = configOptions::macrosList
                    defaultElementSupplier = { Macro(UNKNOWN_KEY, "") }
                    elementName = "Macro".literal()
                    elementBuilder = { value ->
                        listOf(
                            makeString("Command", value::command),
                            makeKeybind("Keybind", value::keyBinding)
                        )
                    }
                }
            }

            category("Aliases") {
                list<Alias>("Aliases (do not include '/')") {
                    property = configOptions::aliasList
                    defaultElementSupplier = { Alias("", "") }
                    elementName = "Alias".literal()
                    elementBuilder = { value ->
                        listOf(
                            makeString("Command", value::command),
                            makeString("Alias", value::alias)
                        )
                    }
                }
            }

            category("Animations") {
                intSlider("posX") {
                    property = configOptions.animationPreset::posX
                    range = -150..150
                }
                intSlider("posY") {
                    property = configOptions.animationPreset::posY
                    range = -150..150
                }
                intSlider("posZ") {
                    property = configOptions.animationPreset::posZ
                    range = -150..50
                }
                intSlider("rotationX") {
                    property = configOptions.animationPreset::rotX
                    range = -180..180
                }
                intSlider("rotationY") {
                    property = configOptions.animationPreset::rotY
                    range = -180..180
                }
                intSlider("rotationZ") {
                    property = configOptions.animationPreset::rotZ
                    range = -180..180
                }
                float("Held Item Scale") {
                    property = configOptions.animationPreset::scale
                    tooltip = "Recommended range of .1 - 2".literal()
                }
                intSlider("Swing Speed") {
                    property = configOptions.animationPreset::swingDuration
                    range = 2..20
                }
                toggle("Cancel Re-Equip Animation") { property = configOptions.animationPreset::cancelReEquip }
            }

            category("Bridge Features") {
                toggle("Format Bridge Messages") { property = configOptions::bridgeFormatter }
                string("Bridge Bot IGN") { property = configOptions::bridgeBotName }
                color("Bridge User Color") {
                    property = configOptions::bridgeNameColor
                    default = Formatting.GOLD.colorValue!!
                }
            }

            category("Slayer") {
                toggle("Miniboss Highlight Box") { property = configOptions::boxMinis }
                toggle("Miniboss Announcement Alert") { property = configOptions::announceMinis }
                toggle("Show Kill Time on Slayer Completion") {
                    property = configOptions::slayerKillTime
                    tooltip = "Shows up in chat!".literal()
                }
                toggle("Blaze Slayer Attunement Display") {
                    property = configOptions::attunementDisplay
                    tooltip = "Shows a wireframe in the correct color for the slayer".literal()
                }
                toggle("Disable ALL particles during Blaze slayer boss") { property = configOptions::cleanBlaze }
                toggle("Vampire Steak Display") {
                    property = configOptions::steakDisplay
                    tooltip = "Shows a wireframe on vampire boss when you can 1 tap it".literal()
                }
                toggle("Blood Ichor Highlight") {
                    property = configOptions::ichorHighlight
                    tooltip = "Highlights the T5 mechanic that you line up with the boss.".literal()
                }
            }

            category("Garden") {
                toggle("Show Visitor Info in HUD") { property = configOptions::visitorHud }
                toggle("Show Composter Info in HUD") { property = configOptions::showComposterInfo }
                toggle("Show Title alert when max visitors") { property = configOptions::visitorAlert }
                toggle("Persistent Visitor alert (dependent on previous)") { property = configOptions::persistentVisitorAlert }
                toggle("Show Blocks per second (SPEED)") { property = configOptions::speedBpsHud }
                toggle("Show Pitch/Yaw in HUD") { property = configOptions::pitchYawDisplay }
            }
        }.let { screen = it }
    }

    @Serializable
    data class ConfigOptions(
        var invScaleBool: Boolean = false,
        var inventoryScale: Float = 1f,
        var macrosList: List<Macro> = listOf(Macro(UNKNOWN_KEY, "")),
        var macrosSkyBlockOnly: Boolean = false,
        var aliasList: List<Alias> = listOf(Alias("", "")),
        var ignoreReverseThirdPerson: Boolean = false,
        var dynamicKey: Key = UNKNOWN_KEY,
        var customBlockOutlines: Boolean = false,
        var blockOutlineThickness: Int = 3,
        var blockOutlineColor: Int = 0xFFFFFF,
        var abiPhoneDND: Boolean = false,
        var abiPhoneCallerID: Boolean = false,
        var tooltipScale: Float = 1f,
        var statusEffectHidden: Boolean = false,
        var inactiveEffigyDisplay: Boolean = false,
        var disableExplosionParticles: Boolean = false,
        var hideArmorOverlay: Boolean = true,
        var hideHungerOverlay: Boolean = true,
        var animationPreset: AnimationPreset = AnimationPreset(),
        var duraCooldown: Boolean = false,
        var alarmTimeout: Int = 0,
        var arachneKeeperWaypoints: Boolean = false,
        var arachneSpawnTimer: Boolean = false,
        var bridgeFormatter: Boolean = false,
        var bridgeBotName: String = "Dilkur",
        var bridgeNameColor: Int = Formatting.GOLD.colorValue!!,
        val positions: MutableMap<String, HudElement.Positioning> = mutableMapOf(),
        var hudifyActionBar: Boolean = true,
        var showEHP: Boolean = false,
        var hideHeldItemTooltip: Boolean = false,
        var showEtherwarpPreview: Boolean = true,
        var etherwarpPreviewColor: Int = 0x99FFFFFF.toInt(),
        var announceMinis: Boolean = false,
        var boxMinis: Boolean = false,
        var attunementDisplay: Boolean = false,
        var hideFireOverlay: Boolean = false,
        var hideLightning: Boolean = false,
        var cleanBlaze: Boolean= false,
        var timeSlayerBoss: Boolean = false,
        var hideNonCrits: Boolean = false,
        var truncateDamage: Boolean = false,
        var hideCrits: Boolean = false,
        var visitorHud: Boolean = false,
        var showComposterInfo: Boolean = false,
        var slayerKillTime: Boolean = false,
        var visitorAlert: Boolean = false,
        var persistentVisitorAlert: Boolean = false,
        var brokenHypNotif: Boolean = false,
        var steakDisplay: Boolean = false,
        var ichorHighlight: Boolean = false,
        var speedHud: Boolean = false,
        var speedBpsHud: Boolean = false,
        var pitchYawDisplay: Boolean = false,
//        var farmBreakKey: Key = fromKeyCode(GLFW_KEY_SPACE, GLFW_KEY_SPACE),
//        var defaultBreakKey: Key = fromTranslationKey("key.mouse.left"),
//        var farmSens: Int = 0,
//        var defaultSens: Int = 100,
    )

    @Serializable
    data class Macro(
        var keyBinding: Key,
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

        val huds = mutableListOf<Triple<HudElement, Point, Float>>()

        fun hudElement(
            id: String, label: Text, width: Int, height: Int,
            defaultPosition: Point, scale: Float = 1f
        ): HudElement {
            val element = HudElement(
                configOptions.positions.getOrPut(
                    id
                ) { HudElement.Positioning(defaultPosition.x(), defaultPosition.y(), scale) },
                id,
                label, width, height,
            )
            huds.add(Triple(element, defaultPosition, scale))
            return element
        }

        fun saveConfig() {
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
            huds.forEach { (element, defaultPosition, scale) ->
                element.positioning = configOptions.positions.getOrPut(
                    element.key
                ) { HudElement.Positioning(defaultPosition.x(), defaultPosition.y(), scale) }
            }
        }
    }
}