package net.voidedaries.farmingplus.mixininterface;

import org.jetbrains.annotations.Nullable;

public interface StatusSavingPlayer {

    @Nullable
    Integer farmingplus$getStoredHealth();
    @Nullable
    Integer farmingplus$getStoredHunger();
    @Nullable
    Integer farmingplus$getStoredSaturation();
    @Nullable
    Integer farmingplus$getWineCount();

    void farmingplus$setStoredHealth(@Nullable Integer storedHealth);

    void farmingplus$setStoredHunger(@Nullable Integer storedHunger);

    void farmingplus$setStoredSaturation(@Nullable Integer storedSaturation);

    void farmingplus$setWineCount(@Nullable Integer wineCount);

    void farmingplus$save();

    void farmingplus$clear();

}
