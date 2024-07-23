package net.alexthedolphin0.tetraticarmory.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.DataResult;
import net.alexthedolphin0.tetraticarmory.modular.ItemModularArmor;
import net.alexthedolphin0.tetraticarmory.modular.ModularHelmetItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import org.jline.utils.Colors;
import org.openjdk.nashorn.internal.parser.JSONParser;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.tetra.client.model.ColorQuadTransformer;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.data.ModuleModel;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HexFormat;

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
                this.getParentModel().copyPropertiesTo(p_117124_);
                thisnt.setPartVisibility(p_117124_, p_117122_);
                net.minecraft.client.model.Model model = net.minecraftforge.client.ForgeHooksClient.getArmorModel(p_117121_, itemstack, p_117122_, p_117124_);
                boolean flag = thisnt.usesInnerModel(p_117122_);
                renderModel(p_117119_, p_117120_, p_117123_, itemstack, model, flag);
                /*if (itemstack.hasFoil()) {
                    thisnt.renderGlint(p_117119_, p_117120_, p_117123_, (A)model);
                }*/
            }
        }
    }
    public void renderModel(PoseStack poseStack, MultiBufferSource buffer, int i, ItemStack stack, Model model, boolean bool) {
        ResourceLocation resourceLocation;
        ImmutableList<ModuleModel> models = ((ItemModularArmor)stack.getItem()).getModels(stack, null);
        ModuleModel[] modelArray = models.toArray(new ModuleModel[0]);
        Arrays.sort(modelArray, Comparator.comparing((ModuleModel o) -> o.renderLayer));
        for (ModuleModel moduleModel : modelArray) {
            float[] tintRgb = rgb(moduleModel.tint);
            renderModel(poseStack, buffer, i, model, bool, tintRgb[0], tintRgb[1], tintRgb[2], 1.0F, moduleModel.location.withSuffix(".png").withPrefix("textures/model/"));
        }
    }
    public void renderModel(PoseStack p_289664_, MultiBufferSource p_289689_, int p_289681_, Model p_289658_, boolean p_289668_, float red, float green, float blue, float alpha, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = p_289689_.getBuffer(RenderType.armorCutoutNoCull(armorResource));
        p_289658_.renderToBuffer(p_289664_, vertexconsumer, p_289681_, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }
    private static float[] rgb(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >>  8) & 0xFF;
        int b = (color) & 0xFF;
        return new float[] { r/255F, g/255F, b/255F };
    }
}
