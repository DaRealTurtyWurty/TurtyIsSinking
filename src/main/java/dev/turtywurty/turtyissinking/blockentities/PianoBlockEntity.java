package dev.turtywurty.turtyissinking.blockentities;

import java.util.Optional;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import dev.turtywurty.turtyissinking.blockentities.base.SyncingBlockEntity;
import dev.turtywurty.turtyissinking.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PianoBlockEntity extends SyncingBlockEntity {
    private final MidiChannel midi;

    public PianoBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.PIANO.get(), pPos, pBlockState);

        this.midi = setMidi().orElseThrow(() -> new IllegalStateException("Unable to load midi instruments!"));
    }
    
    public MidiChannel getMidi() {
        return this.midi;
    }
    
    public Optional<MidiChannel> setMidi() {
        try {
            final Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            final Instrument piano = synth.getDefaultSoundbank().getInstruments()[0];
            synth.loadInstrument(piano);
            return Optional.ofNullable(synth.getChannels()[0]);
        } catch (final MidiUnavailableException exception) {
            return Optional.empty();
        }
    }
}
