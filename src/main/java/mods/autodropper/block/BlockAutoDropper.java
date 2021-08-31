package mods.autodropper.block;

import java.util.Random;

import javax.annotation.Nullable;

import mods.autodropper.BehaviorAutoDropper;
import mods.autodropper.tileentity.TileEntityAutoDropper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.items.VanillaInventoryCodeHooks;

public class BlockAutoDropper extends DropperBlock {
	private static final DispenseItemBehavior AUTO_DROPPER_DISPENSE_BEHAVIOR = new BehaviorAutoDropper();

	public BlockAutoDropper() {
		super(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.5F));
		this.setRegistryName("auto_dropper");
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityAutoDropper(pos, state);
	}

	@Override
	protected DispenseItemBehavior getDispenseMethod(ItemStack stack) {
		return AUTO_DROPPER_DISPENSE_BEHAVIOR;
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide()) {
			return null;
		}
		return (level1, blockPos, blockState, t) -> {
			if (t instanceof TileEntityAutoDropper tile) {
				tile.serverTick();
			}
		};
	}
	
	@Override
	public void dispenseFrom(ServerLevel level, BlockPos pos) {
		BlockSourceImpl blocksourceimpl = new BlockSourceImpl(level, pos);
		DispenserBlockEntity tileDispenser = blocksourceimpl.getEntity();
		int i = tileDispenser.getRandomSlot();
		if (i >= 0) {
			ItemStack randStack = tileDispenser.getItem(i);
			if (!randStack.isEmpty() && VanillaInventoryCodeHooks.dropperInsertHook(level, pos, tileDispenser, i, randStack)) {
				Direction direction = level.getBlockState(pos).getValue(FACING);
				Container container = HopperBlockEntity.getContainerAt(level, pos.relative(direction));
				ItemStack dispensedStack;
				if (container == null) {
					dispensedStack = AUTO_DROPPER_DISPENSE_BEHAVIOR.dispense(blocksourceimpl, randStack);
				} else {
					dispensedStack = HopperBlockEntity.addItem(tileDispenser, container, randStack.copy().split(1), direction.getOpposite());
					if (dispensedStack.isEmpty()) {
						dispensedStack = randStack.copy();
						dispensedStack.shrink(1);
					} else {
						dispensedStack = randStack.copy();
					}
				}
				tileDispenser.setItem(i, dispensedStack);
			}
		}
	}

}
