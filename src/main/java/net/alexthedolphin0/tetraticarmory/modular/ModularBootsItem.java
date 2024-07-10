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
public class ModularBootsItem extends ItemModularArmor {
    public static final String bootLeftKey = "boots/boot_left";
    public static final String bootRightKey = "boots/boot_right";
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
        this.majorModuleKeys = new String[]{"boots/boot_left", "boots/boot_right", "boots/lining"};
        this.minorModuleKeys = new String[]{"boots/sole_left", "boots/sole_right"};
        this.requiredModules = new String[]{"boots/boot_left", "boots/boot_right"};
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, "modular_boots"));
        RemoveSchematic.registerRemoveSchematics(this, "modular_boots");
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
    public static ItemStack createItemStack(String bootLeft, String bootLeftMaterial, String bootRight, String bootRightMaterial, String lining, String liningMaterial) {
        ItemStack itemStack = new ItemStack(instance);
        IModularItem.putModuleInSlot(itemStack, "boots/boot_left", "boots/" + bootLeft, "boots/" + bootLeft + "_material", bootLeft + "/" + bootLeftMaterial);
        IModularItem.putModuleInSlot(itemStack, "boots/boot_right", "boots/" + bootRight, "boots/" + bootRight + "_material", bootRight + "/" + bootRightMaterial);
        IModularItem.putModuleInSlot(itemStack, "boots/lining", "boots/" + lining, "boots/" + lining + "_material", lining + "/" + liningMaterial);
        IModularItem.updateIdentifier(itemStack);
        return itemStack;
    }
}
