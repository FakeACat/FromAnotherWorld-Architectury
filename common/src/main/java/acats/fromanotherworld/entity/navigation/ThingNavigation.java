package acats.fromanotherworld.entity.navigation;

import acats.fromanotherworld.entity.thing.Thing;
import mod.azure.azurelib.ai.pathing.AzureNavigation;
import net.minecraft.world.level.Level;

public class ThingNavigation extends AzureNavigation {
    public ThingNavigation(Thing entity, Level world) {
        super(entity, world);
        this.setCanWalkOverFences(true);
    }
}
