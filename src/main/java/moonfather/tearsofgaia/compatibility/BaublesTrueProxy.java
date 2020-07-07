package moonfather.tearsofgaia.compatibility;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class BaublesTrueProxy implements IBaublesProxy
{
	@Override
	public IItemHandler GetInventory(EntityPlayer player)
	{
		return BaublesApi.getBaublesHandler(player);
	}

	@Override
	public void PutItem(EntityPlayer player, int slot, ItemStack stack)
	{
		IItemHandlerModifiable inventory = BaublesApi.getBaublesHandler(player);
		if (inventory != null)
		{
			inventory.setStackInSlot(slot, stack);
		}
	}

	@Override
	public boolean ImplementsBaubleItemInterface(ItemStack stack)
	{
		return stack.getItem() instanceof IBauble;
	}
}
