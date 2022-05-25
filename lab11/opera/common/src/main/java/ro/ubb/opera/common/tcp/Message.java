package ro.ubb.opera.common.tcp;

import java.io.*;
import java.util.Objects;

public class Message {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final String OK = "ok";

    public static final String ERROR = "error";

    private String header;

    private String body;

    public Message() {
    }

    public Message(String header, String body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return this.header;
    }

    public String getBody() {
        return this.body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Message message = (Message) o;
        return Objects.equals(header, message.header) && Objects.equals(body, message.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, body);
    }

    @Override
    public String toString() {
        return "Message" + "\n" +
                "HEADER\n" + this.header + "\n" +
                "BODY\n" + this.body + "\n";
    }

    public void readFrom(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        this.header = br.readLine();
        this.body = br.readLine();
    }

    public void writeTo(OutputStream os) throws IOException {
        os.write((this.header + LINE_SEPARATOR + this.body + LINE_SEPARATOR).getBytes());
    }
}
