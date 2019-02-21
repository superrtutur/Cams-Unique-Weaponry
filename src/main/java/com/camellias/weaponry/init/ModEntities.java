package com.camellias.weaponry.init;

import com.camellias.weaponry.Main;
import com.camellias.weaponry.Reference;
import com.camellias.weaponry.common.entities.EntityWeightedNet;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities
{
	public static void registerEntities()
	{
		registerEntity("weighted_net", EntityWeightedNet.class, 128);
	}
	
	private static void registerEntity(String name, Class<? extends Entity> entity, int id)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID + ":" + name), 
				entity, name, id, Main.instance, 0, 1, true);
	}
}
