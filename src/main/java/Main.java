import io.dronefleet.mavlink.Mavlink2Message;
import io.dronefleet.mavlink.MavlinkMessage;
import java.io.IOException;
import java.net.Socket;

public class Main {
  static final int SIM_PORT = 5760;
  static final int SYSTEM_ID = 255;
  static final int COMPONENT_ID = 190;

  public static void main(String[] args) throws IOException {
    // Create socket and subsequent connection manager class
    Socket socket = new Socket("127.0.0.1", SIM_PORT);
    MAVGod mavGod = new MAVGod(
        socket.getInputStream(), socket.getOutputStream(),
        SYSTEM_ID, COMPONENT_ID);

    // Send messages
    mavGod.sendHeartbeat();
    mavGod.requestHomePosition();
    System.out.println("Messages sent");

    // Receive messages
    MavlinkMessage message;
    while ((message = mavGod.connection.next()) != null) {
      if (message instanceof Mavlink2Message) { // MAVLink 2 message
        Mavlink2Message mavlink2Message = (Mavlink2Message) message;
        System.out.println("---MAVLink 2 Message Contents---");
        System.out.println(mavlink2Message.getPayload().toString());
      } else { // MAVLink 1 message
        System.out.println("---MAVLink 1 Message Contents---");
        System.out.println(message.getPayload().toString());
      }
    }
  }
}
