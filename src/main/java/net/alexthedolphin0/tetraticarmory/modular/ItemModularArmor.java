package net.alexthedolphin0.tetraticarmory.modular;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.event.ModularItemDamageEvent;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.module.data.EffectData;
import se.mickelus.tetra.module.data.ItemProperties;
import se.mickelus.tetra.module.data.SynergyData;
import se.mickelus.tetra.module.data.ToolData;
import se.mickelus.tetra.properties.AttributeHelper;
import se.mickelus.tetra.properties.IToolProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;


public abstract class ItemModularArmor extends ArmorItem implements Equipable, IClientItemExtensions, IModularItem, IToolProvider {

    public final ArmorItem.Type type;
    private static final Logger logger;
    private final Cache<String, Multimap<Attribute, AttributeModifier>> attributeCache;
    private final Cache<String, ToolData> toolCache;
    private final Cache<String, EffectData> effectCache;
    private final Cache<String, ItemProperties> propertyCache;
    protected int honeBase;
    protected int honeIntegrityMultiplier;
    protected boolean canHone;
    protected String[] majorModuleKeys;
    protected String[] minorModuleKeys;
    protected String[] requiredModules;
    protected int baseDurability;
    protected int baseIntegrity;
    protected SynergyData[] synergies;
    public ItemModularArmor(Properties properties, ArmorItem.Type type) {
        super(new ModularArmorMaterial(), type, properties);
        this.type = type;
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
        this.attributeCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
        this.toolCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
        this.effectCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
        this.propertyCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
        this.honeBase = 450;
        this.honeIntegrityMultiplier = 200;
        this.canHone = true;
        this.requiredModules = new String[0];
        this.baseDurability = 0;
        this.baseIntegrity = 0;
        this.synergies = new SynergyData[0];
        DataManager.instance.moduleData.onReload(this::clearCaches);
    }

    public void clearCaches() {
        logger.debug("Clearing item data caches for {}...", this.toString());
        this.attributeCache.invalidateAll();
        this.toolCache.invalidateAll();
        this.effectCache.invalidateAll();
        this.propertyCache.invalidateAll();
    }

    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        protected ItemStack execute(BlockSource p_40408_, ItemStack p_40409_) {
            return ArmorItem.dispenseArmor(p_40408_, p_40409_) ? p_40409_ : super.execute(p_40408_, p_40409_);
        }
    };

    public InteractionResultHolder<ItemStack> use(Level p_40395_, Player p_40396_, InteractionHand p_40397_) {
        return this.swapWithEquipmentSlot(this, p_40395_, p_40396_, p_40397_);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        if (slot == Mob.getEquipmentSlotForItem(itemStack) && !this.isBroken(itemStack)) {
            return this.getAttributeModifiersCached(itemStack);
        } else {
            return AttributeHelper.emptyMap;
        }
    }

    public boolean isDamageable(ItemStack stack) {
        return this.getMaxDamage(stack) > 0;
    }

    public EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }
    public String[] getMajorModuleKeys() {
        return this.majorModuleKeys;
    }

    public String[] getMinorModuleKeys() {
        return this.minorModuleKeys;
    }

    public String[] getRequiredModules() {
        return this.requiredModules;
    }

    public int getHoneBase() {
        return this.honeBase;
    }

    public int getHoneIntegrityMultiplier() {
        return this.honeIntegrityMultiplier;
    }

    public boolean canGainHoneProgress() {
        return this.canHone;
    }

    public Cache<String, Multimap<Attribute, AttributeModifier>> getAttributeModifierCache() {
        return this.attributeCache;
    }

    public Cache<String, EffectData> getEffectDataCache() {
        return this.effectCache;
    }

    public Cache<String, ItemProperties> getPropertyCache() {
        return this.propertyCache;
    }

    public Cache<String, ToolData> getToolDataCache() {
        return this.toolCache;
    }

    public Item getItem() {
        return this;
    }

    public boolean canProvideTools(ItemStack itemStack) {
        return !this.isBroken(itemStack);
    }

    public ToolData getToolData(ItemStack itemStack) {
        try {
            return (ToolData)this.getToolDataCache().get(this.getDataCacheKey(itemStack), () -> {
                return (ToolData) Optional.ofNullable(this.getToolDataRaw(itemStack)).orElseGet(ToolData::new);
            });
        } catch (ExecutionException var3) {
            var3.printStackTrace();
            return (ToolData)Optional.ofNullable(this.getToolDataRaw(itemStack)).orElseGet(ToolData::new);
        }
    }

    protected ToolData getToolDataRaw(ItemStack itemStack) {
        logger.debug("Gathering tool data for {} ({})", this.getName(itemStack).getString(), this.getDataCacheKey(itemStack));
        return (ToolData) Stream.concat(this.getAllModules(itemStack).stream().map((module) -> {
            return module.getToolData(itemStack);
        }), Arrays.stream(this.getSynergyData(itemStack)).map((synergy) -> {
            return synergy.tools;
        })).filter(Objects::nonNull).reduce((ToolData) null, ToolData::merge);
    }

    public Component getName(ItemStack stack) {
        return Component.literal(this.getItemName(stack));
    }

    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.addAll(this.getTooltip(stack, world, flag));
    }

    public int getMaxDamage(ItemStack itemStack) {
        return (Integer)Optional.of(this.getPropertiesCached(itemStack)).map((properties) -> {
            return (float)(properties.durability + this.baseDurability) * properties.durabilityMultiplier;
        }).map(Math::round).orElse(0);
    }

    public void setDamage(ItemStack itemStack, int damage) {
        super.setDamage(itemStack, Math.min(itemStack.getMaxDamage() - 1, damage));
    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        ModularItemDamageEvent event = new ModularItemDamageEvent(entity, stack, amount);
        MinecraftForge.EVENT_BUS.post(event);
        amount = event.getAmount();
        return Math.min(stack.getMaxDamage() - stack.getDamageValue() - 1, amount);
    }

    public int getBarWidth(ItemStack itemStack) {
        return Math.round(13.0F - (float)itemStack.getDamageValue() * 13.0F / (float)this.getMaxDamage(itemStack));
    }

    public int getBarColor(ItemStack itemStack) {
        float maxDamage = (float)this.getMaxDamage(itemStack);
        float f = Math.max(0.0F, (maxDamage - (float)itemStack.getDamageValue()) / maxDamage);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    public void onCraftedBy(ItemStack itemStack, Level world, Player player) {
        IModularItem.updateIdentifier(itemStack);
    }

    public boolean isFoil(@Nonnull ItemStack itemStack) {
        return (Boolean) ConfigHandler.enableGlint.get() ? super.isFoil(itemStack) : false;
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    public SynergyData[] getAllSynergyData(ItemStack itemStack) {
        return this.synergies;
    }

    public boolean isBookEnchantable(ItemStack itemStack, ItemStack bookStack) {
        return false;
    }

    public boolean canApplyAtEnchantingTable(ItemStack itemStack, Enchantment enchantment) {
        return this.acceptsEnchantment(itemStack, enchantment, true);
    }

    public int getEnchantmentValue(ItemStack itemStack) {
        return this.getEnchantability(itemStack);
    }

    static {
        logger = LogManager.getLogger();
    }
}
