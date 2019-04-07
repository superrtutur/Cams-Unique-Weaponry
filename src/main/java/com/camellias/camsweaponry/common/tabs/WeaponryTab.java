package com.camellias.camsweaponry.common.tabs;

import com.camellias.camsweaponry.core.init.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class WeaponryTab extends CreativeTabs
{
	public WeaponryTab(String label)
	{
		super(label);
	}
	
	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(ModItems.JIN_HALBERD);
	}
}
