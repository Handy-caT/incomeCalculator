package db;

import org.json.simple.JSONArray;
import wallet.money.WebApiJSON;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Scanner;


public class test {



    public static final String DB_URL = "jdbc:h2:/Users/maksim/IdeaProjects/incomeCalculator/db/test";
    public static final String DB_Driver = "org.sqlite.JDBC";

    public static void main(String[] args) throws IOException {
        /*
        try {
            Class.forName(DB_Driver); //Проверяем наличие JDBC драйвера для работы с БД
            Connection connection = DriverManager.getConnection(DB_URL);//соединениесБД
            System.out.println("Соединение с СУБД выполнено.");
            connection.close();       // отключение от БД
            System.out.println("Отключение от СУБД выполнено.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // обработка ошибки  Class.forName
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace(); // обработка ошибок  DriverManager.getConnection
            System.out.println("Ошибка SQL !");
        }*/
        WebApiJSON webApiJSON = WebApiJSON.getInstance();
        JSONArray array = webApiJSON.getCurrenciesJSONArray();

        File file = new File("json/testapi.json");

        FileWriter fileWriter = new FileWriter(file);
        array.writeJSONString(fileWriter);
        fileWriter.close();
    }

}
