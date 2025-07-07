package tannyjung.tanshugetrees.client.gui;

import tannyjung.tanshugetrees.world.inventory.GUIPresetFixerMenu;
import tannyjung.tanshugetrees.network.GUIPresetFixerButtonMessage;
import tannyjung.tanshugetrees.init.TanshugetreesModScreens;
import tannyjung.tanshugetrees.TanshugetreesMod;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import com.mojang.blaze3d.systems.RenderSystem;

public class GUIPresetFixerScreen extends AbstractContainerScreen<GUIPresetFixerMenu> implements TanshugetreesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	EditBox preset;
	Button button_convert;

	public GUIPresetFixerScreen(GUIPresetFixerMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 60;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		menuStateUpdateActive = false;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("tanshugetrees:textures/screens/gui_preset_fixer.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		preset.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		if (preset.isFocused())
			return preset.keyPressed(key, b, c);
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		preset.tick();
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		String presetValue = preset.getValue();
		super.resize(minecraft, width, height);
		preset.setValue(presetValue);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public void init() {
		super.init();
		preset = new EditBox(this.font, this.leftPos + 9, this.topPos + 7, 158, 18, Component.translatable("gui.tanshugetrees.gui_preset_fixer.preset"));
		preset.setHint(Component.translatable("gui.tanshugetrees.gui_preset_fixer.preset"));
		preset.setMaxLength(8192);
		preset.setResponder(content -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 0, "preset", content, false);
		});
		this.addWidget(this.preset);
		button_convert = Button.builder(Component.translatable("gui.tanshugetrees.gui_preset_fixer.button_convert"), e -> {
			int x = GUIPresetFixerScreen.this.x;
			int y = GUIPresetFixerScreen.this.y;
			if (true) {
				TanshugetreesMod.PACKET_HANDLER.sendToServer(new GUIPresetFixerButtonMessage(0, x, y, z));
				GUIPresetFixerButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 72, this.topPos + 30, 32, 20).build();
		this.addRenderableWidget(button_convert);
	}
}