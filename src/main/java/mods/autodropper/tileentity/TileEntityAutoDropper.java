package mods.autodropper.tileentity;

import mods.autodropper.AutoDropper;
import mods.autodropper.block.BlockAutoDropper;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TileEntityAutoDropper extends DispenserTileEntity implements ITickableTileEntity {

	private int ticksExpired = 0;
	private int ticksMax = 4;
	
	public TileEntityAutoDropper() {
		super(AutoDropper.tile_auto_dropper);
	}
	
	@Override
	public ITextComponent getName() {
		ITextComponent itextcomponent = this.getCustomName();
		return (ITextComponent) (itextcomponent != null ? itextcomponent : new TranslationTextComponent("block." + AutoDropper.MODID + ".auto_dropper"));
	}

//	Removed in 1.14
//	@Override
//	public String getGuiID() {
//		return "minecraft:dropper";
//	}

	@Override
	public void read(CompoundNBT nbt) {
        super.read(nbt);
        this.ticksExpired = nbt.getInt("TicksExpired");
    }

	@Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.putInt("TicksExpired", this.ticksExpired);
        return nbt;
    }
	      
	@Override
	public void tick() {
		if (!world.isRemote) {
			this.ticksExpired += 1;
			if (this.ticksExpired >= this.ticksMax) {
				this.ticksExpired = 0;
				if (!world.isBlockPowered(pos)) {
					Block block = this.getBlockState().getBlock();
					if (block instanceof BlockAutoDropper) {
						((BlockAutoDropper) block).dispense(world, pos);
					}
				}
			} else {
				this.ticksExpired += 1;
			}
		}
	}	
	
}
