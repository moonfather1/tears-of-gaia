package moonfather.tearsofgaia.items;

import moonfather.tearsofgaia.ModTears;
import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemGem extends Item
{
	private static Map<String, String> subtitleLine1 = new HashMap<String, String>();

	private String element = "none";
	private int level = 1;
	private String subtitle1;
	private String subtitle2;
	private String subtitle2B;



	public ItemGem(String element, int level)
    {
		this.element = element;
		this.level = level;
	    this.subtitle1 = ItemGem.GetSubtitleLine1Text(element, level);
	    this.subtitle2 = ModTears.proxy.getTranslatedText("item.tearsofgaia.gem_all.subtitle2");
	    this.subtitle2B = ModTears.proxy.getTranslatedText(String.format("item.tearsofgaia.gem_%s.usage_%d", this.element, this.level));
    }



	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(1, this.subtitle1);
		int tempTooltipTime = ElementalHelper.GetTempTooltipTime(stack);
		if (tempTooltipTime == 0)
		{
			tooltip.add(2, " ");
			tooltip.add(3, this.subtitle2);
		}
		else
		{
			tooltip.add(" ");
			tooltip.add(this.subtitle2B);
			ElementalHelper.UpdateTempTooltipTime(stack);
		}
	}



	public static String GetSubtitleLine1Text(String element, int level)
	{
		if (ItemGem.subtitleLine1.size() == 0)
		{
			ItemGem.subtitleLine1.put("earth1", ItemGem.GetSubtitleLine1TextInternal("earth", 1));
			ItemGem.subtitleLine1.put("water1", ItemGem.GetSubtitleLine1TextInternal("water", 1));
			ItemGem.subtitleLine1.put("air1", ItemGem.GetSubtitleLine1TextInternal("air", 1));
			ItemGem.subtitleLine1.put("fire1", ItemGem.GetSubtitleLine1TextInternal("fire", 1));
			ItemGem.subtitleLine1.put("earth2", ItemGem.GetSubtitleLine1TextInternal("earth", 2));
			ItemGem.subtitleLine1.put("water2", ItemGem.GetSubtitleLine1TextInternal("water", 2));
			ItemGem.subtitleLine1.put("air2", ItemGem.GetSubtitleLine1TextInternal("air", 2));
			ItemGem.subtitleLine1.put("fire2", ItemGem.GetSubtitleLine1TextInternal("fire", 2));
		}
		return ItemGem.subtitleLine1.get(element + level);
	}



	private static String GetSubtitleLine1TextInternal(String element, int level)
	{
		TextFormatting format = TextFormatting.DARK_RED;
		switch (element)
		{
			case "earth": format = TextFormatting.GOLD; break;
			case "water": format = TextFormatting.AQUA; break;
			case "fire": format = TextFormatting.RED; break;
			case "air": format = TextFormatting.WHITE; break;
		}
		String subtitle1Key = String.format("item.tearsofgaia.gem_%s.subtitle", element);
		String levelSuffix = level > 1 ? ModTears.proxy.getTranslatedText("item.tearsofgaia.level_suffix", " ", level) : "";
		return format + ModTears.proxy.getTranslatedText(subtitle1Key) + TextFormatting.RESET + levelSuffix;
	}



	public String GetElement()
	{
		return this.element;
	}



	public int GetLevel()
	{
		return this.level;
	}
}
