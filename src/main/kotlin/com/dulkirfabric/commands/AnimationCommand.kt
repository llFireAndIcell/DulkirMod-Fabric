package com.dulkirfabric.commands

import com.dulkirfabric.config.DulkirConfig
import com.dulkirfabric.dsl.literalArgument
import com.dulkirfabric.util.TextUtils
import com.dulkirfabric.util.render.AnimationPreset
import com.google.gson.Gson
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.util.*

object AnimationCommand {

    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
        dispatcher.register(

            literalArgument("animations") {
                executes {
                    TextUtils.info("§6Usage: /animations <import/export>")
                    TextUtils.info("§6For more information about this command, run /animations help")
                    return@executes 0
                }

                then(literalArgument("import") {
                    executes {
                        applyPresetFromClipboard()
                        return@executes 1
                    }
                })

                then(literalArgument("export") {
                    executes {
                        applyPresetToClipboard()
                        return@executes 1
                    }
                })

                then(literalArgument("help") {
                    executes {
                        TextUtils.info("§6§lAnimations Info")
                        TextUtils.info("§7 - Exporting using this command will encode data about your held item (position, scale, and swing variables) to a base64 encoded string that you can share with friends.")
                        TextUtils.info("§7 - Importing using this command will apply settings based on the state of your clipboard, if possible.")
                        return@executes 2
                    }
                })
            }
        )
    }

    private fun applyPresetFromClipboard() {
        val gson = Gson()
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val base64 = clipboard.getData(DataFlavor.stringFlavor) as String
        try {
            val jsonString = String(Base64.getDecoder().decode(base64))
            val import = gson.fromJson(jsonString, AnimationPreset::class.java)
            DulkirConfig.configOptions.animationPreset.posX = import.posX
            DulkirConfig.configOptions.animationPreset.posY = import.posY
            DulkirConfig.configOptions.animationPreset.posZ = import.posZ
            DulkirConfig.configOptions.animationPreset.rotX = import.rotX
            DulkirConfig.configOptions.animationPreset.rotY = import.rotY
            DulkirConfig.configOptions.animationPreset.rotZ = import.rotZ
            DulkirConfig.configOptions.animationPreset.scale = import.scale
            DulkirConfig.configOptions.animationPreset.swingDuration = import.swingDuration
            DulkirConfig.configOptions.animationPreset.cancelReEquip = import.cancelReEquip
            DulkirConfig.configOptions.animationPreset.rotationlessDrink = import.rotationlessDrink
        } catch (e: Exception) {
            TextUtils.info("§6Something went wrong when trying to import settings. Make sure you have a valid string copied to your clipboard!")
            return
        }
        TextUtils.info("§6Successfully imported preset.")
    }

    private fun applyPresetToClipboard() {
        val gson = Gson()
        val jsonString = gson.toJson(DulkirConfig.configOptions.animationPreset)
        val s: String = Base64.getEncoder().encodeToString(jsonString.toByteArray())
        // set clipboard
        val selection = StringSelection(s)
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(selection, selection)
        TextUtils.info("§6Animation config has been copied to clipboard")
    }
}