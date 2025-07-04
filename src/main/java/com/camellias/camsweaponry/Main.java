package com.camellias.camsweaponry;

import java.io.File;
import java.sql.Ref;

import com.camellias.camsweaponry.common.tabs.WeaponryTab;
import com.camellias.camsweaponry.core.handler.ModEventHandler;
import com.camellias.camsweaponry.core.handler.RegistryHandler;
import com.camellias.camsweaponry.core.init.ModSmelting;
import com.camellias.camsweaponry.core.network.NetworkHandler;
import com.camellias.camsweaponry.core.proxy.CommonProxy;

import com.camellias.camsweaponry.core.util.capabilities.CapabilitiesHandler;
import fr.dynamx.api.contentpack.DynamXAddon;
import fr.dynamx.api.events.client.DynamXRenderItemEvent;
import fr.dynamx.common.items.DynamXItem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(
	modid = Reference.MODID, 
	name = Reference.NAME, 
	version = Reference.VERSION, 
	acceptedMinecraftVersions = Reference.ACCEPTEDVERSIONS, 
	dependencies = Reference.DEPENDENCIES)
@DynamXAddon(modid = Reference.MODID, name = "Shot Addon", version = Reference.VERSION)
public class Main 
{
	public static File config;
	
	@Instance
	public static Main instance;
	
	public static final CreativeTabs WEAPONRY_TAB = new WeaponryTab("weaponryTab");
	
	//Proxy
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	//Initialization
	@DynamXAddon.AddonEventSubscriber
	public static void init()
	{
		ModSmelting.init();
		System.out.println("PreRegister Dynamx");
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		CapabilitiesHandler.init();
		NetworkHandler.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		RegistryHandler.initRegistries();
		MinecraftForge.EVENT_BUS.register(new ModEventHandler());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
}
