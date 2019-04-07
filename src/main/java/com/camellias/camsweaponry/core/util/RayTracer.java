package com.camellias.camsweaponry.core.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
	
    public static class Beam
    {
        private World world;
        private EntityPlayer player;
        private double maxDist;
        private Vec3d start;
        private Vec3d lookVec;
        private Vec3d end;
        private double dist;
        private boolean canBreak;
        
        public Beam(World world, EntityPlayer player, double maxDist, boolean canBreak)
        {
            this.world = world;
            this.player = player;
            this.maxDist = maxDist;
            this.canBreak = canBreak;

            calculate();
        }
        
        private void calculate()
        {
            start = this.player.getPositionEyes(1.0f);
            lookVec = this.player.getLookVec();
            end = start.add(lookVec.x * this.maxDist, lookVec.y * this.maxDist, lookVec.z * this.maxDist);
            RayTraceResult result = this.world.rayTraceBlocks(start, end);
            BlockPos pos = result.getBlockPos();
            dist = this.maxDist;
            
            if(result != null && result.typeOfHit == Type.BLOCK && world.getBlockState(pos).getCollisionBoundingBox(world, pos) != Block.NULL_AABB)
            {
            	if(canBreak)
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
                end = start.add(lookVec.x * dist, lookVec.y * dist, lookVec.z * dist);
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
