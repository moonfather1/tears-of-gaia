package moonfather.tearsofgaia;

import moonfather.tearsofgaia.compatibility.BaublesNullProxy;
import moonfather.tearsofgaia.compatibility.IBaublesProxy;
import moonfather.tearsofgaia.enchantments.EnchantmentSoulbound;
import moonfather.tearsofgaia.items.ItemGem;
import moonfather.tearsofgaia.proxies.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION, useMetadata = true) 
public class ModTears
{
    private static Logger logger;
	
	public static Item ItemGemEarth = null;
	public static Item ItemGemWater = null;
	public static Item ItemGemAir = null;
	public static Item ItemGemFire = null;

	public static Item ItemGemEarth2 = null;
	public static Item ItemGemWater2 = null;
	public static Item ItemGemAir2 = null;
	public static Item ItemGemFire2 = null;

	public static IBaublesProxy BaublesProxy = null;

	@EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
		proxy.preInit();
	    ItemGemEarth = new ItemGem("earth", 1);
	    ItemGemWater = new ItemGem("water", 1);
	    ItemGemAir = new ItemGem("air", 1);
	    ItemGemFire = new ItemGem("fire", 1);
	    ItemGemEarth2 = new ItemGem("earth", 2);
	    ItemGemWater2 = new ItemGem("water", 2);
	    ItemGemAir2 = new ItemGem("air", 2);
	    ItemGemFire2 = new ItemGem("fire", 2);
    }



    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		proxy.init(); 
    }

	
	
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
		proxy.postInit();
		if (Loader.isModLoaded("baubles"))
		{
			try
			{
				BaublesProxy = Class.forName("moonfather.tearsofgaia.compatibility.BaublesTrueProxy").asSubclass(IBaublesProxy.class).newInstance();
			}
			catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
			{
				BaublesProxy = new BaublesNullProxy();
			}
		}
		else
	    {
	    	BaublesProxy = new BaublesNullProxy();
	    }
    }



	@SidedProxy(clientSide="moonfather.tearsofgaia.proxies.ClientOnlyProxy", serverSide="moonfather.tearsofgaia.proxies.DedicatedServerProxy")
	public static CommonProxy proxy;
	
	
	
	private static void initializeItem(Item item, String internalName, String oreDictionaryName, CreativeTabs creativeTab, IForgeRegistry<Item> registry)
	{
		item.setUnlocalizedName(Constants.MODID + "." + internalName);
		item.setRegistryName(internalName);
		registry.register(item);
		if (oreDictionaryName != null)
		{
			OreDictionary.registerOre(oreDictionaryName, item); //!samo nula
		}
		if (creativeTab != null)
		{
			item.setCreativeTab(creativeTab);
		}
		proxy.registerModel(item, internalName.replace("_2", ""));
	}
	
	
	/////////////////////////////////////////////////
	
	
	@Mod.EventBusSubscriber
	public static class RegistrationHandler
	{
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event)
		{
			ModTears.initializeItem(ModTears.ItemGemEarth,"gem_earth", null, CreativeTabs.MATERIALS, event.getRegistry());
			ModTears.initializeItem(ModTears.ItemGemWater,"gem_water", null, CreativeTabs.MATERIALS, event.getRegistry());
			ModTears.initializeItem(ModTears.ItemGemAir,"gem_air", null, CreativeTabs.MATERIALS, event.getRegistry());
			ModTears.initializeItem(ModTears.ItemGemFire,"gem_fire", null, CreativeTabs.MATERIALS, event.getRegistry());

			ModTears.initializeItem(ModTears.ItemGemEarth2,"gem_earth_2", null, CreativeTabs.MATERIALS, event.getRegistry());
			ModTears.initializeItem(ModTears.ItemGemWater2,"gem_water_2", null, CreativeTabs.MATERIALS, event.getRegistry());
			ModTears.initializeItem(ModTears.ItemGemAir2,"gem_air_2", null, CreativeTabs.MATERIALS, event.getRegistry());
			ModTears.initializeItem(ModTears.ItemGemFire2,"gem_fire_2", null, CreativeTabs.MATERIALS, event.getRegistry());
		}


		@SubscribeEvent
		public static void registerEnchantments(RegistryEvent.Register<Enchantment> event)
		{
			EnchantmentSoulbound.Instance = new EnchantmentSoulbound();
			event.getRegistry().register(EnchantmentSoulbound.Instance);
		}
	}
}
