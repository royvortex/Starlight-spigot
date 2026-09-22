package me.royvortex.starlight.module.bans.time;

@FunctionalInterface
public interface BanTimeAccumulator {

    long accumulate(long quantity);
}
