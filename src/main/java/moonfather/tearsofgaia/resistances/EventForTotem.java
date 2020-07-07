package moonfather.tearsofgaia.resistances;

import moonfather.tearsofgaia.ModTears;
import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;

@Mod.EventBusSubscriber
public class EventForTotem
{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void OnPlayerDeath(LivingDeathEvent event)
	{
		if (event.isCanceled() || event.getEntity().world.isRemote || event.getSource().canHarmInCreative())
		{
			return;
		}
		if (!(event.getEntity() instanceof EntityPlayer))
		{
			return;
		}


		ItemStack found;
		EntityPlayer player = (EntityPlayer) event.getEntity();
		found = LookForAirLevel2(player.inventory.armorInventory);
		if (found == ItemStack.EMPTY)
		{
			found = LookForAirLevel2(player.inventory.offHandInventory);
		}
		if (found == ItemStack.EMPTY)
		{
			found = LookForAirLevel2(player.inventory.mainInventory, 9);
		}
		if (found == ItemStack.EMPTY)
		{
			found = LookForAirLevel2(ModTears.BaublesProxy.GetInventory(player));
		}
		if (found == ItemStack.EMPTY)
		{
			return;
		}

		ElementalHelper.ReduceAirLevel(found);

		event.setCanceled(true);
		if (player instanceof EntityPlayerMP)
		{
			EntityPlayerMP entityplayermp = (EntityPlayerMP)player;
			entityplayermp.addStat(StatList.getObjectUseStats(Items.TOTEM_OF_UNDYING));
			CriteriaTriggers.USED_TOTEM.trigger(entityplayermp, found);
		}

		player.setHealth(1.0F);
		player.clearActivePotions();
		player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
		player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
		player.world.setEntityState(player, (byte)35);
		//System.out.println("EventHandlersForPlayerHarm.OnPlayerDeath()");
	}



	private static ItemStack LookForAirLevel2(NonNullList<ItemStack> inventory)
	{
		return LookForAirLevel2(inventory, 1000);
	}



	private static ItemStack LookForAirLevel2(NonNullList<ItemStack> inventory, int numberOfItemsToCheck)
	{
		return LookForElementLevel2(inventory, numberOfItemsToCheck, "air");
	}



	public static ItemStack LookForElementLevel2(NonNullList<ItemStack> inventory, int numberOfItemsToCheck, String element)
	{
		ItemStack current;
		for (int i = 0; i < inventory.size() && i < numberOfItemsToCheck; i++)
		{
			current = inventory.get(i);
			if (ElementalHelper.IsItemElementEqual(current, element) && ElementalHelper.GetItemElementLevel(current) == 2)
			{
				return current;
			}
		}
		return ItemStack.EMPTY;
	}



	private static ItemStack LookForAirLevel2(IItemHandler inventory)
	{
		if (inventory == null)
		{
			return ItemStack.EMPTY;
		}
		ItemStack current;
		for (int i = 0; i < inventory.getSlots(); i++)
		{
			current = inventory.getStackInSlot(i);
			if (ElementalHelper.IsItemElementEqual(current, "air") && ElementalHelper.GetItemElementLevel(current) == 2)
			{
				return current;
			}
		}
		return ItemStack.EMPTY;
	}
}
