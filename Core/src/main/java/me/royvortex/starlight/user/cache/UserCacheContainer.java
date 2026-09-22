package me.royvortex.starlight.user.cache;

public interface UserCacheContainer {

    void clear();

    void clearExpired();
}
