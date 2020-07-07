package moonfather.tearsofgaia.forging;

import moonfather.tearsofgaia.ModTears;
import moonfather.tearsofgaia.items.ItemGem;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EventForTooltips
{
	@SubscribeEvent
	public static void OnItemTooltip(ItemTooltipEvent event)
	{
		String element = ElementalHelper.GetItemElement(event.getItemStack());
		if (element != null)
		{
			int level = ElementalHelper.GetItemElementLevel(event.getItemStack());
			AddSpecialTextShownAboveSubtitle(event, element, level);
			event.getToolTip().add(ItemGem.GetSubtitleLine1Text(element, level));
		}
	}


	public static void AddSpecialTextShownAboveSubtitle(ItemTooltipEvent event, String element, int level)
	{
		if (element.equals("air") && level == 2)
		{
			////!! zasto ne radi?
			//event.getToolTip().add(String.format("%s%s%s", TextFormatting.YELLOW, ModTears.proxy.getTranslatedText("item.minecraft.totem_of_undying"), TextFormatting.RESET));
		}
	}
}
