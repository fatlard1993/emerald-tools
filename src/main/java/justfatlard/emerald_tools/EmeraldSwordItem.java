package justfatlard.emerald_tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class EmeraldSwordItem extends Item {
	public EmeraldSwordItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties settings) {
		super(settings.sword(material, attackDamage, attackSpeed));
	}
}
