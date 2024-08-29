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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.NotNull;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RemoveSchematic;
import se.mickelus.tetra.module.schematic.RepairSchematic;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class ModularLeggingsItem extends ItemModularArmor {
    public static final String waistKey = "leggings/waist";
    public static final String upperLegLeftKey = "leggings/upper_leg_left";
    public static final String upperLegRightKey = "leggings/upper_leg_right";
    public static final String lowerLegLeftKey = "leggings/lower_leg_left";
    public static final String lowerLegRightKey = "leggings/lower_leg_right";
    public static final String kneeLeftKey = "leggings/knee_left";
    public static final String kneeRightKey = "leggings/knee_right";
    public static final String identifier = "modular_leggings";
    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(new int[]{-11, -4, 1, -4, -11, 23, 1, 23});
    private static final GuiModuleOffsets minorOffsets = new GuiModuleOffsets(new int[]{-24, 12, 15, 12, -34, 38});
    @ObjectHolder(
            registryName = "item",
            value = "tetra:modular_leggings"
    )
    public static ItemModularArmor instance;
    public ModularLeggingsItem() {
        super((new Item.Properties()).stacksTo(1).fireResistant(), ArmorItem.Type.LEGGINGS);
        this.majorModuleKeys = new String[]{upperLegLeftKey, upperLegRightKey, lowerLegLeftKey, lowerLegRightKey};
        this.minorModuleKeys = new String[]{kneeLeftKey, kneeRightKey, waistKey};
        this.requiredModules = new String[]{waistKey};
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, identifier));
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
    public static ItemStack createItemStack(String waist, String waistMaterial, String upperLegLeft, String upperLegLeftMaterial, String upperLegRight, String upperLegRightMaterial, String lowerLegLeft, String lowerLegLeftMaterial, String lowerLegRight, String lowerLegRightMaterial) {
        ItemStack itemStack = new ItemStack(instance);
        IModularItem.putModuleInSlot(itemStack, waistKey, "leggings/" + waist, "leggings/" + waist + "_material", waist + "/" + waistMaterial);
        IModularItem.putModuleInSlot(itemStack, upperLegLeftKey, "leggings/" + upperLegLeft, "leggings/" + upperLegLeft + "_material", upperLegLeft + "/" + upperLegLeftMaterial);
        IModularItem.putModuleInSlot(itemStack, upperLegRightKey, "leggings/" + upperLegRight, "leggings/" + upperLegRight + "_material", upperLegLeft + "/" + upperLegRightMaterial);
        IModularItem.putModuleInSlot(itemStack, upperLegLeftKey, "leggings/" + lowerLegLeft, "leggings/" + lowerLegLeft + "_material", lowerLegLeft + "/" + lowerLegLeftMaterial);
        IModularItem.putModuleInSlot(itemStack, upperLegRightKey, "leggings/" + lowerLegRight, "leggings/" + lowerLegRight + "_material", lowerLegRight + "/" + lowerLegRightMaterial);
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
    @OnlyIn(Dist.CLIENT)
    public GuiModuleOffsets getMajorGuiOffsets(ItemStack itemStack) {
        return majorOffsets;
    }

    @OnlyIn(Dist.CLIENT)
    public GuiModuleOffsets getMinorGuiOffsets(ItemStack itemStack) {
        return minorOffsets;
    }
}
