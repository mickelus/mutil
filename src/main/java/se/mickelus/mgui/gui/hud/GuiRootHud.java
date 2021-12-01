package se.mickelus.mgui.gui.hud;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import se.mickelus.mgui.gui.GuiElement;
import se.mickelus.mgui.gui.animation.KeyframeAnimation;

public class GuiRootHud extends GuiElement {

    public GuiRootHud() {
        super(0, 0, 0, 0);
    }

    public void draw(MatrixStack matrixStack, Vector3d proj, BlockRayTraceResult rayTrace, VoxelShape shape) {
        BlockPos blockPos = rayTrace.getBlockPos();

        Vector3d hitVec = rayTrace.getLocation();

        draw(matrixStack, blockPos.getX() - proj.x, blockPos.getY() - proj.y, blockPos.getZ() - proj.z,
                hitVec.x - blockPos.getX(), hitVec.y - blockPos.getY(), hitVec.z - blockPos.getZ(),
                rayTrace.getDirection(), shape.bounds());
    }

    public void draw(MatrixStack matrixStack, double x, double y, double z, double hitX, double hitY, double hitZ, Direction facing, AxisAlignedBB boundingBox) {
        activeAnimations.removeIf(keyframeAnimation -> !keyframeAnimation.isActive());
        activeAnimations.forEach(KeyframeAnimation::preDraw);

        matrixStack.pushPose();
        matrixStack.translate(x, y, z);

        int mouseX = 0;
        int mouseY = 0;

        float size = 64;

        // magic number is the same used to offset the outline, stops textures from flickering
        Vector3d magicOffset = Vector3d.atLowerCornerOf(facing.getNormal()).scale(0.0020000000949949026D);
        matrixStack.translate(magicOffset.x(), magicOffset.y(), magicOffset.z());

        switch (facing) {
            case NORTH:
                mouseX = (int) ( ( boundingBox.maxX - hitX ) * size );
                mouseY = (int) ( ( boundingBox.maxY - hitY ) * size );

                width = (int) ((boundingBox.maxX - boundingBox.minX) * size);
                height = (int) ((boundingBox.maxY - boundingBox.minY) * size);

                matrixStack.translate(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
                break;
            case SOUTH:
                mouseX = (int) ( ( hitX - boundingBox.minX ) * size );
                mouseY = (int) ( ( boundingBox.maxY - hitY ) * size );

                width = (int) ((boundingBox.maxX - boundingBox.minX) * size);
                height = (int) ((boundingBox.maxY - boundingBox.minY) * size);

                matrixStack.translate(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
                break;
            case EAST:
                mouseX = (int) ( ( boundingBox.maxZ - hitZ ) * size );
                mouseY = (int) ( ( boundingBox.maxY - hitY ) * size );

                width = (int) ((boundingBox.maxZ - boundingBox.minZ) * size);
                height = (int) ((boundingBox.maxY - boundingBox.minY) * size);

                matrixStack.translate(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(90));
                break;
            case WEST:
                mouseX = (int) ( ( hitZ - boundingBox.minZ ) * size );
                mouseY = (int) ( ( boundingBox.maxY - hitY ) * size );

                width = (int) ((boundingBox.maxZ - boundingBox.minZ) * size);
                height = (int) ((boundingBox.maxY - boundingBox.minY) * size);

                matrixStack.translate(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(-90));
                break;
            case UP:
                mouseX = (int) ( ( boundingBox.maxX - hitX ) * size );
                mouseY = (int) ( ( boundingBox.maxZ - hitZ ) * size );

                width = (int) ((boundingBox.maxX - boundingBox.minX) * size);
                height = (int) ((boundingBox.maxZ - boundingBox.minZ) * size);

                matrixStack.translate(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
                matrixStack.scale(-1, 1, 1);
                break;
            case DOWN:
                mouseX = (int) ( ( hitX - boundingBox.minX ) * size );
                mouseY = (int) ( ( boundingBox.maxZ - hitZ ) * size );

                width = (int) ((boundingBox.maxX - boundingBox.minX) * size);
                height = (int) ((boundingBox.maxZ - boundingBox.minZ) * size);

                matrixStack.translate(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
                break;
        }

        matrixStack.scale(1 / size, -1 / size, 1 / size);
        matrixStack.translate(0.0D, 0, 0.02);
        drawChildren(matrixStack, 0, 0, width, height, mouseX, mouseY, 1);
        matrixStack.popPose();
    }
}
