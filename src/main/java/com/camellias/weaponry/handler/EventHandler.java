package com.camellias.weaponry.handler;

import com.camellias.weaponry.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class EventHandler
{
	@SubscribeEvent
	public void onAttack(LivingDamageEvent event)
	{
		EntityLivingBase entity = event.getEntityLiving();
		
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
						float initialDamage = event.getAmount();
						double velocityX = Math.abs(player.motionX);
						double velocityZ = Math.abs(player.motionZ);
						double d8 = (Math.sqrt(velocityX * velocityX + velocityZ * velocityZ) * 10);
						
						event.setAmount((float) (initialDamage * (1 + (d8 * 0.75))));
						
						System.out.println(velocityX);
						System.out.println(velocityZ);
					}
				}
			}
			
			/**
			 * Pike Code
			 */
			if(attacker.getHeldItemMainhand().getItem() == ModItems.PIKE)
			{
				if(!attacker.world.isRemote)
				{
					if((entity.getHeldItemMainhand().getItem() == Items.SHIELD) || (entity.getHeldItemOffhand().getItem() == Items.SHIELD))
					{
						
					}
				}
			}
		}
	}
}
