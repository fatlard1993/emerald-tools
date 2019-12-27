package justfatlard.emerald_tools;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.SwordItem;

public class EmeraldSwordItem extends SwordItem {
	public EmeraldSwordItem() {
		super(EmeraldTools.EMERALD_TOOL_MATERIAL, 0, -2.4F, new Settings().maxCount(1).group(ItemGroup.COMBAT));
	}
}
