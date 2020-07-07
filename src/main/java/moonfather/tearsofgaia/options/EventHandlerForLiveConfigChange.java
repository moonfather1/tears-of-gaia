package moonfather.tearsofgaia.options;

import moonfather.tearsofgaia.Constants;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class EventHandlerForLiveConfigChange
{
	/**
	 * Inject the new values and save to the config file when the config has been changed from the GUI.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(Constants.MODID))
		{
			ConfigManager.sync(Constants.MODID, Config.Type.INSTANCE);
		}
	}
}
