package moonfather.tearsofgaia.compatibility;

import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

public class TinkersConstructHelper
{
	public static boolean IsValidItemForLevel1(ItemStack stack, String element)
	{
		if (!stack.getTagCompound().hasKey("TinkerData"))
		{
			return false;  // not a tinker's construct tool
		}
		String existingElement = ElementalHelper.GetItemElement(stack);
		if (existingElement != null && !existingElement.equals(element))
		{
			return false; // already imbued, but not the same
		}
		int existingLevel = ElementalHelper.GetItemElementLevel(stack);
		if (existingLevel >= 2)
		{
			return false;
		}
		if (stack.getItem() instanceof ItemArmor)
		{
			return TinkersConstructArmoryHelper.IsValidItemForLevel1(stack, element);
		}
		int freeModifiers = stack.getTagCompound().getCompoundTag("Stats").getInteger("FreeModifiers");
		if (element == "earth")
		{
			return freeModifiers > 0 && GetModifierLevel(stack, "reinforced") < 5;
		}
		else if (element == "water")
		{
			return (GetModifierLevel(stack, "diamond") == 0 && freeModifiers > 0) || (stack.getItemDamage() > stack.getMaxDamage() / 2);
		}
		else if (element == "air")
		{
			return existingElement == null;
		}
		else if (element == "fire")
		{
			return GetModifierLevel(stack, "diamond") == 0 && freeModifiers > 0;
		}
		else
		{
			return false;
		}
	}



	public static boolean IsValidItemForLevel2(ItemStack stack, String element)
	{
		if (!stack.getTagCompound().hasKey("TinkerData"))
		{
			return false;  // not a tinker's construct tool
		}
		String existingElement = ElementalHelper.GetItemElement(stack);
		if (existingElement == null || !existingElement.equals(element))
		{
			return false; // not imbued or already imbued, but not the same
		}
		if (ElementalHelper.GetItemElementLevel(stack) != 1)
		{
			return false;
		}
		if (element == "earth")
		{
			return GetModifierLevel(stack, "reinforced") < 5;
		}
		return true;
	}



	public static boolean IsTinkersConstructItem(ItemStack stack)
	{
		return !stack.isEmpty() && stack.getTagCompound().hasKey("TinkerData");
	}



	public static void ImbueItem(ItemStack tool, String element)
	{
		if (tool.getItem() instanceof ItemArmor)
		{
			TinkersConstructArmoryHelper.ImbueItem(tool, element);
		}
		else if (element == "earth")
		{
			AddModifierTakingSeparateSlotsPerLevel(tool, "reinforced", 5254787);
		}
		else if (element == "water")
		{
			tool.setItemDamage(0);
			int freeModifiers = tool.getTagCompound().getCompoundTag("Stats").getInteger("FreeModifiers");
			if (GetModifierLevel(tool, "diamond") == 0 && freeModifiers > 0)
			{
				AddModifierTakingSeparateSlotsPerLevel(tool, "diamond", 9237730);
			}
		}
		else if (element == "air")
		{
		}
		else if (element == "fire")
		{
			AddModifierTakingSeparateSlotsPerLevel(tool, "diamond", 9237730);
		}
	}



	public static void ImbueItemToLevel2(ItemStack tool, String element)
	{
		if (element == "earth")
		{
			AddModifierInternal(tool, "reinforced", 5254787, (byte)255, true, false, true);
		}
		else if (element == "water")
		{
			AddFreeModifierSlot(tool);
			tool.setItemDamage(0);
		}
		else if (element == "air")
		{
		}
		else if (element == "fire")
		{
		}
	}



	public static void AddModifierTakingSeparateSlotsPerLevel(ItemStack tool, String modifierCode, int color)
	{
		AddModifierInternal(tool, modifierCode, color, (byte)255, true, true, true);
	}



	public static void AddModifierTakingOneSlotforAllLevels(ItemStack tool, String modifierCode, int color)
	{
		AddModifierInternal(tool, modifierCode, color, (byte)255, true, true, false);
	}



	public static void AddModifierForResistance(ItemStack tool, String modifierCode, int color)
	{
		AddModifierInternal(tool, modifierCode, color, (byte)1, true, true, false);
	}



	private static void AddModifierInternal(ItemStack tool, String modifierCode, int color, byte modifierUsed, boolean addToTraits, boolean spendModifierOnLevel1, boolean spendModifierPastLevel1)
	{
		boolean exists = false;
		NBTTagCompound tinkerData = tool.getTagCompound().getCompoundTag("TinkerData");
		NBTTagCompound stats = tool.getTagCompound().getCompoundTag("Stats");
		for (NBTBase item : tool.getTagCompound().getTagList("Modifiers", 10))
		{
			NBTTagCompound modifier = (NBTTagCompound) item;
			if (modifier.getString("identifier").equals(modifierCode))
			{
				int currentLevel = modifier.getInteger("level");
				modifier.setInteger("level", currentLevel + 1);
				if (spendModifierPastLevel1)
				{
					tinkerData.setInteger("UsedModifiers", tinkerData.getInteger("UsedModifiers") + 1);
					stats.setInteger("FreeModifiers", stats.getInteger("FreeModifiers") - 1);
				}
				exists = true;
				break;
			}
		}
		if (exists == false)
		{
			NBTTagCompound modifier = new NBTTagCompound();
			modifier.setString("identifier", modifierCode);
			modifier.setInteger("color", color);
			modifier.setInteger("level", 1);
			tool.getTagCompound().getTagList("Modifiers", 10).appendTag(modifier);
			tinkerData.getTagList("Modifiers", 8).appendTag(new NBTTagString(modifierCode));
			if (addToTraits)
			{
				tool.getTagCompound().getTagList("Traits", 8).appendTag(new NBTTagString(modifierCode));
			}
			if (modifierUsed != (byte)255)
			{
				modifier.setByte("modifierUsed", modifierUsed);
			}
			if (spendModifierOnLevel1)
			{
				tinkerData.setInteger("UsedModifiers", tinkerData.getInteger("UsedModifiers") + 1);
				stats.setInteger("FreeModifiers", stats.getInteger("FreeModifiers") - 1);
			}
		}

		if (modifierCode.equals("diamond"))
		{
			stats.setInteger("Durability", stats.getInteger("Durability") + 500);
			stats.setFloat("MiningSpeed", stats.getFloat("MiningSpeed") + 0.5f);
			stats.setFloat("Attack", stats.getFloat("Attack") + 1.2f); // todo: do these three really work?
		}
	}



	private static void AddFreeModifierSlot(ItemStack tool)
	{
		AddModifierInternal(tool, "creative", 0, (byte)255, false, false, false);
		NBTTagCompound stats = tool.getTagCompound().getCompoundTag("Stats");
		NBTTagCompound tinkerData = tool.getTagCompound().getCompoundTag("TinkerData");
		stats.setInteger("FreeModifiers", stats.getInteger("FreeModifiers") + 1);
	}



	public static int GetModifierLevel(ItemStack stack, String modifierCode)
	{
		for (NBTBase item : stack.getTagCompound().getTagList("Modifiers", 10))
		{
			NBTTagCompound modifier = (NBTTagCompound) item;
			if (modifier.getString("identifier").equals(modifierCode))
			{
				int result = modifier.getInteger("level");
				return result != 0 ? result : 1;
			}
		}
		return 0;
	}
}
