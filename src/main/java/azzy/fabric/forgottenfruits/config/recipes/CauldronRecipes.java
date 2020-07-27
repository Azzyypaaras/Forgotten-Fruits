package azzy.fabric.forgottenfruits.config.recipes;

import azzy.fabric.forgottenfruits.util.recipe.JanksonRecipeParser;
import azzy.fabric.forgottenfruits.util.recipe.RecipeRegistryKey;
import azzy.fabric.forgottenfruits.util.recipe.RecipeTemplate;
import azzy.fabric.forgottenfruits.util.recipe.templates.FFCauldronRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

import static azzy.fabric.forgottenfruits.ForgottenFruits.FFLog;

public class CauldronRecipes extends RecipeTemplate<FFCauldronRecipe> {
    public CauldronRecipes() {
//        Queue<Iterator<String>> recipes = JanksonRecipeParser.getRecipeQueue(RecipeRegistryKey.CAULDRON);
//        Queue<Iterator<String>> recipes = null;
//        while (recipes.peek() != null) {
//            Iterator<String> recipeBits = recipes.poll();
//            try {
//                inject(recipeBits.next(), Registry.ITEM.get(new Identifier(recipeBits.next())), Registry.ITEM.get(new Identifier(recipeBits.next())), Integer.parseInt(recipeBits.next()), recipeBits.next());
//            } catch (NoSuchElementException e) {
//                FFLog.error("A CAULDRON RECIPE IS INVALID");
//            }
//        }
    }

    private void inject(String id, Item brew, Item output, int amount, String rawInputs) {

        String[] inputs = rawInputs.split("~");
        ItemStack[] items = new ItemStack[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            items[i] = new ItemStack(Registry.ITEM.get(new Identifier(inputs[i].trim())));
        }

        recipes.put(serialize(items), new FFCauldronRecipe(RecipeRegistryKey.CAULDRON, id, brew, new ItemStack(output, amount), items));
    }
}
