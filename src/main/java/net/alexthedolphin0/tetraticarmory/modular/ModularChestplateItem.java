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
public class ModularChestplateItem extends ItemModularArmor {
    public static final String breastplateKey = "chestplate/breastplate";
    public static final String plackartKey = "chestplate/plackart";
    public static final String liningKey = "chestplate/lining";
    public static final String armLeftKey = "chestplate/arm_left";
    public static final String armRightKey = "chestplate/arm_right";
    public static final String backKey = "chestplate/back";
    public static final String identifier = "modular_chestplate";
    @ObjectHolder(
            registryName = "item",
            value = "tetra:modular_chestplate"
    )
    public static ItemModularArmor instance;
    public ModularChestplateItem() {
        super((new Item.Properties()).stacksTo(1).fireResistant(), ArmorItem.Type.CHESTPLATE);
        this.majorModuleKeys = new String[]{"chestplate/breastplate", "chestplate/plackart"};
        this.minorModuleKeys = new String[]{"chestplate/arm_left", "chestplate/arm_right","chestplate/back"};
        this.requiredModules = new String[]{"chestplate/breastplate"};
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, "modular_chestplate"));
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
    public static ItemStack createItemStack(String breastplate, String breastplateMaterial, String plackart, String plackartMaterial) {
        ItemStack itemStack = new ItemStack(instance);
        IModularItem.putModuleInSlot(itemStack, "chestplate/breastplate", "chestplate/" + breastplate, "chestplate/" + breastplate + "_material", breastplate + "chestplate/" + breastplateMaterial);
        IModularItem.putModuleInSlot(itemStack, "chestplate/plackart", "chestplate/" + plackart, "chestplate/" + plackart + "_material", plackart + "chestplate/" + plackartMaterial);
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
