package justfatlard.emerald_tools;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.PickaxeItem;

public class EmeraldPickaxeItem extends PickaxeItem {
	public EmeraldPickaxeItem() {
		super(EmeraldTools.EMERALD_TOOL_MATERIAL, 1, -2.8F, new Settings().maxCount(1).group(ItemGroup.TOOLS));
	}
}
