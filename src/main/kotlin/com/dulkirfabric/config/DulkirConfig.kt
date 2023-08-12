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
                makeToggle(
                    text = "Inventory Scale Toggle",
                    property = configOptions::invScaleBool,
                    tooltip = "This is a tooltip"
                )
                makeFloat(
                    text = "Inventory Scale",
                    property = configOptions::inventoryScale,
                    tooltip = "Size of GUI whenever you're in an inventory screen"
                )
                makeFloat(
                    text = "Tooltip Scale",
                    property = configOptions::tooltipScale,
                    tooltip = "Default Value for Scaling a particular tooltip without scroll input"
                )
                makeToggle(
                    text = "Ignore Reverse Third Person",
                    property = configOptions::ignoreReverseThirdPerson
                )
                makeToggle(
                    text = "Disable Status Effect Rendering",
                    property = configOptions::statusEffectHidden
                )
                makeToggle(
                    text = "Custom Block outlines",
                    property = configOptions::customBlockOutlines
                )
                makeIntSlider(
                    text = "Line Thickness",
                    property = configOptions::blockOutlineThickness,
                    min = 1,
                    max = 5
                )
                makeColor(
                    text = "Outline Color",
                    property = configOptions::blockOutlineColor
                )
                makeToggle(
                    text = "Abiphone DND",
                    property = configOptions::abiPhoneDND
                )
                makeToggle(
                    text = "Abiphone Caller ID",
                    property = configOptions::abiPhoneCallerID
                )
                makeToggle(
                    text = "Inactive Effigy Waypoints",
                    property = configOptions::inactiveEffigyDisplay
                )
                makeToggle(
                    text = "Disable Explosion Particles",
                    property = configOptions::disableExplosionParticles
                )
                makeToggle(
                    text = "Durability-Based Cooldown Display",
                    property = configOptions::duraCooldown
                )
                makeToggle(
                    text = "Hide Armor Overlay in Skyblock",
                    property = configOptions::hideArmorOverlay
                )
                makeToggle(
                    text = "Hide Hunger Overlay in Skyblock",
                    property = configOptions::hideHungerOverlay
                )
                makeToggle(
                    text = "Hide Fire Overlay",
                    property = configOptions::hideFireOverlay
                )
                makeToggle(
                    text = "Hide Lightning (SkyBlock only)",
                    property = configOptions::hideLightning
                )
                makeToggle(
                    text = "Hide Non-Crits",
                    property = configOptions::hideNonCrits
                )
                makeToggle(
                    text = "Hide Crits",
                    property = configOptions::hideCrits
                )
                makeToggle(
                    text = "Truncate Crits",
                    property = configOptions::truncateDamage
                )
                makeIntSlider(
                    text = "Anti Downtime Alarm",
                    property = configOptions::alarmTimeout,
                    min = 0,
                    max = 1000,
                    tooltip = "Set to 0 to disable. (Time in seconds)"
                )
                makeToggle(
                    text = "Arachne Keeper Waypoints",
                    property = configOptions::arachneKeeperWaypoints
                )
                makeToggle(
                    text = "Arachne Boss Spawn Timer",
                    property = configOptions::arachneSpawnTimer
                )
                makeToggle(
                    text = "Convert Action Bar to HUD elements",
                    property = configOptions::hudifyActionBar,
                    tooltip = "This converts Mana/Health/Def/Stacks as HUD elements"
                )
                makeToggle(
                    text = "Show Speed in HUD",
                    property = configOptions::speedHud
                )
                makeToggle(
                    text = "Include EHP in def HUD element",
                    property = configOptions::showEHP,
                    tooltip = "Must have Action Bar HUD elements Enabled"
                )
                makeToggle(
                    text = "Hide Held Item Tooltips",
                    property = configOptions::hideHeldItemTooltip,
                    tooltip = "This is for the pesky overlay that pops up on switching items"
                )
                makeToggle(
                    text = "Etherwarp Preview",
                    property = configOptions::showEtherwarpPreview,
                    tooltip = "Highlights the targeted block when shifting with a aotv"
                )
                makeColor(
                    text = "Etherwarp Preview Color",
                    property = configOptions::etherwarpPreviewColor
                ) { setDefaultValue(0x99FFFFFF.toInt()) }
                makeToggle(
                    text = "Broken Hype Notification",
                    property = configOptions::brokenHypNotif
                )
            }

            category("Shortcuts") {
                makeKeybind(
                    text = "Dynamic Key",
                    property = configOptions::dynamicKey
                )
                makeToggle(
                    "Only Register Shortcuts in Skyblock",
                    configOptions::macrosSkyBlockOnly,
                    "Useful if you want to use some of these binds elsewhere for non-skyblock specific stuff"
                )
                makeConfigList(
                    name = "Macros",
                    property = configOptions::macrosList,
                    newT = { Macro(UNKNOWN_KEY, "") },
                    elementName = "Macro",
                    render = { value ->
                        listOf(
                            makeString("Command", value::command),
                            makeKeybind("Keybind", value::keyBinding)
                        )
                    }
                )
            }

            category("Aliases") {
                makeConfigList(
                    name = "Aliases (do not include '/')",
                    property = configOptions::aliasList,
                    newT = { Alias("", "") },
                    elementName = "Alias",
                    render = { value ->
                        listOf(
                            makeString("Command", value::command),
                            makeString("Alias", value::alias)
                        )
                    }
                )
            }

            category("Animations") {
                makeIntSlider(
                    text = "posX",
                    property = configOptions.animationPreset::posX,
                    min = -150,
                    max = 150
                )
                makeIntSlider(
                    text = "posY",
                    property = configOptions.animationPreset::posY,
                    min = -150,
                    max = 150
                )
                makeIntSlider(
                    text = "posZ",
                    property = configOptions.animationPreset::posZ,
                    min = -150, max = 50
                )
                makeIntSlider(
                    text = "rotationX",
                    property = configOptions.animationPreset::rotX,
                    min = -180,
                    max = 180
                )
                makeIntSlider(
                    text = "rotationY",
                    property = configOptions.animationPreset::rotY,
                    min = -180,
                    max = 180
                )
                makeIntSlider(
                    text = "rotationZ",
                    property = configOptions.animationPreset::rotZ,
                    min = -180,
                    max = 180
                )
                makeFloat(
                    text = "Held Item Scale",
                    property = configOptions.animationPreset::scale,
                    tooltip = "Recommended range of .1 - 2"
                )
                makeIntSlider(
                    text = "Swing Speed",
                    property = configOptions.animationPreset::swingDuration,
                    min = 2,
                    max = 20
                )
                makeToggle(
                    text = "Cancel Re-Equip Animation",
                    property = configOptions.animationPreset::cancelReEquip
                )
            }

            category("Bridge Features") {
                makeToggle(
                    text = "Format Bridge Messages",
                    property = configOptions::bridgeFormatter
                )
                makeString(
                    text = "Bridge Bot IGN",
                    property = configOptions::bridgeBotName
                )
                makeColor(
                    text = "Bridge User Color",
                    property = configOptions::bridgeNameColor
                ) { setDefaultValue(Formatting.GOLD.colorValue!!) }
            }

            category("Slayer") {
                makeToggle(
                    text = "MiniBoss Highlight Box",
                    property = configOptions::boxMinis
                )
                makeToggle(
                    text = "Miniboss Announcement Alert",
                    property = configOptions::announceMinis
                )
                makeToggle(
                    text = "Show Kill Time on Slayer Completion",
                    property = configOptions::slayerKillTime,
                    tooltip = "Shows up in chat!"
                )
                makeToggle(
                    text = "Blaze Slayer Attunement Display",
                    property = configOptions::attunementDisplay,
                    tooltip = "Shows a wireframe in the correct color for the slayer"
                )
                makeToggle(
                    text = "Disable ALL particles during Blaze slayer boss",
                    property = configOptions::cleanBlaze
                )
                makeToggle(
                    text = "Vampire Steak Display",
                    property = configOptions::steakDisplay,
                    tooltip = "Shows a wireframe on vampire boss when you can 1 tap it"
                )
                makeToggle(
                    text = "Blood Ichor Highlight",
                    property = configOptions::ichorHighlight,
                    tooltip = "Highlights the T5 mechanic that you line up with the boss."
                )
            }

            category("Garden") {
                makeToggle(
                    text = "Show Visitor Info in HUD",
                    property = configOptions::visitorHud,
                )
                makeToggle(
                    text = "Show Composter Info in HUD",
                    property = configOptions::showComposterInfo
                )
                makeToggle(
                    text = "Show Title alert when max visitors",
                    property = configOptions::visitorAlert
                )
                makeToggle(
                    text = "Persistent Visitor alert (dependent on previous)",
                    property = configOptions::persistentVisitorAlert
                )
                makeToggle(
                    text = "Show Blocks per second (SPEED)",
                    property = configOptions::speedBpsHud
                )
                makeToggle(
                    text = "Show Pitch/Yaw in HUD",
                    property = configOptions::pitchYawDisplay
                )
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