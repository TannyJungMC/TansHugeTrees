package tannyjung.tanshugetrees_core.game;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import tannyjung.tanshugetrees_core.Core;

import java.util.HashMap;
import java.util.Map;

public class KeyBindingMaker {

    public static ServerLevel level_server = null;
    public static ServerPlayer player_server = null;

    public static class Create {

        public static void keyPush (String name, int glfw, Runnable runnable) {

            Storage.add(name, glfw, runnable, null);

        }

        public static void keyPushRelease (String name, int glfw, Runnable runnable_push, Runnable runnable_release) {

            Storage.add(name, glfw, runnable_push, runnable_release);

        }

    }

    public static class Storage {

        public static Map<String, KeyMapping> keys = new HashMap<>();

        public static void tick () {

            for (Map.Entry<String, KeyMapping> entry : keys.entrySet()) {

                entry.getValue().consumeClick();

            }

        }

        private static void add (String name, int glfw, Runnable runnable_push, Runnable runnable_release) {

            InputConstants.Type type = null;

            if (glfw <= 8) {

                type = InputConstants.Type.MOUSE;

            } else {

                type = InputConstants.Type.KEYSYM;

            }

            KeyMapping key = new KeyMapping(name, type, glfw, Core.mod_name) {

                private boolean pause = false;

                @Override
                public void setDown (boolean is_down) {

                    if (is_down == true) {

                        if (pause == false) {

                            pause = true;

                            if (runnable_push != null) {

                                runnable_push.run();

                            }

                        }

                    } else {

                        if (pause == true) {

                            pause = false;

                            if (runnable_release != null) {

                                runnable_release.run();

                            }

                        }

                    }

                    super.setDown(is_down);

                }

            };

            keys.put(name, key);

        }

    }

}
