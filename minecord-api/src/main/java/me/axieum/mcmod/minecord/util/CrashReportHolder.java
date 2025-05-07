package me.axieum.mcmod.minecord.util;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.crash.CrashReport;

/**
 * Simple holder for the last CrashReport.
 */
public final class CrashReportHolder
{
    /** The most recently set crash report, or null. */
    public static @Nullable CrashReport crashReport = null;

    private CrashReportHolder() {}
}
