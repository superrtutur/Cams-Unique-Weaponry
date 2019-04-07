package com.camellias.camsweaponry.common.items;

import java.util.List;
import java.util.Set;

import com.camellias.camsweaponry.Main;
import com.camellias.camsweaponry.Reference;
import com.camellias.camsweaponry.core.init.ModItems;
import com.camellias.camsweaponry.core.util.IHasModel;
import com.camellias.camsweaponry.core.util.capabilities.ItemCap.ItemProvider;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemJinHalberd extends ItemTool implements IHasModel
{
	public ItemJinHalberd(String name, ToolMaterial material, Set<Block> effectiveBlocks)
	{
		super(material, effectiveBlocks);
		this.setTranslationKey(Reference.MODID + "." + name);
		this.setRegistryName(name);
		setCreativeTab(Main.WEAPONRY_TAB);
		this.attackSpeed = -3.0F;
		this.attackDamage = material.getAttackDamage();

		ModItems.ITEMS.add(this);
	}

	@SideOnly(Side.CLIENT)
	@Override
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

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new ItemProvider();
	}
}
