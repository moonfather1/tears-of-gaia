package moonfather.tearsofgaia.enchantments;

import moonfather.tearsofgaia.ModTears;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.Iterator;

@Mod.EventBusSubscriber
public class EventForSoulbound
{
	@SubscribeEvent
	public static void OnClonePlayer(PlayerEvent.Clone event)
	{
		if (!event.isWasDeath() /* changed dimension */ || event.isCanceled())
		{
			return;
		}

		OnClonePlayerInternal(event.getOriginal().inventory.armorInventory, event.getEntityPlayer().inventory.armorInventory);
		OnClonePlayerInternal(event.getOriginal().inventory.mainInventory, event.getEntityPlayer().inventory.mainInventory);
		OnClonePlayerInternal(event.getOriginal().inventory.offHandInventory, event.getEntityPlayer().inventory.offHandInventory);
		OnClonePlayerInternal(ModTears.BaublesProxy.GetInventory(event.getOriginal()), (IItemHandlerModifiable) ModTears.BaublesProxy.GetInventory(event.getEntityPlayer()));
	}



	@SubscribeEvent
	public static void OnPlayerDrops(PlayerDropsEvent event)
	{
		Iterator i = event.getDrops().iterator();
		while (i.hasNext())
		{
			EntityItem ei = (EntityItem) i.next();
			if (EnchantmentHelper.getEnchantmentLevel(EnchantmentSoulbound.Instance, ei.getItem()) > 0)
			{
				NBTTagCompound t = ei.getItem().getOrCreateSubCompound("Soulbound_temp");
				String itemListId = t.getString("inv");
				int slot = t.getInteger("slot");
				if (itemListId.equals("armor"))
				{
					event.getEntityPlayer().inventory.armorInventory.set(slot, ei.getItem().copy());
				}
				else if (itemListId.equals("main"))
				{
					event.getEntityPlayer().inventory.mainInventory.set(slot, ei.getItem().copy());
				}
				else if (itemListId.equals("offh"))
				{
					event.getEntityPlayer().inventory.offHandInventory.set(slot, ei.getItem().copy());
				}
				else if (itemListId.equals("baubles"))
				{
					IItemHandler inventory = ModTears.BaublesProxy.GetInventory(event.getEntityPlayer());
					if (inventory != null)
					{
						ModTears.BaublesProxy.PutItem(event.getEntityPlayer(), slot, ei.getItem().copy());
					}
				}
				else
				{
					continue;
				}
				t.removeTag("Soulbound_temp");
				i.remove();
			}
		}
	}



	@SubscribeEvent
	public static void OnDeath(LivingDeathEvent event)
	{
		if (!event.isCanceled() && event.getEntity() instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
			OnDeathInternal(player.inventory.armorInventory, "armor");
			OnDeathInternal(player.inventory.mainInventory, "main");
			OnDeathInternal(player.inventory.offHandInventory, "offh");
			OnDeathInternal(ModTears.BaublesProxy.GetInventory(player), "baubles");
		}
	}



	private static void OnDeathInternal(NonNullList<ItemStack> itemList, String itemListId)
	{
		int level = 0;
		for (int i = 0; i < itemList.size(); i++)
		{
			level = EnchantmentHelper.getEnchantmentLevel(EnchantmentSoulbound.Instance, itemList.get(i));
			if (level > 0)
			{
				NBTTagCompound t = itemList.get(i).getOrCreateSubCompound("Soulbound_temp");
				t.setString("inv", itemListId);
				t.setInteger("slot", i);
			}
		}
	}



	private static void OnDeathInternal(IItemHandler items, String itemListId)
	{
		if (items == null)
		{
			return;
		}
		int level = 0;
		for (int i = 0; i < items.getSlots(); i++)
		{
			level = EnchantmentHelper.getEnchantmentLevel(EnchantmentSoulbound.Instance, items.getStackInSlot(i));
			if (level > 0)
			{
				NBTTagCompound t = items.getStackInSlot(i).getOrCreateSubCompound("Soulbound_temp");
				t.setString("inv", itemListId);
				t.setInteger("slot", i);
			}
		}
	}



	private static void OnClonePlayerInternal(NonNullList<ItemStack> oldInventory, NonNullList<ItemStack> newInventory)
	{
		int level = 0;
		for (int i = 0; i < oldInventory.size(); i++)
		{
			level = EnchantmentHelper.getEnchantmentLevel(EnchantmentSoulbound.Instance, oldInventory.get(i));
			if (level > 0)
			{
				ItemStack itemToReturn = oldInventory.get(i).copy();
				ReduceLevelOfSoulbound(itemToReturn);
				newInventory.set(i, itemToReturn);
			}
		}
	}



	private static void OnClonePlayerInternal(IItemHandler oldInventory, IItemHandlerModifiable newInventory)
	{
		if (oldInventory == null || newInventory == null)
		{
			return;
		}
		int level = 0;
		for (int i = 0; i < oldInventory.getSlots(); i++)
		{
			level = EnchantmentHelper.getEnchantmentLevel(EnchantmentSoulbound.Instance, oldInventory.getStackInSlot(i));
			if (level > 0)
			{
				ItemStack itemToReturn = oldInventory.getStackInSlot(i).copy();
				ReduceLevelOfSoulbound(itemToReturn);
				newInventory.setStackInSlot(i, itemToReturn);
			}
		}
	}



	private static void ReduceLevelOfSoulbound(ItemStack itemToReturn)
	{
		int id = Enchantment.getEnchantmentID(EnchantmentSoulbound.Instance);
		NBTTagList nbttaglist = itemToReturn.getEnchantmentTagList();
		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			if (nbttagcompound.getShort("id") != id	)
			{
				continue;
			}
			int level = nbttagcompound.getShort("lvl");
			if (level > 1)
			{
				nbttagcompound.setShort("lvl", (short) (level - 1));
			}
			else if (level == 1)
			{
				nbttaglist.removeTag(i);
				break;
			}
		}
	}
}