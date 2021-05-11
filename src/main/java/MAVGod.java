import io.dronefleet.mavlink.MavlinkConnection;
import io.dronefleet.mavlink.common.CommandLong;
import io.dronefleet.mavlink.common.Heartbeat;
import io.dronefleet.mavlink.common.MavAutopilot;
import io.dronefleet.mavlink.common.MavCmd;
import io.dronefleet.mavlink.common.MavState;
import io.dronefleet.mavlink.common.MavType;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MAVGod {
  private final int systemId;
  private final int componentId;
  public MavlinkConnection connection;

  public MAVGod(InputStream inputStream, OutputStream outputStream, int systemId, int componentId) {
    this.systemId = systemId;
    this.componentId = componentId;
    this.connection = MavlinkConnection.create(inputStream, outputStream);
  }

  public void sendHeartbeat() throws IOException {
    Heartbeat heartbeat = Heartbeat.builder()
        .type(MavType.MAV_TYPE_GCS)
        .autopilot(MavAutopilot.MAV_AUTOPILOT_INVALID)
        .systemStatus(MavState.MAV_STATE_UNINIT)
        .mavlinkVersion(3)
        .build();
    connection.send2(systemId, componentId, heartbeat);
  }

  public void returnToLaunch() throws IOException {
    CommandLong command = CommandLong.builder()
        .command(MavCmd.MAV_CMD_NAV_RETURN_TO_LAUNCH)
        .build();
    connection.send2(systemId, componentId, command);
  }

  public void requestHomePosition() throws IOException {
    CommandLong command = CommandLong.builder()
        .command(MavCmd.MAV_CMD_GET_HOME_POSITION)
        .build();
    connection.send2(systemId, componentId, command);
  }
}
