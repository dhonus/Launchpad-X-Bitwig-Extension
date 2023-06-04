package com.dhonus.launchpadx;

import com.bitwig.extension.controller.api.*;
import java.util.function.Consumer;

public class GridButton {

    private HardwareButton button;

    private MultiStateHardwareLight light;
    private int note;

    public GridButton(MidiIn midiIn, MidiOut midiOut, HardwareSurface hs, String name, int note, int i) {
        this.button = hs.createHardwareButton(name);
        this.light = hs.createMultiStateHardwareLight(name + "_Light");
        this.button.setBackgroundLight(this.light);
        this.button.setBounds(13 + 23 * 8, 13, 21, 21);
        this.note = note;

        final Consumer<Light> sendColor = new Consumer<Light>() {
            @Override
            public void accept(Light light) {
                midiOut.sendMidi(0x90, note, light.getState());
            }
        };

    }
}
