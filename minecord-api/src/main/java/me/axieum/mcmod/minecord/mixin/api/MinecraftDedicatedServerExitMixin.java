package me.axieum.mcmod.minecord.mixin.api;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import me.axieum.mcmod.minecord.api.event.ServerShutdownCallback;
import me.axieum.mcmod.minecord.util.CrashReportHolder;

/**
 * Ensures Minecord’s shutdown callbacks fire before any System.exit(0) hook,
 * but delays JDA.shutdown() until after all callbacks have run.
 */
@Mixin(MinecraftDedicatedServer.class)
public class MinecraftDedicatedServerExitMixin
{
    /**
     * Inject into the very start of MinecraftDedicatedServer#exit()
     * so our shutdown callbacks fire before any System.exit(0) hook.
     *
     * @param ci the Mixin callback info
     */
    @Inject(method = "exit()V", at = @At("HEAD"))
    private void beforeExit(CallbackInfo ci)
    {
        MinecraftServer server = (MinecraftServer) (Object) this;
        ServerLifecycleEvents.SERVER_STOPPED.invoker().onServerStopped(server);
        ServerShutdownCallback.EVENT.invoker().onServerShutdown(server, CrashReportHolder.crashReport);
    }
}
