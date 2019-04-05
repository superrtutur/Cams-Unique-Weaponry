package com.camellias.camsweaponry.common.entities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class EntityBullet extends EntityThrowable
{
	public EntityBullet(World world)
	{
		super(world);
		this.setSize(0.25F, 0.25F);
	}
	
	public EntityBullet(World world, EntityPlayer player)
	{
		super(world);
	}
	
	@Override
	public void onImpact(RayTraceResult result)
	{
		if(result.typeOfHit == Type.ENTITY)
		{
			Entity entity = result.entityHit;
			
			entity.attackEntityFrom(DamageSource.ANVIL, 10);
		}
		if(result.typeOfHit == Type.BLOCK)
		{
			BlockPos pos = result.getBlockPos();
			Block block = world.getBlockState(pos).getBlock();
			
			if(block instanceof BlockGlass)
			{
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 2F, 1F, true);
			}
		}
	}
	
	@Override
	public void entityInit()
	{
		super.entityInit();
		setEntityInvulnerable(true);
		setNoGravity(true);
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if(world.isRemote)
		{
			for(int i = 0; i < 5; i++)
			{
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, 0, 0, 0);
			}
		}
	}
}
