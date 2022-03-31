package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

public class NewBankClientHandler extends Thread{
	
	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;

	
	
	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}
	
	public void run() {

		// keep getting requests from the client and processing them
		try {
			out.println("Welcome to the bank. Press 1 to create an account, Press 2 to Login.");
			String answer = in.readLine();
			if (answer.equals("1")) {
				out.println("Set your username");
				String userN = in.readLine();
				out.println("Set your password");
				String pass = in.readLine();
				if (bank.createNewUser(userN, pass)) {
					out.println("Account created.");
				} else {
					out.println("Account has not been created. User may already in database.");
				}
				run();
			}

			if (answer.equals("2")) {
			// ask for username
			out.println("Enter Username");
			String userName = in.readLine().toLowerCase();
			// ask for password
			out.println("Enter Password");
			String password = in.readLine();
			out.println("Checking Details...");
			// authenticate user and get customer ID token from bank for use in subsequent requests
			boolean verified = bank.checkLogInDetails(userName, password);
			// if the user is authenticated then get requests from the user and process them
			if (verified) {
				out.println("Log In Successful. What do you want to do?");
				out.println("You have the following options:");
				out.println("Enter SHOWMYACCOUNTS or enter '1' to view your accounts");
				out.println("Enter NEWACCOUNT or enter '2' to create a new account");
				out.println("Enter WITHDRAW or enter '3' to withdraw money from an account");
				out.println("Enter DEPOSIT or enter '4' to deposit money into an account");
				out.println("5. EXIT");
				while (true) {
					String request = in.readLine();
					System.out.println("Request from " + userName);

					String response = bank.processRequest(userName, request);
					out.println(response);

					if (response.equals("Open a new bank account:")) {
						out.println("What type of account is it? / Account name");
						String accountType = in.readLine().toLowerCase();
						out.println("What would you like your starting balance to be?");
						double startingBalance = Double.parseDouble(in.readLine());
						String accountResponse = bank.processAccountRequest(userName, accountType, startingBalance);
						out.println(accountResponse);
					}

					if (response.equals("Deposit Money:")) {
						out.println("Which account would you like to deposit into?");
						String accountName = in.readLine().toLowerCase();
						out.println("How much would you like to deposit?");
						double deposit = Double.parseDouble(in.readLine());
						String depositResponse = bank.depositMoney(userName, accountName, deposit);
						out.println(depositResponse);
					}

					if (response.equals("Withdraw Money:")) {
						out.println("Which account would you like to withdraw from?");
						String accountName = in.readLine().toLowerCase();
						out.println("How much would you like to withdraw?");
						double withdraw = Double.parseDouble(in.readLine());
						String withdrawResponse = bank.withdrawMoney(userName, accountName, withdraw);
						out.println(withdrawResponse);
					}

					if (response.equals("exit")) run();
				}
			} else {
				out.println("Log In Failed");
				run();
			}
		} else {
				run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

}
