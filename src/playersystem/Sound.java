package playersystem;

import java.io.File;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;


public class Sound {

    Sequence seq;
    Sequencer midi;
    boolean sign;

    public Sound() {
    }

    public void loadSound(String sounndname) {
        try {
            this.seq = MidiSystem.getSequence(new File(sounndname));
            this.midi = MidiSystem.getSequencer();
            this.midi.open();
            this.midi.setSequence(this.seq);
            this.midi.start();
            this.midi.setLoopCount(-1);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        this.sign = true;
    }

    public void mystop() {
        this.midi.stop();
        this.midi.close();
        this.sign = false;
    }

    public boolean isplay() {
        return this.sign;
    }


}
