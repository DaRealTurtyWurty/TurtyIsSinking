package dev.turtywurty.turtyissinking.client.screens;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import javax.sound.midi.MidiChannel;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.blockentities.PianoBlockEntity;
import dev.turtywurty.turtyissinking.client.screens.widgets.MusicWidget;
import dev.turtywurty.turtyissinking.init.BlockEntityInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ForgeSlider;

// TODO: Figure out why it plays the button press sound when left clicking
public class PianoScreen extends Screen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TurtyIsSinking.MODID,
        "textures/gui/piano.png");

    private int leftPos, topPos, imageWidth, imageHeight;
    private final PianoBlockEntity piano;
    private final List<PianoKey.Note> added = new ArrayList<>();
    private ForgeSlider volumeSlider;

    private PianoScreen(BlockPos pos) {
        super(Component.empty());
        this.minecraft = Minecraft.getInstance();
        this.piano = this.minecraft.level.getBlockEntity(pos, BlockEntityInit.PIANO.get()).orElseThrow(
            () -> new IllegalStateException("Piano screen was opened without piano block entity present!"));
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        for (final GuiEventListener listener : children()) {
            listener.keyReleased(pKeyCode, pScanCode, pModifiers);
        }

        return super.keyReleased(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        for (final GuiEventListener listener : children()) {
            if (listener.isMouseOver(pMouseX, pMouseY)) {
                listener.mouseClicked(pMouseX, pMouseY, pButton);
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
    
    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        for (final GuiEventListener listener : children()) {
            if (listener.isMouseOver(pMouseX, pMouseY)) {
                listener.mouseReleased(pMouseX, pMouseY, pButton);
            }
        }

        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        blit(pPoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        final AtomicReference<PianoKey> black = new AtomicReference<>();
        final AtomicReference<PianoKey> white = new AtomicReference<>();

        getKeys().filter(key -> key.isMouseOver(pMouseX, pMouseY)).forEach(key -> {
            if (PianoKey.isBlack(key.note)) {
                black.set(key);
            } else {
                white.set(key);
            }
        });

        final PianoKey key = black.get() == null ? white.get() : black.get();
        if (key == null)
            return;

        renderTooltip(pPoseStack, Component.literal(key.note.name()), pMouseX, pMouseY);
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        final MidiChannel midi = this.piano.getMidi();
        this.added.clear();
        PianoKey.NOTES.stream().sorted(Comparator.comparing(PianoKey::isBlack))
            .forEach(note -> addRenderableWidget(PianoKey.createKey(this, midi, note.name())));

        this.volumeSlider = addRenderableWidget(new ForgeSlider(this.leftPos + 8, this.topPos + 48,
            this.imageWidth - 12, 20, Component.empty(), Component.literal("%"), 0, 100, 50, true) {
            @Override
            public void applyValue() {
                getKeys().forEach(key -> key.setVelocity(mapVelocity(this.value)));
            }

            private static double mapValue(double value, double inputMin, double inputMax, double outputMin,
                double outputMax) {
                return (value - inputMin) / (inputMax - inputMin) * (outputMax - outputMin) + outputMin;
            }
            
            private static byte mapVelocity(double value) {
                return (byte) mapValue(value, 0, 1, 0, 127);
            }
        });
    }

    private Stream<PianoKey> getKeys() {
        return this.renderables.stream().filter(PianoKey.class::isInstance).map(PianoKey.class::cast);
    }

    public static void open(BlockPos pos) {
        Minecraft.getInstance().setScreen(new PianoScreen(pos));
    }

    public static class PianoKey extends MusicWidget {
        private static final List<Note> NOTES = Arrays.asList(new Note("C", InputConstants.KEY_Q, (byte) 60),
            new Note("C#", InputConstants.KEY_A, (byte) 61), new Note("D", InputConstants.KEY_W, (byte) 62),
            new Note("D#", InputConstants.KEY_S, (byte) 63), new Note("E", InputConstants.KEY_E, (byte) 64),
            new Note("F", InputConstants.KEY_R, (byte) 65), new Note("F#", InputConstants.KEY_D, (byte) 66),
            new Note("G", InputConstants.KEY_T, (byte) 67), new Note("G#", InputConstants.KEY_F, (byte) 68),
            new Note("A", InputConstants.KEY_Y, (byte) 69), new Note("A#", InputConstants.KEY_G, (byte) 70),
            new Note("B", InputConstants.KEY_U, (byte) 71), new Note("C", InputConstants.KEY_I, (byte) 72),
            new Note("C#", InputConstants.KEY_H, (byte) 73), new Note("D", InputConstants.KEY_O, (byte) 74),
            new Note("D#", InputConstants.KEY_J, (byte) 75), new Note("E", InputConstants.KEY_P, (byte) 76));

        private final Note note;
        private final PianoScreen screen;

        private PianoKey(PianoScreen screen, MidiChannel midi, Note note, byte volume, int x, int y, int width,
            int height) {
            super(midi, note.noteId(), volume, x, y, width, height);
            this.screen = screen;
            this.note = note;
        }

        @Override
        public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
            if (pKeyCode == this.note.keyCode()) {
                play();
            }

            if (pKeyCode == InputConstants.KEY_K) {
                for (byte b = 0; b < 127; b++) {
                    this.midi.noteOn(b, this.velocity);
                }
            }

            return super.keyReleased(pKeyCode, pScanCode, pModifiers);
        }

        @Override
        public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
            if (isOverThis(pMouseX, pMouseY)) {
                play();
            }

            return super.mouseReleased(pMouseX, pMouseY, pButton);
        }

        @Override
        public void playDownSound(SoundManager pHandler) {

        }

        @Override
        public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            RenderSystem.setShaderTexture(0, TEXTURE);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            final boolean pressed = isKeyDown(this.note.keyCode()) || isClicking(pMouseX, pMouseY);

            blit(pPoseStack, this.x, this.y, isWhite(this.note) ? 176 : 192, pressed ? this.height : 0, this.width,
                this.height);

            if (isWhite(this.note)) {
                this.screen.font.draw(pPoseStack, this.note.name(),
                    this.x + this.width / 2 - this.screen.font.width(this.note.name()) / 2,
                    this.y + this.height - this.screen.font.lineHeight, 0x202020);
            }
        }

        @Override
        public void setVelocity(byte velocity) {
            super.setVelocity(velocity);
        }

        private boolean isClicking(double mouseX, double mouseY) {
            final boolean pressed = glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(),
                GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS;
            return isOverThis(mouseX, mouseY) && pressed;
        }

        private boolean isOverThis(double mouseX, double mouseY) {
            if (!isMouseOver(mouseX, mouseY))
                return false;

            if (isBlack(this.note))
                return true;

            final Stream<PianoKey> keys = this.screen.getKeys().filter(key -> key.isMouseOver(mouseX, mouseY));
            return keys.noneMatch(key -> isBlack(key.note));
        }

        private static PianoKey createKey(PianoScreen screen, MidiChannel midi, String noteName) {
            final Note note = getNote(screen, noteName).orElseThrow(() -> new IllegalStateException(
                "Piano noteId was provided with an invalid note name: '" + noteName + "'!"));
            screen.added.add(note);
            final boolean isBlack = isBlack(note);
            return new PianoKey(screen, midi, note, (byte) 90, screen.leftPos + 8 + getXPos(note), screen.topPos + 8,
                isBlack ? 8 : 16, isBlack ? 16 : 32);
        }

        private static Optional<Note> getNote(PianoScreen screen, String noteName) {
            final List<Note> copy = new ArrayList<>(NOTES);
            screen.added.forEach(copy::remove);
            return copy.stream().filter(note -> note.name().equals(noteName)).findFirst();
        }

        private static int getXPos(Note note) {
            return switch (note.noteId()) {
                case 60 -> 0;
                case 61 -> 12;
                case 62 -> 16;
                case 63 -> 28;
                case 64 -> 32;
                case 65 -> 48;
                case 66 -> 60;
                case 67 -> 64;
                case 68 -> 76;
                case 69 -> 80;
                case 70 -> 92;
                case 71 -> 96;
                case 72 -> 112;
                case 73 -> 124;
                case 74 -> 128;
                case 75 -> 140;
                case 76 -> 144;
                default -> throw new IllegalArgumentException("Unexpected value: " + note.name());
            };
        }

        private static boolean isBlack(Note note) {
            return note.name().endsWith("#");
        }

        private static boolean isKeyDown(int keyCode) {
            return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keyCode);
        }

        private static boolean isWhite(Note note) {
            return !isBlack(note);
        }

        private static record Note(String name, int keyCode, byte noteId) {

        }
    }
}