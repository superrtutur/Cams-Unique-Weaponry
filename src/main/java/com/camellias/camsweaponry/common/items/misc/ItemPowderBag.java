package com.camellias.camsweaponry.common.items.misc;

import com.camellias.camsweaponry.Main;
import com.camellias.camsweaponry.Reference;
import com.camellias.camsweaponry.core.init.ModItems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemPowderBag extends Item
{
	public ItemPowderBag(String name)
	{
		this.setTranslationKey(Reference.MODID + "." + name);
		this.setRegistryName(name);
		this.setCreativeTab(Main.WEAPONRY_TAB);
		this.setMaxStackSize(1);
		this.setMaxDamage(64);
		
		ModItems.ITEMS.add(this);
	}
	
	public static boolean isUsable(ItemStack stack)
	{
		return stack.getItemDamage() < stack.getMaxDamage() - 1;
	}
}
