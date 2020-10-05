/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.experimental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraftforge.common.world.biomes.BiomeModifications;
import net.minecraftforge.common.world.biomes.conditions.base.IBiomeCondition;
import net.minecraftforge.common.world.biomes.modifiers.base.BiomeModifier;
import net.minecraftforge.common.world.biomes.modifiers.base.BiomeModifierType;

import com.alcatrazescapee.primalwinter.mixin.world.biome.BiomeAmbienceAccess;
import com.alcatrazescapee.primalwinter.world.ModConfiguredFeatures;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class WinterizeEverythingModifier extends BiomeModifier
{
    public static final MapCodec<WinterizeEverythingModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        IBiomeCondition.FIELD_CODEC.forGetter(WinterizeEverythingModifier::getCondition)
    ).apply(instance, WinterizeEverythingModifier::new));

    private static void add(List<List<Supplier<ConfiguredFeature<?, ?>>>> features, GenerationStage.Decoration stage, ConfiguredFeature<?, ?> feature)
    {
        while (features.size() < stage.ordinal())
        {
            features.add(new ArrayList<>());
        }
        features.get(stage.ordinal()).add(() -> feature);
    }

    public WinterizeEverythingModifier(IBiomeCondition condition)
    {
        super(condition);
    }

    @Override
    public BiomeModifierType<?> getType()
    {
        return ModBiomeModifiers.WINTERIZE_EVERYTHING.get();
    }

    @Override
    public BiomeModifications modifyBiome(Biome biome)
    {
        return new BiomeModifications()
            .modifyTemperature(t -> -0.5f)
            .modifyRainType(t -> Biome.RainType.SNOW)
            .modifyTemperatureModifier(t -> Biome.TemperatureModifier.NONE)
            .modifyBiomeAmbience(effects -> {
                // cyborg: this is a mixin because there's like five million optional fields I'd have to copy between builder methods which is yuck.
                BiomeAmbienceAccess effectsAccess = (BiomeAmbienceAccess) effects;
                effectsAccess.setWaterColor(0x3938C9);
                effectsAccess.setFogWaterColor(0x050533);
                return effects;
            })
            .modifySpawners(spawns -> {
                Map<EntityClassification, List<MobSpawnInfo.Spawners>> newSpawns = new HashMap<>();
                spawns.forEach((key, value) -> newSpawns.put(key, new ArrayList<>(value)));
                newSpawns.computeIfAbsent(EntityClassification.MONSTER, key -> new ArrayList<>()).add(new MobSpawnInfo.Spawners(EntityType.STRAY, 320, 4, 4));
                newSpawns.computeIfAbsent(EntityClassification.CREATURE, key -> new ArrayList<>()).add(new MobSpawnInfo.Spawners(EntityType.POLAR_BEAR, 4, 1, 2));
                newSpawns.computeIfAbsent(EntityClassification.CREATURE, key -> new ArrayList<>()).add(new MobSpawnInfo.Spawners(EntityType.SNOW_GOLEM, 4, 4, 8));
                return newSpawns;
            })
            .modifyFeatures(lists -> {
                lists = lists.stream().map(ArrayList::new).collect(Collectors.toList());
                add(lists, GenerationStage.Decoration.SURFACE_STRUCTURES, ModConfiguredFeatures.ICE_SPIKES);
                add(lists, GenerationStage.Decoration.SURFACE_STRUCTURES, ModConfiguredFeatures.ICE_PATCH);
                add(lists, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, ModConfiguredFeatures.FREEZE_TOP_LAYER);
                lists.forEach(list -> list.removeIf(feature -> feature.get() == Features.FREEZE_TOP_LAYER));
                return lists;
            })
            .modifyStructures(list -> {
                list = new ArrayList<>(list);
                list.add(() -> StructureFeatures.IGLOO);
                return list;
            });
    }
}
