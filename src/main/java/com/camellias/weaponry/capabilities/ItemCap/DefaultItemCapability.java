package com.camellias.weaponry.capabilities.ItemCap;

import net.minecraft.nbt.NBTTagCompound;

public class DefaultItemCapability implements IItemCap {

	int type = -1;
	double damage = 0;
	boolean effect = false;

	/*Default Constructor*/
	public DefaultItemCapability(){}


	public DefaultItemCapability(int type, boolean on) {
		this.type = type;
		this.effect = on;
	}

	@Override
	public boolean effect() {
		return this.effect;
	}

	@Override
	public void setEffect(boolean on) {
		if(this.effect != on) {
			this.effect = on;
		}
	}
	@Override
	public int Type() {
		return this.type;
	}

	@Override
	public void setType(int type) {
		if (this.type != type) {
			this.type = type;
		}
	}

	@Override
	public double Damage() {
		return this.damage;
	}

	@Override
	public void setDamage(double value) {
		if (this.damage != value) {
			this.damage = value;
		}
	}

	@Override
	public NBTTagCompound saveNBT() {
		return (NBTTagCompound) ItemStorage.storage.writeNBT(ItemProvider.itemCapability, this, null);
	}

	@Override
	public void loadNBT(NBTTagCompound compound) {
		ItemStorage.storage.readNBT(ItemProvider.itemCapability, this, null, compound);
	}

}
