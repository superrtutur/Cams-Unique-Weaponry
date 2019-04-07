package com.camellias.camsweaponry.core.init;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.camellias.camsweaponry.common.items.ItemArquebus;
import com.camellias.camsweaponry.common.items.ItemMacil;
import com.camellias.camsweaponry.common.items.ItemPike;
import com.camellias.camsweaponry.common.items.ItemJinHalberd;
import com.camellias.camsweaponry.common.items.misc.ItemIronBullet;
import com.camellias.camsweaponry.common.items.misc.ItemPowderBag;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;

public class ModItems
{
	public static final List<Item> ITEMS = new ArrayList<Item>();
	public static final Set<Block> EFFECTIVE_ON = new HashSet<Block>();
	
	public static final ItemTool JIN_HALBERD = new ItemJinHalberd("jin_halberd", ModMaterials.HALBERD_MATERIAL, EFFECTIVE_ON);
	public static final ItemTool PIKE = new ItemPike("pike", ModMaterials.PIKE_MATERIAL, EFFECTIVE_ON);
	public static final ItemTool MACIL = new ItemMacil("macil", ModMaterials.MACIL_MATERIAL, EFFECTIVE_ON);
	
	public static final Item ARQUEBUS = new ItemArquebus("arquebus");
	public static final Item POWDER_BAG = new ItemPowderBag("powder_bag");
	public static final Item BULLET = new ItemIronBullet("iron_bullet");
}
