package justfatlard.emerald_tools;

import justfatlard.pandorical.api.ItemRegistration;
import justfatlard.pandorical.api.PandoricalApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmeraldTools implements ModInitializer {
	public static final String MOD_ID = "emerald-tools-justfatlard";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final TagKey<Item> EMERALD_TOOL_REPAIR_ITEMS = TagKey.create(
		Registries.ITEM,
		Identifier.fromNamespaceAndPath(MOD_ID, "emerald_tool_repair_items")
	);

	public static final ToolMaterial EMERALD_TOOL_MATERIAL = new ToolMaterial(
		BlockTags.INCORRECT_FOR_DIAMOND_TOOL,  // Can mine everything diamond can
		1717,                                   // Durability
		9.0f,                                   // Mining speed
		3.5f,                                   // Attack damage bonus
		20,                                     // Enchantability
		EMERALD_TOOL_REPAIR_ITEMS              // Repair items tag
	);

	private static ResourceKey<Item> keyOf(String name) {
		return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));
	}

	private static final ResourceKey<CreativeModeTab> TOOLS_TAB = ResourceKey.create(
		Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath("minecraft", "tools_and_utilities")
	);
	private static final ResourceKey<CreativeModeTab> COMBAT_TAB = ResourceKey.create(
		Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath("minecraft", "combat")
	);

	public static final Item EMERALD_PICKAXE = new EmeraldPickaxeItem(
		EMERALD_TOOL_MATERIAL, 1, -2.8F,
		new Item.Properties().setId(keyOf("emerald_pickaxe"))
	);

	public static final Item EMERALD_AXE = new EmeraldAxeItem(
		EMERALD_TOOL_MATERIAL, 5, -3.0F,
		new Item.Properties().setId(keyOf("emerald_axe"))
	);

	public static final Item EMERALD_SHOVEL = new EmeraldShovelItem(
		EMERALD_TOOL_MATERIAL, 1.5F, -3.0F,
		new Item.Properties().setId(keyOf("emerald_shovel"))
	);

	public static final Item EMERALD_HOE = new EmeraldHoeItem(
		EMERALD_TOOL_MATERIAL, 0, -3.0F,
		new Item.Properties().setId(keyOf("emerald_hoe"))
	);

	public static final Item EMERALD_SWORD = new EmeraldSwordItem(
		EMERALD_TOOL_MATERIAL, 3, -2.4F,
		new Item.Properties().setId(keyOf("emerald_sword"))
	);

	@Override
	public void onInitialize() {
		EmeraldSoil.register();

		if (PandoricalApi.isAvailable()) {
			for (String name : new String[] { "emerald_pickaxe", "emerald_axe", "emerald_shovel", "emerald_hoe", "emerald_sword" }) {
				// The kind decides which vanilla helper the client rebuilds it with, and the
				// material carries the numbers. Without this the client's copy is a plain item
				// and every swing is a bare-handed one.
				String kind = name.substring(name.lastIndexOf('_') + 1);
				PandoricalApi.content().registerItem(MOD_ID + ":" + name, new ItemRegistration()
					.model(MOD_ID + ":item/" + name)
					.tool(kind, EMERALD_TOOL_MATERIAL, attackDamageFor(kind), attackSpeedFor(kind))
					.maxStackSize(1));
			}
			PandoricalApi.content().registerModAssets(MOD_ID);
		}

		Registry.register(BuiltInRegistries.ITEM, keyOf("emerald_pickaxe"), EMERALD_PICKAXE);
		Registry.register(BuiltInRegistries.ITEM, keyOf("emerald_axe"), EMERALD_AXE);
		Registry.register(BuiltInRegistries.ITEM, keyOf("emerald_shovel"), EMERALD_SHOVEL);
		Registry.register(BuiltInRegistries.ITEM, keyOf("emerald_hoe"), EMERALD_HOE);
		Registry.register(BuiltInRegistries.ITEM, keyOf("emerald_sword"), EMERALD_SWORD);

		CreativeModeTabEvents.modifyOutputEvent(TOOLS_TAB).register(entries -> {
			entries.accept(EMERALD_PICKAXE);
			entries.accept(EMERALD_AXE);
			entries.accept(EMERALD_SHOVEL);
			entries.accept(EMERALD_HOE);
		});

		CreativeModeTabEvents.modifyOutputEvent(COMBAT_TAB).register(entries -> {
			entries.accept(EMERALD_SWORD);
		});

		LOGGER.info("Loaded emerald-tools (server-side with Pandorical)");
	}

	/**
	 * The same attack numbers the items above were built with.
	 *
	 * <p>Read from one place by both the real item and the copy the client is told to build, so
	 * the two cannot drift apart the way they had: a tool the client does not know is a tool
	 * swings at hand speed no matter what the server thinks it is holding.
	 */
	private static float attackDamageFor(String kind) {
		return switch (kind) {
			case "axe" -> 5.0F;
			case "shovel" -> 1.5F;
			case "sword" -> 3.0F;
			case "hoe" -> 0.0F;
			default -> 1.0F;
		};
	}

	private static float attackSpeedFor(String kind) {
		return switch (kind) {
			case "sword" -> -2.4F;
			case "pickaxe" -> -2.8F;
			case "hoe" -> -3.0F;
			default -> -3.0F;
		};
	}
}
