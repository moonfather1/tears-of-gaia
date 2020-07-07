package moonfather.tearsofgaia.proxies;

import moonfather.tearsofgaia.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientOnlyProxy extends CommonProxy
{
	private final int DEFAULT_ITEM_SUBTYPE = 0;

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
	  	//required in order for the renderer to know how to render your item.
		this.registerModel(item, DEFAULT_ITEM_SUBTYPE, path);
	}

	@Override
	public void registerModel(Item item, int metadata, String path)
	{
	  	//required in order for the renderer to know how to render your item.
		ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(Constants.MODID + ":" + path, "inventory"));
	}
	
	@Override
	public boolean playerIsInCreativeMode(EntityPlayer player)
	{
		if (player instanceof EntityPlayerMP)
		{
			EntityPlayerMP entityPlayerMP = (EntityPlayerMP)player;
			return entityPlayerMP.interactionManager.isCreative();
		}
		else if (player instanceof EntityPlayerSP)
		{
			return Minecraft.getMinecraft().playerController.isInCreativeMode();
		}
		return false;
	}

	@Override
	public boolean isDedicatedServer()
	{
		return false;
	}
	
	@Override
	public String getTranslatedText(String key, Object... parameters)
	{
		return I18n.format(key, parameters);
	}
}
