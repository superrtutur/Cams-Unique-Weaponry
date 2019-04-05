package com.camellias.camsweaponry.client.render.entities;

import com.camellias.camsweaponry.common.entities.EntityBullet;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderBullet extends Render<EntityBullet>
{
	public RenderBullet(RenderManager renderManager)
	{
		super(renderManager);
	}
	
	@Override
	public ResourceLocation getEntityTexture(EntityBullet entity)
	{
		return null;
	}
}
