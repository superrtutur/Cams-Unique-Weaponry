package com.camellias.weaponry.handler;

import com.camellias.weaponry.common.entities.EntityWeightedNet;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler
{
	public static void registerEntityRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityWeightedNet.class, new IRenderFactory<EntityWeightedNet>()
		{
			@Override
			public Render<? super EntityWeightedNet> createRenderFor(RenderManager manager) 
			{
				return null;
			}
		});
	}
}
