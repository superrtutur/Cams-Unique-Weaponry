package com.camellias.camsweaponry.common.items;

import java.util.List;

import com.camellias.camsweaponry.Main;
import com.camellias.camsweaponry.Reference;
import com.camellias.camsweaponry.core.init.ModItems;
import com.camellias.camsweaponry.core.util.IHasModel;
import com.camellias.camsweaponry.core.util.RayTracer;
import com.camellias.camsweaponry.core.util.RayTracer.Beam;

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
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
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
		this.setMaxDamage(256);
		
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
					player.getCooldownTracker().setCooldown(this, 40);
					world.playSound(player, player.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 10.0F, 2.0F);
					player.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 100.0F, 2.0F);
					
					RayTracer.Beam beam = new RayTracer.Beam(world, player, 128.0D, true);
					
					RayTracer.rayTraceEntity(beam, target ->
			        {
			            if(target instanceof EntityLivingBase)
			            {
			            	target.attackEntityFrom(DamageSource.causePlayerDamage(player), 15);
			                
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
		final String info1 = TextFormatting.DARK_GRAY + I18n.format(this.getTranslationKey() + ".info1");
		final String info2 = TextFormatting.DARK_GRAY + I18n.format(this.getTranslationKey() + ".info2");

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
