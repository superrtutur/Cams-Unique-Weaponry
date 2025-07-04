package com.camellias.camsweaponry.common.items.misc;

import java.util.List;

import com.camellias.camsweaponry.Main;
import com.camellias.camsweaponry.Reference;
import com.camellias.camsweaponry.core.init.ModItems;
import com.camellias.camsweaponry.core.util.IHasModel;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPowderBag extends Item implements IHasModel
{
	public ItemPowderBag(String name)
	{
		this.setUnlocalizedName(Reference.MODID + "." + name);
		this.setRegistryName(name);
		this.setCreativeTab(Main.WEAPONRY_TAB);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
		this.setMaxDamage(64);
		
		ModItems.ITEMS.add(this);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		ItemStack gunpowder = getStack(player, Items.GUNPOWDER);
		
		if(!gunpowder.isEmpty() && stack.getItemDamage() >= 8 && !player.isCreative())
		{
			gunpowder.shrink(1);
			stack.setItemDamage(stack.getItemDamage() - 8);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(this.isInCreativeTab(tab))
		{
			ItemStack is = new ItemStack(this);
			is.setItemDamage(64);
			items.add(is);
		}
	}
	
	@Override
	public Item setNoRepair()
	{
		return this;
	}
	
	@Override
	public boolean isRepairable()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		final String info = TextFormatting.DARK_GRAY + I18n.format(this.getUnlocalizedName() + ".info");

		tooltip.add(info);
	}
	
	@Override
	public void registerModels() 
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
	
	public static boolean isUsable(ItemStack stack)
	{
		return stack.getItemDamage() < stack.getMaxDamage() - 1;
	}
	
	private ItemStack getStack(EntityPlayer player, Item toFind)
	{
		for(ItemStack stack : player.inventory.mainInventory) if(stack.getItem() == toFind) return stack;
		for(ItemStack stack : player.inventory.offHandInventory) if(stack.getItem() == toFind) return stack;
		return ItemStack.EMPTY;
	}
}
