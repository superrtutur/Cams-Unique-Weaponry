package com.camellias.weaponry.handler;

import com.camellias.weaponry.init.ModEntities;
import com.camellias.weaponry.init.ModItems;
import com.camellias.weaponry.util.IHasModel;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

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
		for(Item item : ModItems.ITEMS)
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
