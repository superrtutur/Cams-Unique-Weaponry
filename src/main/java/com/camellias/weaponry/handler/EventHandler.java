package com.camellias.weaponry.handler;

import com.camellias.weaponry.capabilities.ItemCap.IItemCap;
import com.camellias.weaponry.capabilities.ItemCap.ItemProvider;
import com.camellias.weaponry.init.ModItems;
import com.camellias.weaponry.network.ItemPacket;
import com.camellias.weaponry.network.NetworkHandler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler
{

	@SubscribeEvent
	public void attackEvent(AttackEntityEvent event) {
		EntityLivingBase attacker = event.getEntityLiving();
		if(attacker instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) attacker;

			if(player.getHeldItemMainhand().getItem() == ModItems.SPEAR_HALBERD)
			{
				if(player.world.isRemote)
				{
					double velocityX = Math.abs(player.motionX);
					double velocityZ = Math.abs(player.motionZ);
					double d8 = (Math.sqrt((velocityX * velocityX) + (velocityZ * velocityZ)) * 10);
					NetworkHandler.INSTANCE.sendToServer(new ItemPacket(0, false, d8, player.getEntityId()));
				}
			}
		}
	}

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
						ItemStack heldItem = player.getHeldItemMainhand();
						IItemCap cap = heldItem.getCapability(ItemProvider.itemCapability, null);
						float initialDamage = event.getAmount();
						double d8 = cap.Damage();

						event.setAmount((float) (initialDamage * (1 + (d8 * 0.75))));

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
