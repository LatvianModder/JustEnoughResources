package jeresources.config;

import jeresources.reference.Reference;
import jeresources.utils.TranslationHelper;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler
{
    public static Configuration config;
    private static File configDir;

    public static void init(File configDir)
    {
        if (config == null)
        {
            configDir = new File(configDir, Reference.ID);
            configDir.mkdir();
            ConfigHandler.configDir = configDir;
            config = new Configuration(new File(configDir, Reference.ID + ".cfg"));
            loadConfig();
        }
    }

    public static File getConfigDir()
    {
        return configDir;
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.modID.equalsIgnoreCase(Reference.ID))
        {
            loadConfig();
        }
    }

    private static void loadConfig()
    {
        Property prop;

        prop = config.get(Configuration.CATEGORY_GENERAL, "itemsPerColumn", 4);
        prop.comment = TranslationHelper.translateToLocal("jer.config.itemsPerColumn.description");
        prop.setMinValue(1).setMaxValue(4);
        prop.setLanguageKey("jer.config.itemsPerColumn.title");
        Settings.ITEMS_PER_COLUMN = prop.getInt();

        prop = config.get(Configuration.CATEGORY_GENERAL, "itemsPerRow", 4);
        prop.comment = TranslationHelper.translateToLocal("jer.config.itemsPerRow.description");
        prop.setMinValue(1).setMaxValue(4);
        prop.setLanguageKey("jer.config.itemsPerRow.title");
        Settings.ITEMS_PER_ROW = prop.getInt();

        prop = config.get(Configuration.CATEGORY_GENERAL, "extraRange", 3);
        prop.comment = TranslationHelper.translateToLocal("jer.config.extraRange.description");
        prop.setMinValue(0).setMaxValue(25);
        prop.setLanguageKey("jer.config.extraRange.title");
        Settings.EXTRA_RANGE = prop.getInt();

        prop = config.get(Configuration.CATEGORY_GENERAL, "dimNames", true);
        prop.comment = TranslationHelper.translateToLocal("jer.config.dimNames.description");
        prop.setLanguageKey("jer.config.dimNames.title");
        Settings.useDimNames = prop.getBoolean();

        prop = config.get(Configuration.CATEGORY_GENERAL, "diyData", true);
        prop.comment = TranslationHelper.translateToLocal("jer.config.diyData.description");
        prop.setLanguageKey("jer.config.diyData.title");
        prop.requiresMcRestart();
        Settings.useDIYdata = prop.getBoolean();


        prop = config.get(Configuration.CATEGORY_GENERAL, "enchantsBlacklist", new String[] { "flimflam" });
        prop.comment = TranslationHelper.translateToLocal("jer.config.enchantsBlacklist.description");
        prop.setLanguageKey("jer.config.enchantsBlacklist.title");
        Settings.excludedEnchants = prop.getStringList();

        if (config.hasChanged())
            config.save();
        Settings.reload();
    }

    @SuppressWarnings("unchecked")
    public static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.addAll(new ConfigElement(config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
        return list;
    }
}
