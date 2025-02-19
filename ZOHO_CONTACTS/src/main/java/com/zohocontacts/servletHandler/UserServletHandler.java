package com.zohocontacts.servletHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zohocontacts.dboperation.SessionOperation;
import com.zohocontacts.dboperation.UserOperation;
import com.zohocontacts.dbpojo.EmailUser;
import com.zohocontacts.dbpojo.LoginCredentials;
import com.zohocontacts.dbpojo.UserData;
import com.zohocontacts.exception.DBOperationException;
import com.zohocontacts.loggerfiles.LoggerSet;
import com.zohocontacts.sessionstorage.CacheData;
import com.zohocontacts.sessionstorage.CacheModel;
import com.zohocontacts.sessionstorage.SessionCacheHandler;
import com.zohocontacts.sessionstorage.ThreadLocalStorage;
import com.zohocontacts.validation.UserValidation;

public class UserServletHandler {

	public static void addUserEmailRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-geneAddUserEmailServletrated method stub
		try {
			if ((request.getParameter("newemail") != null && !request.getParameter("newemail").isBlank())) {

				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();
				EmailUser email = new EmailUser();
				email.setEmailID(userData.getID());
				email.setEmail(request.getParameter("newemail"));
				email.setIsPrimary(false);
				email.setCreatedAt(Instant.now().toEpochMilli());
				email.setModifiedAt(email.getCreatedAt());
				email = UserOperation.addEmail(email, userData.getID());
				if (email != null) {
					EmailUser emailDB = new EmailUser();
					emailDB.setEmailID(userData.getID());
					List<EmailUser> emails = UserOperation.getAllEmail(emailDB);
					if (emails != null) {

						userData.setAllEmails(emails);
					}

					LoggerSet.logInfo("AddUserEmailServlet", "doPost",
							"email added successfully for user ID: " + userData.getID());
					response.sendRedirect("emails.jsp");
				} else {
					LoggerSet.logWarning("AddUserEmailServlet", "doPost",
							"Error in adding contact for user ID: " + userData.getID());
					request.setAttribute("errorMessage", "Error in adding contact");
					request.getRequestDispatcher("emails.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("AddUserEmailServlet", "doPost", "Parameter Data is empty!");
				request.setAttribute("errorMessage", "Parameter Data is empty!!");
				request.getRequestDispatcher("emails.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("AddUserEmailServlet", "doPost", "Exception occurred", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("emails.jsp").forward(request, response);
		}
	}

	public static void changeUserPasswordRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
			UserData userData = cacheModel.getUserData();
			if (((request.getParameter("password") != null && !request.getParameter("password").isBlank())
					&& (request.getParameter("Newpassword") != null
							&& !request.getParameter("Newpassword").isBlank()))) {

				UserData oldUser = userData;

				String oldPassword = request.getParameter("password");
				String newPassword = request.getParameter("Newpassword");

				if (UserOperation.userPasswordChange(oldUser, oldPassword, newPassword, userData.getID())) {

					response.sendRedirect("changePassword.jsp");
					LoggerSet.logInfo(" ChangeUserPasswordServlet", "doPost", "User Password Update successfully.");
				} else {
					LoggerSet.logWarning(" ChangeUserPasswordServlet", "doPost",
							"Failed to change Password for User ID: " + userData.getID());
					request.setAttribute("errorMessage",
							"Error while trying to update the User Password. Please try again.");
					request.getRequestDispatcher("changePassword.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning(" ChangeUserPasswordServlet", "doPost", "Input fields are empty.");
				request.setAttribute("errorMessage", "Input fields should not be empty!");
				request.getRequestDispatcher("changePassword.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError(" ChangeUserPasswordServlet", "doPost", "Exception occurred during Password Update", e);
			request.setAttribute("errorMessage", "An error occurred while processing your request.");
			request.getRequestDispatcher("changePassword.jsp").forward(request, response);
		}
	}

	public static void deleteUserEmailRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			if (request.getParameter("emailID") != null && !request.getParameter("emailID").isBlank()) {

				CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
				UserData userData = cacheModel.getUserData();
				int emailID = Integer.parseInt(request.getParameter("emailID"));

				EmailUser email = new EmailUser();
				email.setID(emailID);
				email.setEmailID(userData.getID());

				if (UserOperation.deleteUserEmail(email, userData.getID())) {
					EmailUser emailDB = new EmailUser();
					emailDB.setEmailID(userData.getID());
					List<EmailUser> emails = UserOperation.getAllEmail(emailDB);
					if (emails != null) {

						userData.setAllEmails(emails);
					}

					LoggerSet.logInfo("DeleteUserEmailServlet", "doPost", "email deleted successfully: " + emailID);
					response.sendRedirect("emails.jsp");
				} else {
					LoggerSet.logWarning("DeleteUserEmailServlet", "doPost", "Unable to delete email: " + emailID);
					request.setAttribute("errorMessage", "Unable to delete email");
					request.getRequestDispatcher("emails.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("DeleteUserEmailServlet", "doPost", "Email ID is null or empty");
				request.setAttribute("errorMessage", "Unable to delete Email because specified Email ID is null");
				request.getRequestDispatcher("emails.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("DeleteUserEmailServlet", "doPost", "Exception occurred while deleting Email", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("emails.jsp").forward(request, response);
		}
	}

	public static void userLoginRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			if ((request.getParameter("username") != null && !request.getParameter("username").isBlank())
					&& (request.getParameter("password") != null && !request.getParameter("password").isBlank())) {

				if (UserValidation.validateUserPassword(request.getParameter("password"))) {

					UserData userData = new UserData();
					userData = UserOperation.isUser(request.getParameter("username"), request.getParameter("password"));
					if (userData != null) {
						String sessionid = SessionOperation.generateSessionId(userData.getID());
						Cookie sessionCookie = new Cookie("SESSIONID", sessionid);
						sessionCookie.setHttpOnly(true);
						response.addCookie(sessionCookie);
						CacheModel cachemodel = CacheData.getCache(sessionid);
						cachemodel.setUserData(userData);

						LoggerSet.logInfo("LoginSignupServlet", "doPost",
								"User logged in successfully: " + userData.getName());
						response.sendRedirect("home.jsp");
					} else {
						LoggerSet.logWarning("LoginSignupServlet", "doPost",
								"Invalid username or password for user: " + request.getParameter("username"));
						request.setAttribute("errorMessage", "Invalid username and password");
						request.getRequestDispatcher("Login.jsp").forward(request, response);
					}
				} else {
					LoggerSet.logWarning("LoginSignupServlet", "doPost",
							"Password validation failed for user: " + request.getParameter("username"));
					request.setAttribute("errorMessage", "Password is too long or missing cases and numbers");
					request.getRequestDispatcher("Login.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("LoginSignupServlet", "doPost", "Username or password is empty");
				request.setAttribute("errorMessage", "Username and password should not be empty");
				request.getRequestDispatcher("Login.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("LoginSignupServlet", "doPost", "Exception occurred during login", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("Login.jsp").forward(request, response);
		}

	}

	public static void userLogoutRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HttpSession session = request.getSession(false);
			String sessionID = SessionOperation.getCustomSessionId(request.getCookies());
			String servletPath = "/sessioncache";
			if (session != null) {
				session.invalidate();
			}
			System.out.println("the size of the cache server data is" + CacheData.getServers());
			System.out.println("here the server befor delete port:" + CacheData.getServerInfo().getServerPort()
					+ "then the size of cache is:" + CacheData.getServers().size());
			SessionOperation.DeleteSessionData(sessionID);

			SessionCacheHandler.sendCacheDeleteRequest(CacheData.getServers(), servletPath, sessionID);
			Cookie sessionCookie = new Cookie("SESSIONID", null);
			sessionCookie.setMaxAge(0);
			sessionCookie.setPath("/");
			response.addCookie(sessionCookie);
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.sendRedirect("Login.jsp");
		} catch (Exception e) {
			LoggerSet.logError("LogoutServlet", "doGet", "Error during logout", e);
		}
	}

	public static void userSignupRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if ((request.getParameter("password") != null && !request.getParameter("password").isBlank())
					&& (request.getParameter("Name") != null && !request.getParameter("Name").isBlank())
					&& (request.getParameter("phone") != null && !request.getParameter("phone").isBlank())
					&& (request.getParameter("Address") != null && !request.getParameter("Address").isBlank())
					&& (request.getParameter("email") != null && !request.getParameter("email").isBlank())) {

				if (UserValidation.validateUserPassword(request.getParameter("password"))) {
					UserData userData = new UserData();
					LoginCredentials loginCredentials = new LoginCredentials();
					EmailUser emailUser = new EmailUser();
					userData.setCreatedAt(Instant.now().toEpochMilli());
					userData.setModifiedAt(userData.getCreatedAt());
					loginCredentials.setUserName(request.getParameter("username"));
					loginCredentials.setCreatedAt(userData.getCreatedAt());
					loginCredentials.setModifiedAt(userData.getModifiedAt());
					emailUser.setEmail(request.getParameter("email"));
					emailUser.setIsPrimary(true);
					emailUser.setCreatedAt(userData.getCreatedAt());
					emailUser.setModifiedAt(userData.getModifiedAt());
					userData.setName(request.getParameter("Name"));
					userData.setAddress(request.getParameter("Address"));

					userData.setPhoneno(request.getParameter("phone"));

					userData.setPassword(request.getParameter("password"));

					userData.setTimezone(request.getParameter("timezone"));
					userData.setEmail(emailUser);
					userData.setLoginCredentials(loginCredentials);

					userData = UserOperation.createUser(userData);

					if (userData != null) {

						String sessionID = SessionOperation.generateSessionId(userData.getID());
						Cookie sessionCookie = new Cookie("SESSIONID", sessionID);
						sessionCookie.setHttpOnly(true);
						response.addCookie(sessionCookie);
						CacheModel cachemodel = CacheData.getCache(sessionID);

						cachemodel.setUserData(userData);

						response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
						response.setHeader("Pragma", "no-cache");
						response.setDateHeader("Expires", 0);
						response.sendRedirect("home.jsp");

						LoggerSet.logInfo("SignupServlet", "doPost",
								"User signed up successfully: " + loginCredentials.getUserName());
					} else {
						LoggerSet.logWarning("SignupServlet", "doPost", "User creation failed.");
						request.setAttribute("errorMessage", "An error occurred while creating user");
						request.getRequestDispatcher("Signup.jsp").forward(request, response);
					}
				} else {
					LoggerSet.logWarning("SignupServlet", "doPost",
							"Password validation failed for user: " + request.getParameter("username"));
					request.setAttribute("errorMessage",
							"Password should contain at least one lower case, one upper case, and numbers.");
					request.getRequestDispatcher("Signup.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("SignupServlet", "doPost", "Required parameters are missing.");
				request.setAttribute("errorMessage", "Parameters should not be empty!");
				request.getRequestDispatcher("Signup.jsp").forward(request, response);
			}
		} catch (DBOperationException e) {
			LoggerSet.logError("SignupServlet", "doPost", "Exception occurred during signup", e);
			request.setAttribute("errorMessage", e);
			request.getRequestDispatcher("Signup.jsp").forward(request, response);
		}
	}

	public static void userUpdateRequestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
			UserData userData = cacheModel.getUserData();

			Boolean isSuccess = false;

			if ((request.getParameter("name") != null && !request.getParameter("name").isBlank())
					&& (request.getParameter("username") != null && !request.getParameter("username").isBlank())
					&& (request.getParameter("phone") != null && !request.getParameter("phone").isBlank())
					&& (request.getParameter("address") != null && !request.getParameter("address").isBlank())
					&& (request.getParameter("primaryemail") != null) && !request.getParameter("primaryemail").isBlank()
					&& (request.getParameter("timezone") != null) && !request.getParameter("timezone").isBlank()
					&& (request.getParameter("emailID") != null) && !request.getParameter("emailID").isBlank()
					&& (request.getParameter("logID") != null) && !request.getParameter("logID").isBlank()) {
				long userId = userData.getID();

				UserData user = new UserData();
				LoginCredentials loginCredentials = new LoginCredentials();
				EmailUser emailUser = new EmailUser();
				emailUser.setID(Integer.parseInt(request.getParameter("emailID")));

				emailUser.setEmail(request.getParameter("primaryemail"));
				emailUser.setModifiedAt(user.getModifiedAt());
				emailUser.setIsPrimary(true);

				loginCredentials.setID(Integer.parseInt(request.getParameter("logID")));
				loginCredentials.setUserID(userData.getID());
				loginCredentials.setUserName(request.getParameter("username"));
				loginCredentials.setModifiedAt(user.getModifiedAt());
				user.setLoginCredentials(loginCredentials);
				user.setModifiedAt(Instant.now().toEpochMilli());
				user.setID(userData.getID());
				user.setName(request.getParameter("name"));
				user.setAddress(request.getParameter("address"));
				user.setPhoneno(request.getParameter("phone"));
				user.setTimezone(request.getParameter("timezone"));
				isSuccess = UserOperation.userprofileUpdate(user, emailUser);
				if (isSuccess) {
					cacheModel.setUserData(UserOperation.getUserData(userId));

					response.sendRedirect("profile.jsp");
					LoggerSet.logInfo("UserProfileServlet", "doPost",
							"User profile updated successfully for User ID: " + user.getID());
				} else {
					LoggerSet.logWarning("UserProfileServlet", "doPost",
							"Error in updating profile data for User ID: " + user.getID());
					request.setAttribute("errorMessage", "Error in updating profile Data");
					request.getRequestDispatcher("profile.jsp").forward(request, response);
				}
			} else {
				LoggerSet.logWarning("UserProfileServlet", "doPost", "Parameter cannot be empty.");
				request.setAttribute("errorMessage", "Parameter cannot be empty.");
				request.getRequestDispatcher("profile.jsp").forward(request, response);
			}

		} catch (

		DBOperationException e) {
			LoggerSet.logError("UserProfileServlet", "doPost", "Exception occurred while processing user profile.", e);
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("profile.jsp").forward(request, response);
		}
	}

}
