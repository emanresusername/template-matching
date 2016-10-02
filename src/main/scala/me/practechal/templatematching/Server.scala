package me.practechal.templatematching

import java.io.{ BufferedReader, InputStreamReader, PrintWriter }
import java.net.ServerSocket
import scala.util.Try

object Server extends App {
  def processInput(line: String): Boolean = {
    val Array(full, part) = line.split("@")
    ImageTester.doesImageContainPart(full, part)
  }

  val serverSocket = new ServerSocket(args(0).toInt)
  val clientSocket = serverSocket.accept()
  val out = new PrintWriter(clientSocket.getOutputStream, true)
  val in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream))

  for {
    line ← Iterator.continually(in.readLine()).takeWhile(_ != null)
  } {
    Try {
      out.println(processInput(line))
 }  }
}
