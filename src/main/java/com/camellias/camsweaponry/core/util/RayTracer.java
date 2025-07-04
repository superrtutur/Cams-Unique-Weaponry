package com.camellias.camsweaponry.core.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RayTracer
{
	public static void rayTraceEntity(Beam beam, Function<Entity, Boolean> consumer)
	{
		Vec3d start = beam.getStart();
		Vec3d lookVec = beam.getLookVec();
		Vec3d end = beam.getEnd();
		double dist = beam.getDist();
		World world = beam.getWorld();
		EntityPlayer player = beam.getPlayer();
		List<Entity> targets = world.getEntitiesInAABBexcluding(player, player.getEntityBoundingBox().expand(lookVec.x * dist, lookVec.y * dist, lookVec.z * dist).grow(1.0D, 1.0D, 1.0D),
				Predicates.and(EntitySelectors.NOT_SPECTATING, ent -> ent != null && ent.canBeCollidedWith()));
		List<Pair<Entity, Double>> hitTargets = new ArrayList<>();
		
		for(Entity target : targets)
		{
			AxisAlignedBB targetBB = target.getEntityBoundingBox().grow(target.getCollisionBorderSize());
			
			if(targetBB.contains(start))
			{
				hitTargets.add(Pair.of(target, 0.0));
			}
			else
			{
				RayTraceResult targetResult = targetBB.calculateIntercept(start, end);
				
				if(targetResult != null)
				{
					double d3 = start.distanceTo(targetResult.hitVec);
					
					if(d3 < dist)
					{
						hitTargets.add(Pair.of(target, d3));
					}
				}
			}
		}
		
		hitTargets.sort(Comparator.comparing(Pair::getRight));
		hitTargets.stream().filter(pair -> consumer.apply(pair.getLeft())).findFirst();
	}
	
	//Credits for this go to Zabi
	public static void drawLine(Vec3d start, Vec3d end, World world, Beam beam, double density, EnumParticleTypes particle)
	{
	    double dist = beam.getDist();
	    
	    for(double done = 0; done < dist; done += density)
	    {
	        double alpha = done / dist;
	        double x = interpolate(start.x, end.x, alpha);
	        double y = interpolate(start.y, end.y, alpha);
	        double z = interpolate(start.z, end.z, alpha);
	        
	        if(world.isRemote)
			{
				world.spawnParticle(particle, x, y, z, 0, 0, 0);
			}
	    }
	}
	
	private static double interpolate(double start, double end, double alpha)
	{
	    return start + (end - start) * alpha;
	}
	
	public static class Beam
	{
		private World world;
		private EntityPlayer player;
		private double maxDist;
		private Vec3d start;
		private Vec3d lookVec;
		private Vec3d end;
		private double dist;
		private double accuracy;
		private boolean canBreak;
		private Random rand = new Random();
		
		public Beam(World world, EntityPlayer player, double maxDist, double accuracy, boolean canBreak)
		{
			this.world = world;
			this.player = player;
			this.maxDist = maxDist;
			this.accuracy = accuracy / 2;
			this.canBreak = canBreak;
			
			calculate();
		}
		
		private void calculate()
		{
			float prevYaw = this.player.rotationYaw;
			float prevPitch = this.player.rotationPitch;
			
			start = this.player.getPositionEyes(1.0f);
			this.player.rotationYaw += ((2 * accuracy) * rand.nextDouble()) - accuracy;
			this.player.rotationYaw = this.player.rotationYaw % 360;
			this.player.rotationPitch += ((2 * accuracy) * rand.nextDouble()) - accuracy;
			lookVec = this.player.getLookVec();
			this.player.rotationYaw = prevYaw;
			this.player.rotationPitch = prevPitch;
			end = start.add(new Vec3d(lookVec.x * this.maxDist, lookVec.y * this.maxDist, lookVec.z * this.maxDist));
			RayTraceResult result = this.world.rayTraceBlocks(start, end);
			dist = this.maxDist;
			
			if(result != null && result.typeOfHit == Type.BLOCK)
			{
				BlockPos pos = result.getBlockPos();
				
				if(world.getBlockState(pos).getCollisionBoundingBox(world, pos) != Block.NULL_AABB)
				{
					if(canBreak && !world.isRemote)
					{
						if(player.isAllowEdit())
						{
							if(world.getBlockState(pos).getMaterial() == Material.GLASS || world.getBlockState(pos).getMaterial() == Material.ICE)
							{
								world.destroyBlock(pos, false);
							}
						}
					}
					
					dist = result.hitVec.distanceTo(start);
					end = start.add(new Vec3d(lookVec.x * dist, lookVec.y * dist, lookVec.z * dist));
				}
			}
		}
		
		public Vec3d getStart()
		{
			return start;
		}
		
		public Vec3d getLookVec()
		{
			return lookVec;
		}
		
		public Vec3d getEnd()
		{
			return end;
		}
		
		public double getDist()
		{
			return dist;
		}
		
		public World getWorld()
		{
			return world;
		}
		
		public EntityPlayer getPlayer()
		{
			return player;
		}
		
		public double getMaxDist()
		{
			return maxDist;
		}
	}
}
