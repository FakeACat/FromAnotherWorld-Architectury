package mod.acats.fromanotherworld.transformation;

import java.util.ArrayList;
import java.util.List;

public class FormSelector {
    private static final List<Transformation<?>> TRANSFORMATIONS = new ArrayList<>();

    public static void registerTransformation(Transformation<?> transformation) {
        TRANSFORMATIONS.add(transformation);
    }

    public static Transformation<?> getWeightedRandomFor(TransformationContext ctx) {
        List<Transformation<?>> transformations = FormSelector.allowedTransformations(ctx);

        double totalWeight = 0.0D;

        for (Transformation<?> transformation :
                transformations) {
            totalWeight += transformation.weight(ctx);
        }

        int index = 0;
        for (double r = ctx.level().getRandom().nextDouble() * totalWeight; index < transformations.size() - 1; ++index) {
            r -= transformations.get(index).weight(ctx);

            if (r <= 0.0) {
                break;
            }
        }

        return transformations.get(index);
    }

    private static List<Transformation<?>> allowedTransformations(TransformationContext ctx) {
        double highestPriority = 0.0D;

        for (Transformation<?> transformation:
             TRANSFORMATIONS) {
            double priority = transformation.priority(ctx);
            if (transformation.weight(ctx) > 0.0D && priority > highestPriority) {
                highestPriority = priority;
            }
        }

        List<Transformation<?>> allowedTransformations = new ArrayList<>();

        for (Transformation<?> transformation:
                TRANSFORMATIONS) {
            if (transformation.weight(ctx) > 0.0D && transformation.priority(ctx) >= highestPriority) {
                allowedTransformations.add(transformation);
            }
        }

        if (allowedTransformations.isEmpty()) {
            throw new RuntimeException("No appropriate Thing transformations available for the provided context!\n" +
                    "Context Information:\n" +
                    "Previous Form ID: " + ctx.previousFormTag().getString("id") + "\n" +
                    "Position: " + ctx.position().toString());
        }

        return allowedTransformations;
    }
}
