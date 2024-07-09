package mod.acats.fromanotherworld.fabric.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import mod.acats.fromanotherlibrary.client.screen.ConfigListScreen;
import mod.acats.fromanotherworld.FromAnotherWorld;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return prev -> new ConfigListScreen(FromAnotherWorld.MOD_ID, new FromAnotherWorld().getConfigs().orElseThrow(), prev);
    }
}
