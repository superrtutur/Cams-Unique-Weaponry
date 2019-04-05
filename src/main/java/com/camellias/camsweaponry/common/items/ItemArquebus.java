package com.camellias.camsweaponry.common.items;

import com.camellias.camsweaponry.Main;
import com.camellias.camsweaponry.Reference;
import com.camellias.camsweaponry.core.init.ModItems;
import com.camellias.camsweaponry.core.util.IHasModel;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
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
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft)
    {
		if(!world.isRemote)
		{
			if(living instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) living;
				ItemStack ammo = getAmmo(player);
				ItemStack powder = getPowder(player);
				
				if(!stack.hasTagCompound())
				{
					stack.setTagCompound(new NBTTagCompound());
				}
				
				NBTTagCompound nbt = stack.getTagCompound();
				
				if(!nbt.hasKey("isLoaded"))
				{
					nbt.setBoolean("isLoaded", true);
				}
				else
				{
					if(timeLeft >= getMaxItemUseDuration(stack))
					{
						if(!nbt.getBoolean("isLoaded"))
						{
							if(!player.isCreative())
							{
								ammo.shrink(1);
								powder.damageItem(1, player);
							}
						}
						
						if(nbt.getBoolean("isLoaded"))
						{
							if(!player.isCreative())
							{
								stack.damageItem(1, player);
							}
							
							EntityArrow bullet = new EntityTippedArrow(world, player);
							bullet.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.0F, 1.0F);
							world.spawnEntity(bullet);
						}
						
						nbt.setBoolean("isLoaded", !nbt.getBoolean("isLoaded"));
					}
				}
			}
		}
    }
	
	@Override
	public boolean onEntitySwing(EntityLivingBase entity, ItemStack stack)
	{
		if(entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			World world = player.world;
		}
		
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        
        if (player.isCreative() || (!getAmmo(player).isEmpty() && !getPowder(player).isEmpty() &&
        		(getPowder(player).getItemDamage() < getPowder(player).getMaxDamage() - 1)))
        {
            player.setActiveHand(hand);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        else return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return 100;
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
	
	
	
	private ItemStack getAmmo(EntityPlayer player)
	{
		for(ItemStack stack : player.inventory.mainInventory)
			if(stack.getItem() == ModItems.BULLET) return stack;
		
		for(ItemStack stack : player.inventory.offHandInventory)
			if(stack.getItem() == ModItems.BULLET) return stack;
		
		return ItemStack.EMPTY;
	}
	
	private ItemStack getPowder(EntityPlayer player)
	{
		for(ItemStack stack : player.inventory.mainInventory)
			if(stack.getItem() == ModItems.POWDER_BAG) return stack;
		
		for(ItemStack stack : player.inventory.offHandInventory)
			if(stack.getItem() == ModItems.POWDER_BAG) return stack;
		
		return ItemStack.EMPTY;
	}
}
