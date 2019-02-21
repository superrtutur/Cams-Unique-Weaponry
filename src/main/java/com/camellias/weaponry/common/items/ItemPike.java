package com.camellias.weaponry.common.items;

import java.util.List;
import java.util.Set;

import com.camellias.weaponry.Main;
import com.camellias.weaponry.init.ModItems;
import com.camellias.weaponry.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPike extends ItemTool implements IHasModel
{
	public ItemPike(String name, ToolMaterial material, Set<Block> effectiveBlocks)
	{
		super(material, effectiveBlocks);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(Main.WEAPONRY_TAB);
		this.attackSpeed = -3.0F;
		this.attackDamage = material.getAttackDamage();
		
		ModItems.ITEMS.add(this);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		String info = TextFormatting.DARK_GRAY + I18n.format(this.getUnlocalizedName() + ".info");
		
		tooltip.add(info);
	}
	
	@Override
	public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker)
	{
		entity.attackEntityFrom(DamageSource.causeMobDamage(attacker), attackDamage / 2);
		return true;
	}
	
	@Override
	public void registerModels() 
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
