import java.net.{ ServerSocket, SocketException, SocketTimeoutException, Socket }
import java.io.{ PrintWriter, BufferedReader, InputStreamReader, OutputStreamWriter }

object EchoServer {
  var port = 1320

  def run() {
    val serverSocket = new ServerSocket(port)

    def process( socket: Socket) {
      val bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream))
      var line = ""
      var msg = ""
        do {
          line = bufferedReader.readLine()
          msg += line + "\n"
        } while (line != "")

      val out: PrintWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream))
      out.println(msg)
      out.flush()
    }

    def loop() {
      while (true) {
        val socket = serverSocket.accept()
        process(socket)
        socket.close()
      }
    }
    loop()
  }

  def main(args: Array[String]) {

    if(args.length > 0) {
      val arglist = args.toList
      port = arglist(0).toInt
    }
    run()
  }

}
