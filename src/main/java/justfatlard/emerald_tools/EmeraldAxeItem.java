package justfatlard.emerald_tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class EmeraldAxeItem extends Item {
	public EmeraldAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings) {
		super(settings.axe(material, attackDamage, attackSpeed));
	}
}
