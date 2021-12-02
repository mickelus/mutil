package se.mickelus.mutil.gui.animation;

import java.util.function.Consumer;

public class AnimationChain implements GuiAnimation {

    private final KeyframeAnimation[] animations;
    private KeyframeAnimation activeAnimation;

    private boolean looping = false;
    private Consumer<Boolean> stopHandler;

    private boolean isActive;

    public AnimationChain(KeyframeAnimation ... animations) {
        this.animations = animations;

        for (int i = 0; i < animations.length; i++) {
            final int index = i;
            animations[i].onStop(isActive -> {
                if (isActive) {
                    startNext(index);
                } else if (stopHandler != null) {
                    stopHandler.accept(false);
                }
            });
        }
    }

    public AnimationChain setLooping(boolean looping) {
        this.looping = looping;
        return this;
    }

    public AnimationChain onStop(Consumer<Boolean> handler) {
        stopHandler = handler;
        return this;
    }

    public void stop() {
        isActive = false;
        if (activeAnimation != null) {
            activeAnimation.stop();
        }
    }

    public void start() {
        isActive = true;
        activeAnimation = animations[0];
        activeAnimation.start();
    }

    private void startNext(int currentIndex) {
        if (isActive) {
            if (currentIndex + 1 >= animations.length) {
                if (looping) {
                    activeAnimation = animations[0];
                } else {
                    if (stopHandler != null) {
                        stopHandler.accept(true);
                    }
                    activeAnimation = null;
                    isActive = false;
                }
            } else {
                activeAnimation = animations[currentIndex + 1];
            }

            if (activeAnimation != null) {
                activeAnimation.start();
            }
        }
    }
}
