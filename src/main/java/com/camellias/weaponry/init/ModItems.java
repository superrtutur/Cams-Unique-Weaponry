package com.camellias.weaponry.init;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.camellias.weaponry.common.items.ItemMacil;
import com.camellias.weaponry.common.items.ItemPike;
import com.camellias.weaponry.common.items.ItemSpearHalberd;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;

public class ModItems
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	public static final Set<Block> EFFECTIVE_ON = new HashSet<Block>();
	
	public static final ItemTool SPEAR_HALBERD = new ItemSpearHalberd("spear_halberd", ModMaterials.SPEAR_MATERIAL, EFFECTIVE_ON);
	public static final ItemTool PIKE = new ItemPike("pike", ModMaterials.PIKE_MATERIAL, EFFECTIVE_ON);
	public static final ItemTool MACIL = new ItemMacil("macil", ModMaterials.MACIL_MATERIAL, EFFECTIVE_ON);
}
