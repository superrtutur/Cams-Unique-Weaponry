package com.camellias.camsweaponry.common.items.misc;

import com.camellias.camsweaponry.Main;
import com.camellias.camsweaponry.Reference;
import com.camellias.camsweaponry.core.init.ModItems;
import com.camellias.camsweaponry.core.util.IHasModel;

import net.minecraft.item.Item;

public class ItemIronBullet extends Item implements IHasModel
{
	public ItemIronBullet(String name)
	{
		this.setTranslationKey(Reference.MODID + "." + name);
		this.setRegistryName(name);
		this.setCreativeTab(Main.WEAPONRY_TAB);
		
		ModItems.ITEMS.add(this);
	}
	
	@Override
	public void registerModels() 
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
