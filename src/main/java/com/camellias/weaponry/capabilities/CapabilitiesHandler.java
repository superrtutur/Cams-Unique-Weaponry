package com.camellias.weaponry.capabilities;

import java.util.concurrent.Callable;

import com.camellias.weaponry.capabilities.ItemCap.DefaultItemCapability;
import com.camellias.weaponry.capabilities.ItemCap.IItemCap;
import com.camellias.weaponry.capabilities.ItemCap.ItemStorage;

import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilitiesHandler {

	public static void init() {
		CapabilityManager.INSTANCE.register(IItemCap.class, new ItemStorage(), new ItemCapabilityFactory());
	}

	private static class ItemCapabilityFactory implements Callable<IItemCap> {
		@Override
		public IItemCap call() throws Exception {
			return new DefaultItemCapability();
		}
	}
}
