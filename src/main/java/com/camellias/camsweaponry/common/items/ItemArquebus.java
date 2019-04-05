package com.camellias.camsweaponry.common.items;

import com.camellias.camsweaponry.Main;
import com.camellias.camsweaponry.Reference;
import com.camellias.camsweaponry.common.items.misc.ItemIronBullet;
import com.camellias.camsweaponry.common.items.misc.ItemPowderBag;
import com.camellias.camsweaponry.core.init.ModItems;
import com.camellias.camsweaponry.core.network.NetworkHandler;
import com.camellias.camsweaponry.core.network.packets.ArquebusPacket;
import com.camellias.camsweaponry.core.util.IHasModel;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemArquebus extends Item implements IHasModel
{
	public ItemArquebus(String name)
	{
		this.setTranslationKey(Reference.MODID + "." + name);
		this.setRegistryName(name);
		this.setCreativeTab(Main.WEAPONRY_TAB);
		this.setMaxStackSize(1);
		this.setMaxDamage(251);
		
		ModItems.ITEMS.add(this);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft)
	{
		if(entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			
			if(!stack.hasTagCompound())
			{
				stack.setTagCompound(new NBTTagCompound());
			}
			
			NBTTagCompound nbt = stack.getTagCompound();
			
			if(!nbt.getBoolean("isLoaded"))
			{
				ItemStack bulletStack = this.findAmmo(player);
				ItemStack powderStack = this.findPowder(player);
				ItemPowderBag powderItem = (ItemPowderBag) powderStack.getItem();
				
				if((!bulletStack.isEmpty() && !powderStack.isEmpty()) || player.capabilities.isCreativeMode)
				{
					if(!player.world.isRemote)
					{
						if(!player.capabilities.isCreativeMode)
						{
							bulletStack.shrink(1);
							powderStack.damageItem(1, player);
							
							nbt.setBoolean("isLoaded", false);
						}
						else
						{
							nbt.setBoolean("isLoaded", false);
						}
					}
				}
			}
			else
			{
				
			}
		}
	}
	
	@Override
	public boolean onEntitySwing(EntityLivingBase entity, ItemStack stack)
	{
		if(entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			
			if(!stack.hasTagCompound())
			{
				stack.setTagCompound(new NBTTagCompound());
			}
			
			NBTTagCompound nbt = stack.getTagCompound();
			
			if(nbt.getBoolean("isLoaded"))
			{
				nbt.setBoolean("isLoaded", false);
				stack.damageItem(1, player);
				NetworkHandler.INSTANCE.sendToServer(new ArquebusPacket(player));
			}
		}
		
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack itemstack = player.getHeldItem(hand);
		boolean flag = !this.findAmmo(player).isEmpty() && this.findPowder(player).getItemDamage() < this.findPowder(player).getMaxDamage();
		
		if(!player.capabilities.isCreativeMode && !flag)
		{
			return flag ? new ActionResult(EnumActionResult.PASS, itemstack) : new ActionResult(EnumActionResult.FAIL, itemstack);
		}
		else
		{
			player.setActiveHand(hand);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		}
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.NONE;
	}
	
	@Override
	public void registerModels() 
	{
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}
	
	
	
	//--------------------------------------------------------------------------------------------------//
	
	
	
	public ItemStack findPowder(EntityPlayer player)
	{
		if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND)))
		{
			return player.getHeldItem(EnumHand.OFF_HAND);
		}
		else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND)))
		{
			return player.getHeldItem(EnumHand.MAIN_HAND);
		}
		else
		{
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
			{
				ItemStack itemstack = player.inventory.getStackInSlot(i);
				
				if (this.isAmmo(itemstack))
				{
					return itemstack;
				}
			}
			
			return ItemStack.EMPTY;
		}
	}
	
	public ItemStack findAmmo(EntityPlayer player)
	{
		if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND)))
		{
			return player.getHeldItem(EnumHand.OFF_HAND);
		}
		else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND)))
		{
			return player.getHeldItem(EnumHand.MAIN_HAND);
		}
		else
		{
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
			{
				ItemStack itemstack = player.inventory.getStackInSlot(i);
				
				if (this.isAmmo(itemstack))
				{
					return itemstack;
				}
			}
			
			return ItemStack.EMPTY;
		}
	}
	
	public boolean isPowder(ItemStack stack)
	{
		return stack.getItem() instanceof ItemPowderBag;
	}
	
	public boolean isAmmo(ItemStack stack)
	{
		return stack.getItem() instanceof ItemIronBullet;
	}
}
