package com.camellias.camsweaponry.core.handler;

import com.camellias.camsweaponry.core.init.ModItems;
import com.camellias.camsweaponry.core.init.ModSmelting;
import com.camellias.camsweaponry.core.network.NetworkHandler;
import com.camellias.camsweaponry.core.util.IHasModel;
import com.camellias.camsweaponry.core.util.capabilities.CapabilitiesHandler;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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


	public static void initRegistries()
	{

	}
}
