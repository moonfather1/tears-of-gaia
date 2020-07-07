package moonfather.tearsofgaia.forging;

import moonfather.tearsofgaia.Constants;
import moonfather.tearsofgaia.items.ItemGem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;

public class ElementalHelper
{
	public static String GetItemElement(ItemStack stack)
	{
		if (stack.isEmpty() || stack.getTagCompound() == null || !stack.getTagCompound().hasKey(Constants.TAG_KEY_ELEMENT))
		{
			return null;
		}
		return stack.getTagCompound().getString(Constants.TAG_KEY_ELEMENT);
	}



	public static int GetItemElementLevel(ItemStack stack)
	{
		if (stack.isEmpty() || stack.getTagCompound() == null || !stack.getTagCompound().hasKey(Constants.TAG_KEY_LEVEL))
		{
			return 1;
		}
		return stack.getTagCompound().getInteger(Constants.TAG_KEY_LEVEL);
	}



	public static boolean IsItemElementEqual(ItemStack stack, String element)
	{
		String temp = GetItemElement(stack);
		return temp != null ? temp.equals(element) : (element == null);
	}



	public static boolean IsElementalTool(ItemStack stack)
	{
		if (stack.isEmpty() || stack.getTagCompound() == null || !stack.getTagCompound().hasKey(Constants.TAG_KEY_ELEMENT))
		{
			return false;
		}
		return true;
	}



	public static boolean IsTearOrElementalTool(ItemStack stack)
	{
		return IsTear(stack) || IsElementalTool(stack);
	}



	public static boolean IsTear(ItemStack stack)
	{
		return !stack.isEmpty() && stack.getItem() instanceof ItemGem;
	}



	public static String GetTearElement(ItemStack gem)
	{
		return ((ItemGem) gem.getItem()).GetElement();
	}



	public static int GetTempTooltipTime(ItemStack stack)
	{
		if (stack.isEmpty() || stack.getTagCompound() == null || !stack.getTagCompound().hasKey(Constants.TAG_KEY_TTT_TIME))
		{
		//	System.out.println("---- GetTempTooltipTime " + stack.hashCode() + "->0");
			return 0;
		}
		//System.out.println("---- GetTempTooltipTime " + stack.hashCode() + "->" + stack.getTagCompound().getInteger(Constants.TAG_KEY_TTT_TIME));
		return stack.getTagCompound().getInteger(Constants.TAG_KEY_TTT_TIME);
	}



	public static void UpdateTempTooltipTime(ItemStack stack)
	{
		if (stack.isEmpty() || stack.getTagCompound() == null || !stack.getTagCompound().hasKey(Constants.TAG_KEY_TTT_TIME))
		{
			//System.out.println("---- UpdateTempTooltipTime " + stack.hashCode() + ",NIL");
			return;
		}
		int current = stack.getTagCompound().getInteger(Constants.TAG_KEY_TTT_TIME);
		int newTime = current >= 0 ? current - 1 : 0;
		stack.setTagInfo(Constants.TAG_KEY_TTT_TIME, new NBTTagInt(newTime));
		int current2 = stack.getTagCompound().getInteger(Constants.TAG_KEY_TTT_TIME);
		//System.out.println("---- UpdateTempTooltipTime " + stack.hashCode() + ":    " + current + "->" + newTime + "   ====>" + current2);
	}



	public static void SetStartingTempTooltipTime(ItemStack stack)
	{
		stack.setTagInfo(Constants.TAG_KEY_TTT_TIME, new NBTTagInt(500)); // tried 100; was about 1s while i expected 5s.
	}



	public static int GetTearLevel(ItemStack gem) {	return ((ItemGem) gem.getItem()).GetLevel();  }



	public static void ReduceAirLevel(ItemStack stack)
	{
		stack.setTagInfo(Constants.TAG_KEY_LEVEL, new NBTTagInt(1));
	}
}
