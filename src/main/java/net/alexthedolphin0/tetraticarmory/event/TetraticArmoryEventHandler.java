package net.alexthedolphin0.tetraticarmory.event;

import net.alexthedolphin0.tetraticarmory.modular.ItemModularArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Mod.EventBusSubscriber(modid = "tetratic_armory", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TetraticArmoryEventHandler {
    @SubscribeEvent
    public static void onLivingEntityHit(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player) {
            Iterable<ItemStack> a = Objects.requireNonNull(event.getEntity()).getArmorSlots();
            a.forEach(stack -> {
                if (stack.getItem() instanceof ItemModularArmor) {
                    ((ItemModularArmor)(stack.getItem())).tickHoningProgression(event.getEntity(), stack, 1);
                }
            });
        }
    }
}
