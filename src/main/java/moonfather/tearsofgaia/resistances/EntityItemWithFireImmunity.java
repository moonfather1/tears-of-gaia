package moonfather.tearsofgaia.resistances;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

import java.util.UUID;

public class EntityItemWithFireImmunity extends EntityItem
{
	public EntityItemWithFireImmunity(EntityItem original)
	{
		super(original.world);
		NBTTagCompound nbt = new NBTTagCompound();
		this.readFromNBT(original.writeToNBT(nbt));
		this.setFire(0);
		this.isImmuneToFire = true;
		this.entityUniqueID = UUID.randomUUID();
		this.cachedUniqueIdString = this.entityUniqueID.toString();
	}



	@Override
	protected void dealFireDamage(int amount)
	{
	}



	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (source != null && (source.equals(DamageSource.IN_FIRE) || source.equals(DamageSource.ON_FIRE) || source.equals(DamageSource.LAVA) || source.equals(DamageSource.HOT_FLOOR) || source.equals(DamageSource.DRAGON_BREATH)))
		{
			return false;
		}
		return super.attackEntityFrom(source, amount);
	}



	@Override
	public boolean canRenderOnFire()
	{
		return false;
	}
}
