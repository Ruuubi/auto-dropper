package mods.autodropper.tileentity;

import mods.autodropper.AutoDropper;
import mods.autodropper.block.BlockAutoDropper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAutoDropper extends DispenserBlockEntity {

	private int ticksExpired = 0;
	private int ticksMax = 4;

	public TileEntityAutoDropper(BlockPos pos, BlockState state) {
		super(AutoDropper.tile_auto_dropper, pos, state);
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("block." + AutoDropper.MODID + ".auto_dropper");
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		this.ticksExpired = nbt.getInt("TicksExpired");
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		nbt.putInt("TicksExpired", this.ticksExpired);
	}

	public void serverTick() {
		this.ticksExpired += 1;
		if (this.ticksExpired >= this.ticksMax) {
			this.ticksExpired = 0;
			if (!this.getLevel().hasNeighborSignal(this.getBlockPos())) {
				Block block = this.getBlockState().getBlock();
				if (block instanceof BlockAutoDropper) {
					((BlockAutoDropper) block).dispenseFrom((ServerLevel) this.getLevel(), this.getBlockPos());
				}
			}
		} else {
			this.ticksExpired += 1;
		}
	}

}
