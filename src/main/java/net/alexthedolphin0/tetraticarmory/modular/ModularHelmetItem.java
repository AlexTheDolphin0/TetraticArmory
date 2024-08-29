package net.alexthedolphin0.tetraticarmory.modular;

import net.alexthedolphin0.tetraticarmory.client.ClientModEvents;
import net.alexthedolphin0.tetraticarmory.client.ModularArmorModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
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
public class ModularHelmetItem extends ItemModularArmor {
    public static final String topKey = "helmet/top";
    public static final String backKey = "helmet/back";
    public static final String sideLeftKey = "helmet/side_left";
    public static final String sideRightKey = "helmet/side_right";
    public static final String chinKey = "helmet/chin";
    public static final String faceKey = "helmet/face";
    public static final String neckKey = "helmet/neck";
    public static final String identifier = "modular_helmet";
    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(new int[]{-11, -4, 1, -4, -11, 23, 1, 23});
    private static final GuiModuleOffsets minorOffsets = new GuiModuleOffsets(new int[]{-24, 12, 15, 12, -34, 38});
    @ObjectHolder(
            registryName = "item",
            value = "tetra:modular_helmet"
    )
    public static ItemModularArmor instance;
    public ModularHelmetItem() {
        super((new Item.Properties()).stacksTo(1).fireResistant(), ArmorItem.Type.HELMET);
        this.majorModuleKeys = new String[]{topKey, backKey, sideLeftKey, sideRightKey};
        this.minorModuleKeys = new String[]{faceKey, chinKey, neckKey};
        this.requiredModules = new String[]{topKey};
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, identifier));
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> {
            this.synergies = DataManager.instance.getSynergyData("helmet/");
        });
    }
    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }
    public static ItemStack createItemStack(String top, String topMaterial, String sideLeft, String sideLeftMaterial, String sideRight, String sideRightMaterial, String back, String backMaterial) {
        ItemStack itemStack = new ItemStack(instance);
        IModularItem.putModuleInSlot(itemStack, topKey, "helmet/" + top, "helmet/" + top + "_material", top + "/" + topMaterial);
        IModularItem.putModuleInSlot(itemStack, sideLeftKey, "helmet/" + sideLeft, "helmet/" + sideLeft + "_material", sideLeft + "/" + sideLeftMaterial);
        IModularItem.putModuleInSlot(itemStack, sideRightKey, "helmet/" + sideRight, "helmet/" + sideRight + "_material", sideRight + "/" + sideRightMaterial);
        IModularItem.putModuleInSlot(itemStack, backKey, "helmet/" + back, "helmet/" + back + "_material", back + "/" + backMaterial);
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
