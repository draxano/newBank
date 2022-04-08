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
				instructions();
				while (true) {
					String request = in.readLine();
					if (request.equalsIgnoreCase("h") || request.equalsIgnoreCase("help")) {
						instructions();
						continue;
					}
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

					if (response.equals("Transfer Money:")) {
						out.println("Which account would you like to transfer money from?");
						String firstAccountName = in.readLine().toLowerCase();
						out.println("Which account would you like to transfer money to?");
						String secondAccountName = in.readLine().toLowerCase();
						out.println("Please enter the transfer amount: ");
						double transfer = Double.parseDouble(in.readLine());
						String transferResponse = bank.transferMoney(userName, firstAccountName, secondAccountName, transfer);
						out.println(transferResponse);
					}

					if (response.equals("Close an Account:")) {
						out.println("Which account would you like to close?");
						String accountName = in.readLine().toLowerCase();
						out.println("Are you sure you want to close "+ accountName
								+ "? All of this account's data will be deleted.\nEnter 1 to proceed, any other key to cancel.");
						String userResponse = in.readLine().toLowerCase();
						if (userResponse.equalsIgnoreCase("1")){
							String deleteAccountResponse = bank.deleteAccount(userName, accountName);
							out.println(deleteAccountResponse);
						} else {out.println("Account " + accountName + " not deleted.");}

					}

					if (response.equals("Make a payment:")) {
						out.println("Enter the username of the customer or company you wish to make a payment to:");
						String userName2 = in.readLine().toLowerCase();
						out.println("Enter " + userName2 + "'s" + " account name you wish to send the payment to:");
						String accountName2 = in.readLine().toLowerCase();
						out.println("Please enter the payment amount: ");
						double pay = Double.parseDouble(in.readLine());
						out.println("Which account would you like to make the payment from? Enter your account:");
						String accountName1 = in.readLine().toLowerCase();
						String confirm = "Are you sure to proceed with the following payment:\n\nPAY "
								+ userName2 + " £" + pay + " to account " + accountName2.toUpperCase()
								+ "\n\nEnter 1 to confirm, or any other key to cancel";
						out.println(confirm);
						String userResponse = in.readLine().toLowerCase();
						if (userResponse.equalsIgnoreCase("1")){
							String payResponse = bank.pay(userName, accountName1, userName2, accountName2, pay);
							out.println(payResponse);
						} else {out.println("Payment transaction cancelled.");}
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

	private void instructions() {
		out.println("You have the following options:");
		out.println("Enter SHOWMYACCOUNTS or enter '1' to view your accounts");
		out.println("Enter NEWACCOUNT or enter '2' to create a new account");
		out.println("Enter WITHDRAW or enter '3' to withdraw money from an account.");
		out.println("Enter DEPOSIT or enter '4' to deposit money into an account");
		out.println("Enter TRANSFER or enter '5' to transfer money between accounts");
		out.println("Enter CLOSEACCOUNT or enter '6' to close an account");
		out.println("Enter PAY or enter '7' to pay to someone's account");
		out.println("Enter HELP or enter 'h' to see options again");
		out.println("Enter EXIT or 'x' to exit");
	}

}
