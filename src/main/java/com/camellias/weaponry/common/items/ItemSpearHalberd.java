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

@EventBusSubscriber
public class ItemSpearHalberd extends ItemTool implements IHasModel
{
	public ItemSpearHalberd(String name, ToolMaterial material, Set<Block> effectiveBlocks)
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
	
	//---------------------------------------------------------------------------------------------------------------------------------//
	
	private static float motionX;
	private static float motionZ;
	
	public static void setMotionX(float motion)
	{
		motionX = motion;
	}
	
	public static float getMotionX()
	{
		return motionX;
	}
	
	public static void setMotionZ(float motion)
	{
		motionZ = motion;
	}
	
	public static float getMotionZ()
	{
		return motionZ;
	}
	
	@SubscribeEvent
	public static void onAttack(LivingDamageEvent event)
	{
		if(event.getSource().getTrueSource() instanceof EntityLivingBase)
		{
			EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
			
			if(attacker.getHeldItemMainhand().getItem() == ModItems.SPEAR_HALBERD)
			{
				if(!attacker.world.isRemote)
				{
					float initialDamage = event.getAmount();
					double velocityX = Math.abs(getMotionX());
					double velocityZ = Math.abs(getMotionZ());
					double d8 = (Math.sqrt(velocityX * velocityX + velocityZ * velocityZ) * 10);
					
					event.setAmount((float) (initialDamage * (1 + (d8 * 0.75))));
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onTick(PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		
		if(player.world.isRemote && event.phase == Phase.END)
		{
			if(player.isRiding())
			{
				setMotionX((float) player.getRidingEntity().motionX);
				setMotionZ((float) player.getRidingEntity().motionZ);
			}
			else
			{
				setMotionX((float) player.motionX);
				setMotionZ((float) player.motionZ);
			}
		}
	}
}
