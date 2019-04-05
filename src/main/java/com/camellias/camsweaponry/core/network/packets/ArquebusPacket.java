package com.camellias.camsweaponry.core.network.packets;

import com.camellias.camsweaponry.Main;
import com.camellias.camsweaponry.common.entities.EntityBullet;
import com.camellias.camsweaponry.common.items.ItemArquebus;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ArquebusPacket implements IMessage
{
	public ArquebusPacket()
	{
		
	}
	
	public int playerID;
	
	public ArquebusPacket(EntityPlayer player)
	{
		this.playerID = player.getEntityId();
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(playerID);
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.playerID = buf.readInt();
	}
	
//-------------------------------------------------------------------------------------------------------------------------//
	
	public static class ArquebusPacketHandler implements IMessageHandler<ArquebusPacket, IMessage>
	{
		@Override
		public IMessage onMessage(ArquebusPacket message, MessageContext ctx)
		{
			Main.proxy.getThreadListener(ctx).addScheduledTask(() ->
			{
				if(Main.proxy.getEntityLivingBase(ctx, message.playerID) != null)
				{
					EntityPlayer player = (EntityPlayer) Main.proxy.getEntityLivingBase(ctx, message.playerID);
					World world = ctx.getServerHandler().player.getServerWorld();
					
					if(!world.isRemote)
					{
						EntityBullet bullet = new EntityBullet(world, player);
						
						bullet.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 10.0F, 1.0F);
						world.spawnEntity(bullet);
					}
				}
			});
			
			return null;
		}
	}
}
