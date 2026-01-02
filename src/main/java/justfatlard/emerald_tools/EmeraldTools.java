package justfatlard.emerald_tools;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class EmeraldTools implements ModInitializer {
	public static final String MOD_ID = "emerald-tools-justfatlard";

	// Custom item tag for emerald tool repair items
	public static final TagKey<Item> EMERALD_TOOL_REPAIR_ITEMS = TagKey.of(
		RegistryKeys.ITEM,
		Identifier.of(MOD_ID, "emerald_tool_repair_items")
	);

	// Create the emerald tool material
	public static final ToolMaterial EMERALD_TOOL_MATERIAL = new ToolMaterial(
		BlockTags.INCORRECT_FOR_DIAMOND_TOOL,  // Can mine everything diamond can
		1717,                                   // Durability
		9.0f,                                   // Mining speed
		3.5f,                                   // Attack damage bonus
		20,                                     // Enchantability
		EMERALD_TOOL_REPAIR_ITEMS              // Repair items tag
	);

	// Helper method to create registry key
	private static RegistryKey<Item> keyOf(String name) {
		return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, name));
	}

	// Tool items - using Item.Settings factory methods for 1.21.11+
	public static final Item EMERALD_PICKAXE = Registry.register(
		Registries.ITEM,
		keyOf("emerald_pickaxe"),
		new Item(new Item.Settings()
			.registryKey(keyOf("emerald_pickaxe"))
			.pickaxe(EMERALD_TOOL_MATERIAL, 1, -2.8F))
	);

	public static final Item EMERALD_AXE = Registry.register(
		Registries.ITEM,
		keyOf("emerald_axe"),
		new AxeItem(EMERALD_TOOL_MATERIAL, 5, -3.0F,
			new Item.Settings().registryKey(keyOf("emerald_axe")))
	);

	public static final Item EMERALD_SHOVEL = Registry.register(
		Registries.ITEM,
		keyOf("emerald_shovel"),
		new ShovelItem(EMERALD_TOOL_MATERIAL, 1.5F, -3.0F,
			new Item.Settings().registryKey(keyOf("emerald_shovel")))
	);

	public static final Item EMERALD_HOE = Registry.register(
		Registries.ITEM,
		keyOf("emerald_hoe"),
		new HoeItem(EMERALD_TOOL_MATERIAL, 0, -3.0F,
			new Item.Settings().registryKey(keyOf("emerald_hoe")))
	);

	public static final Item EMERALD_SWORD = Registry.register(
		Registries.ITEM,
		keyOf("emerald_sword"),
		new Item(new Item.Settings()
			.registryKey(keyOf("emerald_sword"))
			.sword(EMERALD_TOOL_MATERIAL, 3, -2.4F))
	);

	@Override
	public void onInitialize() {
		// Add items to creative tabs
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
			entries.add(EMERALD_PICKAXE);
			entries.add(EMERALD_AXE);
			entries.add(EMERALD_SHOVEL);
			entries.add(EMERALD_HOE);
		});

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
			entries.add(EMERALD_SWORD);
		});
	}
}
