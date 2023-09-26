package dev.turtywurty.turtyissinking.client.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * A model that can be animated (usable with anything, not just limited to entities like {@link HierarchicalModel})
 */
public abstract class AnimatableModel extends Model {
    private final ModelPart root;

    /**
     * Creates a new animatable model
     *
     * @param pRenderType The render type of the model
     * @param root        The root part of the model
     */
    public AnimatableModel(Function<ResourceLocation, RenderType> pRenderType, ModelPart root) {
        super(pRenderType);
        this.root = root;
    }

    /**
     * Gets the root part of the model
     *
     * @return The root part
     */
    public ModelPart getRoot() {
        return this.root;
    }

    /**
     * Gets a descendant part of the model with the given name
     *
     * @param name The name of the descendant
     * @return The descendant part
     */
    public Optional<ModelPart> getAnyDescendantWithName(String name) {
        return name.equals("root") ? Optional.of(getRoot()) : getRoot()
                .getAllParts()
                .filter(part -> part.hasChild(name))
                .findFirst()
                .map(part -> part.getChild(name));
    }

    /**
     * Animates the model with the given animation definition (sequence of keyframes)
     *
     * @param pModel               The model to animate
     * @param pAnimationDefinition The animation definition
     * @param pAccumulatedTime     The accumulated time in milliseconds
     * @param pScale               The scale of the animation
     * @param pAnimationVecCache   The animation vector cache
     */
    public static void animate(AnimatableModel pModel, AnimationDefinition pAnimationDefinition, long pAccumulatedTime, float pScale, Vector3f pAnimationVecCache) {
        float elapsedSeconds = getElapsedSeconds(pAnimationDefinition, pAccumulatedTime);

        for(Map.Entry<String, List<AnimationChannel>> entry : pAnimationDefinition.boneAnimations().entrySet()) {
            // Find a part with the name of the entry
            Optional<ModelPart> optPart = pModel.getAnyDescendantWithName(entry.getKey());
            if(optPart.isEmpty())
                continue;

            ModelPart part = optPart.get();
            List<AnimationChannel> animations = entry.getValue();
            for (AnimationChannel animation : animations) {
                if (animation == null)
                    continue;

                // Get the keyframes of the animation
                Keyframe[] keyframes = animation.keyframes();

                // Find the current and next keyframes by comparing if the elapsed time is less than the timestamp
                int foundIndex = Mth.binarySearch(
                        0,
                        keyframes.length,
                        threshold -> elapsedSeconds <= keyframes[threshold].timestamp());
                int currentFrameIdx = Math.max(0, foundIndex - 1);
                int nextFrameIdx = Math.min(keyframes.length - 1, currentFrameIdx + 1);

                // Get the current and next keyframes
                Keyframe currentFrame = keyframes[currentFrameIdx];
                Keyframe nextFrame = keyframes[nextFrameIdx];

                // Calculate the progress between the current and next keyframes
                float timeLeft = elapsedSeconds - currentFrame.timestamp();
                float progress = Mth.clamp(
                        timeLeft / (nextFrame.timestamp() - currentFrame.timestamp()),
                        0.0F,
                        1.0F);

                // Interpolate the keyframes
                nextFrame.interpolation().apply(
                        pAnimationVecCache,
                        progress,
                        keyframes,
                        currentFrameIdx,
                        nextFrameIdx,
                        pScale);

                // Apply the interpolated keyframes to the part
                animation.target().apply(part, pAnimationVecCache);
            }
        }
    }

    /**
     * Gets the elapsed seconds of the animation
     *
     * @param pAnimationDefinition The animation definition
     * @param pAccumulatedTime     The accumulated time in milliseconds
     * @return The elapsed seconds
     */
    public static float getElapsedSeconds(AnimationDefinition pAnimationDefinition, long pAccumulatedTime) {
        float secondsAccumulated = (float)pAccumulatedTime / 1000.0F;

        return pAnimationDefinition.looping()
                ? secondsAccumulated % pAnimationDefinition.lengthInSeconds()
                : secondsAccumulated;
    }

    public static class Interpolations {
        public static final AnimationChannel.Interpolation SINE = (pAnimationVecCache, pKeyframeDelta, pKeyframes, pCurrentKeyframeIdx, pNextKeyframeIdx, pScale) -> {
            Vector3f currentVec = pKeyframes[pCurrentKeyframeIdx].target();
            Vector3f nextVec = pKeyframes[pNextKeyframeIdx].target();
            float delta = Mth.sin(pKeyframeDelta * (float)Math.PI);
            pAnimationVecCache.set(
                    Mth.lerp(delta, currentVec.x(), nextVec.x()) * pScale,
                    Mth.lerp(delta, currentVec.y(), nextVec.y()) * pScale,
                    Mth.lerp(delta, currentVec.z(), nextVec.z()) * pScale);
            return pAnimationVecCache;
        };

        public static final AnimationChannel.Interpolation QUAD = (pAnimationVecCache, pKeyframeDelta, pKeyframes, pCurrentKeyframeIdx, pNextKeyframeIdx, pScale) -> {
            Vector3f currentVec = pKeyframes[pCurrentKeyframeIdx].target();
            Vector3f nextVec = pKeyframes[pNextKeyframeIdx].target();
            float delta = pKeyframeDelta * pKeyframeDelta;
            pAnimationVecCache.set(
                    Mth.lerp(delta, currentVec.x(), nextVec.x()) * pScale,
                    Mth.lerp(delta, currentVec.y(), nextVec.y()) * pScale,
                    Mth.lerp(delta, currentVec.z(), nextVec.z()) * pScale);
            return pAnimationVecCache;
        };

        public static final AnimationChannel.Interpolation CUBIC = (pAnimationVecCache, pKeyframeDelta, pKeyframes, pCurrentKeyframeIdx, pNextKeyframeIdx, pScale) -> {
            Vector3f currentVec = pKeyframes[pCurrentKeyframeIdx].target();
            Vector3f nextVec = pKeyframes[pNextKeyframeIdx].target();
            float delta = pKeyframeDelta * pKeyframeDelta * pKeyframeDelta;
            pAnimationVecCache.set(
                    Mth.lerp(delta, currentVec.x(), nextVec.x()) * pScale,
                    Mth.lerp(delta, currentVec.y(), nextVec.y()) * pScale,
                    Mth.lerp(delta, currentVec.z(), nextVec.z()) * pScale);
            return pAnimationVecCache;
        };

        public static final AnimationChannel.Interpolation QUART = (pAnimationVecCache, pKeyframeDelta, pKeyframes, pCurrentKeyframeIdx, pNextKeyframeIdx, pScale) -> {
            Vector3f currentVec = pKeyframes[pCurrentKeyframeIdx].target();
            Vector3f nextVec = pKeyframes[pNextKeyframeIdx].target();
            float delta = pKeyframeDelta * pKeyframeDelta * pKeyframeDelta * pKeyframeDelta;
            pAnimationVecCache.set(
                    Mth.lerp(delta, currentVec.x(), nextVec.x()) * pScale,
                    Mth.lerp(delta, currentVec.y(), nextVec.y()) * pScale,
                    Mth.lerp(delta, currentVec.z(), nextVec.z()) * pScale);
            return pAnimationVecCache;
        };

        public static final AnimationChannel.Interpolation QUINT = (pAnimationVecCache, pKeyframeDelta, pKeyframes, pCurrentKeyframeIdx, pNextKeyframeIdx, pScale) -> {
            Vector3f currentVec = pKeyframes[pCurrentKeyframeIdx].target();
            Vector3f nextVec = pKeyframes[pNextKeyframeIdx].target();
            float delta = pKeyframeDelta * pKeyframeDelta * pKeyframeDelta * pKeyframeDelta * pKeyframeDelta;
            pAnimationVecCache.set(
                    Mth.lerp(delta, currentVec.x(), nextVec.x()) * pScale,
                    Mth.lerp(delta, currentVec.y(), nextVec.y()) * pScale,
                    Mth.lerp(delta, currentVec.z(), nextVec.z()) * pScale);
            return pAnimationVecCache;
        };

        public static final AnimationChannel.Interpolation EXPO = (pAnimationVecCache, pKeyframeDelta, pKeyframes, pCurrentKeyframeIdx, pNextKeyframeIdx, pScale) -> {
            Vector3f currentVec = pKeyframes[pCurrentKeyframeIdx].target();
            Vector3f nextVec = pKeyframes[pNextKeyframeIdx].target();
            float delta = (float)Math.pow(2.0F, 10.0F * (pKeyframeDelta - 1.0F));
            pAnimationVecCache.set(
                    Mth.lerp(delta, currentVec.x(), nextVec.x()) * pScale,
                    Mth.lerp(delta, currentVec.y(), nextVec.y()) * pScale,
                    Mth.lerp(delta, currentVec.z(), nextVec.z()) * pScale);
            return pAnimationVecCache;
        };

        public static final AnimationChannel.Interpolation CIRC = (pAnimationVecCache, pKeyframeDelta, pKeyframes, pCurrentKeyframeIdx, pNextKeyframeIdx, pScale) -> {
            Vector3f currentVec = pKeyframes[pCurrentKeyframeIdx].target();
            Vector3f nextVec = pKeyframes[pNextKeyframeIdx].target();
            float delta = -((float)Math.sqrt(1.0F - pKeyframeDelta * pKeyframeDelta) - 1.0F);
            pAnimationVecCache.set(
                    Mth.lerp(delta, currentVec.x(), nextVec.x()) * pScale,
                    Mth.lerp(delta, currentVec.y(), nextVec.y()) * pScale,
                    Mth.lerp(delta, currentVec.z(), nextVec.z()) * pScale);
            return pAnimationVecCache;
        };

        public static final AnimationChannel.Interpolation BACK = (pAnimationVecCache, pKeyframeDelta, pKeyframes, pCurrentKeyframeIdx, pNextKeyframeIdx, pScale) -> {
            Vector3f currentVec = pKeyframes[pCurrentKeyframeIdx].target();
            Vector3f nextVec = pKeyframes[pNextKeyframeIdx].target();

            final float s = 1.70158F;
            float delta = pKeyframeDelta - 1.0F;
            float pDelta = delta * delta * ((s + 1.0F) * delta + s) + 1.0F;
            pAnimationVecCache.set(
                    Mth.lerp(pDelta, currentVec.x(), nextVec.x()) * pScale,
                    Mth.lerp(pDelta, currentVec.y(), nextVec.y()) * pScale,
                    Mth.lerp(pDelta, currentVec.z(), nextVec.z()) * pScale);
            return pAnimationVecCache;
        };

        public static final AnimationChannel.Interpolation ELASTIC = (pAnimationVecCache, pKeyframeDelta, pKeyframes, pCurrentKeyframeIdx, pNextKeyframeIdx, pScale) -> {
            Vector3f currentVec = pKeyframes[pCurrentKeyframeIdx].target();
            Vector3f nextVec = pKeyframes[pNextKeyframeIdx].target();

            final float s = 1.70158F;
            final float p = 0.3F;
            float delta = pKeyframeDelta - 1.0F;
            float pDelta = (float)Math.pow(2.0F, -10.0F * delta) * (float)Math.sin((delta - p / (float)Math.PI * 2.0F / p) * ((float)Math.PI * 2.0F) / s) + 1.0F;
            pAnimationVecCache.set(
                    Mth.lerp(pDelta, currentVec.x(), nextVec.x()) * pScale,
                    Mth.lerp(pDelta, currentVec.y(), nextVec.y()) * pScale,
                    Mth.lerp(pDelta, currentVec.z(), nextVec.z()) * pScale);
            return pAnimationVecCache;
        };

        public static final AnimationChannel.Interpolation BOUNCE = (pAnimationVecCache, pKeyframeDelta, pKeyframes, pCurrentKeyframeIdx, pNextKeyframeIdx, pScale) -> {
            Vector3f currentVec = pKeyframes[pCurrentKeyframeIdx].target();
            Vector3f nextVec = pKeyframes[pNextKeyframeIdx].target();

            float delta = pKeyframeDelta;
            if (delta < 1.0F / 2.75F) {
                delta = 7.5625F * delta * delta;
            } else if (delta < 2.0F / 2.75F) {
                delta = 7.5625F * (delta -= 1.5F / 2.75F) * delta + 0.75F;
            } else if (delta < 2.5F / 2.75F) {
                delta = 7.5625F * (delta -= 2.25F / 2.75F) * delta + 0.9375F;
            } else {
                delta = 7.5625F * (delta -= 2.625F / 2.75F) * delta + 0.984375F;
            }

            pAnimationVecCache.set(
                    Mth.lerp(delta, currentVec.x(), nextVec.x()) * pScale,
                    Mth.lerp(delta, currentVec.y(), nextVec.y()) * pScale,
                    Mth.lerp(delta, currentVec.z(), nextVec.z()) * pScale);
            return pAnimationVecCache;
        };

        public static AnimationChannel.Interpolation power(int power) {
            final int clampedPower = Mth.clamp(power, 1, 25);
            return (pAnimationVecCache, pKeyframeDelta, pKeyframes, pCurrentKeyframeIdx, pNextKeyframeIdx, pScale) -> {
                Vector3f currentVec = pKeyframes[pCurrentKeyframeIdx].target();
                Vector3f nextVec = pKeyframes[pNextKeyframeIdx].target();
                float delta = pKeyframeDelta;
                for (int i = 1; i < clampedPower; i++) {
                    delta *= pKeyframeDelta;
                }

                pAnimationVecCache.set(
                        Mth.lerp(delta, currentVec.x(), nextVec.x()) * pScale,
                        Mth.lerp(delta, currentVec.y(), nextVec.y()) * pScale,
                        Mth.lerp(delta, currentVec.z(), nextVec.z()) * pScale);
                return pAnimationVecCache;
            };
        }

        public static AnimationChannel.Interpolation bezier(float point1X, float point1Y, float point2X, float point2Y) {
            return (pAnimationVecCache, pKeyframeDelta, pKeyframes, pCurrentKeyframeIdx, pNextKeyframeIdx, pScale) -> {
                Vector3f currentVec = pKeyframes[pCurrentKeyframeIdx].target();
                Vector3f nextVec = pKeyframes[pNextKeyframeIdx].target();

                float inverseDelta = 1.0F - pKeyframeDelta;
                float point0 = inverseDelta * inverseDelta * inverseDelta;
                float point1 = 3.0F * inverseDelta * inverseDelta * pKeyframeDelta;
                float point2 = 3.0F * inverseDelta * pKeyframeDelta * pKeyframeDelta;
                float point3 = pKeyframeDelta * pKeyframeDelta * pKeyframeDelta;
                float x = point0 * currentVec.x() + point1 * point1X + point2 * point2X + point3 * nextVec.x();
                float y = point0 * currentVec.y() + point1 * point1Y + point2 * point2Y + point3 * nextVec.y();
                float z = point0 * currentVec.z() + point1 * point1Y + point2 * point2Y + point3 * nextVec.z();
                pAnimationVecCache.set(x * pScale, y * pScale, z * pScale);
                return pAnimationVecCache;
            };
        }

        public static AnimationChannel.Interpolation bezier(Vector3f point1, Vector3f point2) {
            return bezier(point1.x(), point1.y(), point2.x(), point2.y());
        }
    }
}
