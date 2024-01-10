package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;
    public Patient(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }
    public void addPatient(){
        System.out.println("Enter patient name");
        String name = scanner.next();
        System.out.println("Enter patient age");
        int age = scanner.nextInt();
        System.out.println("Enter patient gender");
        String gender = scanner.next();
        try {
            String query = "INSERT INTO patient(name, age, gender) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,"Raghavan");
            preparedStatement.setInt(2,34);
            preparedStatement.setString(3,"Male");
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows>0){
                System.out.println("Patient is added successfully");
            }
            else {
                System.out.println("Failed to add Patient!!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void viewPatient(){
        String query = "Select * from patient";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+--------+--------------------+--------------+---------------+");
            System.out.println("| Patient Id | Name           + Age          + Gender        +");
            System.out.println("+--------+--------------------+--------------+---------------+");

            while (resultSet.next()){
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            String gender = resultSet.getString("gender");
            System.out.printf("|%-12s|%-16s|%-14s|%-15s");
            System.out.println("+--------+--------------------+--------------+---------------+");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean getPatientById(int Id){
        String query = "SELECT * FROM patients WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }
            else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
