package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.*;

public class DaoStudent implements IDaoStudent{

    @Override
    public Student createStudent(String name, String password, String email) {
        return new Student(name, password, email);
    }

    @Override
    public Student createStudent(int userId, String name, String password, String email) {
        return new Student(userId, name, password, email);
    }


    @Override
    public Student importStudent(int studentId) {
        Student student = null;
        PreparedStatement preparedStatement = null;
        int roleId = getRoleID("student");

        String query = "SELECT * FROM users WHERE id_user = ? AND id_role = ?;";

        try {
            preparedStatement = DbConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, roleId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.isClosed()){

                int userId = resultSet.getInt("id_user");

                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                student = createStudent(userId, name, password, email);
                Wallet wallet = new DaoWallet().importWallet(studentId);
                student.setWallet(wallet);

                resultSet.close();
            }
            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return student;
        }

        return student;
    }

    @Override
    public Student importNewStudent(String userEmail){

        Student student = null;
        PreparedStatement preparedStatement = null;
        int roleId = getRoleID("student");

        String query = "SELECT * FROM users WHERE email = ? AND id_role = ?;";

        try {
            preparedStatement = DbConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, userEmail);
            preparedStatement.setInt(2, roleId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.isClosed()){

                int userId = resultSet.getInt("id_user");

                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");

                student = createStudent(userId, name, password, email);

                resultSet.close();
            }
            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return student;
        }

        return student;
    }


    @Override
    public boolean exportStudent(Student student) {

        String name = student.getName();
        String password = student.getPassword();
        String email = student.getEmail();
        int roleId = getRoleID("student");

        PreparedStatement preparedStatement = null;
        String query = "INSERT INTO users (name, password, email, id_role)" +
                "VALUES (?, ?, ?, ?);";

        try {
            preparedStatement = DbConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setInt(4, roleId);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;

        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean updateStudent(Student student) {
        String name = student.getName();
        String password = student.getPassword();
        String email = student.getEmail();
        int studentId = student.getUserId();
        int roleId = getRoleID("student");


        PreparedStatement preparedStatement = null;
        String query = "UPDATE users set name = ?, password = ?, email = ? WHERE id_user= ? AND id_role = ?;";

        try {
            preparedStatement = DbConnection.getConnection().prepareStatement(query);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setInt(4, studentId);
            preparedStatement.setInt(5, roleId);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;

        } catch (SQLException | ClassNotFoundException e) {
            return false;
        }
    }

    public int getRoleID(String roleName){

        int roleId = 0;
        PreparedStatement preparedStatement = null;

        String query = "SELECT id_role FROM roles WHERE name = ?;";

        try {
            preparedStatement = DbConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, roleName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.isClosed()) {
                roleId = resultSet.getInt("id_role");
                resultSet.close();
            }
            preparedStatement.close();

        }catch (SQLException | ClassNotFoundException e){
            System.out.println("Role not found");
        }

        return roleId;

    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        int roleId = getRoleID("student");
        String query = "SELECT id_user FROM users WHERE id_role = ?;";

        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, roleId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int userId = resultSet.getInt("id_user");
                Student student = importStudent(userId);
                students.add(student);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return students;
    }
    
}
