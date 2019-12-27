package justfatlard.emerald_tools;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ShovelItem;

public class EmeraldShovelItem extends ShovelItem {
	public EmeraldShovelItem() {
		super(EmeraldTools.EMERALD_TOOL_MATERIAL, -2, -3, new Settings().maxCount(1).group(ItemGroup.TOOLS));
	}
}
