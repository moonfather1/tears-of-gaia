package moonfather.tearsofgaia.compatibility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class BaublesNullProxy implements IBaublesProxy
{
	@Override
	public IItemHandler GetInventory(EntityPlayer player)
	{
		return null;
	}

	@Override
	public void PutItem(EntityPlayer player, int slot, ItemStack stack)
	{
	}

	@Override
	public boolean ImplementsBaubleItemInterface(ItemStack stack)
	{
		return false;
	}
}
