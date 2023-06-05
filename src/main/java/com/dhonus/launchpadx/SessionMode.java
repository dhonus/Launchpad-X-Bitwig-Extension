package com.dhonus.launchpadx;

import com.bitwig.extension.controller.api.*;

public class SessionMode {

    private ControllerHost host;

    private HardwareSurface hs;

    TrackBank tb;

    private SessionLight[][] sl = new SessionLight[8][8];

    private GridButton[][] gridButtons = new GridButton[8][8];

    private void navButtons() {
        CCButton upButton = new CCButton(
                host.getMidiInPort(0),
                host.getMidiOutPort(0),
                hs,"SessionUpButton", 91);

        upButton.getButton().pressedAction().setActionMatcher(
                host.getMidiInPort(0).createCCActionMatcher(0, 79, 127)
        );
        upButton.getHardwareLight().state().setValue(new Light(3));

        CCButton downButton = new CCButton(
                host.getMidiInPort(0),
                host.getMidiOutPort(0),
                hs,"SessionDownButton", 92);

        downButton.getButton().pressedAction().setActionMatcher(
                host.getMidiInPort(0).createCCActionMatcher(0, 80, 127)
        );
        downButton.getHardwareLight().state().setValue(new Light(3));

        CCButton leftButton = new CCButton(
                host.getMidiInPort(0),
                host.getMidiOutPort(0),
                hs,"SessionLeftButton", 93);

        leftButton.getButton().pressedAction().setActionMatcher(
                host.getMidiInPort(0).createCCActionMatcher(0, 81, 127)
        );
        leftButton.getHardwareLight().state().setValue(new Light(3));

        CCButton rightButton = new CCButton(
                host.getMidiInPort(0),
                host.getMidiOutPort(0),
                hs,"SessionRightButton", 94);

        rightButton.getButton().pressedAction().setActionMatcher(
                host.getMidiInPort(0).createCCActionMatcher(0, 82, 127)
        );
        rightButton.getHardwareLight().state().setValue(new Light(3));
    }

    private void gridButtons() {
        /* The mappings for the array are:

            81 82 83 84 85 86 87 88 | CC 89
            71 72 73 74 75 76 77 78 | CC 79
            61 62 63 64 65 66 67 68 | CC 69
            51 52 53 54 55 56 57 58 | CC 59
            41 42 43 44 45 46 47 48 | CC 49
            31 32 33 34 35 36 37 38 | CC 39
            21 22 23 24 25 26 27 28 | CC 29
            11 12 13 14 15 16 17 18 | CC 19

         */
        for (int i = 0; i < 8; i++) {
            int note = 0x68 + i;
            for (int j = 0; j < 8; j++) {
                int cc = 0x10 + j;
                gridButtons[i][j] = new GridButton(
                        host.getMidiInPort(0),
                        host.getMidiOutPort(0),
                        hs,
                        "SessionButton_" + i + "_" + j,
                        note,
                        cc
                );
            }
        }

        // list all 8x8 tracks
        // for each of 8 scenes
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Track track = tb.getItemAt(j);
                track.exists().markInterested();
                // change light color based on track state
                host.println("Scene " + i + " - Track " + j + " is " + track.exists().get());

                ClipLauncherSlotBank slotBank = track.clipLauncherSlotBank();
                ClipLauncherSlot slot = slotBank.getItemAt(i);

                slot.hasContent().markInterested();

                host.println("Scene " + i + " - Track " + j + " has content " + slot.hasContent().get());

                }
            }

        /*
        for(int i = 0; i < 8; i++) {
            Scene scene = tb.sceneBank().getItemAt(i);
            sl[i] = new Light(3);
            int finalI = i;
            sceneLaunchActions[i] = host.createAction(() -> {
                scene.launch();
                scene.selectInEditor();
            }, () -> "Press Scene " + finalI);
        }
*/

    }

    public SessionMode(ControllerHost host, HardwareSurface hs, TrackBank tb) {
        this.host = host;
        this.hs = hs;
        this.tb = tb;
        navButtons();

        gridButtons();


    }

    private void redraw() {

    }
}
