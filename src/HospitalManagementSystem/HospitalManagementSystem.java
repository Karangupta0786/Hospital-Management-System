package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String userName = "root";
    private static final String password = "karan";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url,userName,password);
            Patient patient = new Patient(connection,scanner);
            Doctor doctor = new Doctor(connection);
            while (true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctor");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        //add patient
                        patient.addPatient();
                    case 2:
                        //view patient
                        patient.viewPatient();
                    case 3:
                        //view doctor
                        doctor.viewDoctor();
                    case 4:
                        //book appointment
                        bookAppointment(patient,doctor,connection,scanner);
                    case 5:
                        //exit
                        System.out.println("Thanks for visiting.");
                        return;
                    default:
                        System.out.println("please enter valid choice!!");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public static void bookAppointment(Patient patient, Doctor doctor,Connection connection, Scanner scanner){
        System.out.println("Enter Patient Id");
        int patientId = scanner.nextInt();
        System.out.println("Enter Doctor Id");
        int doctorId = scanner.nextInt();
        System.out.println("Enter appointment date (YYYY-MM-DD)");
        String appointmentDate = scanner.next();
        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if (checkDoctorAvailability(doctorId,appointmentDate, connection)){
                String appointmentQuery = "INSERT INTO APPOINTMENTS(patient_id,doctor_id,appointment_date) VALUES(?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentQuery);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected>0){
                        System.out.println("Appointment booked");
                    }else {
                        System.out.println("Failed to book Appointment!");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                System.out.println("Doctor is not available on this date!!");
            }
        }
        else {
            System.out.println("Either doctor or patient doesn't exist!!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId,String appointment_date,Connection connection){
        String query = "SELECT COUNT(*) FROM APPOINTMENT WHERE DOCTOR_ID = ? AND APPOINTMENT_DATE = ?";
        try {
         PreparedStatement preparedStatement = connection.prepareStatement(query);
         preparedStatement.setString(1,"doctor_id");
         preparedStatement.setString(2,"appointment_date");
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()){
             int count = resultSet.getInt(1);
             if (count==0){
                 return true;
             }
             else {
                 return false;
             }
         }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
