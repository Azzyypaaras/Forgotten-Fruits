package azzy.fabric.forgottenfruits.registry;

import azzy.fabric.forgottenfruits.item.AmalgamItems;
import azzy.fabric.forgottenfruits.item.AttunedAttunedStone;
import azzy.fabric.forgottenfruits.item.FoodItems;
import azzy.fabric.forgottenfruits.item.LiquorBottle;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

import static azzy.fabric.forgottenfruits.ForgottenFruits.*;

public class ItemRegistry extends Item {

    public static Item CLOUDBERRY_FRUIT, CLOUDBERRY_SEEDS, CLOUDBERRY_DRINK;
    public static Item IGNOBLE_SILK;
    public static Item BASKET_ITEM;
    public static Item CINDERMOTE_FRUIT, CINDERMOTE_SEEDS, CINDERMOTE_DRINK;
    public static Item APPLE_ALLOY;
    public static Item MULCH;
    public static Item SAMMICH;
    public static Item ATTUNED, ATTUNED_EFFULGENT, ATTUNED_CHAOTIC;
    public static Item MUTANDIS;
    public static final List<AmalgamItems.ConstructAmalgam> AMALGAM_REGISTRY = new ArrayList<>();
    public static Item CLOUDBERRY_BUCKET;
    public static Item CINDERMOTE_BUCKET;

    private ItemRegistry(Item.Settings settings) {
        super(settings);
    }


    public static void init() {

        //What, why
        SAMMICH = register(new Identifier(MOD_ID, "sammich"), new Item(defaultSettings().food(FoodItems.FoodBackend(8, 1f, false))));

        //Misc
        MULCH = register(new Identifier(MOD_ID, "mulch"), new Item(new Item.Settings().group(PLANT_MATERIALS)));
        ATTUNED = register(new Identifier(MOD_ID, "attuned_stone"), new Item(new Item.Settings().group(PLANT_MATERIALS)));
        ATTUNED_EFFULGENT = register(new Identifier(MOD_ID, "effulgent_stone"), new AttunedAttunedStone(new Item.Settings().group(PLANT_MATERIALS)));
        ATTUNED_CHAOTIC = register(new Identifier(MOD_ID, "chaotic_stone"), new AttunedAttunedStone(new Item.Settings().group(PLANT_MATERIALS)));
        MUTANDIS = register(new Identifier(MOD_ID, "mutandis"), new Item(defaultSettings()));

        //Drinks
        CLOUDBERRY_DRINK = register(new Identifier(MOD_ID, "drinkcloudberry"), new LiquorBottle(drinkSettings()));
        CINDERMOTE_DRINK = register(new Identifier(MOD_ID, "drinkcindermote"), new LiquorBottle(drinkSettings()));

        //Threads
        IGNOBLE_SILK = register(new Identifier(MOD_ID, "thread_basic"), new Item(new Item.Settings().group(PLANT_MATERIALS)));

        //Storage
        BASKET_ITEM = register(new Identifier(MOD_ID, "basket_block"), new AliasedBlockItem(BlockRegistry.BASKET, new Item.Settings().group(BLOCK_ENTITIES)));

        //Alloys
        APPLE_ALLOY = register(new Identifier(MOD_ID, "apple_alloy"), new Item(new Item.Settings().group(PLANT_MATERIALS)));

        //Berries
        CLOUDBERRY_FRUIT = register(new Identifier(MOD_ID, "cloudberry_fruit"), new Item(defaultSettings().food(FoodItems.FoodBackendSpecial(3, 0.5f, true, false, StatusEffects.LEVITATION, 0.1f, 200))));
        CINDERMOTE_FRUIT = register(new Identifier(MOD_ID, "cinde_rmote_fruit"), new Item(defaultSettings().food(FoodItems.FoodBackend(6, 0.3f, false)).fireproof()));

        //Seeds
        CLOUDBERRY_SEEDS = register(new Identifier(MOD_ID, "cloudberry_seeds"), new AliasedBlockItem(CropRegistry.CLOUDBERRY_CROP, defaultSettings().food(FoodItems.FoodBackendSpecial(-3, -2f, false, false, StatusEffects.LEVITATION, 1f, 600))));
        CINDERMOTE_SEEDS = register(new Identifier(MOD_ID, "cindermote_seeds"), new AliasedBlockItem(CropRegistry.CINDERMOTE_CROP, defaultSettings().fireproof()));

        CLOUDBERRY_BUCKET = registerBucket("cloudberry_bucket", FluidRegistry.CLOUDBERRY);
        CINDERMOTE_BUCKET = registerBucket("cinder_juice_bucket", FluidRegistry.CINDERMOTE);

        //Jellies
        for (int i = 0; i < 2; i++) {
            AMALGAM_REGISTRY.add(i, new AmalgamItems.ConstructAmalgam(Rarity.RARE, i));
        }

        for (int j = 0; j < 2; j++) {
            register(AMALGAM_REGISTRY.get(j).getKey(), AMALGAM_REGISTRY.get(j).getJelly());
        }
    }

    private static Item.Settings defaultSettings() {
        return new Item.Settings().group(PLANT_STUFF);
    }

    private static Item.Settings materialSettings() {
        return new Item.Settings().group(PLANT_MATERIALS);
    }

    private static Item.Settings drinkSettings() {
        return new Item.Settings().group(PLANT_STUFF).food(FoodItems.FoodBackend(0, 0, false)).maxCount(16);
    }

    private static Item register(Identifier name, Item item) {
        Registry.register(Registry.ITEM, name, item);
        return item;
    }

    public static Item registerBucket(String name, FlowableFluid item) {
        return Registry.register(Registry.ITEM, new Identifier(MOD_ID, name), new BucketItem(item, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(ItemGroup.MISC)));
    }
}


