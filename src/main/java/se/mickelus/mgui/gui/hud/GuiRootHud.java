package se.mickelus.mgui.gui.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import se.mickelus.mgui.gui.GuiElement;
import se.mickelus.mgui.gui.animation.KeyframeAnimation;

public class GuiRootHud extends GuiElement {

    public GuiRootHud() {
        super(0, 0, 0, 0);
    }

    public void draw(PlayerEntity player, BlockRayTraceResult rayTrace, VoxelShape shape, float partialTicks) {
        BlockPos playerPos = player.getPosition();
        double offsetX = player.lastTickPosX + (playerPos.getX() - player.lastTickPosX) * partialTicks;
        double offsetY = player.lastTickPosY + (playerPos.getY() - player.lastTickPosY) * partialTicks + player.getEyeHeight();
        double offsetZ = player.lastTickPosZ + (playerPos.getZ() - player.lastTickPosZ) * partialTicks;

        BlockPos blockPos = rayTrace.getPos();

        Vec3d hitVec = rayTrace.getHitVec();

        draw(blockPos.getX() - offsetX, blockPos.getY() - offsetY, blockPos.getZ() - offsetZ,
                hitVec.x - blockPos.getX(), hitVec.y - blockPos.getY(), hitVec.z - blockPos.getZ(),
                rayTrace.getFace(), shape.getBoundingBox());
    }

    public void draw(double x, double y, double z, double hitX, double hitY, double hitZ, Direction facing, AxisAlignedBB boundingBox) {
        activeAnimations.removeIf(keyframeAnimation -> !keyframeAnimation.isActive());
        activeAnimations.forEach(KeyframeAnimation::preDraw);

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.depthMask(false);

        RenderSystem.pushMatrix();
        RenderSystem.translated(x, y, z);

        int mouseX = 0;
        int mouseY = 0;

        // magic number is the same used to offset the outline, stops textures from flickering
        Vec3d magicOffset = new Vec3d(facing.getDirectionVec()).scale(0.0020000000949949026D);
        RenderSystem.translated(magicOffset.getX(), magicOffset.getY(), magicOffset.getZ());

        switch (facing) {
            case NORTH:
                mouseX = (int) ( ( boundingBox.maxX - hitX ) * 32 );
                mouseY = (int) ( ( boundingBox.maxY - hitY ) * 32 );

                width = (int) ((boundingBox.maxX - boundingBox.minX) * 32);
                height = (int) ((boundingBox.maxY - boundingBox.minY) * 32);

                RenderSystem.translated(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
                RenderSystem.rotatef(180, 0, 1, 0);
                break;
            case SOUTH:
                mouseX = (int) ( ( hitX - boundingBox.minX ) * 32 );
                mouseY = (int) ( ( boundingBox.maxY - hitY ) * 32 );

                width = (int) ((boundingBox.maxX - boundingBox.minX) * 32);
                height = (int) ((boundingBox.maxY - boundingBox.minY) * 32);

                RenderSystem.translated(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
                break;
            case EAST:
                mouseX = (int) ( ( boundingBox.maxZ - hitZ ) * 32 );
                mouseY = (int) ( ( boundingBox.maxY - hitY ) * 32 );

                width = (int) ((boundingBox.maxZ - boundingBox.minZ) * 32);
                height = (int) ((boundingBox.maxY - boundingBox.minY) * 32);

                RenderSystem.translated(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
                RenderSystem.rotatef(90, 0, 1, 0);
                break;
            case WEST:
                mouseX = (int) ( ( hitZ - boundingBox.minZ ) * 32 );
                mouseY = (int) ( ( boundingBox.maxY - hitY ) * 32 );

                width = (int) ((boundingBox.maxZ - boundingBox.minZ) * 32);
                height = (int) ((boundingBox.maxY - boundingBox.minY) * 32);

                RenderSystem.translated(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
                RenderSystem.rotatef(-90, 0, 1, 0);
                break;
            case UP:
                mouseX = (int) ( ( boundingBox.maxX - hitX ) * 32 );
                mouseY = (int) ( ( boundingBox.maxZ - hitZ ) * 32 );

                width = (int) ((boundingBox.maxX - boundingBox.minX) * 32);
                height = (int) ((boundingBox.maxZ - boundingBox.minZ) * 32);

                RenderSystem.translated(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
                RenderSystem.rotatef(90, 1, 0, 0);
                RenderSystem.scalef(-1, 1, 1);
                break;
            case DOWN:
                mouseX = (int) ( ( hitX - boundingBox.minX ) * 32 );
                mouseY = (int) ( ( boundingBox.maxZ - hitZ ) * 32 );

                width = (int) ((boundingBox.maxX - boundingBox.minX) * 32);
                height = (int) ((boundingBox.maxZ - boundingBox.minZ) * 32);

                RenderSystem.translated(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
                RenderSystem.rotatef(90, 1, 0, 0);
                break;
        }


        // 0.03125 = 1/32
        RenderSystem.scaled(0.03125, -0.03125, 0.03125);
        drawChildren(0, 0, 32, 32, mouseX, mouseY, 1);
        RenderSystem.popMatrix();

        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
