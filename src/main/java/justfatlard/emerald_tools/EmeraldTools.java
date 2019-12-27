package justfatlard.emerald_tools;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EmeraldTools implements ModInitializer {
	private final static String MOD_ID = "emerald-tools-justfatlard";

	public final static EmeraldToolMaterial EMERALD_TOOL_MATERIAL = new EmeraldToolMaterial();

	public final static EmeraldPickaxeItem EMERALD_PICKAXE_ITEM = new EmeraldPickaxeItem();
	public final static EmeraldAxeItem EMERALD_AXE_ITEM = new EmeraldAxeItem();
	public final static EmeraldShovelItem EMERALD_SHOVEL_ITEM = new EmeraldShovelItem();
	public final static EmeraldHoeItem EMERALD_HOE_ITEM = new EmeraldHoeItem();
	public final static EmeraldSwordItem EMERALD_SWORD_ITEM = new EmeraldSwordItem();

	@Override
	public void onInitialize(){
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "emerald_pickaxe"), EMERALD_PICKAXE_ITEM);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "emerald_axe"), EMERALD_AXE_ITEM);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "emerald_shovel"), EMERALD_SHOVEL_ITEM);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "emerald_hoe"), EMERALD_HOE_ITEM);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "emerald_sword"), EMERALD_SWORD_ITEM);
	}
}
