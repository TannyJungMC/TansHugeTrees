package tannyjung.tanshugetrees.client.gui;

import tannyjung.tanshugetrees.world.inventory.TreeSummonerStaffGUIMenu;
import tannyjung.tanshugetrees.network.TreeSummonerStaffGUIButtonMessage;
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

public class TreeSummonerStaffGUIScreen extends AbstractContainerScreen<TreeSummonerStaffGUIMenu> implements TanshugetreesModScreens.ScreenAccessor {
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	private boolean menuStateUpdateActive = false;
	private EditBox path;
	private Button button_apply;

	public TreeSummonerStaffGUIScreen(TreeSummonerStaffGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 0;
		this.imageHeight = 0;
	}

	@Override
	public void updateMenuState(int elementType, String name, Object elementState) {
		menuStateUpdateActive = true;
		if (elementType == 0 && elementState instanceof String stringState) {
			if (name.equals("path"))
				path.setValue(stringState);
		}
		menuStateUpdateActive = false;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("tanshugetrees:textures/screens/tree_summoner_staff_gui.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		path.render(guiGraphics, mouseX, mouseY, partialTicks);
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
		if (path.isFocused())
			return path.keyPressed(key, b, c);
		return super.keyPressed(key, b, c);
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		String pathValue = path.getValue();
		super.resize(minecraft, width, height);
		path.setValue(pathValue);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, Component.translatable("gui.tanshugetrees.tree_summoner_staff_gui.label_example_tannyjungmainpackr"), -176, -24, -6710887, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.tanshugetrees.tree_summoner_staff_gui.label_the_path_will_be_test_from_extra"), -208, 92, -6710887, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.tanshugetrees.tree_summoner_staff_gui.label_path_of_preset_inside_the_tempor"), -208, 80, -6710887, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.tanshugetrees.tree_summoner_staff_gui.label_is_extracted_then_it_will_go_ge"), -208, 104, -6710887, false);
	}

	@Override
	public void init() {
		super.init();
		path = new EditBox(this.font, this.leftPos + -175, this.topPos + -7, 350, 18, Component.translatable("gui.tanshugetrees.tree_summoner_staff_gui.path"));
		path.setMaxLength(8192);
		path.setResponder(content -> {
			if (!menuStateUpdateActive)
				menu.sendMenuStateUpdate(entity, 0, "path", content, false);
		});
		this.addWidget(this.path);
		button_apply = Button.builder(Component.translatable("gui.tanshugetrees.tree_summoner_staff_gui.button_apply"), e -> {
			int x = TreeSummonerStaffGUIScreen.this.x;
			int y = TreeSummonerStaffGUIScreen.this.y;
			if (true) {
				TanshugetreesMod.PACKET_HANDLER.sendToServer(new TreeSummonerStaffGUIButtonMessage(0, x, y, z));
				TreeSummonerStaffGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 128, this.topPos + 16, 48, 20).build();
		this.addRenderableWidget(button_apply);
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		path.tick();
	}
}