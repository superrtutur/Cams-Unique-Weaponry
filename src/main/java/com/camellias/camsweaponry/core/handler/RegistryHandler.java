package com.camellias.camsweaponry.core.handler;

import com.camellias.camsweaponry.client.render.entities.RenderBullet;
import com.camellias.camsweaponry.common.entities.EntityBullet;
import com.camellias.camsweaponry.core.init.ModEntities;
import com.camellias.camsweaponry.core.init.ModItems;
import com.camellias.camsweaponry.core.network.NetworkHandler;
import com.camellias.camsweaponry.core.util.IHasModel;
import com.camellias.camsweaponry.core.util.capabilities.CapabilitiesHandler;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class RegistryHandler
{
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for(final Item item : ModItems.ITEMS)
		{
			if(item instanceof IHasModel)
			{
				((IHasModel)item).registerModels();
			}
		}
	}

	@SubscribeEvent
	public static void registerEntities(Register<EntityEntry> event)
	{
		for(EntityEntry entry : ModEntities.REGISTRY)
		{
			event.getRegistry().register(entry);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void clientRegistries(FMLPreInitializationEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, RenderBullet::new);
	}

	public static void preInitRegistries(FMLPreInitializationEvent event)
	{
		CapabilitiesHandler.init();
		NetworkHandler.init();

		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	public static void initRegistries()
	{

	}
}
