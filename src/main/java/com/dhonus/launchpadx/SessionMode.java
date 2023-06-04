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
