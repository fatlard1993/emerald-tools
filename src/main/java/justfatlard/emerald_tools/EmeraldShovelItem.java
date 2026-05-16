package justfatlard.emerald_tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class EmeraldShovelItem extends Item {
	public EmeraldShovelItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings) {
		super(settings.shovel(material, attackDamage, attackSpeed));
	}
}
