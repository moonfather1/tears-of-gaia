package moonfather.tearsofgaia.forging;

import moonfather.tearsofgaia.Constants;
import moonfather.tearsofgaia.ModTears;
import moonfather.tearsofgaia.enchantments.EnchantmentSoulbound;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSaddle;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Mod.EventBusSubscriber
public class EventForAnvil
{
	@SubscribeEvent
	public static void OnAnvilUpdate(AnvilUpdateEvent event)
	{
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();

		if (ElementalHelper.IsTear(right))
		{
			ElementalHelper.SetStartingTempTooltipTime(right);
		}
		if (ElementalHelper.IsTear(left))
		{
			ElementalHelper.SetStartingTempTooltipTime(left);
		}

		if (left.isEmpty() || right.isEmpty())
		{
			return;
		}
		else if (ElementalHelper.IsTear(right) && IsValidItem(left, ElementalHelper.GetTearElement(right), ElementalHelper.GetTearLevel(right)))
		{
			ImbueItem(left, right, event);
		}
		else if (ElementalHelper.IsTear(left) && IsValidItem(right, ElementalHelper.GetTearElement(left), ElementalHelper.GetTearLevel(left)))
		{
			ImbueItem(right, left, event);
		}
	}


	private static boolean IsValidItemForLevel2(ItemStack stack, String gemElement)
	{
		if (stack.getItem() == Items.ENCHANTED_BOOK)
		{
			return false;
		}
		String itemElement = ElementalHelper.GetItemElement(stack);
		int itemLevel = 0;
		if (itemElement == null)
		{
			return false; // not imbued at all
		}
		else if (!itemElement.equals(gemElement))
		{
			return false; // wrong element
		}
		else if ((itemLevel = ElementalHelper.GetItemElementLevel(stack)) != 1)
		{
			if (itemLevel == 2 && itemElement == "earth" && EnchantmentHelper.getEnchantmentLevel(EnchantmentSoulbound.Instance, stack) == 0)
			{
				return true; // spent soulbound; allow refill
			}
			return false; // not imbued to level 1
		}
		else if (gemElement.equals("fire") && stack.getMaxDamage() == 0)
		{
			// can't apply onto items that can't be damaged
			return (stack.getItem() instanceof ItemSaddle || stack.getItem().getHorseArmorType(stack) != HorseArmorType.NONE);
		}
		else
		{
			return true;
		}
	}



	private static boolean IsValidItemForLevel1(ItemStack stack, String element)
	{
		//if (element=="earth")
		//return true; /////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

		if (ElementalHelper.GetItemElement(stack) != null)
		{
			return false; // already imbued
		}
		else if (stack.getItem() == Items.ENCHANTED_BOOK)
		{
			return false;
		}
		else if (element == "earth")
		{
			return EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack) > 0;
		}
		else if (element == "water")
		{
			if (stack.getItem().isDamageable() && stack.getItemDamage() > 5)
			{
				return true;
			}
			for (Enchantment enchantment : Enchantment.REGISTRY)
			{
				if (enchantment.isCurse() && EnchantmentHelper.getEnchantmentLevel(enchantment, stack) > 0)
				{
					return true;
				}
			}
			return false;
		}
		else if (element == "air")
		{
			return  IsEquipableArmorOrShieldOrHorseEquipement(stack);//return stack.getItem().getItemStackLimit(stack) == 1;
		}
		else if (element == "fire")
		{
			return  IsEquipableArmorOrShieldOrHorseEquipement(stack);
		}
		else
		{
			return false;
		}
	}



	private static boolean IsValidItem(ItemStack stack, String element, int level)
	{
		if (level == 1)
		{
			return IsValidItemForLevel1(stack, element);
		}
		else if (level == 2)
		{
			return IsValidItemForLevel2(stack, element);
		}
		else
		{
			return false;
		}
	}



	private static void ImbueItem(ItemStack stack, ItemStack gem, AnvilUpdateEvent event)
	{
		if (ElementalHelper.GetTearLevel(gem) > 1)
		{
			ImbueItemToLevel2(stack, gem, event);
			return;
		}

		String element = ElementalHelper.GetTearElement(gem);
		ItemStack output = stack.copy();
		output.setStackDisplayName(event.getName() != null && !event.getName().equals("") ? event.getName() : stack.getDisplayName());
		output.setTagInfo(Constants.TAG_KEY_ELEMENT, new NBTTagString(element));
		output.setTagInfo(Constants.TAG_KEY_LEVEL, new NBTTagInt(1));
		if (element.equals("earth"))
		{
			NBTTagList nbttaglist = output.getEnchantmentTagList();
			int i = 0;
			while (i < nbttaglist.tagCount())
			{
				NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
				Enchantment enchantment = Enchantment.getEnchantmentByID(nbttagcompound.getShort("id"));
				if (enchantment== Enchantments.UNBREAKING)
				{
					int level = nbttagcompound.getShort("lvl");
					nbttagcompound.setShort("lvl", (short)(level + 1));
					break;
				}
				i += 1;
			}
			//TC:    NBTTagCompound extra = output.getTagCompound();              /////////////////////////////
		}
		else if (element.equals("water"))
		{
			output.setItemDamage(0);

			NBTTagList nbttaglist = output.getEnchantmentTagList();
			int i = 0;
			while (i < nbttaglist.tagCount())
			{
				NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
				Enchantment enchantment = Enchantment.getEnchantmentByID(nbttagcompound.getShort("id"));
				if (enchantment.isCurse())
				{
					nbttaglist.removeTag(i);
				}
				else
				{
					i = i + 1;
				}
			}
		}
		else if (element.equals("air"))
		{
		}
		else if (element.equals("fire"))
		{
		}
		event.setOutput(output);
		event.setMaterialCost(1);
		event.setCost(output.getDisplayName().equals(stack.getDisplayName()) ? 1 : 2);
	}



	private static void ImbueItemToLevel2(ItemStack stack, ItemStack gem, AnvilUpdateEvent event)
	{
		String element = ElementalHelper.GetTearElement(gem);
		ItemStack output = stack.copy();
		output.setStackDisplayName(event.getName() != null && !event.getName().equals("") ? event.getName() : stack.getDisplayName());
		output.setTagInfo(Constants.TAG_KEY_ELEMENT, new NBTTagString(element));
		output.setTagInfo(Constants.TAG_KEY_LEVEL, new NBTTagInt(2));
		if (element.equals("earth"))
		{
			output.setItemDamage((int) Math.floor(output.getItemDamage() * 0.75));

			NBTTagList nbttaglist = output.getEnchantmentTagList();
			int i = 0; boolean done = false;
			while (i < nbttaglist.tagCount())
			{
				NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
				Enchantment enchantment = Enchantment.getEnchantmentByID(nbttagcompound.getShort("id"));
				if (enchantment == EnchantmentSoulbound.Instance)
				{
					int level = nbttagcompound.getShort("lvl");
					nbttagcompound.setShort("lvl", (short)(level + 1));
					done = true;
					break;
				}
				i += 1;
			}
			if (!done)
			{
				output.addEnchantment(EnchantmentSoulbound.Instance, 1);
			}
		}
		else if (element.equals("water"))
		{
			output.setItemDamage(0);

			NBTTagList nbttaglist = output.getEnchantmentTagList();
			int i = 0;
			while (i < nbttaglist.tagCount())
			{
				NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
				Enchantment enchantment = Enchantment.getEnchantmentByID(nbttagcompound.getShort("id"));
				if (enchantment.isCurse())
				{
					nbttaglist.removeTag(i);
				}
				else
				{
					if (enchantment.getMaxLevel() > 1)
					{
						int level = nbttagcompound.getShort("lvl");
						nbttagcompound.setShort("lvl", (short)(level + 1));
					}
					i = i + 1;
				}
			}
		}
		else if (element.equals("air"))
		{
		}
		else if (element.equals("fire"))
		{
		}
		event.setOutput(output);
		event.setMaterialCost(1);
		event.setCost(output.getDisplayName().equals(stack.getDisplayName()) ? 10 : 11);
	}



	private static boolean IsEquipableArmorOrShieldOrHorseEquipement(ItemStack stack)
	{
		EntityEquipmentSlot slot = EntityLiving.getSlotForItemStack(stack);
		if (slot != EntityEquipmentSlot.MAINHAND)
		{
			return true;
		}
		if (stack.isItemEnchanted() || stack.getItem().isEnchantable(stack) || stack.getItem() instanceof ItemTool)
		{
			return true;
		}
		if (stack.getItem() instanceof ItemSaddle || stack.getItem().getHorseArmorType(stack) != HorseArmorType.NONE)
		{
			return true;
		}
		if (ModTears.BaublesProxy.ImplementsBaubleItemInterface(stack))
		{
			return true;
		}
		return  false;
	}
}
