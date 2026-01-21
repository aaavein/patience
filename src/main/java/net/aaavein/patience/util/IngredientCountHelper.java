package net.aaavein.patience.util;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public final class IngredientCountHelper {
    private static final Logger LOGGER = LogManager.getLogger(IngredientCountHelper.class);

    public static int getRecipeIngredientCount(AbstractContainerMenu menu) {
        if (menu == null) {
            return 1;
        }

        String className = menu.getClass().getName();

        if (className.equals("net.joefoxe.hexerei.container.WoodcutterContainer")) {
            return getHexereiWoodcutterIngredientCount(menu);
        }

        return 1;
    }

    private static int getHexereiWoodcutterIngredientCount(AbstractContainerMenu menu) {
        try {
            Field indexField = menu.getClass().getDeclaredField("selectedRecipeIndex");
            indexField.setAccessible(true);
            DataSlot indexSlot = (DataSlot) indexField.get(menu);
            int selectedIndex = indexSlot.get();

            if (selectedIndex < 0) {
                return 1;
            }

            Field recipesField = menu.getClass().getDeclaredField("recipes");
            recipesField.setAccessible(true);
            List<?> recipes = (List<?>) recipesField.get(menu);

            if (recipes.isEmpty() || selectedIndex >= recipes.size()) {
                return 1;
            }

            Object recipeHolder = recipes.get(selectedIndex);
            Method valueMethod = recipeHolder.getClass().getMethod("value");
            Object recipe = valueMethod.invoke(recipeHolder);

            Field countField = recipe.getClass().getField("ingredientCount");
            return Math.max(1, countField.getInt(recipe));

        } catch (Exception e) {
            LOGGER.debug("Failed to get Hexerei woodcutter ingredient count: {}", e.getMessage());
            return 1;
        }
    }

    private IngredientCountHelper() {}
}