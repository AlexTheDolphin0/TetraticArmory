package net.alexthedolphin0.tetraticarmory.modular;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class ModularArmorMaterial implements ArmorMaterial {
    @Override
    public int getDurabilityForType(ArmorItem.Type p_266807_) {
        return 0;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type p_267168_) {
        return 0;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public SoundEvent getEquipSound() {
        return null;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return null;
    }

    @Override
    public String getName() {
        return "modular_armor_material";
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}
