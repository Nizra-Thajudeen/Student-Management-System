package studentManagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import studentManagement.bean.Student;

public class StudentDao {
 
	private String jdbcURL="jdbc:mysql://localhost:3306/userdb?userSSL=false";
	private String jdbcUsername="root";
	private String jdbcPassword="user";
	private String jdbcDriver="com.mysql.jdbc.Driver";
	
	private static final String INSERT_STUDENTS_SQL ="INSERT INTO Students"+" (name, email, age) VALUES "+"(?,?,?);";
	
	private static final String SELECT_STUDENT_BY_ID ="Select id, name, email,age from students where id=?";
	private static final String SELECT_ALL_STUDENTS = "select * from students";
	private static final String DELETE_STUDENT_SQL="delete from students where id=?;";
	private static final String UPDATE_STUDENTS_SQL ="update students set name=?, email=?, age=? where id=?;";
	
	public StudentDao() {}
	
	protected Connection getConnection() {
		Connection connection=null;
		try {
			Class.forName("jdbcDriver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	
	//insert students
	public void insertStudent(Student student) throws SQLException{
		System.out.println(INSERT_STUDENTS_SQL);
		try (Connection connection= getConnection();
				PreparedStatement ps=connection.prepareStatement(INSERT_STUDENTS_SQL)){
			ps.setString(1, student.getName());
			ps.setString(2, student.getEmail());
			ps.setInt(3, student.getAge());
			
			System.out.println(ps);
			ps.executeUpdate();
			
			} catch(SQLException e){
				printSQLException(e);
			//e.printStackTrace(); 
		}
	}
	
	//select student by id
	
	//select all students
	public List<Student> getAllStudent() throws SQLException{
		List<Student> studentList = new ArrayList<>();
		try {
			Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(SELECT_ALL_STUDENTS);
			ResultSet rs= ps.executeQuery();
			
			while(rs.next()) {
				Student st = new Student();
				
				//int id = rs.getInt("id");
				st.setId(rs.getInt("id"));
				st.setName(rs.getString("name"));
				st.setEmail(rs.getString("email"));
				st.setAge(rs.getInt("age"));
				
				//studentList.add(new Student(id, name, email, age));
				studentList.add(st);
				
			}
		} catch(SQLException e){
			printSQLException(e);
			//e.printStackTrace();  
		}
		return studentList;
		
		
	}
	
	//update student
	
	//delete student by id
	public boolean deleteStudent(int id) throws SQLException {
		boolean recordDeleted;
		try (
			Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(DELETE_STUDENT_SQL);)
			{
			ps.setInt(1, id);
			
			//boolean recordDeleted;
			
			recordDeleted= ps.executeUpdate()>0;	
				
		}
		return recordDeleted;
		
	}
	
	
	private void printSQLException(SQLException ex) {
		for(Throwable e : ex) {
			if(e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: "+ ((SQLException) e).getSQLState());
				System.err.println("Error Code: "+ ((SQLException) e).getErrorCode());
				System.err.println("Message: "+ ((SQLException) e).getMessage());
				Throwable t= ex.getCause();
				
				while(t != null) {
					System.out.println("Cause: "+t);
					t=t.getCause();
				}
			}
		}
	}
	
	
	
	
}
