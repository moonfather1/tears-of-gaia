package moonfather.tearsofgaia.proxies;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;

public class DedicatedServerProxy extends CommonProxy 
{
	@Override
	public void preInit()
	{
		super.preInit();		
	}

	@Override
	public void init()
	{
		super.init();
	}

	@Override
	public void postInit()
	{
		super.postInit();
	}
	
	@Override
	public void registerModel(Item item, String path)
	{
	}

	@Override
	public void registerModel(Item item, int metadata, String path)
	{
	}

	@Override
	public boolean playerIsInCreativeMode(EntityPlayer player)
	{
		if (player instanceof EntityPlayerMP)
		{
	    	EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
	      	return entityPlayerMP.interactionManager.isCreative();
	    }
	    return false;
	}

	@Override
	public boolean isDedicatedServer() 
	{
		return true;
	}
	
	@Override
	public String getTranslatedText(String key, Object... parameters)
	{
		return "TRANSLATE(" + key + ")";
	}
}
