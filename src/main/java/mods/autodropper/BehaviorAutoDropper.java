package mods.autodropper;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public class BehaviorAutoDropper implements IDispenseItemBehavior {

	public final ItemStack dispense(IBlockSource source, ItemStack stack) {
	      ItemStack dispensedStack = this.dispenseStack(source, stack);
	      source.getWorld().playEvent(1000, source.getBlockPos(), 0);
	      source.getWorld().playEvent(2000, source.getBlockPos(), source.getBlockState().get(DispenserBlock.FACING).getIndex());
	      return dispensedStack;
	   }

	   protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
		  Direction enumfacing = source.getBlockState().get(DispenserBlock.FACING);
	      IPosition iposition = DispenserBlock.getDispensePosition(source);
	      ItemStack itemstack = stack.split(1);
	      doDispense(source.getWorld(), itemstack, enumfacing, iposition);
	      return stack;
	   }

	   public static void doDispense(World world, ItemStack stack, Direction facing, IPosition position) {
	      double x = position.getX();
	      double y = position.getY();
	      double z = position.getZ();
	      if (facing == Direction.DOWN) {
	    	  ItemEntity entityitem = new ItemEntity(world, x, y -0.2, z, stack);
	    	  entityitem.setMotion(0, 0, 0);
	        	world.addEntity(entityitem); //world.spawnEntity
	        } else if (facing == Direction.UP) {
	        	ItemEntity entityitem = new ItemEntity(world, x, y + 0.05, z, stack);
	        	entityitem.setMotion((double)facing.getXOffset() * 0.3D, 0, (double)facing.getZOffset() * 0.3D);
	        	world.addEntity(entityitem); //world.spawnEntity
	        } else {
	        	ItemEntity entityitem = new ItemEntity(world, x, y - 0.1, z, stack);
	        	entityitem.setMotion((double)facing.getXOffset() * 0.3D, 0.2F, (double)facing.getZOffset() * 0.3D);
		        world.addEntity(entityitem); //world.spawnEntity
	        }
	   }

}
