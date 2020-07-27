package azzy.fabric.forgottenfruits.config.programatic;

import azzy.fabric.forgottenfruits.util.recipe.JanksonRecipeParser;
import azzy.fabric.forgottenfruits.util.recipe.RecipeRegistryKey;
import azzy.fabric.forgottenfruits.util.recipe.RecipeTemplate;
import azzy.fabric.forgottenfruits.util.recipe.templates.FFFermentingOutput;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

import static azzy.fabric.forgottenfruits.ForgottenFruits.FFLog;

public class BarrelRecipes extends RecipeTemplate<FFFermentingOutput> {
    public BarrelRecipes() {
//        Queue<Iterator<String>> recipes = JanksonRecipeParser.getRecipeQueue(RecipeRegistryKey.BARREL);
//        Queue<Iterator<String>> recipes = null;
//        while (recipes.peek() != null) {
//            Iterator<String> recipeBits = recipes.poll();
//            try {
//                inject(recipeBits.next(), recipeBits.next(), recipeBits.next(), Integer.parseInt(recipeBits.next().replace("0x", ""), 16), Double.parseDouble(recipeBits.next()), Integer.parseInt(recipeBits.next()), Integer.parseInt(recipeBits.next()), Double.parseDouble(recipeBits.next()), Double.parseDouble(recipeBits.next()));
//            } catch (NoSuchElementException e) {
//                FFLog.error("A FERMENTING RECIPE IS INVALID");
//            }
//        }
    }

    public void inject(String id, String out, String fluidIn, int baseColor, double quality, int idealHeight, int idealLight, double idealTemp, double alcoholContent) {
        recipes.put(fluidIn, new FFFermentingOutput(RecipeRegistryKey.BARREL, id, out, fluidIn, baseColor, quality, idealHeight, idealLight, idealTemp, alcoholContent));
    }
}
