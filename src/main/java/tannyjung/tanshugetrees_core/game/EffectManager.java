package tannyjung.tanshugetrees_core.game;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.*;

public class EffectManager {

    private static final Map<String, Object> effect_holders = new HashMap<>();

    public static Object getEffect (ServerLevel level_server, String id) {

        Holder<MobEffect> holder = null;

        if (effect_holders.containsKey(id) == false) {

            try {

                holder = level_server.registryAccess().lookupOrThrow(Registries.MOB_EFFECT).getOrThrow(ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation.parse(id)));

            } catch (Exception ignored) {



            }

            effect_holders.put(id, holder);

        } else {

            holder = (Holder<MobEffect>) effect_holders.get(id);

        }

        return holder;

    }

    public static Object getInstance (ServerLevel level_server, Entity entity, String id) {

        if (entity instanceof LivingEntity entity_living) {

            Holder<MobEffect> effect = (Holder<MobEffect>) getEffect(level_server, id);

            if (effect == null) {

                return null;

            }

            return entity_living.getEffect(effect);

        }

        return null;

    }

    public static Object createInstance (ServerLevel level_server, String id, int level, int duration_tick, boolean is_ambient, boolean is_visible) {

        Holder<MobEffect> effect = (Holder<MobEffect>) getEffect(level_server, id);

        if (effect == null) {

            return null;

        }

        return new MobEffectInstance(effect, duration_tick, level - 1, is_ambient, is_visible);

    }

    public static boolean has (ServerLevel level_server, Entity entity, String id) {

        if (entity instanceof LivingEntity entity_living) {

            Holder<MobEffect> effect = (Holder<MobEffect>) getEffect(level_server, id);

            if (effect == null) {

                return false;

            }

            return entity_living.getEffect(effect) != null;

        }

        return false;

    }

    public static boolean hasDuration (ServerLevel level_server, Entity entity, String id, int min_duration_tick) {

        if (entity instanceof LivingEntity == true) {

            MobEffectInstance instance = (MobEffectInstance) getInstance(level_server, entity, id);

            if (instance == null) {

                return false;

            }

            return min_duration_tick <= instance.getDuration();

        }

        return false;

    }

    public static void giveBasic (ServerLevel level_server, Entity entity, String id, int level, int duration_tick) {

        MobEffectInstance instance = (MobEffectInstance) createInstance(level_server, id, level, duration_tick, false, false);
        giveAdvance(entity, instance);

    }

    public static void giveAdvance (Entity entity, Object object_instance) {

        if (entity instanceof LivingEntity entity_living) {

            entity_living.addEffect((MobEffectInstance) object_instance);

        }

    }

    public static boolean clearAll (Entity entity) {

        if (entity instanceof LivingEntity entity_living) {

            return entity_living.removeAllEffects();

        }

        return false;

    }

    public static void clearSpecific (ServerLevel level_server, Entity entity, String id) {

        if (entity instanceof LivingEntity entity_living) {

            Holder<MobEffect> effect = (Holder<MobEffect>) getEffect(level_server, id);

            if (effect == null) {

                return;

            }

            entity_living.removeEffect(effect);

        }

    }

    public static void copyAll (Entity entity_from, Entity entity_to) {

        if (entity_from instanceof LivingEntity entity_living_from && entity_to instanceof LivingEntity entity_living_to) {

            for (MobEffectInstance instance : entity_living_from.getActiveEffects()) {

                entity_living_to.addEffect(new MobEffectInstance(instance.getEffect(), instance.getDuration(), instance.getAmplifier(), instance.isAmbient(), instance.isVisible()));

            }

        }

    }

    public static void copySpecific (ServerLevel level_server, Entity entity_from, Entity entity_to, String id) {

        if (entity_from instanceof LivingEntity entity_living_from) {

            if (entity_to instanceof LivingEntity entity_living_to) {

                MobEffectInstance instance = (MobEffectInstance) getInstance(level_server, entity_from, id);

                if (instance == null) {

                    return;

                }

                entity_living_to.addEffect(instance);

            }

        }

    }

    public static List<Object> getActive (Entity entity) {

        if (entity instanceof LivingEntity entity_living) {

            return Arrays.asList(entity_living.getActiveEffects().toArray());

        }

        return new ArrayList<>();

    }

    public static Object modify (Object object_instance, int level, int duration_tick) {

        MobEffectInstance instance = (MobEffectInstance) object_instance;
        int set_level = 0;
        int set_duration_tick = 0;

        if (level == 0) {

            set_level = instance.getAmplifier();

        } else {

            set_level = level - 1;

        }

        if (duration_tick == 0) {

            set_duration_tick = instance.getDuration();

        } else {

            set_duration_tick = duration_tick;

        }

        return new MobEffectInstance(instance.getEffect(), set_duration_tick, set_level, instance.isAmbient(), instance.isVisible());

    }

}
