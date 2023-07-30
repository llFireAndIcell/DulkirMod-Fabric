package com.dulkirfabric

import com.dulkirfabric.DulkirModFabric.EVENT_BUS
import com.dulkirfabric.commands.*
import com.dulkirfabric.events.*
import com.dulkirfabric.events.chat.ChatEvents
import com.dulkirfabric.events.chat.ModifyCommandEvent
import com.dulkirfabric.events.chat.OverlayReceivedEvent
import com.dulkirfabric.features.*
import com.dulkirfabric.features.chat.AbiPhoneDND
import com.dulkirfabric.features.chat.BridgeBotFormatter
import com.dulkirfabric.features.chat.ChatStacking
import com.dulkirfabric.hud.ActionBarHudReplacements
import com.dulkirfabric.hud.SpeedOverlay
import com.dulkirfabric.util.ActionBarUtil
import com.dulkirfabric.util.TablistUtils
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.fabricmc.loader.api.FabricLoader


/**
 * Collection of different mod registration stuff ran on initializing the mod. It is separated for readability
 * purposes, as the list of features is planned to be quite large.
 */
object Registrations {
    private var tickCount: Int = 0

    fun registerCommands() {
        arrayOf(
            ConfigCommand::register,
            JoinDungeonCommands.F1Command::register,
            JoinDungeonCommands.F2Command::register,
            JoinDungeonCommands.F3Command::register,
            JoinDungeonCommands.F4Command::register,
            JoinDungeonCommands.F5Command::register,
            JoinDungeonCommands.F6Command::register,
            JoinDungeonCommands.F7Command::register,
            JoinDungeonCommands.M1Command::register,
            JoinDungeonCommands.M2Command::register,
            JoinDungeonCommands.M3Command::register,
            JoinDungeonCommands.M4Command::register,
            JoinDungeonCommands.M5Command::register,
            JoinDungeonCommands.M6Command::register,
            JoinDungeonCommands.M7Command::register,
            DynamicKeyCommand::register,
            AnimationCommand::register
        ).forEach { ClientCommandRegistrationCallback.EVENT.register(it) }

        if (FabricLoader.getInstance().isDevelopmentEnvironment)
            ClientCommandRegistrationCallback.EVENT.register(TestCommand::register)
    }

    fun registerEventListeners() {
        arrayOf(
            DulkirModFabric,
            KeyShortCutImpl,
            RenderTest,
            TooltipImpl,
            CustomBlockOutline,
            AbiPhoneDND,
            InventoryScale,
            IPhoneAlarm,
            AliasImpl,
            EffigyDisplay,
            TablistUtils,
            CullExplosionParticles,
            CooldownDisplays,
            ArachneFeatures,
            BridgeBotFormatter,
            SpeedOverlay,
            ActionBarUtil,
            ActionBarHudReplacements,
            ChatStacking
        ).forEach { EVENT_BUS.subscribe(it) }
    }

    fun registerEvents() {
        // Register Custom Tick event, so we can use it like 1.8.9 forge
        ClientTickEvents.START_CLIENT_TICK.register { _ ->
            ClientTickEvent.post()
            if (tickCount % 20 == 0) LongUpdateEvent.post()
            tickCount++
        }
        ClientReceiveMessageEvents.ALLOW_GAME.register { message, overlay ->
            if (!overlay)
                return@register !ChatEvents.AllowChat(message).post()
            return@register true
        }
        ClientReceiveMessageEvents.MODIFY_GAME.register { message, overlay ->
            if (overlay)
                return@register OverlayReceivedEvent(message).post()
            return@register ChatEvents.ModifyChat(message).post()
        }

        ClientSendMessageEvents.MODIFY_COMMAND.register { command ->
            ModifyCommandEvent(command).apply { post() }.command
        }

        WorldRenderEvents.END.register { context -> WorldRenderLastEvent(context).post() }

        ScreenEvents.BEFORE_INIT.register(
            ScreenEvents.BeforeInit { client, screen, scaledWidth, scaledHeight ->
                ScreenMouseEvents.beforeMouseScroll(screen)
                    .register(ScreenMouseEvents.BeforeMouseScroll { coolScreen, mouseX, mouseY, horizontalAmount, verticalAmount ->
                        MouseScrollEvent(coolScreen, mouseX, mouseY, horizontalAmount, verticalAmount).post()
                    })
            }
        )

        WorldRenderEvents.BLOCK_OUTLINE.register { worldRenderContext, blockOutlineContext ->
            !BlockOutlineEvent(worldRenderContext, blockOutlineContext).post()
        }
        ClientEntityEvents.ENTITY_LOAD.register { entity, world ->
            EntityLoadEvent(entity, world).post()
        }
        ServerWorldEvents.LOAD.register { server, world ->
            WorldLoadEvent(server, world).post()
        }
        HudRenderCallback.EVENT.register { context, delta ->
            HudRenderEvent(context, delta).post()
        }
    }
}