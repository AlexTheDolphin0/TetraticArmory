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
public class ModularHelmetItem extends ItemModularArmor {
    public static final String skullKey = "helmet/skull";
    public static final String liningKey = "helmet/lining";
    public static final String headpieceKey = "helmet/headpiece";
    public static final String faceKey = "helmet/face";
    public static final String gorgetKey = "helmet/gorget";
    public static final String identifier = "modular_helmet";
    @ObjectHolder(
            registryName = "item",
            value = "tetra:modular_helmet"
    )
    public static ItemModularArmor instance;
    public ModularHelmetItem() {
        super((new Item.Properties()).stacksTo(1).fireResistant(), ArmorItem.Type.HELMET);
        this.majorModuleKeys = new String[]{"helmet/skull"};
        this.minorModuleKeys = new String[]{"helmet/headpiece", "helmet/face", "helmet/gorget"};
        this.requiredModules = new String[]{"helmet/skull"};
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, "modular_helmet"));
        RemoveSchematic.registerRemoveSchematics(this, "modular_helmet");
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
    public static ItemStack createItemStack(String skull, String skullMaterial) {
        ItemStack itemStack = new ItemStack(instance);
        IModularItem.putModuleInSlot(itemStack, "helmet/skull", "helmet/" + skull, "helmet/" + skull + "_material", skull + "/" + skullMaterial);
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

}
