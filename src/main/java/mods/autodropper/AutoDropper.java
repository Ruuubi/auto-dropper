package mods.autodropper;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mods.autodropper.block.BlockAutoDropper;
import mods.autodropper.tileentity.TileEntityAutoDropper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod("autodropper")
public class AutoDropper {
	public static final String MODID = "autodropper";
	public static final String MODNAME = "Auto Dropper";
	public static final String MODNAME_NOSPACE = "AutoDropper";
	public static final Logger LOGGER = LogManager.getLogger();

	// Blocks
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	public static final RegistryObject<Block> BLOCK_AUTO_DROPPER = BLOCKS.register("auto_dropper", BlockAutoDropper::new);

	// Items
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	public static final RegistryObject<Item> ITEM_AUTO_DROPPER = ITEMS.register("auto_dropper", () -> new BlockItem(BLOCK_AUTO_DROPPER.get(), new Item.Properties()));
	
	private void setCreativeTab(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
			event.accept(ITEM_AUTO_DROPPER);
		  }
	}
	
	// Block Entities
	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
	public static final RegistryObject<BlockEntityType<TileEntityAutoDropper>> TILE_AUTO_DROPPER = BLOCK_ENTITIES.register("tile_auto_dropper", () -> BlockEntityType.Builder.of(TileEntityAutoDropper::new, BLOCK_AUTO_DROPPER.get()).build(null));

	public AutoDropper() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);
		BLOCK_ENTITIES.register(modEventBus);
		modEventBus.addListener(this::setCreativeTab);
	}

}
