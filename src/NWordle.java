import java.io.InputStreamReader;

import controller.SyncController;
import model.GameLogic;

public class NWordle {
  public static void main(String[] args) {
    GameLogic logic = new GameLogic();
    SyncController controller = new SyncController(logic, new InputStreamReader(System.in), System.out);
    controller.execute();
  }
}
