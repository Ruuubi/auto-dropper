package mods.autodropper;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BehaviorAutoDropper implements IBehaviorDispenseItem {

	public final ItemStack dispense(IBlockSource source, ItemStack stack) {
	      ItemStack dispensedStack = this.dispenseStack(source, stack);
	      source.getWorld().playEvent(1000, source.getBlockPos(), 0);
	      source.getWorld().playEvent(2000, source.getBlockPos(), source.getBlockState().get(BlockDispenser.FACING).getIndex());
	      return dispensedStack;
	   }

	   protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
	      EnumFacing enumfacing = source.getBlockState().get(BlockDispenser.FACING);
	      IPosition iposition = BlockDispenser.getDispensePosition(source);
	      ItemStack itemstack = stack.split(1);
	      doDispense(source.getWorld(), itemstack, enumfacing, iposition);
	      return stack;
	   }

	   public static void doDispense(World world, ItemStack stack, EnumFacing facing, IPosition position) {
	      double x = position.getX();
	      double y = position.getY();
	      double z = position.getZ();
	      if (facing == EnumFacing.DOWN) {
	        	EntityItem entityitem = new EntityItem(world, x, y -0.2, z, stack);
	        	entityitem.motionX = 0;
	        	entityitem.motionY = 0;
	        	entityitem.motionZ = 0;
	        	world.spawnEntity(entityitem);
	        } else if (facing == EnumFacing.UP) {
	        	EntityItem entityitem = new EntityItem(world, x, y + 0.05, z, stack);
	        	entityitem.motionX = (double)facing.getXOffset() * 0.3D;
		        entityitem.motionY = 0;
		        entityitem.motionZ = (double)facing.getZOffset() * 0.3D;
	        	world.spawnEntity(entityitem);
	        } else {
	        	EntityItem entityitem = new EntityItem(world, x, y - 0.1, z, stack);
		        entityitem.motionX = (double)facing.getXOffset() * 0.3D;
		        entityitem.motionY = 0.2F;
		        entityitem.motionZ = (double)facing.getZOffset() * 0.3D;
		        world.spawnEntity(entityitem);
	        }
	   }

}
