package justfatlard.emerald_tools;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EmeraldTools implements ModInitializer {
	private final static String MOD_ID = "emerald-tools-justfatlard";

	public final static EmeraldToolMaterial EMERALD_TOOL_MATERIAL = new EmeraldToolMaterial();

	public final static EmeraldAxeItem EMERALD_AXE = new EmeraldAxeItem();
	public final static HoeItem EMERALD_HOE = new HoeItem(EmeraldTools.EMERALD_TOOL_MATERIAL, 0, new Item.Settings().maxCount(1).group(ItemGroup.TOOLS));
	public final static EmeraldPickaxeItem EMERALD_PICKAXE = new EmeraldPickaxeItem();
	public final static ShovelItem EMERALD_SHOVEL = new ShovelItem(EmeraldTools.EMERALD_TOOL_MATERIAL, -2, -3, new Item.Settings().maxCount(1).group(ItemGroup.TOOLS));
	public final static SwordItem EMERALD_SWORD = new SwordItem(EmeraldTools.EMERALD_TOOL_MATERIAL, 0, -2.4F, new Item.Settings().maxCount(1).group(ItemGroup.COMBAT));

	@Override
	public void onInitialize(){
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "emerald_pickaxe"), EMERALD_PICKAXE);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "emerald_axe"), EMERALD_AXE);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "emerald_shovel"), EMERALD_SHOVEL);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "emerald_hoe"), EMERALD_HOE);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "emerald_sword"), EMERALD_SWORD);
	}
}
