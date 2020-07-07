package moonfather.tearsofgaia.options;

import moonfather.tearsofgaia.Constants;
import net.minecraftforge.common.config.Config;

@Config(modid = Constants.MODID)
@Config.LangKey("tearsofgaia.config.title")
public class Options112
{
	private static final int defaultNumberOfTearsInA1000DiamondBlocks = 100;
	private static final boolean defaultShouldReplaceAGem = true;
	private static final boolean defaultLevelTwoGemsEnabled = true;
	private static final boolean defaultSoulboundBookEnabledInWorld = false;


	@Config.Name("Number of tears in a 1000 gem blocks")
	@Config.Comment("Gaia's tears are obtained from diamond ore blocks and emerald ore blocks. This is a weight value which sets how often you will find a tear gem. Default value is 100 (meaning 100 out of every 100, or in other words 10%) and might even be a generous weight. Set to 0 do disable (in which case you need to establish a different way of obtaining these gems). There is no worldgen.")
	@Config.RangeInt(min = 0, max = 1000)
	public static int NumberOfTearsInA1000DiamondBlocks = defaultNumberOfTearsInA1000DiamondBlocks;

	
	@Config.Name("Should replace a gem")
	@Config.Comment("Gaia's tears are obtained from diamond ore blocks and emerald ore blocks. This setting controls whether they replace one of the gems you get from a block when breaking it, or is a tear added to the drop list.")
	public static boolean ShouldReplaceAGem = defaultShouldReplaceAGem;


	@Config.RequiresMcRestart
	@Config.Name("Level two gems enabled")
	@Config.Comment("Advanced versions of these gems crafted from normal versions and aplied after normal ones. Quite powerful.")
	public static boolean LevelTwoGemsEnabled = defaultLevelTwoGemsEnabled;

	@Config.RequiresMcRestart
	@Config.Name("Enchanted books with Soulbound available")
	@Config.Comment("If set to true, enchanted books with Soulbound enchantment can be found in fishing loot or bought from librarians or found in chests. If set to false, only mods can put the book into loot or make it otherwise available to players. This mod bestows soulbound enchantment through earth gem.")
	public static boolean SoulboundBookEnabledInWorld = defaultSoulboundBookEnabledInWorld;
}
