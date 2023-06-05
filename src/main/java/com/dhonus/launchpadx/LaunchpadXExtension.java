package com.dhonus.launchpadx;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.api.*;
import com.bitwig.extension.controller.ControllerExtension;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class LaunchpadXExtension extends ControllerExtension
{
   private final String SYSEX_PREFIX = "F0 00 20 29 02 0C ";

   private Mode mixerMode = Mode.SESSION;
   private HardwareSurface hs;

   private CCButton logoButton;
   protected LaunchpadXExtension(final LaunchpadXExtensionDefinition definition, final ControllerHost host)
   {
      super(definition, host);
   }

   @Override
   public void init()
   {
      final ControllerHost host = getHost();

      mTransport = host.createTransport();
      host.getMidiInPort(0).setMidiCallback((ShortMidiMessageReceivedCallback)msg -> onMidi0(msg));
      host.getMidiInPort(0).setSysexCallback((String data) -> onSysex0(data));
      host.getMidiInPort(1).setMidiCallback((ShortMidiMessageReceivedCallback)msg -> onMidi1(msg));
      host.getMidiInPort(1).setSysexCallback((String data) -> onSysex1(data));

      host.getMidiOutPort(1).sendSysex("F0 7E 7F 06 01 F7");

      // Programmer mode
      //host.getMidiOutPort(1).sendSysex("F0 00 20 29 02 0C 0E 01 F7");
      //host.getMidiOutPort(1).sendSysex("F0 00 20 29 02 0C 03   00 63 0D   F7");

      // I turn on the DAW mode automatically. This sequence is reversible and IS reversed in the exit() method
      host.getMidiOutPort(1).sendSysex("F0 00 20 29 02 0C 10 01 F7");

      NoteInput noteIn = host.getMidiInPort(1).createNoteInput("My controller", "8?????", "9?????");

      hs = host.createHardwareSurface();
      hs.setPhysicalSize(241, 241);

      TrackBank tb = host.createTrackBank(8, 0, 8, true);
      tb.setSkipDisabledItems(true);

      tb.sceneBank().setIndication(true);

      Preferences prefs = host.getPreferences();
      BooleanValue mViewableBanks = prefs.getBooleanSetting("Viewable Bank?", "Behavior", true);

      mViewableBanks.addValueObserver(vb -> {
         tb.sceneBank().setIndication(vb);
      });

      /*for(int i = 0; i < tb.getCapacityOfBank(); i++) {
         tb.getItemAt(i).name().markInterested();
            tb.getItemAt(i).volume().markInterested();
            tb.getItemAt(i).volume().setIndication(true);
            tb.getItemAt(i).pan().markInterested();
            tb.getItemAt(i).pan().setIndication(true);
            tb.getItemAt(i).mute().markInterested();
      }*/

      ///////////////////////
      // LOGO light
      logoButton = new CCButton(
              host.getMidiInPort(0),
              host.getMidiOutPort(0),
              hs, "logoButton", 99);
      logoButton.getButton().pressedAction().setActionMatcher(
              host.getMidiInPort(0).createCCActionMatcher(0, 99, 0)
      );
      logoButton.getHardwareLight().state().setValue(new Light(17));

      ///////////////////////
      // RECORD button creation and binding
      CCButton captureMidiButton = new CCButton(
              host.getMidiInPort(0),
              host.getMidiOutPort(0),
              hs,"CaptureMidiButton", 98);

      captureMidiButton.getButton().pressedAction().setActionMatcher(
              host.getMidiInPort(0).createCCActionMatcher(0, 98, 127)
      );

      // add observer to the Recoding ON/OFF state in bitwig and change Capture MIDI button color accordingly
      captureMidiButton.getButton().pressedAction().addBinding(mTransport.recordAction());
      mTransport.isArrangerRecordEnabled().addValueObserver(arrangeRecordEnabled -> {
         if(arrangeRecordEnabled) {
            captureMidiButton.getHardwareLight().state().setValue(new Light(5));
         } else {
            captureMidiButton.getHardwareLight().state().setValue(new Light(7));
         }
      });
      //host.getMidiOutPort(0).sendSysex("F0 00 20 29 02 0C 03   00 0B 0D      F7");

      ///////////////////////
      // MIXER MODE button creation and binding
      CCButton mixerVolumeButton = new CCButton(
              host.getMidiInPort(0),
              host.getMidiOutPort(0),
              hs,"MixerVolumeButton", 89);

      mixerVolumeButton.getButton().pressedAction().setActionMatcher(
              host.getMidiInPort(0).createCCActionMatcher(0, 89, 127)
      );

      mixerVolumeButton.getHardwareLight().state().setValue(new Light(3));

      CCButton mixerPanButton = new CCButton(
              host.getMidiInPort(0),
              host.getMidiOutPort(0),
              hs,"MixerPanButton", 79);

      mixerPanButton.getButton().pressedAction().setActionMatcher(
              host.getMidiInPort(0).createCCActionMatcher(0, 79, 127)
      );

      mixerPanButton.getHardwareLight().state().setValue(new Light(3));

      SessionMode sm = new SessionMode(host, hs, tb);

      // color and prog mode off
      host.getMidiOutPort(1).sendSysex("F0 00 20 29 02 0C 03 00 63 35 F7");
      //host.getMidiOutPort(1).sendSysex("F0 00 20 29 02 0C 0E 00 F7");

      host.showPopupNotification("LaunchpadX Initialized");
      host.println("good");


   }

   @Override
   public void exit()
   {
      // here we perform a rollback to the Standalone mode. This turns off the Session mode
      getHost().getMidiOutPort(1).sendSysex("F0 00 20 29 02 0C 10 00 F7");
      getHost().showPopupNotification("LaunchpadX Exited");
   }

   @Override
   public void flush()
   {
      // TODO Send any updates you need here.
      hs.updateHardware();
   }

   /** Called when we receive short MIDI message on port 0. */
   private void onMidi0(ShortMidiMessage msg) 
   {
      // TODO: Implement your MIDI input handling code here.

      if (msg.isNoteOn()) {
         getHost().println("processing");
         getHost().println(msg.toString());
         return;
      }
      if (msg.isNoteOff()) {
         getHost().println("processingprocessing OFF");

      }
   }

   /** Called when we receive sysex MIDI message on port 0. */
   private void onSysex0(final String data) 
   {
      getHost().println(data);
      // MMC Transport Controls:
      if (data.equals("f07f7f0605f7"))
            mTransport.rewind();
      else if (data.equals("f07f7f0604f7"))
            mTransport.fastForward();
      else if (data.equals("f07f7f0601f7"))
            mTransport.stop();
      else if (data.equals("f07f7f0602f7"))
            mTransport.play();
      else if (data.equals("f07f7f0606f7"))
            mTransport.record();
   }

   /** Called when we receive short MIDI message on port 0. */
   private void onMidi1(ShortMidiMessage msg)
   {
      // TODO: Implement your MIDI input handling code here.

      if (msg.isNoteOn()) {
         getHost().println("processing");
         getHost().println(msg.toString());
         return;
      }
      if (msg.isNoteOff()) {
         getHost().println("processingprocessing OFF");

      }
   }

   /** Called when we receive sysex MIDI message on port 0. */
   private void onSysex1(final String data)
   {
      getHost().println(data);
      // MMC Transport Controls:
      if (data.equals("f07f7f0605f7"))
         mTransport.rewind();
      else if (data.equals("f07f7f0604f7"))
         mTransport.fastForward();
      else if (data.equals("f07f7f0601f7"))
         mTransport.stop();
      else if (data.equals("f07f7f0602f7"))
         mTransport.play();
      else if (data.equals("f07f7f0606f7"))
         mTransport.record();
   }

   private Transport mTransport;
}
