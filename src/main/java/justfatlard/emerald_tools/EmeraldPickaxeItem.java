package justfatlard.emerald_tools;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

public class EmeraldPickaxeItem extends Item implements PolymerItem {
	private final Identifier modelId;

	public EmeraldPickaxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
		super(settings.pickaxe(material, attackDamage, attackSpeed));
		this.modelId = Identifier.of(EmeraldTools.MOD_ID, "emerald_pickaxe");
	}

	@Override
	public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
		return Items.DIAMOND_PICKAXE;
	}

	@Override
	public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context) {
		return this.modelId;
	}
}
