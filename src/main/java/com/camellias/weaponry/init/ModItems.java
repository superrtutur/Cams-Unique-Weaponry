package com.camellias.weaponry.init;

import java.util.ArrayList;
import java.util.List;

import com.camellias.weaponry.common.items.ItemMacil;
import com.camellias.weaponry.common.items.ItemNet;
import com.camellias.weaponry.common.items.ItemPike;
import com.camellias.weaponry.common.items.ItemSpearHalberd;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;

public class ModItems
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final ItemTool SPEAR_HALBERD = new ItemSpearHalberd("spear_halberd", ModMaterials.SPEAR_MATERIAL, null);
	public static final ItemTool PIKE = new ItemPike("pike", ModMaterials.PIKE_MATERIAL, null);
	public static final ItemTool MACIL = new ItemMacil("macil", ModMaterials.MACIL_MATERIAL, null);
	//public static final Item WEIGHTED_NET = new ItemNet("weighted_net");
}
