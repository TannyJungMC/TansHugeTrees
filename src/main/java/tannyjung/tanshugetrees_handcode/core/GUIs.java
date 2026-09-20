package tannyjung.tanshugetrees_handcode.core;

import tannyjung.tanshugetrees_core.game.screen.GUIManager;
import tannyjung.tanshugetrees_core.game.screen.ScreenDrawing;

public class GUIs {

    public static void add () {

        GUIManager.create("main", "tree_generator_staff", () -> {

            ScreenDrawing.Position.set(ScreenDrawing.Anchor.Type.CENTER, -150, -100);
            ScreenDrawing.ComponentOverlay.drawTextBasic(ScreenDrawing.normal_font_scale, "§fThis is new design of tree summoner staff GUI");
            ScreenDrawing.Position.addZ(8);
            ScreenDrawing.ComponentWidget.Board.drawBasic(300, 175, "tree_generator_staff");

        });

    }

}
