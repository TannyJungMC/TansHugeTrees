package tannyjung.tanshugetrees_core.game;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import tannyjung.tanshugetrees_core.Core;

import java.util.*;

public class EntityManager {

    public static Entity summon (ServerLevel level_server, Vec3 vec3, boolean is_always_show_name, String id, String name, String[] tags, String custom) {

        EntityType<?> type = level_server.registryAccess().registryOrThrow(Registries.ENTITY_TYPE).get(ResourceLocation.parse(id));

        if (type == null) {

            return null;

        }

        Entity entity = type.create(level_server);

        if (entity == null) {

            return null;

        }

        if (custom.isEmpty() == false) {

            entity.load(NBTManager.convertJSONToTag(custom));

        }

        if (is_always_show_name == true) {

            entity.setCustomNameVisible(true);

        }

        entity.setCustomName(Component.literal(name));
        entity.addTag("TANNYJUNG");
        entity.addTag(Core.mod_id_big);

        for (String get : tags) {

            entity.addTag(get);

        }

        entity.setPos(vec3);
        level_server.addFreshEntity(entity);
        return entity;

    }

    public static void summonWorldGen (ServerLevel level_server, Vec3 vec3, String id, String name, String[] tags, String custom) {

        level_server.getServer().execute(() -> {

            summon(level_server, vec3, false, id, name, tags, custom);

        });

    }

    public static String getID (Entity entity) {

        return EntityType.getKey(entity.getType()).toString();

    }

    public static Entity getByUUID (ServerLevel level_server, String uuid) {

        UUID uuid_convert = null;

        try {

            uuid_convert = UUID.fromString(uuid);

        } catch (Exception ignored) {

            return null;

        }

        return level_server.getEntity(uuid_convert);

    }

    public static Vec3 getPosLook (Entity entity, double offsetX, double offsetY, double offsetZ) {

        Vec3 vec3_forward = Vec3.directionFromRotation(entity.getXRot(), entity.getYRot());
        Vec3 vec3_vertical = null;

        if (Math.abs(vec3_forward.y) > 0.999) {

            vec3_vertical = new Vec3(0,0,1);

        } else {

            vec3_vertical = new Vec3(0,1,0);

        }

        Vec3 vec3_horizontal = vec3_forward.cross(vec3_vertical).normalize();
        Vec3 vec3_vertical_adjust = vec3_horizontal.cross(vec3_forward).normalize();
        return entity.position().add(vec3_horizontal.scale(offsetX)).add(vec3_vertical_adjust.scale(offsetY)).add(vec3_forward.scale(offsetZ));
    }

    public static Vec3 getPosLookReverse (Entity entity, Vec3 vec3, double offsetX, double offsetY, double offsetZ) {

        Vec3 forward_behind = vec3.subtract(entity.getEyePosition()).normalize();
        Vec3 left_right = forward_behind.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 up = left_right.cross(forward_behind).normalize();
        return entity.getEyePosition().add(left_right.scale(offsetX)).add(up.scale(offsetY)).add(forward_behind.scale(offsetZ));

    }

    public static Vec3 getPosRay (Entity entity, Vec3 vec3, boolean is_feet_position) {

        Vec3 vec3_from = null;
        
        if (is_feet_position == true) {

            vec3_from = entity.position();
            
        } else {

            vec3_from = entity.getEyePosition();
            
        }
        
        return entity.level().clip(new ClipContext(vec3_from, vec3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getLocation();

    }

    public static Vec3 getPosRayWithDistance (Entity entity, Vec3 vec3, double distance, boolean is_feet_position) {

        Vec3 vec3_base = null;

        if (is_feet_position == true) {

            vec3_base = entity.getPosition(1f);

        } else {

            vec3_base = entity.getEyePosition(1f);

        }

        vec3 = vec3.subtract(vec3_base).normalize();
        return getPosRay(entity, vec3_base.add(vec3.scale(distance)), is_feet_position);

    }

    public static Vec3 getPosRayForward (Entity entity, double distance, boolean is_feet_position) {

        Vec3 vec3 = null;

        if (is_feet_position == true) {

            vec3 = entity.getPosition(1f);

        } else {

            vec3 = entity.getEyePosition(1f);

        }

        return getPosRay(entity, vec3.add(entity.getViewVector(1f).scale(distance)), is_feet_position);

    }

    public static boolean testPosRayNoBlocking (Entity entity, Vec3 vec3, boolean is_feet_position) {

        return getPosRay(entity, vec3, is_feet_position).distanceTo(vec3) < 0.1;

    }

    public static Entity getTarget (Entity entity) {

        if (entity instanceof Mob mob) {

            return mob.getTarget();

        }

        return null;

    }

    public static void setTarget (Entity entity_attacker, Entity entity_target) {

        if (entity_attacker instanceof Mob mob) {

            if (entity_target instanceof LivingEntity entity_living) {

                mob.setTarget(entity_living);

            }

        }

    }

    public static void clearTarget (Entity entity) {

        if (entity instanceof Mob mob) {

            Entity entity_summon = summon((ServerLevel) entity.level(), entity.position(), false, "minecraft:chicken", "", new String[]{}, "");

            if (entity_summon == null) {

                return;

            }

            mob.setTarget((LivingEntity) entity_summon);
            entity_summon.discard();

        }

    }

    public static void go (Entity entity, Vec3 vec3, double speed) {

        if (entity instanceof Mob mob) {

            mob.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, speed);

        }

    }

    public static class Population {

        private record Request (String id, String name, List<String> tag) {}
        private static final Map<Request, List<Entity>> population = new HashMap<>();
        private static final Set<Request> pause_updatable = new HashSet<>();

        public static void refresh () {

            population.clear();

        }

        private static boolean test (Entity entity, Request request) {

            // ID
            {

                if (request.id.isEmpty() == false) {

                    String id = getID(entity);

                    if (request.id.startsWith("!") == true) {

                        if (request.id.substring(1).equals(id) == true) {

                            return false;

                        }

                    } else {

                        if (request.id.equals(id) == false) {

                            return false;

                        }

                    }

                }

            }

            // Name
            {

                if (request.name.isEmpty() == false) {

                    String name = entity.getDisplayName().getString();

                    if (request.name.startsWith("!") == true) {

                        if (request.name.substring(1).equals(name) == true) {

                            return false;

                        }

                    } else {

                        if (request.name.equals(name) == false) {

                            return false;

                        }

                    }

                }

            }

            // Tag
            {

                if (request.tag.isEmpty() == false) {

                    Set<String> tag = entity.getTags();
                    boolean is_tag_blacklist = false;

                    for (String scan : request.tag) {

                        if (scan.startsWith("!") == true) {

                            is_tag_blacklist = true;
                            scan = scan.substring(1);

                        } else {

                            is_tag_blacklist = false;

                        }

                        if (tag.contains(scan) == false) {

                            if (is_tag_blacklist == false) {

                                return false;

                            }

                        } else {

                            if (is_tag_blacklist == true) {

                                return false;

                            }

                        }

                    }

                }

            }

            return true;

        }

        public static void eventAddRemove (Entity entity, boolean is_join) {

            for (Request request : population.keySet()) {

                if (test(entity, request) == true) {

                    if (is_join == true) {

                        population.get(request).add(entity);

                    } else {

                        Core.DelayedWork.createBasic(1, () -> {

                            population.getOrDefault(request, new ArrayList<>()).removeIf(scan -> scan.getId() == entity.getId());

                        });

                    }

                }

            }

        }

        private static List<Entity> getEverywhere (ServerLevel level_server, Request request) {

            List<Entity> entities = population.get(request);

            if (entities == null) {

                List<Entity> list = new ArrayList<>();

                level_server.getAllEntities().forEach(entity -> {

                    if (test(entity, request) == true) {

                        list.add(entity);

                    }

                });

                entities = list;
                population.put(request, entities);

            } else {

                if (entities.isEmpty() == true) {

                    population.remove(request);

                }

            }

            return entities;

        }

        public static List<Entity> getEverywhereStatic (ServerLevel level_server, String id, String name, String[] tags) {

            List<String> tags_convert = List.of(tags);
            Request request = new Request(id, name, tags_convert);
            return getEverywhere(level_server, request);

        }

        public static List<Entity> getEverywhereUpdatable (ServerLevel level_server, String id, String name, String[] tags) {

            List<String> tags_convert = List.of(tags);
            Request request = new Request(id, name, tags_convert);

            if (pause_updatable.contains(request) == false) {

                pause_updatable.add(request);

                Core.DelayedWork.createBasic(20, () -> {

                    pause_updatable.remove(request);

                });

                population.remove(request);

            }

            return getEverywhere(level_server, request);

        }

        private static Entity getEverywhereOne (ServerLevel level_server, boolean is_updatable, String id, String name, String[] tags) {

            List<Entity> entities = new ArrayList<>();

            if (is_updatable == true) {

                entities = getEverywhereUpdatable(level_server, id, name, tags);

            } else {

                entities = getEverywhereStatic(level_server, id, name, tags);

            }

            if (entities.isEmpty() == true) {

                return null;

            }

            return entities.getFirst();

        }

        public static Entity getEverywhereOneStatic (ServerLevel level_server, String id, String name, String[] tags) {

            return getEverywhereOne(level_server, false, id, name, tags);

        }

        public static Entity getEverywhereOneUpdatable (ServerLevel level_server, String id, String name, String[] tags) {

            return getEverywhereOne(level_server, true, id, name, tags);

        }

        public static List<Entity> getArea (ServerLevel level_server, Vec3 vec3, double radius, String id, String name, String[] tags) {

            Request request = new Request(id, name, List.of(tags));

            return level_server.getEntitiesOfClass(Entity.class, new AABB(vec3, vec3).inflate(radius), entity -> {

                if (entity.position().distanceTo(vec3) > radius) {

                    return false;

                }

                return test(entity, request);

            });

        }

        public static Entity[] sort (List<Entity> entities, Vec3 vec3_center, boolean is_farthest, int count_limit) {

            List<Entity> list = entities.stream().sorted(Comparator.comparingDouble(entity -> entity.position().distanceTo(vec3_center))).toList();

            if (is_farthest == true) {

                list = list.reversed();

            }

            if (count_limit > 0) {

                if (list.size() > count_limit) {

                    list = list.subList(0, count_limit);

                }

            }

            return list.toArray(new Entity[]{});

        }

        public static List<Entity> filter (List<Entity> entities, String id, String name, String[] tags) {

            List<Entity> list = new ArrayList<>();
            Request request = new Request(id, name, List.of(tags));

            for (Entity entity : entities) {

                if (test(entity, request) == true) {

                    list.add(entity);

                }

            }

            return list;

        }

        public static List<ServerPlayer> filterPlayerServer (List<Entity> entities) {

            List<ServerPlayer> list = new ArrayList<>();

            for (Entity entity : entities) {

                if (entity instanceof ServerPlayer test) {

                    list.add(test);

                }

            }

            return list;

        }

        public static List<Entity> filterLivingEntity (List<Entity> entities) {

            List<Entity> list = new ArrayList<>();

            for (Entity entity : entities) {

                if (entity instanceof LivingEntity test) {

                    list.add(test);

                }

            }

            return list;

        }

    }

}
