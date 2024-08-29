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
public class ModularChestplateItem extends ItemModularArmor {
    public static final String chestKey = "chestplate/chest";
    public static final String stomachKey = "chestplate/stomach";
    public static final String armLeftKey = "chestplate/arm_left";
    public static final String armRightKey = "chestplate/arm_right";
    public static final String handLeftKey = "chestplate/hand_left";
    public static final String handRightKey = "chestplate/hand_right";
    public static final String backKey = "chestplate/back";
    public static final String identifier = "modular_chestplate";
    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(new int[]{-11, -4, 1, -4, -11, 23, 1, 23});
    private static final GuiModuleOffsets minorOffsets = new GuiModuleOffsets(new int[]{-24, 12, 15, 12, -34, 38});
    @ObjectHolder(
            registryName = "item",
            value = "tetra:modular_chestplate"
    )
    public static ItemModularArmor instance;
    public ModularChestplateItem() {
        super((new Item.Properties()).stacksTo(1).fireResistant(), ArmorItem.Type.CHESTPLATE);
        this.majorModuleKeys = new String[]{chestKey, stomachKey, armLeftKey, armRightKey};
        this.minorModuleKeys = new String[]{handLeftKey, handRightKey, backKey};
        this.requiredModules = new String[]{chestKey};
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, identifier));
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> {
            this.synergies = DataManager.instance.getSynergyData("chestplate/");
        });
    }
    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }
    public static ItemStack createItemStack(String chest, String chestMaterial, String stomach, String stomachMaterial) {
        ItemStack itemStack = new ItemStack(instance);
        IModularItem.putModuleInSlot(itemStack, chestKey, "chestplate/" + chest, "chestplate/" + chest + "_material", chest + "chestplate/" + chestMaterial);
        IModularItem.putModuleInSlot(itemStack, stomachKey, "chestplate/" + stomach, "chestplate/" + stomach + "_material", stomach + "chestplate/" + stomachMaterial);
        IModularItem.updateIdentifier(itemStack);
        return itemStack;
    }
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                ModularArmorModel model = new ModularArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ClientModEvents.ARMOR_OUTER));
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
