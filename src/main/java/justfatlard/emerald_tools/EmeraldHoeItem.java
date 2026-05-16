package justfatlard.emerald_tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class EmeraldHoeItem extends Item {
	public EmeraldHoeItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings) {
		super(settings.hoe(material, attackDamage, attackSpeed));
	}
}
