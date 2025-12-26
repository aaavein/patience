package net.aaavein.patience.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.aaavein.patience.api.CraftingContainer;
import net.aaavein.patience.config.ContainerSettings;
import net.aaavein.patience.handler.CraftingHandler;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu>
        extends Screen implements CraftingContainer {

    @Unique
    private static final Logger patience$LOGGER = LogManager.getLogger("Patience/Mixin");

    @SuppressWarnings("unchecked")
    @Unique
    private final AbstractContainerScreen<T> patience$self = (AbstractContainerScreen<T>) (Object) this;

    @Shadow
    protected int leftPos;

    @Shadow
    protected int topPos;

    @Unique
    private boolean patience$completing = false;

    protected AbstractContainerScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    protected abstract void slotClicked(Slot slot, int slotId, int button, ClickType type);

    @Override
    public void patience$completeCraft(Slot slot, int slotId) {
        patience$completing = true;
        slotClicked(slot, slotId, 0, ClickType.PICKUP);
    }

    @Inject(method = "slotClicked", at = @At("HEAD"), cancellable = true)
    private void patience$onSlotClicked(Slot slot, int slotId, int button, ClickType type, CallbackInfo ci) {
        if (patience$completing) {
            patience$completing = false;
            return;
        }

        try {
            boolean shift = Screen.hasShiftDown();
            if (CraftingHandler.getInstance().handleSlotClick(patience$self, slotId, shift)) {
                ci.cancel();
            }
        } catch (Exception e) {
            patience$LOGGER.error("Error handling slot click", e);
            try {
                if (CraftingHandler.getInstance().shouldBlockSlotClick(patience$self, slotId)) {
                    ci.cancel();
                }
            } catch (Exception ex) {
                patience$LOGGER.error("Error in fallback check", ex);
            }
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void patience$onRender(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        try {
            CraftingHandler handler = CraftingHandler.getInstance();
            ContainerSettings settings = handler.getCurrentContainerSettings();

            if (!settings.isEnabled()) {
                return;
            }

            if (handler.isCrafting() || handler.getCurrentTime() > 0) {
                if (settings.isShowOverlay() && handler.getTotalTime() > 0) {
                    patience$renderOverlay(graphics, settings, handler.getCurrentTime() / handler.getTotalTime());
                }
            }
        } catch (Exception e) {
            patience$LOGGER.error("Error rendering overlay", e);
        }
    }

    @Unique
    private void patience$renderOverlay(GuiGraphics graphics, ContainerSettings settings, float progress) {
        ResourceLocation texture = ResourceLocation.parse(settings.getOverlayTexture());
        int x = leftPos + settings.getOverlayX();
        int y = topPos + settings.getOverlayY();
        int width = settings.getOverlayWidth();
        int height = settings.getOverlayHeight();

        String direction = settings.getOverlayDirection();
        if (direction == null) direction = "right";

        float[] colors = CraftingHandler.getInstance().getOverlayColor();
        RenderSystem.setShaderColor(colors[0], colors[1], colors[2], colors[3]);

        switch (direction.toLowerCase()) {
            case "left" -> {
                int w = (int) (progress * width);
                int offset = width - w;
                graphics.blit(texture, x + offset, y, offset, 0, w, height, width, height);
            }
            case "up" -> {
                int h = (int) (progress * height);
                int offset = height - h;
                graphics.blit(texture, x, y + offset, 0, offset, width, h, width, height);
            }
            case "down" -> {
                int h = (int) (progress * height);
                graphics.blit(texture, x, y, 0, 0, width, h, width, height);
            }
            default -> {
                int w = (int) (progress * width);
                graphics.blit(texture, x, y, 0, 0, w, height, width, height);
            }
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Inject(method = "onClose", at = @At("TAIL"))
    private void patience$onClose(CallbackInfo ci) {
        CraftingHandler.getInstance().clearScreen();
    }
}