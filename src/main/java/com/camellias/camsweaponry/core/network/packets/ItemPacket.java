package com.camellias.camsweaponry.core.network.packets;

import com.camellias.camsweaponry.Main;
import com.camellias.camsweaponry.core.util.capabilities.ItemCap.IItemCap;
import com.camellias.camsweaponry.core.util.capabilities.ItemCap.ItemProvider;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ItemPacket implements IMessage {
	// A default constructor is always required
	public ItemPacket(){}

	int type = 0; //Value isn't used Yet
	boolean on = false; // Value Isn't Used Yet
	double damage = 0; //Used for `SPEAR_HALBERD` Damage Calculations
	int entityID = 0; //Used to get the Correct Entity

	public ItemPacket(int type, boolean on, double damage, int entityID)
	{
		this.type = type;
		this.on = on;
		this.damage = damage;
		this.entityID = entityID;
	}

	@Override public void toBytes(ByteBuf buf)
	{
		// Writes the int into the buf
		buf.writeInt(this.type);
		buf.writeBoolean(this.on);
		buf.writeDouble(this.damage);
		buf.writeInt(this.entityID);
	}

	@Override public void fromBytes(ByteBuf buf)
	{
		// Reads the int back from the buf. Note that if you have multiple values, you must read in the same order you wrote.
		this.type = buf.readInt();
		this.on = buf.readBoolean();
		this.damage = buf.readDouble();
		this.entityID = buf.readInt();
	}

	public static class ClientHandler implements IMessageHandler<ItemPacket, IMessage>
	{
		@Override public IMessage onMessage(ItemPacket message, MessageContext ctx)
		{
			Main.proxy.getThreadListener(ctx).addScheduledTask(() ->
			{
				if(Main.proxy.getEntityLivingBase(ctx, message.entityID) != null)
				{
					System.out.println("You sent the Packet to the Client Side");
				}
			});
			return null;
		}
	}

	public static class ServerHandler implements IMessageHandler<ItemPacket, IMessage>
	{
		@Override public IMessage onMessage(ItemPacket message, MessageContext ctx)
		{
			Main.proxy.getThreadListener(ctx).addScheduledTask(() ->
			{
				if(Main.proxy.getEntityLivingBase(ctx, message.entityID) != null)
				{
					final ItemStack heldItem = Main.proxy.getEntityLivingBase(ctx, message.entityID).getHeldItemMainhand();
					final IItemCap wepCap = heldItem.getCapability(ItemProvider.itemCapability, null);
					
					if(wepCap != null)
					{
						wepCap.setDamage(message.damage);
					}
				}
			});
			return null;
		}
	}
}