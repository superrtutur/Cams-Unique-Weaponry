package com.camellias.camsweaponry.common.items;

import com.camellias.camsweaponry.Main;
import com.camellias.camsweaponry.Reference;
import com.camellias.camsweaponry.core.init.ModItems;
import com.camellias.camsweaponry.core.util.IHasModel;
import com.camellias.camsweaponry.core.util.RayTracer;
import com.camellias.camsweaponry.core.util.RayTracer.Beam;
import fr.dynamx.common.items.DynamXItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemMusket extends DynamXItem implements IHasModel
{
	public ItemMusket(String name)
	{
		//this.setUnlocalizedName(Reference.MODID + "." + name);
		//this.setRegistryName(name);
		//this.setCreativeTab(Main.WEAPONRY_TAB);

		super(Reference.MODID, name, new ResourceLocation(Reference.MODID, "models/item/musket/musket.obj"));

		this.setMaxStackSize(1);
		this.setMaxDamage(256);
		this.setCreativeTab(Main.WEAPONRY_TAB);
		this.addPropertyOverride(new ResourceLocation("ads"), new IItemPropertyGetter()
		{
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, World world, EntityLivingBase entity)
			{
				GameSettings settings = Minecraft.getMinecraft().gameSettings;
				GuiScreen gui = Minecraft.getMinecraft().currentScreen;
				KeyBinding rclick = settings.keyBindUseItem;
				
				if(stack.hasTagCompound())
				{
					if(stack.getTagCompound().hasKey("isLoaded"))
					{
						return entity != null && settings.isKeyDown(rclick) && gui == null && stack.getTagCompound().getBoolean("isLoaded") ? 1.0F : 0.0F;
					}
					else return 0.0F;
				}
				else return 0.0F;
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
					player.getCooldownTracker().setCooldown(this, 300);
					world.playSound(player, player.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 10.0F, 2.0F);
					player.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 100.0F, 2.0F);
					
					Beam beam = new Beam(world, player, 128.0D, 1D, true);
					
					RayTracer.rayTraceEntity(beam, target ->
			        {
			            if(target instanceof EntityLivingBase)
			            {
			            	target.attackEntityFrom(DamageSource.causePlayerDamage(player), 40);
			                
			                return true;
			            }
			            else
			            {
			                return false;
			            }
			        });
					
					RayTracer.drawLine(beam.getStart(), beam.getEnd(), world, beam, 0.5D, EnumParticleTypes.SMOKE_NORMAL);
					
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
		return 60;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
    {
		return EnumAction.NONE;
    }
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		ItemStack stack = new ItemStack(Items.IRON_INGOT);
		
		if(!stack.isEmpty())
		{
			return true;
		}
		
		return super.getIsRepairable(toRepair, repair);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		final String info1 = TextFormatting.DARK_GRAY + I18n.format(this.getUnlocalizedName() + ".info1");
		final String info2 = TextFormatting.DARK_GRAY + I18n.format(this.getUnlocalizedName() + ".info2");

		tooltip.add(info1);
		tooltip.add(info2);
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
