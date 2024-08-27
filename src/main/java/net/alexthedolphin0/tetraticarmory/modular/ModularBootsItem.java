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
public class ModularBootsItem extends ItemModularArmor {
    public static final String footLeftKey = "boots/foot_left";
    public static final String footRightKey = "boots/foot_right";
    public static final String liningKey = "boots/lining";
    public static final String soleLeftKey = "boots/sole_left";
    public static final String soleRightKey = "boots/sole_right";
    public static final String identifier = "modular_boots";
    @ObjectHolder(
            registryName = "item",
            value = "tetra:modular_boots"
    )
    public static ItemModularArmor instance;
    public ModularBootsItem() {
        super((new Item.Properties()).stacksTo(1).fireResistant(), ArmorItem.Type.BOOTS);
        this.majorModuleKeys = new String[]{"boots/foot_left", "boots/foot_right"};
        this.minorModuleKeys = new String[]{"boots/sole_left", "boots/sole_right"};
        this.requiredModules = new String[]{"boots/foot_left", "boots/foot_right"};
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, "modular_boots"));
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.FEET;
    }
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> {
            this.synergies = DataManager.instance.getSynergyData("boots/");
        });
    }
    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }
    public static ItemStack createItemStack(String footLeft, String footLeftMaterial, String footRight, String footRightMaterial) {
        ItemStack itemStack = new ItemStack(instance);
        IModularItem.putModuleInSlot(itemStack, "boots/foot_left", "boots/" + footLeft, "boots/" + footLeft + "_material", footLeft + "/" + footLeftMaterial);
        IModularItem.putModuleInSlot(itemStack, "boots/foot_right", "boots/" + footRight, "boots/" + footRight + "_material", footRight + "/" + footRightMaterial);
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
