package moonfather.tearsofgaia.resistances;

import moonfather.tearsofgaia.forging.ElementalHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Iterator;

@Mod.EventBusSubscriber
public class EventsForPersistence
{
	@SubscribeEvent
	public static void OnItemExpire(ItemExpireEvent event)
	{
		if (ElementalHelper.IsTearOrElementalTool(event.getEntityItem().getItem()))
		{
			event.setExtraLife(20*1200*5); // no expiration (5 extra days)
			event.setCanceled(true);
		}
	}



	private static final double zeroHeightFloatVelocity = 0.12345;// 1.0/20/4;


	private static void DoItemFloat(EntityItem entity)
	{
		DoStupidParticles(entity);
		if (entity.posY < 3.8)
		{
			// fell out of world
			if (entity.motionY == zeroHeightFloatVelocity)
			{
				if (entity.posY >= 1.9)
				{
					entity.setNoGravity(true);
					entity.motionY = -zeroHeightFloatVelocity;
					entity.motionX = entity.motionZ = 0;
					//System.out.println("go down!: " + entity.getItem().getDisplayName() + "; speed==" + entity.motionY);
				}
			}
			else if (entity.motionY == -zeroHeightFloatVelocity)
			{
				if (entity.posY < -1)
				{
					entity.setNoGravity(true);
					entity.motionY = zeroHeightFloatVelocity;
					entity.motionX = entity.motionZ = 0;
					//System.out.println("go up!: " + entity.getItem().getDisplayName() + "; speed==" + entity.motionY);
				}
			}
			else
			{
				entity.setNoGravity(true);
				entity.motionY = zeroHeightFloatVelocity;
				entity.motionX = entity.motionZ = 0;
				//System.out.println("go up!!: " + entity.getItem().getDisplayName() + "; speed==" + entity.motionY);
			}
		}
		else
		{
			// ne bi trebalo da se desi!
			if (entity.posY >= 9.9 && entity.motionY == zeroHeightFloatVelocity)
			{
				entity.setNoGravity(false);
				entity.motionY = -zeroHeightFloatVelocity;
				//System.out.println("go down!!: " + entity.getItem().getDisplayName() + "; speed==" + entity.motionY);
			}
		}
	}



	@SubscribeEvent
	public static void OnWorldTick(TickEvent.WorldTickEvent event)
	{
		DoWorldTickForFire(event);
		DoWorldTickForAir(event);
	}



	@SubscribeEvent
	public static void DoWorldTickForFire(TickEvent.WorldTickEvent event)
	{
		if (droppedElementalItemsFire.size() == 0)
		{
			return;
		}
		if (event.phase == TickEvent.Phase.START || event.world.isRemote || event.world.getTotalWorldTime() % 2*20 != 0)
		{
			return;
		}
		Iterator iterator = droppedElementalItemsFire.iterator();
		while (iterator.hasNext())
		{
			EntityItem e = (EntityItem) iterator.next();
			if (e.world == event.world)
			{
				if (e.isDead)
				{
					iterator.remove();
				}
				else
				{
					DoStupidParticles(e);
				}
			}
		}
	}

	@SubscribeEvent
	public static void DoWorldTickForAir(TickEvent.WorldTickEvent event)
	{
		if (droppedElementalItemsAir.size() == 0)
		{
			return;
		}
		if (event.phase == TickEvent.Phase.START || event.world.isRemote || event.world.getTotalWorldTime() % 10 != 0)
		{
			return;
		}
		Iterator iterator = droppedElementalItemsAir.iterator();
		while (iterator.hasNext())
		{
			EntityItem e = (EntityItem) iterator.next();
			if (e.world == event.world)
			{
				if (e.isDead)
				{
					//System.out.println("dead " + e.getItem().getDisplayName() + "; speed==" + e.motionY + "; set_size==" + droppedElementalItemsAir.size());
					iterator.remove();
				}
				else if (e.posY < 0 || e.motionY == zeroHeightFloatVelocity || e.motionY == -zeroHeightFloatVelocity || e.getTags().contains("DoItemFloat"))
				{
					//System.out.println("below zero: " + e.getItem().getDisplayName() + "; Y==" + e.posY + "; speed==" + e.motionY + "; set_size==" + droppedElementalItemsAir.size());
					DoItemFloat(e);
					e.addTag("DoItemFloat");
				}
			}
		}
	}


	private static void DoStupidParticles(Entity entity)
	{
		if (entity.world.getTotalWorldTime() % 5 != 0)
		{
			//System.out.println("NO particles for " + ((EntityItem)entity).getItem().getDisplayName());
			return;
		}
		//((WorldServer)entity.world).spawnParticle(EnumParticleTypes.VILLAGER_ANGRY, entity.posX + (double)(entity.world.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width, entity.posY + 0.5D + (double)(entity.world.rand.nextFloat() * entity.height), entity.posZ + (double)(entity.world.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width, entity.world.rand.nextFloat(), entity.world.rand.nextFloat(), entity.world.rand.nextFloat());
		if (entity.world instanceof WorldServer)
		{
			//((WorldServer) entity.world).spawnParticle(EnumParticleTypes.VILLAGER_ANGRY, true, entity.posX + (double) (entity.world.rand.nextFloat() * entity.width * 2.0F) - (double) entity.width, entity.posY + 0.5D + (double) (entity.world.rand.nextFloat() * entity.height), entity.posZ + (double) (entity.world.rand.nextFloat() * entity.width * 2.0F) - (double) entity.width, entity.world.rand.nextFloat(), entity.world.rand.nextFloat(), entity.world.rand.nextFloat(), 1, 2, 3);
			double xSpeed, ySpeed, zSpeed;
			for (int i = 1; i <= 8; i++)
			{
				xSpeed = (entity.world.rand.nextFloat() - 0.5D) * 0.40D;
				ySpeed = (entity.world.rand.nextFloat() - 0.5D) * 0.05D;
				zSpeed = (entity.world.rand.nextFloat() - 0.5D) * 0.40D;
				((WorldServer) entity.world).spawnParticle(EnumParticleTypes.SPELL_INSTANT, false, entity.posX + xSpeed * 8, entity.posY + ySpeed * 6, entity.posZ + zSpeed * 8, 1 /*numberOfParticles*/, xSpeed, ySpeed, zSpeed, 0.10d /*particleSpeed*/ /*, int... particleArguments*/);
				//entityItem.worldObj.spawnParticle(particleType, entityItem.posX + xSpeed * 8, entityItem.posY + ySpeed * 4, entityItem.posZ + zSpeed * 8, xSpeed, 2 * ySpeed, zSpeed, new int[0]);
			}

		}
		else
		{
			//System.out.println("particles params: xcoord==" + (entity.posX + (double)(entity.world.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width) + "; ycoord==" + (entity.posY + 0.5D + (double)(entity.world.rand.nextFloat() * entity.height)) + "; zcord==" + (entity.posZ + (double)(entity.world.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width) + "; xs==" + entity.world.rand.nextFloat() + "; ys==" + entity.world.rand.nextFloat() + "; zs==" + entity.world.rand.nextFloat());
		}
		//System.out.println("particles for " + ((EntityItem)entity).getItem().getDisplayName() + "; speed==" + entity.motionY + "; width==" + entity.width);
	}


	private static HashSet<EntityItem> droppedElementalItemsAir = new HashSet<EntityItem>();
	private static HashSet<EntityItem> droppedElementalItemsFire = new HashSet<EntityItem>();


	@SubscribeEvent
	public static void OnEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if (!event.getWorld().isRemote && event.getEntity() instanceof EntityItem)
		{
			ItemStack stack = ((EntityItem)event.getEntity()).getItem();
			if ((ElementalHelper.IsTear(stack) && ElementalHelper.GetTearElement(stack) == "air")
				|| ElementalHelper.IsItemElementEqual(stack,"air"))
			{
				//System.out.println("join world " + stack.getDisplayName() + "; speed==" + event.getEntity().motionY);
				droppedElementalItemsAir.add((EntityItem) event.getEntity());
				if (event.getEntity().posY < 0)
				{
					event.getEntity().setNoGravity(true);
					event.getEntity().motionY = zeroHeightFloatVelocity;
					event.getEntity().motionX = event.getEntity().motionZ = 0;
					if (event.getEntity().posY < -20)
					{
						//System.out.println("void?");
						event.getEntity().setPositionAndUpdate(event.getEntity().posX, -19, event.getEntity().posZ);
					}
				}
			}
			else if ((ElementalHelper.IsTear(stack) && ElementalHelper.GetTearElement(stack) == "fire")
					|| ElementalHelper.IsItemElementEqual(stack,"fire"))
			{
				if (!(event.getEntity() instanceof EntityItemWithFireImmunity))
				{
					EntityItemWithFireImmunity e2 = new EntityItemWithFireImmunity((EntityItem) event.getEntity());
					event.getEntity().setDead();
					event.getWorld().spawnEntity(e2);
					droppedElementalItemsFire.add(e2);
				}
			}
		}
	}

}
