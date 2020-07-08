package moonfather.tearsofgaia.compatibility;

import net.minecraft.item.ItemStack;

public class TinkersConstructArmoryHelper
{
	public static boolean IsValidItemForLevel1(ItemStack stack, String element)
	{
		int freeModifiers = stack.getTagCompound().getCompoundTag("Stats").getInteger("FreeModifiers");
		int level_earth = TinkersConstructHelper.GetModifierLevel(stack, "resistant_armor");
		int level_water = TinkersConstructHelper.GetModifierLevel(stack, "blast_resistant_armor");
		int level_fire = TinkersConstructHelper.GetModifierLevel(stack, "fire_resistant_armor");
		int level_air = TinkersConstructHelper.GetModifierLevel(stack, "projectile_resistant_armor");
		if (element == "earth")
		{
			return level_water == 0 && level_fire == 0 && level_air == 0 &&
					((level_earth == 0 && freeModifiers > 0) || (level_earth > 0 && level_earth < 8));
		}
		else if (element == "water")
		{
			return level_earth == 0 && level_fire == 0 && level_air == 0 &&
					((level_water == 0 && freeModifiers > 0) || (level_water > 0 && level_water < 8));
		}
		else if (element == "air")
		{
			return level_water == 0 && level_fire == 0 && level_earth == 0 &&
					((level_air == 0 && freeModifiers > 0) || (level_air > 0 && level_air < 8));
		}
		else if (element == "fire")
		{
			return level_water == 0 && level_earth == 0 && level_air == 0 &&
					((level_fire == 0 && freeModifiers > 0) || (level_fire > 0 && level_fire < 8));
		}
		else
		{
			return false;
		}
	}



	public static void ImbueItem(ItemStack tool, String element)
	{
		if (element == "earth")
		{
			TinkersConstructHelper.AddModifierForResistance(tool, "resistant_armor", 16774902);
		}
		else if (element == "water")
		{
			TinkersConstructHelper.AddModifierForResistance(tool, "blast_resistant_armor", 8793389);
		}
		else if (element == "air")
		{
			TinkersConstructHelper.AddModifierForResistance(tool, "projectile_resistant_armor", 1070923);
		}
		else if (element == "fire")
		{
			TinkersConstructHelper.AddModifierForResistance(tool, "fire_resistant_armor", 15375922);
		}
	}
}