package net.alexthedolphin0.tetraticarmory.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.alexthedolphin0.tetraticarmory.modular.ItemModularArmor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
    @Shadow @Final private A innerModel;
    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> p_117346_) {
        super(p_117346_);
    }
    HumanoidArmorLayer thisnt = ((HumanoidArmorLayer)(Object)this);
    @Inject(method = "renderArmorPiece", at = @At("HEAD"))
    private void iCanLiterallyNameThisMethodAnythingLMAO(PoseStack p_117119_, MultiBufferSource p_117120_, T p_117121_, EquipmentSlot p_117122_, int p_117123_, A p_117124_, CallbackInfo ci) {
        if (p_117121_.getItemBySlot(p_117122_).getItem() instanceof ItemModularArmor armoritem) {
            ItemStack itemstack = p_117121_.getItemBySlot(p_117122_);
            if (armoritem.getEquipmentSlot() == p_117122_) {
                thisnt.getParentModel().copyPropertiesTo(p_117124_);
                thisnt.setPartVisibility(p_117124_, p_117122_);
                net.minecraft.client.model.Model model = net.minecraftforge.client.ForgeHooksClient.getArmorModel(p_117121_, itemstack, p_117122_, p_117124_);
                boolean flag = thisnt.usesInnerModel(p_117122_);
                renderModel(p_117119_, p_117120_, p_117123_, itemstack, model, flag, 1.0F, 1.0F, 1.0F);
                }
                /*if (itemstack.hasFoil()) {
                    thisnt.renderGlint(p_117119_, p_117120_, p_117123_, model);
                }*/
            }
        }
    public void renderModel(PoseStack poseStack, MultiBufferSource buffer, int i, ItemStack stack, Model model, boolean bool, float f1, float f2, float f3) {
        renderModel(poseStack, buffer, i, model, bool, f1, f2, f3, new ResourceLocation("tetratic_armory", ""));
    }
    public void renderModel(PoseStack p_289664_, MultiBufferSource p_289689_, int p_289681_, Model p_289658_, boolean p_289668_, float p_289678_, float p_289674_, float p_289693_, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = p_289689_.getBuffer(RenderType.armorCutoutNoCull(armorResource));
        p_289658_.renderToBuffer(p_289664_, vertexconsumer, p_289681_, OverlayTexture.NO_OVERLAY, p_289678_, p_289674_, p_289693_, 1.0F);
    }
    }
