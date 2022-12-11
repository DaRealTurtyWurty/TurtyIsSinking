package dev.turtywurty.turtyissinking.client.screens.widgets;

import java.util.Optional;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class MusicWidget extends AbstractWidget {
    protected MidiChannel midi;
    protected Instrument instrument;
    protected int synthChannelIndex;
    protected final byte noteId;
    protected byte velocity;

    protected MusicWidget(Instrument instrument, int synthChannelIndex, byte noteId, byte velocity, int pX, int pY,
        int pWidth, int pHeight) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        setInstrument(instrument, synthChannelIndex);
        this.noteId = noteId < 0 ? 0 : noteId;
        setVelocity(velocity);
    }

    protected MusicWidget(MidiChannel midi, byte noteId, byte velocity, int pX, int pY, int pWidth, int pHeight) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        setMidiChannel(midi);
        this.noteId = noteId < 0 ? 0 : noteId;
        setVelocity(velocity);
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {
        defaultButtonNarrationText(pNarrationElementOutput);
    }

    protected void play() {
        this.midi.noteOn(this.noteId, this.velocity);
    }

    protected void setInstrument(Instrument instrument, int synthChannelIndex) {
        if (this.instrument != null && (this.instrument.equals(instrument)
            || this.synthChannelIndex == synthChannelIndex || this.synthChannelIndex < 0))
            return;

        this.instrument = instrument;
        this.synthChannelIndex = synthChannelIndex;
        this.midi = setupMidi(instrument, synthChannelIndex)
            .orElseThrow(() -> new IllegalStateException("Unable to load midi instrument!"));
    }

    protected void setMidiChannel(MidiChannel midiChannel) {
        this.midi = midiChannel;
        this.instrument = null;
        this.synthChannelIndex = 0;
    }

    protected void setVelocity(byte velocity) {
        if (this.velocity == velocity || velocity < 0)
            return;

        this.velocity = velocity;
    }

    protected void stop() {
        this.midi.noteOff(this.noteId, this.velocity);
    }

    protected static Optional<MidiChannel> setupMidi(Instrument instrument, int synthChannelIndex) {
        if (instrument == null || synthChannelIndex < 0)
            return Optional.empty();

        try {
            final Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            synth.loadInstrument(instrument);
            return Optional.ofNullable(synth.getChannels()[synthChannelIndex]);
        } catch (final MidiUnavailableException exception) {
            return Optional.empty();
        }
    }
}
