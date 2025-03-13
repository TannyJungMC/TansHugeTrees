package tannyjung.tht.client.gui;

import tannyjung.tht.world.inventory.PresetFixerGUIMenu;
import tannyjung.tht.network.PresetFixerGUIButtonMessage;
import tannyjung.tht.ThtMod;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.Minecraft;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class PresetFixerGUIScreen extends AbstractContainerScreen<PresetFixerGUIMenu> {
	private final static HashMap<String, Object> guistate = PresetFixerGUIMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	EditBox preset;
	Button button_convert;

	public PresetFixerGUIScreen(PresetFixerGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 60;
	}

	public static HashMap<String, String> getEditBoxAndCheckBoxValues() {
		HashMap<String, String> textstate = new HashMap<>();
		if (Minecraft.getInstance().screen instanceof PresetFixerGUIScreen sc) {
			textstate.put("textin:preset", sc.preset.getValue());

		}
		return textstate;
	}

	private static final ResourceLocation texture = new ResourceLocation("tht:textures/screens/preset_fixer_gui.png");

	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderTooltip(ms, mouseX, mouseY);
		preset.render(ms, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void renderBg(PoseStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShaderTexture(0, texture);
		this.blit(ms, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
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
	public void containerTick() {
		super.containerTick();
		preset.tick();
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
	}

	@Override
	public void onClose() {
		super.onClose();
		Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
	}

	@Override
	public void init() {
		super.init();
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
		preset = new EditBox(this.font, this.leftPos + 8, this.topPos + 6, 160, 20, Component.translatable("gui.tht.preset_fixer_gui.preset")) {
			{
				setSuggestion(Component.translatable("gui.tht.preset_fixer_gui.preset").getString());
			}

			@Override
			public void insertText(String text) {
				super.insertText(text);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.tht.preset_fixer_gui.preset").getString());
				else
					setSuggestion(null);
			}

			@Override
			public void moveCursorTo(int pos) {
				super.moveCursorTo(pos);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.tht.preset_fixer_gui.preset").getString());
				else
					setSuggestion(null);
			}
		};
		preset.setMaxLength(32767);
		guistate.put("text:preset", preset);
		this.addWidget(this.preset);
		button_convert = new Button(this.leftPos + 72, this.topPos + 30, 32, 20, Component.translatable("gui.tht.preset_fixer_gui.button_convert"), e -> {
			if (true) {
				ThtMod.PACKET_HANDLER.sendToServer(new PresetFixerGUIButtonMessage(0, x, y, z, getEditBoxAndCheckBoxValues()));
				PresetFixerGUIButtonMessage.handleButtonAction(entity, 0, x, y, z, getEditBoxAndCheckBoxValues());
			}
		});
		guistate.put("button:button_convert", button_convert);
		this.addRenderableWidget(button_convert);
	}
}
