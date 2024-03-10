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
                    String answer="";
                    switch (option) {
                        case "1":
                            s = (Student) inFromClient.readObject();
                             answer = sql.select(s.id);
                            if(answer.equals("student do not exist!")){
                                sql.insert_statement(s.id, s.name, s.phone);
                                outToClient.writeBytes("student added\n");
                            }else   {
                                outToClient.writeBytes("error, student may already exist\n");
                            }
                            break;
                        case "2":
                            s = (Student) inFromClient.readObject();
                             answer = sql.select(s.id);
                            outToClient.writeBytes(answer + "\n");
                            break;
                        case "3":
                            s = (Student) inFromClient.readObject();
                            answer = sql.select(s.id);
                            if(answer.equals("student do not exist!")){
                                outToClient.writeBytes(answer+"\n");
                            }else{
                                sql.delete_statement(s.id);
                                outToClient.writeBytes("student deleted\n");
                            }
                            break;
                        case "4":
                            s = (Student) inFromClient.readObject();
                            answer = sql.select(s.id);
                            if(answer.equals("student do not exist!")){
                                outToClient.writeBytes(answer+"\n");
                                break;
                            }else{
                                if (s.name == null && s.phone == null){
                                    outToClient.writeBytes("error, info can not be null\n");
                                    break;
                                }
                                if (s.name != null ) {
                                    sql.update_statement_name(s.id, s.name);
                                }
                                if (s.phone != null )
                                    sql.update_statement_phone(s.id, s.phone);
                            }
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
