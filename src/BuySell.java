import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Evan Law, Kenneth Carroll, Tsun Chu
 *
 */

public class BuySell {
	//Variables
	private static boolean loggedIn = false;

	public static void main(String[] args) 
	{
		final String URL = "jdbc:mysql://localhost:3306/db1";  //database URL
		Connection conn;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String command;
		String input;
		String uid = "";
		String pwd = "";
		String item_name;
		String item_desc;
		String item_price;
		
		Scanner user_input = new Scanner(System.in);
		
		try
		{
			//Connect to database
			System.out.printf("Connecting to database.......\n\n");
			conn = DriverManager.getConnection(URL, "root", "powerful1");
			if(conn != null)
			{
				conn.setAutoCommit(false);
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
								   /***CHANGE HERE 1***/
								   	stmt = conn.prepareStatement("SELECT UID FROM CUSTOMER WHERE UID = ?");
								   	stmt.setString(1, uid);
								   	rs = stmt.executeQuery();
									/*stmt = conn.createStatement();
									rs = stmt.executeQuery("SELECT UID FROM CUSTOMER");*/
									while (rs.next())
									{
										//if((rs.getString("uid")).equals(uid))
										//{
										System.out.printf("Sorry, the UID - \"" + uid + "\" has already been taken. Please choose another.\n\n");
										taken = true;
										//}
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
												    loggedIn = input.equals(pwd);
												    if(loggedIn) System.out.printf("Congratulations, you are now logged in as %s, welcome to BuyIt!\n", uid);
											    }
										    }			
										    /**CHANGE HERE 2 **/
										   	stmt = conn.prepareStatement("INSERT INTO CUSTOMER VALUES(?,?)");
										   	stmt.setString(1, uid);
										   	stmt.setString(2, pwd);
										   	stmt.executeUpdate();
											//stmt = conn.createStatement();
											//stmt.executeUpdate("INSERT INTO Customer " + "VALUES('" + uid + "', '" + pwd + "')");
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
									    
									   //stmt = conn.createStatement();
									   /**CHANGE HERE 3**/
									   stmt = conn.prepareStatement("SELECT * FROM CUSTOMER WHERE uid = ? AND pwd = ?");
									   stmt.setString(1, uid);
									   stmt.setString(2, pwd);
									   rs = stmt.executeQuery();
									   //rs = stmt.executeQuery("SELECT * FROM CUSTOMER WHERE uid ='" + uid + "' AND pwd = '" + pwd + "'");
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
								   /**CHANGE HERE 7 - finally block closes stmt and connection which causes error when we try to use later..**/
									/*finally //handle errors
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
									}*/
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
						case "1" : String adId; //SELL
								   String search;
								   int count;
									
      						       System.out.printf("--- Creating new ad ---\nPlease provide the item name, its description, and price\nItem for sale: ");
								   item_name = user_input.nextLine().trim();
								   System.out.printf("Item description: ");
								   item_desc = user_input.nextLine();
								   do //Check if price is a number
								   {
		 						   System.out.printf("Price: $");
								   item_price = user_input.nextLine();
								   } while (!isPriceNumber(item_price));
									
								   //stmt = conn.createStatement();
								   rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM PRODUCT");
								   rs.next();
								   count = rs.getInt("count");
								   adId = "" + count; 	
									
								   /**Insert ad into database**/
								   //stmt = conn.createStatement();
								   
								   /**CHANGE HERE 4**/
								   PreparedStatement sell_stmt = conn.prepareStatement("INSERT INTO Product VALUES(?, ?, ?, ?, ?, ?)");
								   sell_stmt.setString(1, adId);
								   sell_stmt.setString(2, item_name);
								   sell_stmt.setString(3, item_price);
								   sell_stmt.setString(4, item_desc);
								   sell_stmt.setString(5, uid);
								   sell_stmt.setString(6, pwd);
								   sell_stmt.executeUpdate();
								   
								   /*stmt.executeUpdate("INSERT INTO Product " + "VALUES('" + adId  + "', '" + item_name + "', '" + item_price + "', '" + item_desc + "',"
											+ " '" + uid + "', '" + pwd + "')");*/
								   conn.commit();
								   rs.close();
											
								   System.out.println("Thank you, your ad has been posted!\n");
								   System.out.println("Summary--");
								   System.out.println("Order number: " + adId + "\nItem Name: " + item_name + "\nDescription: " + item_desc
											+ "\nPrice: $" + item_price);
								   System.out.println();
								   break;
						case "2" : String ad_ID; //SEARCH
								   String seller;
								   String search_item;
								   String search_price;
								   String search_desc;
								   
								   System.out.print("Enter description of the item you are searching for: ");
						           search = user_input.nextLine().trim();
			                      
			                       //stmt = conn.createStatement();
						           
						           /**CHANGE HERE 5**/
								   PreparedStatement search_stmt = conn.prepareStatement("SELECT * FROM PRODUCT WHERE UPPER(prod_description)LIKE UPPER(?) OR UPPER(prod_name)LIKE UPPER(?) ");
								   search_stmt.setString(1, "%"+search+"%");
								   search_stmt.setString(2, "%"+search+"%");
								   rs = search_stmt.executeQuery();
								   
								   
			                       //rs = stmt.executeQuery ("SELECT * FROM PRODUCT WHERE UPPER(prod_description)LIKE UPPER('%" + search + "%') OR UPPER(prod_name)LIKE UPPER('%" + search + "%') ");
			                       System.out.printf("%-30.30s %-30.30s %-30.30s %-30.30s %-30.30s\n", "Ad ID", "Item", "Seller", "Price", "Description");
			                       while(rs.next())
			                       {
			                           ad_ID = rs.getString("PROD_ID");
			                           search_item = rs.getString("PROD_NAME");
			                           seller = rs.getString("UID");
			                           search_price = rs.getString("PROD_PRICE");
			                           search_desc = rs.getString("PROD_DESCRIPTION");
			                           System.out.printf("%-30.30s %-30.30s %-30.30s %-30.30s %-30.30s", ad_ID, search_item, seller, search_price, search_desc);
			                       }
			                       System.out.println();
			                       break;
						case "3" : String buy_ID; //BUY
								   String buy_confirm;
								   System.out.print("Enter the ID of the item you would like to purchase: ");
								   buy_ID = user_input.nextLine().trim();
								   
								   /**If item is found, ask for purchase confirmation. Else, print that no ad matches search**/
			                       //stmt = conn.createStatement();
								   
								   /**CHANGE HERE 6**/
								   PreparedStatement buy_stmt = conn.prepareStatement("SELECT * FROM PRODUCT WHERE PROD_ID = ?");
								   buy_stmt.setString(1, buy_ID);
								   rs = buy_stmt.executeQuery();
								   
			                       //rs = stmt.executeQuery("SELECT * FROM PRODUCT WHERE PROD_ID = '" + buy_ID + "'");
			                       System.out.printf("\n%-30.30s %-30.30s %-30.30s %-30.30s %-30.30s\n", "Ad ID", "Item", "Seller", "Price", "Description");
			                       if (rs.next())
			                       {
			                           ad_ID = rs.getString("PROD_ID");
			                           search_item = rs.getString("PROD_NAME");
			                           seller = rs.getString("UID");
			                           search_price = rs.getString("PROD_PRICE");
			                           search_desc = rs.getString("PROD_DESCRIPTION");
			                           System.out.printf("%-30.30s %-30.30s %-30.30s %-30.30s %-30.30s\n", ad_ID, search_item, seller, search_price, search_desc);
			                           
			                           System.out.printf("\nAd found. Would you like to purchase? (y/n): ");
			                           buy_confirm = user_input.nextLine().trim();
			                           if (buy_confirm.toLowerCase().equals("y"))
			                           {
			                            
											try
				                            {
												/**Need to implement way to delete from database without causing future ID conflicts**/
				                                //stmt = conn.createStatement();				                               
				                                stmt.executeUpdate("UPDATE PRODUCT SET PROD_ID = '-1' WHERE PROD_ID = '" + buy_ID + "'");

				                            }
				                            catch(SQLException z)
				                            {
				                                z.printStackTrace();
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
				                            }
			                           	   System.out.println("Purchase complete. Thank you for shopping with BuyIt!");
			                        	   System.out.println("Receipt has been emailed to " + uid + " (to be implemented)");
			                        	   /**
			                        	    * To be implemented - email receipt to user, email confirmation to seller, pull credit card information for database to complete purchase
			                        	    */
			                       	   }
			                       }
			                       else
			                       {
			                           System.out.println("Ad not found!");
			                       }
								   break;
						case "4" : user_input.close(); conn.close(); //EXIT
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
	
	/**Check password function. Checks if password contains at least one capital, one lowercase, one special character, one number, and is between 12-25 characters**/
	private static boolean checkPassword(String pw)
	{
		return (pw.length() >= 12 && pw.length() < 25 && pw.matches(".*[A-Z].*") && pw.matches(".*[a-z].*" ) && pw.matches(".*\\d.*" ) && pw.matches(".*[`~!@#$%^&*()_+-={}:'<>,./].*"));
	}
	
	/**Error checking for price. Returns true if price is an integer, false if not**/
	private static boolean isPriceNumber(String price)
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

}
