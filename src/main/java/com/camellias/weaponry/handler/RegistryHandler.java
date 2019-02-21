package com.camellias.weaponry.handler;

import com.camellias.weaponry.capabilities.CapabilitiesHandler;
import com.camellias.weaponry.init.ModItems;
import com.camellias.weaponry.network.NetworkHandler;
import com.camellias.weaponry.util.IHasModel;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

	public static void serverRegistries(FMLServerStartingEvent event)
	{

	}

	public static void otherRegistries()
	{

	}

	public static void preInitRegistries(FMLPreInitializationEvent event)
	{
		CapabilitiesHandler.init();
		NetworkHandler.init();
		/*if(event.getSide() == Side.CLIENT)
		{
			RenderHandler.registerEntityRenders();
		}

		ModEntities.registerEntities();*/

		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	public static void initRegistries()
	{

	}
}
