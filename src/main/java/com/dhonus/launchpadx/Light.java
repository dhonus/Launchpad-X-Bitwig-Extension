package com.dhonus.launchpadx;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.controller.api.HardwareLightVisualState;
import com.bitwig.extension.controller.api.InternalHardwareLightState;

public class Light extends InternalHardwareLightState {

    private int color = 3;
    @Override
    public HardwareLightVisualState getVisualState() {
        return HardwareLightVisualState.createForColor(Color.nullColor());
        /*return HardwareLightVisualState.createBlinking(
                c,
                Color.mix(c, Color.nullColor(), 0.7),
                60.0 / 5,
                60.0 / 5
        );*/
    }

    @Override
    public boolean equals(Object o) {
        if(o != null && o.getClass() == Light.class) {
            Light other = (Light)o;
            return color == other.color;
        } else {
            return false;
        }
    }

    Light(int baseColor) {
        super();
        color = baseColor;
    }


    public int getState() {
        return color;
    }
}
