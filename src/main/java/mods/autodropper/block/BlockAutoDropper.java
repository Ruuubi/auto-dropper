package mods.autodropper.block;

import java.util.Random;

import mods.autodropper.BehaviorAutoDropper;
import mods.autodropper.tileentity.TileEntityAutoDropper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BlockSourceImpl;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.VanillaInventoryCodeHooks;

public class BlockAutoDropper extends BlockDropper {
	private static final IBehaviorDispenseItem DISPENSE_BEHAVIOR = new BehaviorAutoDropper();
	
	public BlockAutoDropper() {
		super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F));
		this.setRegistryName("auto_dropper");
	}
	
	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
	      return new TileEntityAutoDropper();
	   }

	@Override
	public void tick(IBlockState state, World world, BlockPos pos, Random random) {}
	
	@Override
	public void dispense(World world, BlockPos pos) {
		BlockSourceImpl blocksourceimpl = new BlockSourceImpl(world, pos);
		TileEntityDispenser tileDispenser = blocksourceimpl.getBlockTileEntity();
		int i = tileDispenser.getDispenseSlot();
		if (i >= 0) {
			ItemStack randStack = tileDispenser.getStackInSlot(i);
			if (!randStack.isEmpty() && VanillaInventoryCodeHooks.dropperInsertHook(world, pos, tileDispenser, i, randStack)) {
				EnumFacing enumfacing = world.getBlockState(pos).get(FACING);
				IInventory iinventory = TileEntityHopper.getInventoryAtPosition(world, pos.offset(enumfacing));
				ItemStack dispensedStack;
				if (iinventory == null) {
					dispensedStack = DISPENSE_BEHAVIOR.dispense(blocksourceimpl, randStack);
				} else {
					dispensedStack = TileEntityHopper.putStackInInventoryAllSlots(tileDispenser, iinventory, randStack.copy().split(1), enumfacing.getOpposite());
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
