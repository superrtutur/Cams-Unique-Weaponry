package com.camellias.weaponry.network;

import com.camellias.weaponry.Reference;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MODID.toLowerCase());

	private static int ID = 0;

	private static int nextId() {
		return ID++;
	}

	public static void init() {

		INSTANCE.registerMessage(ItemPacket.ServerHandler.class, ItemPacket.class, nextId(), Side.SERVER);
		INSTANCE.registerMessage(ItemPacket.ClientHandler.class, ItemPacket.class, nextId(), Side.CLIENT);

	}
}
