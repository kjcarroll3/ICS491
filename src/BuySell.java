import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Scanner;


/**
 * @author Evan
 *
 */



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
		
		Scanner scan = new Scanner(System.in);
		Scanner user_input = new Scanner(System.in);
		
		try
		{
			//Connect to database
			System.out.printf("Connecting to database.......\n");
			conn = DriverManager.getConnection(URL, "root", "##WingGundam2015$$");
			if(conn != null)
			{
				System.out.printf("\t\t\tWelcome to BuyIt!!!\n");
				while(!loggedIn)
				{
					System.out.printf("[1] Create Account\t[2] Login\t[3] Exit\n");
					command = scan.nextLine();
					
					switch(command)
					{
						case "1" : 
								   System.out.printf("Please enter a username: "); uid = user_input.nextLine(); //query check
								   //if query returns false (!exists username in db)
								   while(!loggedIn)
								   {
									   System.out.printf("Enter a password of length >= 12\nMust include at least one of each: uppercase, lowercase, number, special character: ");
									   pwd = user_input.nextLine(); 
								   
									   //bug in password check
									   if(pwd.matches("^[A-Za-z0-9]{12,50}$") /*&& pwd.matches("^[a-z]{12,50}$") && pwd.matches("^[0-9]{12,50}$")*/)
									   {
										   System.out.printf("Re-enter password: "); input = user_input.nextLine();
										   loggedIn = input.equals(pwd) ? true : false;
										   if(loggedIn) System.out.printf("Congratulations, you are now logged in as %s, welcome to BuyIt!\n", uid);
									   }
								   }
								   break;
						case "2" : System.out.printf("Username: "); uid = user_input.nextLine(); 
								   System.out.printf("Password: "); pwd = user_input.nextLine();
								   //query check
								   break;
						case "3" : scan.close(); user_input.close(); conn.close(); break;
						default : System.out.printf("################Connection to database closed, thank you for shopping with BuyIt################"); break;
					}
				}
				
				//Logged in user segment
				while(loggedIn)
				{
					System.out.printf("[1] Sell\t[2] Search\t[3] Buy\n"); command = user_input.nextLine();
					
					switch(command)
					{
						case "1" : 
							break;
						case "2" : 
							break;
						case "3" :
							break;
					}
					
				}
			}
		}
		catch (SQLException a)
		{
			a.printStackTrace();
		}
        
	}

}
