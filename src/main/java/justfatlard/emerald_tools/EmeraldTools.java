package justfatlard.emerald_tools;

import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemGroups;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

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

	// Tool items using Polymer
	public static final Item EMERALD_PICKAXE = new EmeraldPickaxeItem(
		EMERALD_TOOL_MATERIAL, 1, -2.8F,
		new Item.Settings().registryKey(keyOf("emerald_pickaxe"))
	);

	public static final Item EMERALD_AXE = new EmeraldAxeItem(
		EMERALD_TOOL_MATERIAL, 5, -3.0F,
		new Item.Settings().registryKey(keyOf("emerald_axe"))
	);

	public static final Item EMERALD_SHOVEL = new EmeraldShovelItem(
		EMERALD_TOOL_MATERIAL, 1.5F, -3.0F,
		new Item.Settings().registryKey(keyOf("emerald_shovel"))
	);

	public static final Item EMERALD_HOE = new EmeraldHoeItem(
		EMERALD_TOOL_MATERIAL, 0, -3.0F,
		new Item.Settings().registryKey(keyOf("emerald_hoe"))
	);

	public static final Item EMERALD_SWORD = new EmeraldSwordItem(
		EMERALD_TOOL_MATERIAL, 3, -2.4F,
		new Item.Settings().registryKey(keyOf("emerald_sword"))
	);

	@Override
	public void onInitialize() {
		// Register mod assets with Polymer resource pack system
		PolymerResourcePackUtils.addModAssets(MOD_ID);
		PolymerResourcePackUtils.markAsRequired();

		// Register items
		Registry.register(Registries.ITEM, keyOf("emerald_pickaxe"), EMERALD_PICKAXE);
		Registry.register(Registries.ITEM, keyOf("emerald_axe"), EMERALD_AXE);
		Registry.register(Registries.ITEM, keyOf("emerald_shovel"), EMERALD_SHOVEL);
		Registry.register(Registries.ITEM, keyOf("emerald_hoe"), EMERALD_HOE);
		Registry.register(Registries.ITEM, keyOf("emerald_sword"), EMERALD_SWORD);

		// Create Polymer item group (access via /polymer creative)
		ItemGroup emeraldToolsGroup = PolymerItemGroupUtils.builder()
			.displayName(Text.literal("Emerald Tools"))
			.icon(() -> new ItemStack(EMERALD_PICKAXE))
			.entries((context, entries) -> {
				entries.add(new ItemStack(EMERALD_PICKAXE));
				entries.add(new ItemStack(EMERALD_AXE));
				entries.add(new ItemStack(EMERALD_SHOVEL));
				entries.add(new ItemStack(EMERALD_HOE));
				entries.add(new ItemStack(EMERALD_SWORD));
			})
			.build();
		PolymerItemGroupUtils.registerPolymerItemGroup(Identifier.of(MOD_ID, "emerald_tools"), emeraldToolsGroup);

		// Add tools to vanilla Tools creative tab
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
			entries.add(EMERALD_PICKAXE);
			entries.add(EMERALD_AXE);
			entries.add(EMERALD_SHOVEL);
			entries.add(EMERALD_HOE);
		});

		// Add sword to vanilla Combat creative tab
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
			entries.add(EMERALD_SWORD);
		});

		System.out.println("[emerald-tools] Loaded emerald-tools (server-side with Polymer)");
	}
}
