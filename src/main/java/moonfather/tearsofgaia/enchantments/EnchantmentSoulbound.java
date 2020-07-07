package moonfather.tearsofgaia.enchantments;

import moonfather.tearsofgaia.options.Options112;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentSoulbound extends Enchantment
{
	public static EnchantmentSoulbound Instance = null;


	public EnchantmentSoulbound()
	{
		super(Rarity.VERY_RARE, EnumEnchantmentType.ALL, EntityEquipmentSlot.values());
		this.setName("soulbound_mf");
		this.setRegistryName("enchantment.soulbound_mf");
	}

	/**
	 * Returns the minimal value of enchantability needed on the enchantment level passed.
	 */
	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return enchantmentLevel * 25;
	}

	/**
	 * Returns the maximum value of enchantability needed on the enchantment level passed.
	 */
	@Override
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return this.getMinEnchantability(enchantmentLevel) + 50;
	}

	@Override
	public boolean isTreasureEnchantment()
	{
		return true;
	}

	@Override
	public boolean isAllowedOnBooks() { return Options112.SoulboundBookEnabledInWorld; }

	/**
	 * Returns the maximum level that the enchantment can have.
	 */
	@Override
	public int getMaxLevel()
	{
		return 3;
	}

	public boolean canApplyTogether(Enchantment ench)
	{
		return super.canApplyTogether(ench) && ench != Enchantments.VANISHING_CURSE;
	}
}
