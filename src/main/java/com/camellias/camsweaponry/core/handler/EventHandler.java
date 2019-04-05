package com.camellias.camsweaponry.core.handler;

import java.lang.reflect.Field;

import com.camellias.camsweaponry.core.init.ModItems;
import com.camellias.camsweaponry.core.network.NetworkHandler;
import com.camellias.camsweaponry.core.network.packets.ItemPacket;
import com.camellias.camsweaponry.core.util.capabilities.ItemCap.IItemCap;
import com.camellias.camsweaponry.core.util.capabilities.ItemCap.ItemProvider;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EventHandler
{
	public final Field isUnblockable = ReflectionHelper.findField(DamageSource.class, "field_76374_o", "isUnblockable");
	
	@SubscribeEvent
	public void onPlayerAttack(AttackEntityEvent event)
	{
		EntityPlayer attacker = event.getEntityPlayer();
		
		/**
		 * Spear-Halberd Code
		 */
		if(attacker.getHeldItemMainhand().getItem() == ModItems.SPEAR_HALBERD)
		{
			if(attacker.world.isRemote)
			{
				if(!attacker.isRiding())
				{
					double velocityX = Math.abs(attacker.motionX);
					double velocityY = Math.abs(attacker.motionY);
					double velocityZ = Math.abs(attacker.motionZ);
					
					double d8 = (Math.sqrt((velocityX * velocityX) + (velocityY * velocityY) + (velocityZ * velocityZ)) * 10);
					NetworkHandler.INSTANCE.sendToServer(new ItemPacket(0, false, d8, attacker.getEntityId()));
				}
				else
				{
					double velocityX = Math.abs(attacker.getRidingEntity().motionX);
					double velocityY = Math.abs(attacker.getRidingEntity().motionY);
					double velocityZ = Math.abs(attacker.getRidingEntity().motionZ);
					
					double d8 = (Math.sqrt((velocityX * velocityX) + (velocityY * velocityY) + (velocityZ * velocityZ)) * 10);
					NetworkHandler.INSTANCE.sendToServer(new ItemPacket(0, false, d8, attacker.getEntityId()));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event)
	{
		if(event.getSource().getTrueSource() instanceof EntityLivingBase)
		{
			EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
			
			/**
			 * Pike Code
			 */
			if(attacker.getHeldItemMainhand().getItem() == ModItems.PIKE)
			{
				DamageSource source = event.getSource();
				
				if(source.isUnblockable() == false)
				{
					try
					{
						isUnblockable.set(source, true);
					}
					catch(IllegalArgumentException | IllegalAccessException e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					try
					{
						isUnblockable.set(source, true);
					}
					catch(IllegalArgumentException | IllegalAccessException e)
					{
						e.printStackTrace();
					}
				}
				
				System.out.println(event.getSource().isUnblockable());
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event)
	{
		if(event.getSource().getTrueSource() instanceof EntityLivingBase)
		{
			EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();
			
			/**
			 * Macil Code
			 */
			if(attacker.getHeldItemMainhand().getItem() == ModItems.MACIL)
			{
				if(!attacker.world.isRemote)
				{
					float initialDamage = event.getAmount();
					double armour = event.getEntityLiving().getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ARMOR).getAttributeValue();
					
					event.setAmount((float) (initialDamage + (armour / 3)));
				}
			}
			
			/**
			 * Spear-Halberd Code
			 */
			if(attacker instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) attacker;
				
				if(player.getHeldItemMainhand().getItem() == ModItems.SPEAR_HALBERD)
				{
					if(!player.world.isRemote)
					{
						ItemStack heldItem = player.getHeldItemMainhand();
						IItemCap cap = heldItem.getCapability(ItemProvider.itemCapability, null);
						float initialDamage = event.getAmount();
						double d8 = cap.Damage();
						
						event.setAmount((float) (initialDamage * (1 + (d8 * 0.75))));
					}
				}
			}
		}
	}
}
