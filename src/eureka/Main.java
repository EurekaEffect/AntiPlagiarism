package eureka;

import eureka.gui.Gui;
import eureka.utils.Compare;
import eureka.utils.Output;
import eureka.utils.Transfer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static Compare compare = new Compare();
    public static Transfer transfer = new Transfer();

    public static final Logger log = Logger.getLogger("Logger");
    public static final Output output = new Output();

    public static void main(String[] args) {
        try {
            output.init();
        } catch (IOException e) {
            e.fillInStackTrace();
        }

        new Gui(456, 280);
        output.loadConfig();

        Runtime.getRuntime().addShutdownHook(new Thread(output::close));
    }

    public static void debug(Level level, String text) {
        if (!Gui.debug.isSelected()) return;
        log.log(level, text);
        output.write("debug.txt", text);
    }
}
