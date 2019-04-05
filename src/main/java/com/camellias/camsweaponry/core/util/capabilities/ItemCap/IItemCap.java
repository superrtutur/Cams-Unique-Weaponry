package com.camellias.camsweaponry.core.util.capabilities.ItemCap;

import net.minecraft.nbt.NBTTagCompound;

public interface IItemCap {

	boolean effect();

	void setEffect(boolean transformed);

	int Type();

	void setType(int type);

	double Damage();

	void setDamage(double value);

	NBTTagCompound saveNBT();

	void loadNBT(NBTTagCompound compound);

}