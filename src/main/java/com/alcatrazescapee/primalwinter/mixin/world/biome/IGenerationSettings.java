/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.mixin.world.biome;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GenerationSettings.class)
public interface IGenerationSettings
{
    @Accessor(value = "features")
    void setFeatures(List<List<Supplier<ConfiguredFeature<?, ?>>>> features);

    @Accessor(value = "structureFeatures")
    void setStructureFeatures(List<Supplier<ConfiguredStructureFeature<?, ?>>> structureFeatures);
}
