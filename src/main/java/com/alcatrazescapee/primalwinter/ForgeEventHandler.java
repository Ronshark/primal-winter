/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter;

import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeEventHandler
{
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event)
    {
        if (Config.COMMON.disableWeatherCommand.get())
        {
            // Vanilla weather command... NOT ALLOWED
            event.getDispatcher().getRoot().getChildren().removeIf(node -> node.getName().equals("weather"));
            event.getDispatcher().register(Commands.literal("weather").executes(source -> {
                source.getSource().sendSuccess(new StringTextComponent("Not even a command can overcome this storm... (This command is disabled by Primal Winter)"), false);
                return 0;
            }));
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event)
    {
        // todo: check dimension == overworld
        if (event.getWorld() instanceof ServerWorld)
        {
            ServerWorld world = (ServerWorld) event.getWorld();
            world.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, world.getServer());
            world.setWeatherParameters(0, Integer.MAX_VALUE, true, true);
        }
    }

    /**
     * {@link com.alcatrazescapee.primalwinter.experimental.ModBiomeModifiers}
     */
    @SubscribeEvent
    public static void onBiomeLoading(BiomeLoadingEvent event)
    {
        /*
        if (Config.COMMON.nonWinterBiomes.get().stream().anyMatch(id -> id.equals(event.getName() == null ? "" : event.getName().toString())))
        {
            // This requires a mixin because forge hasn't exposed any mutators, a constructor, or a builder...
            BiomeClimateAccess climateAccess = (BiomeClimateAccess) event.getClimate();
            climateAccess.setTemperature(-0.5f);
            climateAccess.setPrecipitation(Biome.RainType.SNOW);
            climateAccess.setTemperatureModifier(Biome.TemperatureModifier.NONE); // The frozen modifier has large >0.15 zones which result in non-ztormy behavior... bad!

            // Modify effects - mixin required as there is no accessors or usable builder
            BiomeAmbienceAccess effectsAccess = (BiomeAmbienceAccess) event.getEffects();
            effectsAccess.setWaterColor(0x3938C9);
            effectsAccess.setFogWaterColor(0x050533);

            // Modify spawn settings
            MobSpawnInfoBuilder spawnSettingsBuilder = event.getSpawns();
            spawnSettingsBuilder.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.STRAY, 320, 4, 4));
            spawnSettingsBuilder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.POLAR_BEAR, 4, 1, 2));
            spawnSettingsBuilder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.SNOW_GOLEM, 4, 4, 8));

            // Add features and structures
            BiomeGenerationSettingsBuilder generationSettingsBuilder = event.getGeneration();
            generationSettingsBuilder.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, ModConfiguredFeatures.ICE_SPIKES);
            generationSettingsBuilder.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, ModConfiguredFeatures.ICE_PATCH);
            generationSettingsBuilder.addFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, ModConfiguredFeatures.FREEZE_TOP_LAYER);

            generationSettingsBuilder.addStructureStart(StructureFeatures.IGLOO);

            // Removals need to access the underlying list for now
            ((BiomeGenerationSettingsBuilderAccess) generationSettingsBuilder).getFeatures().forEach(list -> list.removeIf(feature -> feature.get() == Features.FREEZE_TOP_LAYER));
        }
        */
    }
}