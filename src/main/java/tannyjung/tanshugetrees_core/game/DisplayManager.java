package tannyjung.tanshugetrees_core.game;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import tannyjung.tanshugetrees_core.Core;

public class DisplayManager {

    public static Entity summonText (ServerLevel level_server, Vec3 vec3, double size, String[] tags, String data) {

        Entity entity = EntityManager.summon(level_server, vec3, false, "minecraft:text_display", "Display Text", tags, "{billboard:vertical,alignment:\"center\",see_through:true,brightness:{block:15, sky:15},text_opacity:0,line_width:1000,transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],translation:[0f,0f,0f],scale:[" + size + "f," + size + "f," + size + "f]},text:'" + GameUtils.Data.createText(data) + "'}");

        if (entity != null) {

            entity.addTag(Core.mod_id_big + "-display_text");

        }

        return entity;

    }

    public static Entity summonTextTemporary (ServerLevel level_server, Vec3 vec3, double size, String[] tags, String data) {

        Entity entity = summonText(level_server, vec3, size, tags, data);

        Core.DelayedWork.createBasic(200, () -> {

            for (Entity scan : EntityManager.Population.getArea(level_server, vec3, 1, "minecraft:text_display", "", new String[]{Core.mod_id_big + "-display_text"})) {

                scan.discard();

            }

        });

        return entity;

    }

    public static Entity summonItem (ServerLevel level_server, Vec3 vec3, int rotate_horizontal, int rotate_vertical, double scale, boolean is_luminous, String name, String[] tags, String id) {

        StringBuilder builder = new StringBuilder();
        builder.append("item:{id:\"").append(id).append("\",Count:1b},teleport_duration:10");
        builder.append(",Rotation:[").append(rotate_horizontal).append("f,").append(rotate_vertical).append("f]");
        builder.append(",transformation:{left_rotation:[0.0f,0.0f,0.0f,1.0f],right_rotation:[0.0f,0.0f,0.0f,1.0f],translation:[0.0f,0.0f,0.0f],scale:[").append(scale).append("f,").append(scale).append("f,").append(scale).append("f]}");

        if (is_luminous == true) {

            builder.append(",brightness:{block:15,sky:15}");

        }

        Entity entity = EntityManager.summon(level_server, vec3, false, "minecraft:item_display", name, tags, "{" + builder + "}");
        NBTManager.Mob.setText(entity, "main", "display_id", id, false);
        return entity;

    }

    public static String getItemID (Entity entity) {

        return NBTManager.Mob.getText(entity, "main", "display_id");

    }

    public static void setItemID (Entity entity, String id) {

        NBTManager.Mob.setText(entity, "main", "display_id", id, false);
        GameUtils.runCommandEntity(entity, "data modify entity @s item.id set value \"" + id + "\"");

    }

    public static void setItemRotation (Entity entity, String axis, double degree) {

        double angle = Math.toRadians(degree);
        double sin = Math.sin(angle / 2);
        double cos = Math.cos(angle / 2);

        String data = "";

        if (axis.equals("x") == true) {

            data = "[" + sin + "f," + 0 + "f," + 0 + "f," + cos + "f]";

        } else if (axis.equals("y") == true) {

            data = "[" + 0 + "f," + sin + "f," + 0 + "f," + cos + "f]";

        } else if (axis.equals("z") == true) {

            data = "[" + 0 + "f," + 0 + "f," + sin + "f," + cos + "f]";

        }

        GameUtils.runCommandEntity(entity, "data modify entity @s transformation.left_rotation set value " + data);

    }

    public static void setItemScale (Entity entity, double scale) {

        GameUtils.runCommandEntity(entity, "data modify entity @s transformation.scale set value [" + scale + "f," + scale + "f," + scale + "f]");

    }

}
