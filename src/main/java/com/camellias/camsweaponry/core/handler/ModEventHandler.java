package com.camellias.camsweaponry.core.handler;

import java.lang.reflect.Field;

import com.camellias.camsweaponry.common.items.ItemArquebus;
import com.camellias.camsweaponry.common.items.ItemMusket;
import com.camellias.camsweaponry.core.init.ModItems;
import com.camellias.camsweaponry.core.network.NetworkHandler;
import com.camellias.camsweaponry.core.network.packets.ItemPacket;
import com.camellias.camsweaponry.core.util.capabilities.ItemCap.IItemCap;
import com.camellias.camsweaponry.core.util.capabilities.ItemCap.ItemProvider;

import com.mrcrayfish.obfuscate.client.event.ModelPlayerEvent;
import fr.dynamx.api.events.client.DynamXRenderItemEvent;
import fr.dynamx.common.items.DynamXItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEventHandler
{

	@SubscribeEvent
	public void changeFOV(FOVUpdateEvent event)
	{
		if(event.getEntity() != null)
		{
			final EntityPlayer player = event.getEntity();
			GameSettings settings = Minecraft.getMinecraft().gameSettings;
			GuiScreen gui = Minecraft.getMinecraft().currentScreen;
			KeyBinding rclick = settings.keyBindUseItem;
			
			if(settings.isKeyDown(rclick) && gui == null)
			{
				if(player.getHeldItemMainhand().getItem() == ModItems.ARQUEBUS || player.getHeldItemMainhand().getItem() == ModItems.MUSKET)
				{
					ItemStack stack = player.getHeldItemMainhand();
					
					if(stack.hasTagCompound())
					{
						if(stack.getTagCompound().hasKey("isLoaded"))
						{
							if(stack.getTagCompound().getBoolean("isLoaded"))
							{
								event.setNewfov(event.getFov() * 0.65F);
							}
						}
					}
				}
				
				if(player.getHeldItemOffhand().getItem() == ModItems.ARQUEBUS || player.getHeldItemOffhand().getItem() == ModItems.MUSKET)
				{
					ItemStack stack = player.getHeldItemOffhand();
					
					if(stack.hasTagCompound())
					{
						if(stack.getTagCompound().hasKey("isLoaded"))
						{
							if(stack.getTagCompound().getBoolean("isLoaded"))
							{
								event.setNewfov(event.getFov() * 0.65F);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void setupPlayerRotations(ModelPlayerEvent.SetupAngles.Post event) {
		if(event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getUnlocalizedName().equals("item.dynx.camsweaponry.arquebus") || event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getUnlocalizedName().equals("item.dynx.camsweaponry.musket")) {
			if(event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getTagCompound() != null) {
				if(event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getTagCompound().getBoolean("isLoaded")) {
					ModelRenderer renderer = event.getModelPlayer().bipedRightArm;
					renderer.rotateAngleZ = event.getModelPlayer().bipedHead.rotateAngleZ;
					renderer.rotateAngleX = 80 + event.getModelPlayer().bipedHead.rotateAngleX * 1.5f;
					renderer.rotateAngleY = event.getModelPlayer().bipedHead.rotateAngleY;
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderDynamXItem(DynamXRenderItemEvent event) {
		if (event.getStage() == DynamXRenderItemEvent.EventStage.TRANSFORM) {
			switch (event.getContext().getRenderType()) {
				case FIRST_PERSON_LEFT_HAND:
					event.setCanceled(true);
					if (event.getContext().getStack().getItem() instanceof DynamXItem) {
						if(event.getContext().getStack().getItem().getUnlocalizedName().equals("item.dynx.camsweaponry.arquebus") || event.getContext().getStack().getItem().getUnlocalizedName().equals("item.dynx.camsweaponry.musket")) {
							GlStateManager.rotate(180, 0, 1, 0);
							GlStateManager.rotate(-90, 1, 0, 0);
							GlStateManager.translate(0.5, 1, 0.52);
						}
					}
					break;
				case FIRST_PERSON_RIGHT_HAND:
					event.setCanceled(true);
					if (event.getContext().getStack().getItem() instanceof DynamXItem) {
						if(event.getContext().getStack().getItem().getUnlocalizedName().equals("item.dynx.camsweaponry.arquebus") || event.getContext().getStack().getItem().getUnlocalizedName().equals("item.dynx.camsweaponry.musket")) {
							if(event.getContext().getStack().getTagCompound() != null) {
								if(event.getContext().getStack().getTagCompound().getBoolean("isLoaded")) {
									GlStateManager.translate(-0.05, 0.8, 1.04);
									GlStateManager.rotate(180, 0, 1, 0);
									break;
								}
							}
							GlStateManager.translate(0.5, 0.5, 0.52);
							GlStateManager.rotate(180, 0, 1, 0);
						}
					}
					break;
				case THIRD_PERSON_LEFT_HAND:
					event.setCanceled(true);
					if (event.getContext().getStack().getItem() instanceof DynamXItem) {
						if(event.getContext().getStack().getItem().getUnlocalizedName().equals("item.dynx.camsweaponry.arquebus") || event.getContext().getStack().getItem().getUnlocalizedName().equals("item.dynx.camsweaponry.musket")) {
							GlStateManager.translate(0.5, 0.5, 0.52);
						}
					}
					break;
				case THIRD_PERSON_RIGHT_HAND:
					event.setCanceled(true);
					if (event.getContext().getStack().getItem() instanceof DynamXItem) {
						if(event.getContext().getStack().getItem().getUnlocalizedName().equals("item.dynx.camsweaponry.arquebus") || event.getContext().getStack().getItem().getUnlocalizedName().equals("item.dynx.camsweaponry.musket")) {
							GlStateManager.translate(0.5, 0.5, 0.52);
							GlStateManager.rotate(180, 0, 1, 0);
						}
					}
					break;
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerAttack(AttackEntityEvent event)
	{
		EntityPlayer attacker = event.getEntityPlayer();
		
		/**
		 * Spear-Halberd Code
		 */
		if(attacker.getHeldItemMainhand().getItem() == ModItems.JIN_HALBERD)
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

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		if (player == null) return;

		ItemStack stack = player.getActiveItemStack();
		if (stack.isEmpty() || !(stack.getItem() instanceof ItemMusket || stack.getItem() instanceof ItemArquebus)) return;

		int screenWidth = event.getResolution().getScaledWidth();
		int screenHeight = event.getResolution().getScaledHeight();

		int maxUse = stack.getMaxItemUseDuration(); // 60 ticks
		int remaining = player.getItemInUseCount();
		int progress = maxUse - remaining;

		float percent = (float) progress / (float) maxUse;

		// Dimensions de la barre
		int barWidth = 30;
		int barHeight = 4;

		// Position centrée
		int x = (screenWidth / 2) - (barWidth / 2);
		int y = (screenHeight / 2) + 15; // 15px sous le crosshair

		// Barre vide (fond gris)
		Gui.drawRect(x, y, x + barWidth, y + barHeight, 0xFF555555);

		// Barre de remplissage (blanche ou colorée)
		int fillWidth = (int) (barWidth * percent);
		Gui.drawRect(x, y, x + fillWidth, y + barHeight, 0xFFFFFFFF);
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
				
				if(player.getHeldItemMainhand().getItem() == ModItems.JIN_HALBERD)
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
