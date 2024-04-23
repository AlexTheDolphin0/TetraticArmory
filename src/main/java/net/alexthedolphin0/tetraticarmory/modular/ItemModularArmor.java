package net.alexthedolphin0.tetraticarmory.modular;

import com.google.common.collect.Multimap;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.properties.AttributeHelper;


public abstract class ItemModularArmor extends ModularItem implements Equipable, IClientItemExtensions {

    public ItemModularArmor(Properties properties) {
        super(properties);
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }

    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        protected ItemStack execute(BlockSource p_40408_, ItemStack p_40409_) {
            return ArmorItem.dispenseArmor(p_40408_, p_40409_) ? p_40409_ : super.execute(p_40408_, p_40409_);
        }
    };

    public InteractionResultHolder<ItemStack> use(Level p_40395_, Player p_40396_, InteractionHand p_40397_) {
        return this.swapWithEquipmentSlot(this, p_40395_, p_40396_, p_40397_);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        if (slot == Mob.getEquipmentSlotForItem(itemStack) && !this.isBroken(itemStack)) {
            return this.getAttributeModifiersCached(itemStack);
        } else {
            return AttributeHelper.emptyMap;
        }
    }

    public boolean isDamageable(ItemStack stack) {
        return this.getMaxDamage(stack) > 0;
    }

}
