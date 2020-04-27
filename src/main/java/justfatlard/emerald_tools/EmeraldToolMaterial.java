package justfatlard.emerald_tools;

import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class EmeraldToolMaterial implements ToolMaterial {
	@Override
	public int getMiningLevel(){ return 3; }

	@Override
	public int getDurability(){ return 1632; }

	@Override
	public float getMiningSpeed(){ return 9.0f; }

	@Override
	public float getAttackDamage(){ return 3.5F; }

	@Override
	public int getEnchantability(){ return 20; }

	@Override
	public Ingredient getRepairIngredient(){ return Ingredient.ofItems(Items.EMERALD_BLOCK); }
}
