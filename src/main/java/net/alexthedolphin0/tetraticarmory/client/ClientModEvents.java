package net.alexthedolphin0.tetraticarmory.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = "tetratic_armory", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    public static ModelLayerLocation ARMOR_OUTER = new ModelLayerLocation(new ResourceLocation("tetratic_armory", "modular_armor_model"),"outer");
    public static ModelLayerLocation ARMOR_INNER = new ModelLayerLocation(new ResourceLocation("tetratic_armory", "modular_armor_model"),"inner");
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ARMOR_OUTER, new Supplier<LayerDefinition>() {
            @Override
            public LayerDefinition get() {
                return LayerDefinition.create(ModularArmorModel.createMesh(new CubeDeformation(1.0F), 0.0F), 64, 64);
            }
        });
        event.registerLayerDefinition(ARMOR_INNER, new Supplier<LayerDefinition>() {
            @Override
            public LayerDefinition get() {
                return LayerDefinition.create(ModularArmorModel.createMesh(new CubeDeformation(0.5F), 0.0F), 64, 64);
            }
        });
    }
}
