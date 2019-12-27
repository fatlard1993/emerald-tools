package justfatlard.emerald_tools;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class EmeraldToolMaterial implements ToolMaterial {
	@Override
	public int getDurability() {
		return 1000;
	}

	@Override
	public float getAttackDamage() {
		return 5.5F;
	}

	@Override
	public float getMiningSpeed() {
		return 13.0f;
	}

	@Override
	public int getMiningLevel() {
		return 3;
	}

	@Override
	public int getEnchantability() {
		return 20;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.ofItems(Items.EMERALD);
	}
}
