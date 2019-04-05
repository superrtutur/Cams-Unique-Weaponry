package com.camellias.camsweaponry.common.items.misc;

import com.camellias.camsweaponry.Main;
import com.camellias.camsweaponry.Reference;
import com.camellias.camsweaponry.core.init.ModItems;

import net.minecraft.item.Item;

public class ItemIronBullet extends Item
{
	public ItemIronBullet(String name)
	{
		this.setTranslationKey(Reference.MODID + "." + name);
		this.setRegistryName(name);
		this.setCreativeTab(Main.WEAPONRY_TAB);
		
		ModItems.ITEMS.add(this);
	}
}
