/**
 * Project: BuyIt
 * Authors: Evan Law, Kenneth Carroll, Tsun (Jaeryn) Chu
 * Version: 0.0.1 (BETA)
 * Date: 2015/25/07
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class BuySell {
	//Variables
	static boolean loggedIn = false;

	public static void main(String[] args) 
	{
		final String URL = "jdbc:mysql://localhost:3306/db1";  //database URL
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Savepoint save = null; //maybe
		
		String command = "";
		String input = "";
		String uid = "";
		String pwd = "";
		String fname = "";
		String lname = "";
		String address = "";
		String city = "";
		String state = "";
		String email = "";
		String ccnum = "";
		String item_name = "";
		String item_desc = "";
		String item_price = "";
		boolean priceIsInt = false;
		
		int length = 5;
		
		Scanner user_input = new Scanner(System.in);
		
		try
		{
			//Connect to database
			System.out.printf("Connecting to database.......\n\n");
			conn = DriverManager.getConnection(URL, "root", "##WingGundam2015$$");
			if(conn != null)
			{
				System.out.printf("\t\t\tWelcome to BuyIt!!!\n");
				while(!loggedIn)
				{
					System.out.printf("[1] Create Account\t[2] Login\t[3] Exit\n\n");
					System.out.printf("Select an option: ");
					command = user_input.nextLine().trim();
					
					/*Case 1 - Register, Case 2 - Login, Case 3 - Exit*/
					switch(command)
					{
						case "1" : //REGISTER
								   System.out.printf("\nPlease enter a username: "); uid = user_input.nextLine().trim(); //query check
								   boolean taken = false;
								   
								   
									//CREATE USER
									stmt = conn.createStatement();
									rs = stmt.executeQuery("SELECT UID FROM CUSTOMER");
									while (rs.next())
									{
										if((rs.getString("uid")).equals(uid))
										{
											System.out.printf("Sorry, the UID - \"" + uid + "\" has already been taken. Please choose another.\n\n");
											taken = true;
										}
									}
									try
									{
										if(!taken)
										{
											//if query returns false (!exists username in db)
										    while(!loggedIn)
										    {
											    System.out.printf("Enter a password of length >= 12\nMust include at least one of each: uppercase, lowercase, number, special character: ");
											    pwd = user_input.nextLine(); 
										   
											    //Check if password meets requirements
											    if(checkPassword(pwd))
											    {
												    System.out.printf("Re-enter password: "); input = user_input.nextLine().trim();
												    loggedIn = input.equals(pwd) ? true : false;
												    /*loggedIn = input.equals(pwd) ? true : false;
												    if(loggedIn) System.out.printf("Congratulations, you are now logged in as %s, welcome to BuyIt!\n", uid);*/
											    }
											    
											    //Ask for personal information
											    System.out.printf("First name: ");
											    fname = user_input.nextLine();
											    
											    System.out.printf("Last name: ");
											    lname = user_input.nextLine();
											    
											    System.out.printf("Street address: ");
											    address = user_input.nextLine();
											    
											    System.out.printf("City: ");
											    city = user_input.nextLine();
											    
											    System.out.printf("State: ");
											    state = user_input.nextLine();
											    
											    System.out.printf("Email: ");
											    email = user_input.nextLine();
											    
											    System.out.printf("Credit Card Number: ");
											    ccnum = user_input.nextLine();
											    
											    if(checkCreditcard(ccnum))
											    {
											    	System.out.printf("Re-enter Credit Card Number: "); input = user_input.nextLine().trim();
												    
												    if(loggedIn) System.out.printf("Congratulations, you are now logged in as %s, welcome to BuyIt!\n", uid);
											    }
										    }											
											stmt = conn.createStatement();
											stmt.executeUpdate("INSERT INTO Customer " + "VALUES('" + uid + "', '" + pwd + "', '" + fname + "', '" + lname + "', '" + address + "', '" + city + "', '" + state + "', '" + email + "','" + ccnum + "')");
											conn.commit();
										}
									}
									catch (SQLException err)
									{
										err.printStackTrace();
									}
									finally
									{
										if(stmt != null) 
										{
											try
											{
												stmt.close();
											}
											catch (SQLException ex)
											{
												ex.printStackTrace();
											}
										}
										else if (rs != null)
										{
											try
											{
												rs.close();
											} 
											catch (SQLException exe) 
											{
												exe.printStackTrace();   
											}
										}
									}
								   
								   break;
						case "2" : //LOGIN
								   //CHECK IF USER EXISTS TO LOG THEM ON
								   try {
									   System.out.printf("Username: "); 
									   uid = user_input.nextLine().trim(); 
									   System.out.printf("Password: "); 
									   pwd = user_input.nextLine().trim();
									    
									   stmt = conn.createStatement();
									   rs = stmt.executeQuery("SELECT * FROM CUSTOMER WHERE uid ='" + uid + "' AND pwd = '" + pwd + "'");
									   if (!rs.next()) 
									   {
										   System.out.println("Error, username/password combination does not exist.\n");
									   }
									   else
									   {
										 uid = rs.getString("uid");
										 pwd = rs.getString("pwd");
										 loggedIn = true; //after this UID will be set and should not be changed to signify log in
										 System.out.printf("Log in successful, welcome %s!\n\n", uid);
										}
									}
									catch (SQLException a)
									{
										a.printStackTrace();
									}
									finally //handle errors
									{
										if(stmt != null)
										{
											try
										    {
										        stmt.close();
										    }
										    catch (SQLException b)
										    {
										        b.printStackTrace();
										    }
										}
										else if (conn != null)
										{
											try
											{
												conn.close();
											}
											catch (SQLException c)
											{
											    c.printStackTrace();
											}
										}
										else if (rs != null)
										{
						    				try
											{
						    					rs.close();
											}
											catch (SQLException d)
											{
											    d.printStackTrace();   
											}
										}
									}
								   
								   
								   
								   break;
						case "3" : user_input.close(); conn.close(); 
								   System.out.printf("################Connection to database closed, thank you for shopping with BuyIt################");
								   System.exit(0);
								   break;
						default : System.out.printf("Please enter a valid option\n"); break;
					}
				}
				
				//Logged in user segment
				while(loggedIn)
				{
					System.out.printf("[1] Sell\t[2] Search\t[3] Buy\t\t[4] Exit\n\nSelect an option: "); 
					command = user_input.nextLine().trim();
					
					switch(command)
					{
						case "1" : 
							long adId = 0;
							int count = 0;
							
							System.out.printf("--- Creating new ad ---\nPlease provide the item name, its description, and price\nItem for sale: ");
							item_name = user_input.nextLine().trim();
							System.out.printf("Item description: ");
							item_desc = user_input.nextLine();
							do //Check if price is a number
							{
 							System.out.printf("Price: $");
							item_price = user_input.nextLine();
							} while (!isPriceNumber(item_price));
							
							adId = randomNumber(length); 
							
							stmt = conn.createStatement();
							rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM PRODUCT");
							rs.next();
							count = rs.getInt("count");
						
							stmt = conn.createStatement();
							stmt.executeUpdate("INSERT INTO Product " + "VALUES('" + item_name  + "', '" + item_price + "', '" + item_desc + "', '" + adId + "',"+ " '" + uid + "', '" + pwd + "')");
							conn.commit();
							
							rs.close();
							stmt.close();
								
							System.out.println("Thank you, your ad has been posted!");
							System.out.println("Summary--");
							System.out.println("Order number: " + adId + "\nItem Name: " + item_name + "\nDescription: " + item_desc
									+ "\nPrice: $");
							
							break;
						case "2" : 
							break;
						case "3" :
							break;
						case "4" : user_input.close(); conn.close(); 
						   	System.out.printf("################Connection to database closed, thank you for shopping with BuyIt################");
						   	loggedIn = false;
						   	break;
						default : System.out.printf("Please enter a valid option\n"); break;
					}
					
				}
			}
		}
		catch (SQLException a)
		{
			a.printStackTrace();
		}
	}
	public static boolean checkPassword(String pw) 
	{
		return (pw.length() >= 12 && pw.length() < 25 && pw.matches(".*[A-Z].*") && pw.matches(".*[a-z].*" ) && pw.matches(".*\\d.*" ) && pw.matches(".*[`~!@#$%^&*()_+-={}:'<>,./].*")) ? true : false;
	}
	
	public static boolean isPriceNumber(String price)
	{
	    try
	    {
	        Integer.parseInt(price);
	    }
	    catch(NumberFormatException ex)
	    {
	    	System.out.println("Error, only numbers are allowed. Re-enter $ amount!");
	        return false;
	    }
	    return true;
	}
	public static boolean checkCreditcard(String ccnum)
	{
		try
		{
			return (ccnum.length() >= 12 && ccnum.length() <= 16) ? true : false;
		}
		catch(NumberFormatException ex)
		{
			System.out.println("Error, only numbers are allowed. Please re-enter credit card number!");
			return false;
		}
	}
	public static long randomNumber(int length)
	{
		Random random = new Random();
		char[] digits = new char[length];
		digits[0] = (char) (random.nextInt(9) + '1');
		for (int i = 1; i < length; i++)
		{
			digits[i] = (char) (random.nextInt(10) + '0');
		}
		return Long.parseLong(new String(digits));
	}

}
