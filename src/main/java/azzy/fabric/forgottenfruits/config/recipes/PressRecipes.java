package azzy.fabric.forgottenfruits.config.recipes;

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount;
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys;
import azzy.fabric.forgottenfruits.util.recipe.JanksonRecipeParser;
import azzy.fabric.forgottenfruits.util.recipe.RecipeRegistryKey;
import azzy.fabric.forgottenfruits.util.recipe.RecipeTemplate;
import azzy.fabric.forgottenfruits.util.recipe.templates.FFPressRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

import static azzy.fabric.forgottenfruits.ForgottenFruits.FFLog;
import static azzy.fabric.forgottenfruits.ForgottenFruits.config;

public class PressRecipes extends RecipeTemplate<FFPressRecipe> {
    public PressRecipes() {
//        Queue<Iterator<String>> recipes = JanksonRecipeParser.getRecipeQueue(RecipeRegistryKey.PRESS);
//        Queue<Iterator<String>> recipes = null;
        //while (recipes.peek() != null) {
        //    Iterator<String> recipeBits = recipes.poll();
        //    try {
        //        inject(recipeBits.next(), Integer.parseInt(recipeBits.next()), recipeBits.next(), recipeBits.next(), recipeBits.next());
        //    } catch (NoSuchElementException e) {
        //        FFLog.error("A PRESS RECIPE IS INVALID");
        //    }
        //}
    }

    private void inject(String in, int amount, String by, String out, String id) {
        ItemStack input = new ItemStack(Registry.ITEM.get(new Identifier(in)), amount);
        recipes.put(serialize(new ItemStack[]{input}), new FFPressRecipe(RecipeRegistryKey.PRESS, id, input, Registry.ITEM.get(new Identifier(by)), FluidKeys.get(Registry.FLUID.get(new Identifier(out))).withAmount(FluidAmount.BUCKET)));
        if (config.isDebug())
            FFLog.debug("DEBUG - KEY - " + serialize(new ItemStack[]{input}));
    }
}
