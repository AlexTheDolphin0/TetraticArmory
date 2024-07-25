package net.alexthedolphin0.tetraticarmory.modular;

import net.alexthedolphin0.tetraticarmory.client.ClientModEvents;
import net.alexthedolphin0.tetraticarmory.client.ModularArmorModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.NotNull;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RemoveSchematic;
import se.mickelus.tetra.module.schematic.RepairSchematic;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class ModularLeggingsItem extends ItemModularArmor {
    public static final String tassetKey = "leggings/tasset";
    public static final String legLeftKey = "leggings/leg_left";
    public static final String legRightKey = "leggings/leg_right";
    public static final String liningKey = "leggings/lining";
    public static final String kneeLeftKey = "leggings/knee_left";
    public static final String kneeRightKey = "leggings/knee_right";
    public static final String identifier = "modular_leggings";
    @ObjectHolder(
            registryName = "item",
            value = "tetra:modular_leggings"
    )
    public static ItemModularArmor instance;
    public ModularLeggingsItem() {
        super((new Item.Properties()).stacksTo(1).fireResistant(), ArmorItem.Type.LEGGINGS);
        this.majorModuleKeys = new String[]{"leggings/leg_left", "leggings/leg_right"};
        this.minorModuleKeys = new String[]{"leggings/knee_left", "leggings/knee_right", "leggings/tasset"};
        this.requiredModules = new String[]{"leggings/tasset"};
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, "modular_leggings"));
        RemoveSchematic.registerRemoveSchematics(this, "modular_leggings");
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.LEGS;
    }
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> {
            this.synergies = DataManager.instance.getSynergyData("leggings/");
        });
    }
    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }
    public static ItemStack createItemStack(String tasset, String tassetMaterial, String legLeft, String legLeftMaterial, String legRight, String legRightMaterial) {
        ItemStack itemStack = new ItemStack(instance);
        IModularItem.putModuleInSlot(itemStack, "leggings/tasset", "leggings/" + tasset, "leggings/" + tasset + "_material", tasset + "/" + tassetMaterial);
        IModularItem.putModuleInSlot(itemStack, "leggings/leg_left", "leggings/" + legLeft, "leggings/" + legLeft + "_material", legLeft + "/" + legLeftMaterial);
        IModularItem.putModuleInSlot(itemStack, "leggings/leg_right", "leggings/" + legRight, "leggings/" + legRight + "_material", legRight + "/" + legRightMaterial);
        IModularItem.updateIdentifier(itemStack);
        return itemStack;
    }
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                ModularArmorModel model = new ModularArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ClientModEvents.ARMOR_INNER));
                original.copyPropertiesTo(model);
                model.setOverlayProperties();
                return model;
            }
        });
    }
}
