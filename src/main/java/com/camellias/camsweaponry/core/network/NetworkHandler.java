package com.camellias.camsweaponry.core.network;

import com.camellias.camsweaponry.Reference;
import com.camellias.camsweaponry.core.network.packets.ArquebusPacket;
import com.camellias.camsweaponry.core.network.packets.ItemPacket;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID.toLowerCase());
	
	private static int ID = 0;
	
	private static int nextId()
	{
		return ID++;
	}
	
	public static void init()
	{
		INSTANCE.registerMessage(ItemPacket.ServerHandler.class, ItemPacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(ItemPacket.ClientHandler.class, ItemPacket.class, nextId(), Side.CLIENT);
		
		INSTANCE.registerMessage(ArquebusPacket.ArquebusPacketHandler.class, ArquebusPacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(ArquebusPacket.ArquebusPacketHandler.class, ArquebusPacket.class, nextId(), Side.CLIENT);
	}
}
