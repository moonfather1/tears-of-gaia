package moonfather.tearsofgaia.items;

import com.google.gson.JsonObject;
import moonfather.tearsofgaia.options.Options112;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

public class LevelTwoRecipesConditionFactory implements IConditionFactory
{

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json)
	{
		return () -> Options112.LevelTwoGemsEnabled;
	}
}
