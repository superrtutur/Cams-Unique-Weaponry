package com.camellias.camsweaponry.core.init;

import com.camellias.camsweaponry.Reference;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSmelting
{
	public static void init()
	{
		GameRegistry.addSmelting(new ItemStack(Items.IRON_NUGGET), new ItemStack(ModItems.BULLET), 0.0F);
	}
}
