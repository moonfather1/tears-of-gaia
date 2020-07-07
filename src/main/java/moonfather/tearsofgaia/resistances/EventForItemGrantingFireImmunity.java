package moonfather.tearsofgaia.resistances;

import moonfather.tearsofgaia.ModTears;
import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Iterator;


@Mod.EventBusSubscriber
public class EventForItemGrantingFireImmunity
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void OnLivingHurt(LivingHurtEvent event)
	{
		if (event.isCanceled() || event.getEntity().world.isRemote)
		{
			return;
		}
		if (!(event.getEntity() instanceof EntityPlayer) || !(event.getSource() == DamageSource.IN_FIRE || event.getSource() == DamageSource.ON_FIRE || event.getSource() == DamageSource.LAVA))
		{
			return;
		}

		EntityPlayer player = null;
		ItemStack item = ItemStack.EMPTY;
		Iterator i = playersWithFireLevel2Item.iterator();
		boolean foundInCache = false;
		while (i.hasNext())
		{
			PlayerRecord cached = (PlayerRecord) i.next();
			if (event.getEntity().world.getTotalWorldTime() > cached.recordCreationTimestamp + 20 * 12 /*12s*/)
			{
				i.remove();
				continue;
			}
			if (cached.player == event.getEntity())
			{
				ItemStack stack = GetPlayersSlotContents(cached);
				if (stack != ItemStack.EMPTY && ItemIsntTooDamaged(stack) && ElementalHelper.IsItemElementEqual(stack, "fire") && ElementalHelper.GetItemElementLevel(stack) == 2)
				{
					player = cached.player;
					item = stack;
					cached.recordCreationTimestamp = event.getEntity().world.getTotalWorldTime();
					foundInCache = true;
					break;
				}
				else
				{
					i.remove();
				}
			}
		}

		PlayerRecord toCache = null;
		if (item == ItemStack.EMPTY)
		{
			toCache = SearchForFire2((EntityPlayer) event.getEntity());
			if (toCache == null)
			{
				return; //before return store as false ?!?
			}
			player = (EntityPlayer) event.getEntity();
			item = GetPlayersSlotContents(toCache);
		}

		if (foundInCache == false)
		{
			playersWithFireLevel2Item.add(toCache);
		}

		int damage = (int) Math.floor((item.getMaxDamage() - item.getItemDamage()) * 0.1d);
		item.damageItem(damage, player);
		event.setAmount(0);
		player.setFire(0);
		//player.setFlag(0,false);

	}



	private static boolean ItemIsntTooDamaged(ItemStack stack)
	{
		return stack.getItemDamage() < stack.getMaxDamage() - 16;
	}



	private static ItemStack GetPlayersSlotContents(PlayerRecord record)
	{
		if (record.inventoryId == 'M')
		{
			return record.player.inventory.mainInventory.get(record.slot);
		}
		else if (record.inventoryId == 'A')
		{
			return record.player.inventory.armorInventory.get(record.slot);
		}
		else if (record.inventoryId == 'O')
		{
			return record.player.inventory.offHandInventory.get(record.slot);
		}
		else if (record.inventoryId == 'B')
		{
			IItemHandler bcn = ModTears.BaublesProxy.GetInventory(record.player);
			if (bcn != null)
			{
				bcn.getStackInSlot(record.slot);
			}
		}
		return ItemStack.EMPTY;
	}



	private static PlayerRecord SearchForFire2(EntityPlayer player)
	{
		int slot = LookForFireLevel2(player.inventory.armorInventory, 1000);
		if (slot >= 0)
		{
			return new PlayerRecord(player, player.world.getTotalWorldTime(), slot, 'A');
		}
		slot = LookForFireLevel2(player.inventory.offHandInventory, 1000);
		if (slot >= 0)
		{
			return new PlayerRecord(player, player.world.getTotalWorldTime(), slot, 'O');
		}
		slot = LookForFireLevel2(player.inventory.mainInventory, 9);
		if (slot >= 0)
		{
			return new PlayerRecord(player, player.world.getTotalWorldTime(), slot, 'M');
		}
		slot = LookForFireLevel2InBaubles(player);
		if (slot >= 0)
		{
			return new PlayerRecord(player, player.world.getTotalWorldTime(), slot, 'B');
		}
		return null;
	}



	private static int LookForFireLevel2InBaubles(EntityPlayer player)
	{
		IItemHandler baubles = ModTears.BaublesProxy.GetInventory(player);
		if (baubles != null)
		{
			for (int i = 0; i < baubles.getSlots(); i++)
			{
				ItemStack current = baubles.getStackInSlot(i);
				if (ItemIsntTooDamaged(current) && ElementalHelper.IsItemElementEqual(current, "fire") && ElementalHelper.GetItemElementLevel(current) == 2)
				{
					return i;
				}
			}
		}
		return -1;
	}



	private static int LookForFireLevel2(NonNullList<ItemStack> inventory, int numberOfItemsToCheck)
	{
		ItemStack current;
		for (int i = 0; i < inventory.size() && i < numberOfItemsToCheck; i++)
		{
			current = inventory.get(i);
			if (ItemIsntTooDamaged(current) && ElementalHelper.IsItemElementEqual(current, "fire") && ElementalHelper.GetItemElementLevel(current) == 2)
			{
				return i;
			}
		}
		return -1;
	}



	private static ArrayList<PlayerRecord> playersWithFireLevel2Item = new ArrayList<PlayerRecord>();

	private static class PlayerRecord
	{
		private EntityPlayer player;
		private long recordCreationTimestamp;
		private int slot;
		private char inventoryId;

		PlayerRecord(EntityPlayer player, long recordCreationTimestamp, int slot, char inventoryId)
		{
			this.player = player;
			this.recordCreationTimestamp = recordCreationTimestamp;
			this.slot = slot;
			this.inventoryId = inventoryId;
		}
	}
}
