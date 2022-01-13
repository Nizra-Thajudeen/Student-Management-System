package studentManagement.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import studentManagement.bean.Student;
import studentManagement.dao.StudentDao;


//Servlet implementation class StudentServlet
@WebServlet("/")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   private StudentDao studentDao;

	
	// @see Servlet#init(ServletConfig) 
	public void init(ServletConfig config) throws ServletException {
		studentDao = new StudentDao();
	}
	
	//@see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

	// @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		
		try {
			switch(action) {
			
			case "/new":
				newForm(request, response);
				break;
				
			case "/insert":
				insertStudent(request, response);
				break;
				
			case "/delete":
				deleteStudent(request, response);
				break;
				
			case "/edit":
				editForm(request, response);
				break;
				
			case "/update":
				updatestudent(request, response);
				break;
				
			default:
				listStudent(request, response);
				break;
			
			}
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
		
	}
		
		//method if action is new
		private void newForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			RequestDispatcher dispatcher = request.getRequestDispatcher("student-form.jsp");
			dispatcher.forward(request, response);
		}
		
		//insert new students
		private void insertStudent(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ClassNotFoundException{
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			int age = Integer.parseInt(request.getParameter("age")); 
			Student newStudent = new Student(name, email, age);
			
			studentDao.insertStudent(newStudent);
			response.sendRedirect("list");
		}
		
		//delete students
		private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
			int id= Integer.parseInt(request.getParameter("id"));
			try {
				studentDao.deleteStudent(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.sendRedirect("list");
		}
		
		//shows the form for editing student details
		private void editForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException{
			int id= Integer.parseInt(request.getParameter("id"));
			
			Student existingStudent;
			
			try {
				existingStudent = studentDao.getStudentById(id);
				RequestDispatcher dispatcher= request.getRequestDispatcher("student-form.jsp");
				request.setAttribute("student", existingStudent);
				dispatcher.forward(request, response);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//update students
		private void updatestudent(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ClassNotFoundException{
			int id = Integer.parseInt(request.getParameter("id"));
			
			String name= request.getParameter("name");
			String email= request.getParameter("email");
			int age= Integer.parseInt(request.getParameter("age"));
			
			Student updatedStudent = new Student(id, name, email, age);
			studentDao.updateStudent(updatedStudent);
			response.sendRedirect("list");
		}
		
		//default 
		private void listStudent(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException{
			try {
				List<Student> listStudent= studentDao.getAllStudent();
				request.setAttribute("listStudent", listStudent);
				RequestDispatcher dispatcher = request.getRequestDispatcher("student-list.jsp");
				dispatcher.forward(request, response);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	


