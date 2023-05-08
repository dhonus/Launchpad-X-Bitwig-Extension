package com.dhonus.launchpadx;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.api.util.midi.SysexBuilder;
import com.bitwig.extension.controller.api.*;

import java.util.function.Consumer;

public class CCButton {
    private final int cc;
    private final HardwareButton button;
    private MultiStateHardwareLight c_light;
    private MidiIn midiIn;
    private MidiOut midiOut;

    CCButton(MidiIn midiIn, MidiOut midiOut, HardwareSurface hs, String name, int cc) {
        this.midiIn = midiIn;
        this.midiOut = midiOut;
        this.cc = cc;
        button = hs.createHardwareButton(name);
        c_light = hs.createMultiStateHardwareLight(name + "_Light");
        button.setBackgroundLight(c_light);
        button.setBounds(13 + 23 * 8, 13, 21, 21);

        final Consumer<Light> sendColor = new Consumer<Light>() {
            @Override
            public void accept(Light light) {
                midiOut.sendMidi(0xB0, cc, light.getState());
            }
        };

        c_light.state().onUpdateHardware(sendColor);
        /*c_light.state().onUpdateHardware(state -> {
            Light l_light = (Light)state;
            midiOut.sendMidi(0xB0, cc, 17);
        });*/
        c_light.setColorToStateFunction(color -> new Light(3));

        //HardwareActionMatcher onRelease = midiIn.createCCActionMatcher(0, cc, 127);
        //button.releasedAction().setActionMatcher(onRelease);
        /*c_light.state().onUpdateHardware(state -> {
            midiOut.sendMidi(0xB0, cc, 17);
        });*/
        //midiOut.sendMidi(0xB0, 98, 6);
        //c_light.state().setValue(l);
        //button.setBackgroundLight(c_light);
    }

    public HardwareButton getButton() {
        return this.button;
    }

    public MultiStateHardwareLight getHardwareLight() {
        return this.c_light;
    }
}
