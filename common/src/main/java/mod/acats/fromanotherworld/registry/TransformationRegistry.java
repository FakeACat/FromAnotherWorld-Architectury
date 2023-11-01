package mod.acats.fromanotherworld.registry;

import mod.acats.fromanotherworld.transformation.FormSelector;
import mod.acats.fromanotherworld.transformation.transformations.*;

public class TransformationRegistry {
    public static void register() {
        FormSelector.registerTransformation(new CrawlerTransformation());
        FormSelector.registerTransformation(new JulietteThingTransformation());
        FormSelector.registerTransformation(new PalmerThingTransformation());

        FormSelector.registerTransformation(new DogBeastSpitterTransformation());
        FormSelector.registerTransformation(new DogBeastTransformation());
        FormSelector.registerTransformation(new ImpalerTransformation());

        FormSelector.registerTransformation(new ProwlerTransformation());

        FormSelector.registerTransformation(new BeastTransformation());

        FormSelector.registerTransformation(new MiscTransformation());
    }
}
