package mods.autodropper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mods.autodropper.block.BlockAutoDropper;
import mods.autodropper.tileentity.TileEntityAutoDropper;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod("autodropper")
public class AutoDropper {
	public static final String MODID = "autodropper";
	public static final String MODNAME = "Auto Dropper";
	public static final String MODNAME_NOSPACE = "AutoDropper";
	public static final Logger LOGGER = LogManager.getLogger();

	public static BlockAutoDropper auto_dropper;
	public static TileEntityType<TileEntityAutoDropper> tile_auto_dropper;

	public AutoDropper() {}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			IForgeRegistry<Block> registry = event.getRegistry();
			registry.register(auto_dropper = new BlockAutoDropper());
		}

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			IForgeRegistry<Item> registry = event.getRegistry();
			registry.register(new BlockItem(auto_dropper, new Item.Properties().group(ItemGroup.REDSTONE)).setRegistryName("auto_dropper"));
		}

		@SuppressWarnings("unchecked")
		@SubscribeEvent
		public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
			event.getRegistry().register(tile_auto_dropper = (TileEntityType<TileEntityAutoDropper>) TileEntityType.Builder.create(TileEntityAutoDropper::new, auto_dropper).build(null).setRegistryName("auto_dropper"));
		}
	}
}
