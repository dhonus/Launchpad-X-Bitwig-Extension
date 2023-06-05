package com.dhonus.launchpadx;
import java.util.UUID;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

public class LaunchpadXExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("3ecbe8d7-717b-4cb7-97b6-dbc902cc8ceb");
   
   public LaunchpadXExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "LaunchpadX";
   }
   
   @Override
   public String getAuthor()
   {
      return "dhonus";
   }

   @Override
   public String getVersion()
   {
      return "0.1";
   }

   @Override
   public UUID getId()
   {
      return DRIVER_ID;
   }
   
   @Override
   public String getHardwareVendor()
   {
      return "Novation";
   }
   
   @Override
   public String getHardwareModel()
   {
      return "LaunchpadX";
   }

   @Override
   public int getRequiredAPIVersion()
   {
      return 17;
   }

   @Override
   public int getNumMidiInPorts()
   {
      return 2;
   }

   @Override
   public int getNumMidiOutPorts()
   {
      return 2;
   }

   @Override
   public void listAutoDetectionMidiPortNames(final AutoDetectionMidiPortNamesList list, final PlatformType platformType)
   {
      if (platformType == PlatformType.WINDOWS)
      {
         // TODO: Set the correct names of the ports for auto detection on Windows platform here
         // and uncomment this when port names are correct.
         //list.add(new String[]{"MIDIIN2 (LPX MIDI)"}, new String[]{"MIDIOUT2 (LPX MIDI)"});
         list.add(new String[]{"LPX MIDI", "MIDIIN2 (LPX MIDI)"}, new String[]{"LPX MIDI", "MIDIOUT2 (LPX MIDI)"});
      }
      else if (platformType == PlatformType.MAC)
      {
         // TODO: Set the correct names of the ports for auto detection on Windows platform here
         // and uncomment this when port names are correct.
         //list.add(new String[]{"Input Port 0"}, new String[]{"Output Port 0"});
         list.add(new String[]{"LPX MIDI", "MIDIIN2 (LPX MIDI)"}, new String[]{"LPX MIDI", "MIDIOUT2 (LPX MIDI)"});
      }
      else if (platformType == PlatformType.LINUX)
      {
         // TODO: Set the correct names of the ports for auto detection on Windows platform here
         // and uncomment this when port names are correct.
         //list.add(new String[]{"Input Port 0"}, new String[]{"Output Port 0"});
         list.add(new String[]{"LPX MIDI", "MIDIIN2 (LPX MIDI)"}, new String[]{"LPX MIDI", "MIDIOUT2 (LPX MIDI)"});
      }
   }

   @Override
   public LaunchpadXExtension createInstance(final ControllerHost host)
   {
      return new LaunchpadXExtension(this, host);
   }
}
