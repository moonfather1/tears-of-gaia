package moonfather.tearsofgaia.obtaining;

import moonfather.tearsofgaia.ModTears;
import moonfather.tearsofgaia.options.Options112;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EventForBlockDrops
{
	@SubscribeEvent
	public static void OnHarvestDropsEvent(BlockEvent.HarvestDropsEvent event)
	{
		if (event.isCanceled() || Options112.NumberOfTearsInA1000DiamondBlocks == 0 || event.isSilkTouching())
		{
			return;
		}
		if (event.getWorld().rand.nextInt(1000) < Options112.NumberOfTearsInA1000DiamondBlocks)
		{
			java.util.List<ItemStack> drops = event.getDrops();
			for (int i = 0; i < drops.size(); i++)
			{
				if (drops.get(i).getItem() == Items.DIAMOND || drops.get(i).getItem() == Items.EMERALD)
				{
					if (Options112.ShouldReplaceAGem)
					{
						int kind = event.getWorld().rand.nextInt(4);
						event.getDrops().set(i, GetRandomTear(kind));
					}
					else
					{
						int kind = event.getWorld().rand.nextInt(4);
						event.getDrops().add(GetRandomTear(kind));
					}
					break;
				}
			}
		}
	}



	private static ItemStack GetRandomTear(int kind)
	{
		switch (kind)
		{
			case 0: return new ItemStack(ModTears.ItemGemAir);
			case 1: return new ItemStack(ModTears.ItemGemEarth);
			case 2: return new ItemStack(ModTears.ItemGemFire);
			case 3: return new ItemStack(ModTears.ItemGemWater);
		}
		return new ItemStack(Items.POISONOUS_POTATO);
	}
}
