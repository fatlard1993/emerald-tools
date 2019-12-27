package justfatlard.emerald_tools;

import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemGroup;

public class EmeraldHoeItem extends HoeItem {
	public EmeraldHoeItem() {
		super(EmeraldTools.EMERALD_TOOL_MATERIAL, 0, new Settings().maxCount(1).group(ItemGroup.TOOLS));
	}
}
