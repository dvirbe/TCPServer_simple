import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class socketHandler extends Thread {
    Socket incoming;
    Sql sql;

    socketHandler(Socket _in, Sql sql) {
        incoming = _in;
        this.sql = sql;
    }

    public void run() {
        String clientSentence;
        String capitalizedSentence = null;
        int num;
        int num2;
        try {


            ObjectInputStream inFromClient = new ObjectInputStream(incoming.getInputStream());

            DataOutputStream outToClient =
                    new DataOutputStream(incoming.getOutputStream());

            while (true) {
                try {
                    String option = (String) inFromClient.readObject();
                    Student s = null;
                    switch (option) {
                        case "1":
                            s = (Student) inFromClient.readObject();
                            sql.insert_statement(s.id, s.name, s.phone);
                            outToClient.writeBytes("it was ok everything here\n");
                            break;
                        case "2":
                            s = (Student) inFromClient.readObject();
                            String answer = sql.select(s.id);
                            outToClient.writeBytes(answer + "\n");
                            break;
                        case "3":
                            s = (Student) inFromClient.readObject();
                            sql.delete_statement(s.id);
                            outToClient.writeBytes("student deleted\n");
                            break;
                        case "4":
                            s = (Student) inFromClient.readObject();
                            sql.update_statement(s.id, s.name);
                            outToClient.writeBytes("student info updated\n");
                            break;
                    }
                } catch (ClassNotFoundException e) {
                    outToClient.writeBytes("it was not ok\n");
                }
            }
        } catch (IOException e) {

        }

    }
}
