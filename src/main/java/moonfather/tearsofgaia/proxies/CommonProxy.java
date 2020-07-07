package moonfather.tearsofgaia.proxies;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public abstract class CommonProxy 
{
	public void preInit()
	{
	}

	public void init()
	{
	}

	public void postInit()
	{
	}
	
	public void registerModel(Item item, String path)
	{
	}

	public void registerModel(Item item, int metadata, String path)
	{
	}

	// helper to determine whether the given player is in creative mode
	//  not necessary for most examples
	abstract public boolean playerIsInCreativeMode(EntityPlayer player);

	/**
	 * is this a dedicated server?
	 * @return true if this is a dedicated server, false otherwise
	 */
	abstract public boolean isDedicatedServer();
	
	
	abstract public String getTranslatedText(String key, Object... parameters);
}
