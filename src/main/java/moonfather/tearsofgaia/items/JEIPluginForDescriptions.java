package moonfather.tearsofgaia.items;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import moonfather.tearsofgaia.ModTears;
import moonfather.tearsofgaia.enchantments.EnchantmentSoulbound;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEIPluginForDescriptions implements IModPlugin
{
	public void register(IModRegistry registry)
	{
		registry.addIngredientInfo(new ItemStack(ModTears.ItemGemEarth), VanillaTypes.ITEM, "item.tearsofgaia.gem_earth.usage_1");
		registry.addIngredientInfo(new ItemStack(ModTears.ItemGemEarth2), VanillaTypes.ITEM, "item.tearsofgaia.gem_earth.usage_2");
		registry.addIngredientInfo(new ItemStack(ModTears.ItemGemWater), VanillaTypes.ITEM, "item.tearsofgaia.gem_water.usage_1");
		registry.addIngredientInfo(new ItemStack(ModTears.ItemGemWater2), VanillaTypes.ITEM, "item.tearsofgaia.gem_water.usage_2");
		registry.addIngredientInfo(new ItemStack(ModTears.ItemGemFire), VanillaTypes.ITEM, "item.tearsofgaia.gem_fire.usage_1");
		registry.addIngredientInfo(new ItemStack(ModTears.ItemGemFire2), VanillaTypes.ITEM, "item.tearsofgaia.gem_fire.usage_2");
		registry.addIngredientInfo(new ItemStack(ModTears.ItemGemAir), VanillaTypes.ITEM, "item.tearsofgaia.gem_air.usage_1");
		registry.addIngredientInfo(new ItemStack(ModTears.ItemGemAir2), VanillaTypes.ITEM, "item.tearsofgaia.gem_air.usage_2");

		registry.addIngredientInfo(ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(EnchantmentSoulbound.Instance, 1)), VanillaTypes.ITEM, "enchantment.soulbound_mf.description");
		registry.addIngredientInfo(ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(EnchantmentSoulbound.Instance, 2)), VanillaTypes.ITEM, "enchantment.soulbound_mf.description");
		registry.addIngredientInfo(ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(EnchantmentSoulbound.Instance, 3)), VanillaTypes.ITEM, "enchantment.soulbound_mf.description");
	}
}
