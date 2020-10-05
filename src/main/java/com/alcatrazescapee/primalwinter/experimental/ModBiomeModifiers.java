/*
 * Part of the Primal Winter by AlcatrazEscapee
 * Work under Copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.primalwinter.experimental;

import net.minecraftforge.common.world.biomes.modifiers.base.BiomeModifierType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.alcatrazescapee.primalwinter.PrimalWinter.MOD_ID;

public class ModBiomeModifiers
{
    public static final DeferredRegister<BiomeModifierType<?>> MODIFIERS = DeferredRegister.create(ForgeRegistries.BIOME_MODIFIER_TYPES, MOD_ID);

    public static final RegistryObject<BiomeModifierType<WinterizeEverythingModifier>> WINTERIZE_EVERYTHING = MODIFIERS.register("winterize_everything", () -> new BiomeModifierType<>(WinterizeEverythingModifier.CODEC));
}
