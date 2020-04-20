package justfatlard.emerald_tools;

import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemGroup;

public class EmeraldAxeItem extends AxeItem {
	public EmeraldAxeItem() {
		super(EmeraldTools.EMERALD_TOOL_MATERIAL, 5, -3.0F, new Settings().maxCount(1).group(ItemGroup.TOOLS));
	}
}
