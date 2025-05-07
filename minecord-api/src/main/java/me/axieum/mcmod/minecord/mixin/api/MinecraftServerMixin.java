package me.axieum.mcmod.minecord.mixin.api;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.crash.CrashReport;

import me.axieum.mcmod.minecord.api.event.ServerShutdownCallback;
import me.axieum.mcmod.minecord.util.CrashReportHolder;

/**
 * Captures any CrashReport set on MinecraftServer#setCrashReport
 * and re-fires ServerShutdownCallback at the end of runServer.
 */
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
    /**
     * Broadcasts a server shutdown event, be it gracefully or forcefully exited.
     *
     * @param ci mixin callback info
     */
    @Inject(method = "runServer", at = @At("TAIL"))
    private void onRunServerTail(CallbackInfo ci)
    {
        ServerShutdownCallback.EVENT.invoker().onServerShutdown((MinecraftServer) (Object) this,
            CrashReportHolder.crashReport);
        CrashReportHolder.crashReport = null;
    }

    /**
     * Captures any server crash reports.
     *
     * @param crashReport Minecraft crash report being set
     * @param ci          mixin callback info
     */
    @Inject(method = "setCrashReport", at = @At("TAIL"))
    private void onSetCrashReport(CrashReport crashReport, CallbackInfo ci)
    {
        CrashReportHolder.crashReport = crashReport;
    }
}
