package com.camellias.camsweaponry.common.items;

import com.camellias.camsweaponry.Main;
import com.camellias.camsweaponry.Reference;
import com.camellias.camsweaponry.common.entities.EntityBullet;
import com.camellias.camsweaponry.core.init.ModItems;
import com.camellias.camsweaponry.core.util.IHasModel;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArquebus extends Item implements IHasModel
{
	public ItemArquebus(String name)
	{
		this.setTranslationKey(Reference.MODID + "." + name);
		this.setRegistryName(name);
		this.setCreativeTab(Main.WEAPONRY_TAB);
		this.setMaxStackSize(1);
		this.setMaxDamage(251);
		
		this.addPropertyOverride(new ResourceLocation("ads"), new IItemPropertyGetter()
		{
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, World world, EntityLivingBase entity)
			{
				return entity != null && entity.isHandActive() && stack.getTagCompound().getBoolean("isLoaded") &&
						entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
			}
		});
		
		ModItems.ITEMS.add(this);
	}
	
	@Override
	public boolean onEntitySwing(EntityLivingBase entity, ItemStack stack)
	{
		if(entity instanceof EntityPlayer)
		{
			World world = entity.world;
			EntityPlayer player = (EntityPlayer) entity;
			
			ItemStack bullet = this.getStack(player, ModItems.BULLET);
			ItemStack powder = this.getStack(player, ModItems.POWDER_BAG);
		
			if(!stack.hasTagCompound())
			{
				stack.setTagCompound(new NBTTagCompound());
			}
			
			if(!stack.getTagCompound().hasKey("isLoaded"))
			{
				stack.getTagCompound().setBoolean("isLoaded", false);
			}
			else
			{
				if(stack.getTagCompound().getBoolean("isLoaded"))
				{
					stack.damageItem(1, player);
					player.getCooldownTracker().setCooldown(this, 40);
					world.playSound(player.posX, player.posY + player.height, player.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE,
							SoundCategory.MASTER, 10.0F, 2.0F, true);
					
					EntityBullet entitybullet = new EntityBullet(world, player);
					entitybullet.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 10.0F, 1.0F);
					entitybullet.setPosition(player.posX, player.posY + player.eyeHeight, player.posZ);
					world.spawnEntity(entitybullet);
					
					stack.getTagCompound().setBoolean("isLoaded", false);
				}
			}
		}
		
		return true;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
	{
		if(!world.isRemote)
		{
			if(entity instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) entity;
				
				if(!stack.getTagCompound().getBoolean("isLoaded"))
				{
					if(!player.isCreative())
					{
						getStack(player, ModItems.BULLET).shrink(1);
						getStack(player, ModItems.POWDER_BAG).damageItem(1, player);
					}
					
					stack.getTagCompound().setBoolean("isLoaded", !stack.getTagCompound().getBoolean("isLoaded"));
				}
			}
		}
		
		return stack;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		ItemStack bullet = getStack(player, ModItems.BULLET);
		ItemStack powder = getStack(player, ModItems.POWDER_BAG);
		
		if(player.isCreative() || (!bullet.isEmpty() && !powder.isEmpty() && (powder.getItemDamage() < powder.getMaxDamage())))
		{
			if(!stack.hasTagCompound())
			{
				stack.setTagCompound(new NBTTagCompound());
			}
			
			if(!stack.getTagCompound().hasKey("isLoaded"))
			{
				stack.getTagCompound().setBoolean("isLoaded", false);
			}
			else
			{
				if(!stack.getTagCompound().getBoolean("isLoaded"))
				{
					player.setActiveHand(hand);
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
				}
				else
				{
					player.setActiveHand(hand);
					return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
				}
			}
			
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
		}
		
		else return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return !stack.getTagCompound().getBoolean("isLoaded") ? 60 : 7200;
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
	
	
	
	private ItemStack getStack(EntityPlayer player, Item toFind)
	{
		for(ItemStack stack : player.inventory.mainInventory) if(stack.getItem() == toFind) return stack;
		for(ItemStack stack : player.inventory.offHandInventory) if(stack.getItem() == toFind) return stack;
		return ItemStack.EMPTY;
	}
}
