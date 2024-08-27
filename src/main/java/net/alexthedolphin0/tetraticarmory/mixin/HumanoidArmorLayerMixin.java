package net.alexthedolphin0.tetraticarmory.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.*;
import net.alexthedolphin0.tetraticarmory.client.ModularArmorModel;
import net.alexthedolphin0.tetraticarmory.modular.ItemModularArmor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.tetra.module.data.ModuleModel;

import java.util.Arrays;
import java.util.Comparator;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    @Shadow public abstract void renderGlint(PoseStack p_289673_, MultiBufferSource p_289654_, int p_289649_, A p_289659_);

    @Shadow public abstract void setPartVisibility(A p_117126_, EquipmentSlot p_117127_);

    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
    }
    HumanoidArmorLayer thisnt = ((HumanoidArmorLayer)(Object)this);
    @Inject(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/armortrim/ArmorTrim;getTrim(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/item/ItemStack;)Ljava/util/Optional;"))
    private void iCanLiterallyNameThisMethodAnythingLMAO(PoseStack p_117119_, MultiBufferSource p_117120_, T p_117121_, EquipmentSlot p_117122_, int p_117123_, A p_117124_, CallbackInfo ci, @Local ItemStack itemStack, @Local Model model, @Local boolean flag) {
        if (itemStack.getItem() instanceof ItemModularArmor) {
            ResourceLocation resourceLocation;
            ImmutableList<ModuleModel> models = ((ItemModularArmor) itemStack.getItem()).getModels(itemStack, null);
            ModuleModel[] modelArray = models.toArray(new ModuleModel[0]);
            Arrays.sort(modelArray, Comparator.comparing((ModuleModel o) -> o.renderLayer));
            for (ModuleModel moduleModel : modelArray) {
                float[] tintRgb = tetraticArmory$rgb(moduleModel.tint);
                renderModel(p_117119_, p_117120_, p_117123_, model, flag, tintRgb[0], tintRgb[1], tintRgb[2], 1.0F, moduleModel.location.withSuffix(".png").withPrefix("textures/models/armor/"));
            }
        }
    }
    public void renderModel(PoseStack p_289664_, MultiBufferSource p_289689_, int p_289681_, Model p_289658_, boolean p_289668_, float red, float green, float blue, float alpha, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = p_289689_.getBuffer(RenderType.armorCutoutNoCull(armorResource));
        p_289658_.renderToBuffer(p_289664_, vertexconsumer, p_289681_, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }
    @Unique
    private static float[] tetraticArmory$rgb(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >>  8) & 0xFF;
        int b = (color) & 0xFF;
        return new float[] { r/255F, g/255F, b/255F };
    }
}
