package mods.autodropper;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class BehaviorAutoDropper implements DispenseItemBehavior {

	@Override
	public ItemStack dispense(BlockSource source, ItemStack stack) {
		ItemStack dispensedStack = this.dispenseStack(source, stack);
		source.getLevel().levelEvent(1000, source.getPos(), 0);
		source.getLevel().levelEvent(2000, source.getPos(), source.getBlockState().getValue(DispenserBlock.FACING).get3DDataValue());
		return dispensedStack;
	}

	protected ItemStack dispenseStack(BlockSource source, ItemStack stack) {
		Direction enumfacing = source.getBlockState().getValue(DispenserBlock.FACING);
		Position position = DispenserBlock.getDispensePosition(source);
		ItemStack itemstack = stack.split(1);
		doDispense(source.getLevel(), itemstack, enumfacing, position);
		return stack;
	}

	public static void doDispense(Level level, ItemStack stack, Direction facing, Position position) {
		double x = position.x();
		double y = position.y();
		double z = position.z();
		if (facing == Direction.DOWN) {
			ItemEntity entityitem = new ItemEntity(level, x, y - 0.2, z, stack);
			entityitem.setDeltaMovement(0, 0, 0);
			level.addFreshEntity(entityitem);
		} else if (facing == Direction.UP) {
			ItemEntity entityitem = new ItemEntity(level, x, y + 0.05, z, stack);
			entityitem.setDeltaMovement((double) facing.getStepX() * 0.3D, 0, (double) facing.getStepZ() * 0.3D);
			level.addFreshEntity(entityitem);
		} else {
			ItemEntity entityitem = new ItemEntity(level, x, y - 0.1, z, stack);
			entityitem.setDeltaMovement((double) facing.getStepX() * 0.3D, 0.2F, (double) facing.getStepZ() * 0.3D);
			level.addFreshEntity(entityitem);
		}
	}

}
