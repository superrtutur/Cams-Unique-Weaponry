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
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class ItemMacil extends ItemTool implements IHasModel
{
	public ItemMacil(String name, ToolMaterial material, Set<Block> effectiveBlocks)
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
	public void registerModels() 
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------//
	
	@SubscribeEvent
	public static void onAttack(LivingDamageEvent event)
	{
		if(event.getSource().getTrueSource() instanceof EntityLivingBase)
		{
			EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
			
			if(attacker.getHeldItemMainhand().getItem() == ModItems.MACIL)
			{
				if(!attacker.world.isRemote)
				{
					float initialDamage = event.getAmount();
					double armour = event.getEntityLiving().getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ARMOR).getAttributeValue();
					
					event.setAmount((float) (initialDamage + (armour / 3)));
				}
			}
		}
	}
}
