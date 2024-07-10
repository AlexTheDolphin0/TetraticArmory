package net.alexthedolphin0.tetraticarmory.modular;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RemoveSchematic;
import se.mickelus.tetra.module.schematic.RepairSchematic;

import javax.annotation.ParametersAreNonnullByDefault;

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
        this.majorModuleKeys = new String[]{"leggings/tasset", "leggings/leg_left", "leggings/leg_right", "leggings/lining"};
        this.minorModuleKeys = new String[]{"leggings/knee_left", "leggings/knee_right"};
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
    public static ItemStack createItemStack(String tasset, String tassetMaterial, String cuiseLeft, String cuiseLeftMaterial, String cuiseRight, String cuiseRightMaterial, String lining, String liningMaterial) {
        ItemStack itemStack = new ItemStack(instance);
        IModularItem.putModuleInSlot(itemStack, "leggings/tasset", "leggings/" + tasset, "leggings/" + tasset + "_material", tasset + "/" + tassetMaterial);
        IModularItem.putModuleInSlot(itemStack, "leggings/cuise_left", "leggings/" + cuiseLeft, "leggings/" + cuiseLeft + "_material", cuiseLeft + "/" + cuiseLeftMaterial);
        IModularItem.putModuleInSlot(itemStack, "leggings/cuise_right", "leggings/" + cuiseRight, "leggings/" + cuiseRight + "_material", cuiseRight + "/" + cuiseRightMaterial);
        IModularItem.putModuleInSlot(itemStack, "leggings/lining", "leggings/" + lining, "leggings/" + lining + "_material", lining + "/" + liningMaterial);
        IModularItem.updateIdentifier(itemStack);
        return itemStack;
    }
}
