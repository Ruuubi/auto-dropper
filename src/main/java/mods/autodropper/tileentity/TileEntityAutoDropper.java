package mods.autodropper.tileentity;

import mods.autodropper.AutoDropper;
import mods.autodropper.block.BlockAutoDropper;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityAutoDropper extends TileEntityDispenser implements ITickable {

	private int ticksExpired = 0;
	private int ticksMax = 4;
	
	public TileEntityAutoDropper() {
		super(AutoDropper.tile_auto_dropper);
	}
	
	@Override
	public ITextComponent getName() {
		ITextComponent itextcomponent = this.getCustomName();
		return (ITextComponent) (itextcomponent != null ? itextcomponent : new TextComponentTranslation("block." + AutoDropper.MODID + ".auto_dropper"));
	}

	@Override
	public String getGuiID() {
		return "minecraft:dropper";
	}

	@Override
	public void read(NBTTagCompound nbt) {
        super.read(nbt);
        this.ticksExpired = nbt.getInt("TicksExpired");
    }

	@Override
    public NBTTagCompound write(NBTTagCompound nbt) {
        super.write(nbt);
        nbt.setInt("TicksExpired", this.ticksExpired);
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
