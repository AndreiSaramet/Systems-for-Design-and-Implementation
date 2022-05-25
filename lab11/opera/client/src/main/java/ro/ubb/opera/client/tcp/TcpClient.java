package ro.ubb.opera.client.tcp;

import ro.ubb.opera.common.service.Service;
import ro.ubb.opera.common.service.exceptions.ServiceException;
import ro.ubb.opera.common.tcp.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpClient {
    public TcpClient() {
    }

    public Message sendAndReceive(Message request) {
        try (Socket socket = new Socket(Service.HOST, Service.PORT);
             InputStream is = socket.getInputStream();
             OutputStream os = socket.getOutputStream()) {
            System.err.println("sending request: " + request);
            request.writeTo(os);
            System.err.println("request sent");

            Message response = new Message();
            response.readFrom(is);
            System.err.println("received response: " + response);

            return response;
        } catch (IOException e) {
            throw new ServiceException("TCP exception in send and receive", e);
        }
    }
}
