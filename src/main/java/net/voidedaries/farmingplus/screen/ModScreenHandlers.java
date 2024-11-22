package net.voidedaries.farmingplus.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.voidedaries.farmingplus.FarmingPlus;

public class ModScreenHandlers {
    public static final ScreenHandlerType<FermentationBarrelScreenHandler> FERMENTATION_BARREL_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(FarmingPlus.MOD_ID, "fermentation_barrel"),
                    new ExtendedScreenHandlerType<>(FermentationBarrelScreenHandler::new));

    public static final ScreenHandlerType<BottlerScreenHandler> BOTTLER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(FarmingPlus.MOD_ID, "bottler"),
                    new ExtendedScreenHandlerType<>(BottlerScreenHandler::new));

    public static void registerScreenHandlers() {
        FarmingPlus.LOGGER.info("Registering Screen Handlers for " + FarmingPlus.MOD_ID);
    }
}
