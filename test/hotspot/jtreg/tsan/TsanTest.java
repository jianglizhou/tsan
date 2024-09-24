/* @test TsanTest
 * @library /test/lib
 * @build TsanRunner
 * @run main TsanTest
 */

import java.io.IOException;
import jdk.test.lib.process.OutputAnalyzer;
import jdk.test.lib.process.ProcessTools;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TsanTest {
  public static void main(String[] args) throws IOException {
    TsanRunner.runTsanTestExpectSuccess(ServerSocketStreamLoopRunner.class);
  }
}

class ServerSocketStreamLoopRunner {
  public static void main(String[] args) throws Exception {
    Socket client;
    Socket server;
    try (ServerSocket serverSocket = new ServerSocket(0)) {
      Future<Socket> serverFuture = Executors.newCachedThreadPool().submit(serverSocket::accept);
      client = new Socket();
      client.connect(serverSocket.getLocalSocketAddress());
      server = serverFuture.get();
    }

    class State {
      int i;
    }

    State s = new State();
    new Thread(() -> {
      try {
        InputStream is = server.getInputStream();
        OutputStream os = server.getOutputStream();
        int b = is.read();
        s.i++;
        os.write(b);
        os.flush();
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }).start();
    InputStream is = client.getInputStream();
    OutputStream os = client.getOutputStream();
    s.i++;
    os.write(1);
    os.flush();
    is.read();
    s.i++;
  }
}
