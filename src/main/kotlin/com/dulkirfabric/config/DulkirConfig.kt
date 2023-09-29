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
import com.dulkirfabric.dsl.config.*
import com.dulkirfabric.dsl.literal
import com.dulkirfabric.util.render.AnimationPreset
import com.dulkirfabric.util.render.HudElement
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.shedaniel.clothconfig2.api.Modifier
import me.shedaniel.clothconfig2.api.ModifierKeyCode
import moe.nea.jarvis.api.Point
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.InputUtil.UNKNOWN_KEY
import net.minecraft.text.LiteralTextContent
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import java.io.File

class DulkirConfig {

    @Suppress("unused")
    private val buttonText: Text =
        MutableText.of(LiteralTextContent("Dulkir")).formatted(Formatting.BOLD, Formatting.YELLOW)
    var screen: Screen

    init {
        screen = config {
            defaultBackground = Identifier("minecraft:textures/block/oak_planks.png")
            globalized = true
            globalizedExpanded = false
            parent = mc.currentScreen!!
            saveRunnable = ::saveConfig
            
            category("General") {
                toggle {
                    name = "Inventory Scale Toggle".literal
                    property = configOptions::invScaleBool
                    tooltip = arrayOf("This is a tooltip".literal)
                }
                float {
                    name = "Inventory Scale Toggle".literal
                    property = configOptions::inventoryScale
                    tooltip = arrayOf("Size of GUI whenever you're in an inventory screen".literal)
                }
                float {
                    name = "Tooltip Scale".literal
                    property = configOptions::tooltipScale
                    tooltip = arrayOf("Default Value for Scaling a particular tooltip without scroll input".literal)
                }
                toggle {
                    name = "Ignore Reverse Third Person".literal
                    property = configOptions::ignoreReverseThirdPerson
                }
                toggle {
                    name = "Disable Status Effect Rendering".literal
                    property = configOptions::statusEffectHidden
                }
                toggle {
                    name = "Custom Block outlines".literal
                    property = configOptions::customBlockOutlines
                }
                intSlider {
                    name = "Line Thickness".literal
                    property = configOptions::blockOutlineThickness
                    range = 1..5
                }
                color {
                    name = "Outline Color".literal
                    property = configOptions::blockOutlineColor
                }
                toggle {
                    name = "Abiphone DND".literal
                    property = configOptions::abiPhoneDND
                }
                toggle {
                    name = "Abiphone Caller ID".literal
                    property = configOptions::abiPhoneCallerID
                }
                toggle {
                    name = "Inactive Effigy Waypoints".literal
                    property = configOptions::inactiveEffigyDisplay
                }
                toggle {
                    name = "Disable Explosion Particles".literal
                    property = configOptions::disableExplosionParticles
                }
                toggle {
                    name = "Durability-Based Cooldown Display".literal
                    property = configOptions::duraCooldown
                }
                toggle {
                    name = "Hide Armor Overlay in Skyblock".literal
                    property = configOptions::hideArmorOverlay
                }
                toggle {
                    name = "Hide Hunger Overlay in Skyblock".literal
                    property = configOptions::hideHungerOverlay
                }
                toggle {
                    name = "Hide Fire Overlay".literal
                    property = configOptions::hideFireOverlay
                }
                toggle {
                    name = "Hide Lightning (SkyBlock only)".literal
                    property = configOptions::hideLightning
                }
                toggle {
                    name = "Hide Non-Crits".literal
                    property = configOptions::hideNonCrits
                }
                toggle {
                    name = "Hide Crits".literal
                    property = configOptions::hideCrits
                }
                toggle {
                    name = "Truncate Crits".literal
                    property = configOptions::truncateDamage
                }
                intSlider {
                    name = "Anti Downtime Alarm".literal
                    property = configOptions::alarmTimeout
                    tooltip = arrayOf("Set to 0 to disable. (Time in seconds)".literal)
                    range = 0..1000
                }
                toggle {
                    name = "Arachne Keeper Waypoints".literal
                    property = configOptions::arachneKeeperWaypoints
                }
                toggle {
                    name = "Arachne Boss Spawn Timer".literal
                    property = configOptions::arachneSpawnTimer
                }
                toggle {
                    name = "Convert Action Bar to HUD elements".literal
                    property = configOptions::hudifyActionBar
                    tooltip = arrayOf("This converts Mana/Health/Def/Stacks as HUD elements".literal)
                }
                toggle {
                    name = "Show Speed in HUD".literal
                    property = configOptions::speedHud
                }
                toggle {
                    name = "Include EHP in def HUD element".literal
                    property = configOptions::showEHP
                    tooltip = arrayOf("Must have Action Bar HUD elements Enabled".literal)
                }
                toggle {
                    name = "Hide Held Item Tooltips".literal
                    property = configOptions::hideHeldItemTooltip
                    tooltip = arrayOf("This is for the pesky overlay that pops up on switching items".literal)
                }
                toggle {
                    name = "Etherwarp Preview".literal
                    property = configOptions::showEtherwarpPreview
                    tooltip = arrayOf("Highlights the targeted block when shifting with a aotv".literal)
                }
                color {
                    name = "Etherwarp Preview Color".literal
                    property = configOptions::etherwarpPreviewColor
                    default = 0x99FFFFFF.toInt()
                }
            }

            category("Shortcuts") {
                keybind {
                    name = "Dynamic Key".literal
                    property = configOptions::dynamicKey
                }
                toggle {
                    name = "Only Register Shortcuts in Skyblock".literal
                    property = configOptions::macrosSkyBlockOnly
                    tooltip = arrayOf("Useful if you want to use some of these binds elsewhere for non-skyblock specific stuff".literal)
                }
                list<Macro> {
                    property = configOptions::macrosList
                    default = listOf(Macro(EMPTY_KEYBIND, ""))
                    entry = {
                        listOf(
                            stringEntry {
                                name = "Command".literal
                                property = it::command
                            },
                            keybindEntry {
                                name = "Keybind".literal
                                property = it::keyBinding
                            }
                        )
                    }
                }
            }

            category("Aliases") {
                list<Alias> {
                    name = "Aliases (do not include '/')".literal
                    property = configOptions::aliasList
                    entryName = "Alias".literal
                    entry = {
                        listOf(
                            stringEntry {
                                name = "Command".literal
                                property = it::command
                            },
                            stringEntry {
                                name = "Alias".literal
                                property = it::alias
                            }
                        )
                    }
                }
            }

            category("Animations") {
                intSlider {
                    name = "X Position".literal
                    property = configOptions.animationPreset::posX
                    range = -150..150
                }
                intSlider {
                    name = "Y Position".literal
                    property = configOptions.animationPreset::posY
                    range = -150..150
                }
                intSlider {
                    name = "Z Position".literal
                    property = configOptions.animationPreset::posZ
                    range = -150..50
                }
                intSlider {
                    name = "X Rotation".literal
                    property = configOptions.animationPreset::rotX
                    range = -180..180
                }
                intSlider {
                    name = "Y Rotation".literal
                    property = configOptions.animationPreset::rotY
                    range = -180..180
                }
                intSlider {
                    name = "Z Rotation".literal
                    property = configOptions.animationPreset::rotZ
                    range = -180..180
                }
                float {
                    name = "Held Item Scale".literal
                    property = configOptions.animationPreset::scale
                    tooltip = arrayOf("Recommended range of .1 - 2".literal)
                }
                intSlider {
                    name = "Swing Speed".literal
                    property = configOptions.animationPreset::swingDuration
                    range = 2..20
                }
                toggle {
                    name = "Cancel Re-Equip Animation".literal
                    property = configOptions.animationPreset::cancelReEquip
                }
            }

            category("Bridge") {
                toggle {
                    name = "Format Bridge Messages".literal
                    property = configOptions::bridgeFormatter
                }
                string {
                    name = "Bridge Bot IGN".literal
                    property = configOptions::bridgeBotName
                }
                color {
                    name = "Bridge User Color".literal
                    property = configOptions::bridgeNameColor
                    default = Formatting.GOLD.colorValue!!
                }
            }

            category("Slayer") {
                toggle {
                    name = "Miniboss Highlight Box".literal
                    property = configOptions::boxMinis
                }
                toggle {
                    name = "Miniboss Announcement Alert".literal
                    property = configOptions::announceMinis
                }
                toggle {
                    name = "Show Kill Time on Slayer Completion".literal
                    property = configOptions::slayerKillTime
                    tooltip = arrayOf("Shows up in chat!".literal)
                }
                toggle {
                    name = "Blaze Slayer Attunement Display".literal
                    property = configOptions::attunementDisplay
                    tooltip = arrayOf("Shows a wireframe in the correct color for the slayer".literal)
                }
                toggle {
                    name = "Disable ALL particles during Blaze slayer boss".literal
                    property = configOptions::cleanBlaze
                }
                toggle {
                    name = "Vampire Steak Display".literal
                    property = configOptions::steakDisplay
                    tooltip = arrayOf("Shows a wireframe on vampire boss when you can 1 tap it".literal)
                }
                toggle {
                    name = "Blood Ichor Highlight".literal
                    property = configOptions::ichorHighlight
                    tooltip = arrayOf("Highlights the T5 mechanic that you line up with the boss.".literal)
                }
            }

            category("Garden") {
                toggle {
                    name = "Show Visitor Info in HUD".literal
                    property = configOptions::visitorHud
                }
                toggle {
                    name = "Show Composter Info in HUD".literal
                    property = configOptions::showComposterInfo
                }
                toggle {
                    name = "Show Title alert when max visitors".literal
                    property = configOptions::visitorAlert
                }
                toggle {
                    name = "Persistent Visitor alert (dependent on previous)".literal
                    property = configOptions::persistentVisitorAlert
                }
                toggle {
                    name = "Show Blocks per second (SPEED)".literal
                    property = configOptions::speedBpsHud
                }
                toggle {
                    name = "Show Pitch/Yaw in HUD".literal
                    property = configOptions::pitchYawDisplay
                }
            }
        }
    }

    @Serializable
    data class ConfigOptions(
        var invScaleBool: Boolean = false,
        var inventoryScale: Float = 1f,
        var macrosList: List<Macro> = listOf(Macro(EMPTY_KEYBIND, "")),
        var macrosSkyBlockOnly: Boolean = false,
        var aliasList: List<Alias> = listOf(Alias("", "")),
        var ignoreReverseThirdPerson: Boolean = false,
        var dynamicKey: ModifierKeyCode = ModifierKeyCode.of(UNKNOWN_KEY, Modifier.none()),
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
        var keyBinding: ModifierKeyCode,
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