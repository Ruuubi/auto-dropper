package mods.autodropper.block;

import java.util.Random;

import mods.autodropper.BehaviorAutoDropper;
import mods.autodropper.tileentity.TileEntityAutoDropper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DropperBlock;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.ProxyBlockSource;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.VanillaInventoryCodeHooks;

public class BlockAutoDropper extends DropperBlock {
	private static final IDispenseItemBehavior DISPENSE_BEHAVIOR = new BehaviorAutoDropper();

	public BlockAutoDropper() {
		super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F));
		this.setRegistryName("auto_dropper");
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileEntityAutoDropper();
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {}

	@Override
	public void dispense(ServerWorld world, BlockPos pos) {
		ProxyBlockSource blocksourceimpl = new ProxyBlockSource(world, pos);
		DispenserTileEntity tileDispenser = blocksourceimpl.getBlockTileEntity();
		int i = tileDispenser.getDispenseSlot();
		if (i >= 0) {
			ItemStack randStack = tileDispenser.getStackInSlot(i);
			if (!randStack.isEmpty() && VanillaInventoryCodeHooks.dropperInsertHook(world, pos, tileDispenser, i, randStack)) {
				Direction enumfacing = world.getBlockState(pos).get(FACING);
				IInventory iinventory = HopperTileEntity.getInventoryAtPosition(world, pos.offset(enumfacing));
				ItemStack dispensedStack;
				if (iinventory == null) {
					dispensedStack = DISPENSE_BEHAVIOR.dispense(blocksourceimpl, randStack);
				} else {
					dispensedStack = HopperTileEntity.putStackInInventoryAllSlots(tileDispenser, iinventory, randStack.copy().split(1), enumfacing.getOpposite());
					if (dispensedStack.isEmpty()) {
						dispensedStack = randStack.copy();
						dispensedStack.shrink(1);
					} else {
						dispensedStack = randStack.copy();
					}
				}
				tileDispenser.setInventorySlotContents(i, dispensedStack);
			}
		}
	}

}
