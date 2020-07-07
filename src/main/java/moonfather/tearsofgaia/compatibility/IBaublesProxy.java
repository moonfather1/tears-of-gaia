package moonfather.tearsofgaia.compatibility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public interface IBaublesProxy
{
	IItemHandler GetInventory(EntityPlayer player);

	void PutItem(EntityPlayer player, int slot, ItemStack stack);

	boolean ImplementsBaubleItemInterface(ItemStack stack);
}
