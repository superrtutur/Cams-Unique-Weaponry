package com.camellias.weaponry.common.entities;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWeightedNet extends EntityThrowable implements IEntityAdditionalSpawnData
{
	private EntityLivingBase thrower;
	
    public EntityWeightedNet(World world)
    {
        super(world);
    }
    
    public EntityWeightedNet(World world, EntityLivingBase thrower)
    {
        super(world, thrower);
        this.thrower = thrower;
    }
    
    @SideOnly(Side.CLIENT)
    public EntityWeightedNet(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }
    
    @Override
    protected void onImpact(RayTraceResult result)
    {
        EntityLivingBase entitylivingbase = this.getThrower();
        
        if(result.entityHit != null)
        {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, entitylivingbase), 2.0F);
            
            if(result.entityHit instanceof EntityLivingBase)
            {
            	EntityLivingBase entity = (EntityLivingBase) result.entityHit;
            	
            	entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 30 * 20, 100, true, false));
            }
        }
        
        if(result.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos blockpos = result.getBlockPos();
            TileEntity tileentity = this.world.getTileEntity(blockpos);

            this.setDead();
        }
    }
    
    @Override
    public void onUpdate()
    {
    	super.onUpdate();
    }
    
    @Override
    @Nullable
    public Entity changeDimension(int dimension, net.minecraftforge.common.util.ITeleporter teleporter)
    {
        if(this.thrower.dimension != dimension)
        {
            this.thrower = null;
        }
        
        return super.changeDimension(dimension, teleporter);
    }

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		
	}
}
