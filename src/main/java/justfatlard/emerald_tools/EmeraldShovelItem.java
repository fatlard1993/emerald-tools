package justfatlard.emerald_tools;

import eu.pb4.polymer.core.api.item.PolymerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Identifier;
import xyz.nucleoid.packettweaker.PacketContext;

public class EmeraldShovelItem extends ShovelItem implements PolymerItem {
	private final Identifier modelId;

	public EmeraldShovelItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
		super(material, attackDamage, attackSpeed, settings);
		this.modelId = Identifier.of(EmeraldTools.MOD_ID, "emerald_shovel");
	}

	@Override
	public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
		return Items.DIAMOND_SHOVEL;
	}

	@Override
	public Identifier getPolymerItemModel(ItemStack itemStack, PacketContext context) {
		return this.modelId;
	}
}
