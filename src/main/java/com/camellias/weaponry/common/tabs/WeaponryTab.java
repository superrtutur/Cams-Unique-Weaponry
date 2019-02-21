package com.camellias.weaponry.common.tabs;

import com.camellias.weaponry.init.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class WeaponryTab extends CreativeTabs
{
	public WeaponryTab(String label)
	{
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() 
	{
		return new ItemStack(ModItems.SPEAR_HALBERD);
	}
}
