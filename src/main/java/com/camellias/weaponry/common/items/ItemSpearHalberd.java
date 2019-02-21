package com.camellias.weaponry.common.items;

import java.util.List;
import java.util.Set;

import com.camellias.weaponry.Main;
import com.camellias.weaponry.Reference;
import com.camellias.weaponry.init.ModItems;
import com.camellias.weaponry.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpearHalberd extends ItemTool implements IHasModel
{
	public ItemSpearHalberd(String name, ToolMaterial material, Set<Block> effectiveBlocks)
	{
		super(material, effectiveBlocks);
		this.setUnlocalizedName(Reference.MODID + "." + name);
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
		String info1 = TextFormatting.DARK_GRAY + I18n.format(this.getUnlocalizedName() + ".info1");
		String info2 = TextFormatting.DARK_GRAY + I18n.format(this.getUnlocalizedName() + ".info2");
		
		tooltip.add(info1);
		tooltip.add(info2);
	}
	
	@Override
	public void registerModels() 
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
