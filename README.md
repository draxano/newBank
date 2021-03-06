# New Bank
New Bank is an assignment as part of the University of Bath's Software Engineering 2 module. 

## Getting Started

- Ensure you have git installed. If not, please see [here](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) for instructions.
- Use an appropriate IDE (we recommend [IntelliJ](https://www.jetbrains.com/idea/))

## Setup & Requirements

- The minimum requirement to run this project is JDK 16.
- Ensure your IDE has the latest version.


## Using Git

In your terminal (with git installed), run the following commands:

To clone the repo:
`git clone https://github.com/draxano/newBank.git`

For IntelliJ users: we recommend cloning the repo using the IntelliJ GUI. When opening a new project, please select "get from VCS" and paste the link https://github.com/draxano/newBank.git. You might be asked to enter your GitHub login details or enter a personal access token.

please note: you should only have to run this once

To create a new branch:
`git checkout -b <new-branch-name>`

To view your changes:
`git status`

To commit your changes:
`git commit -a -m <description-of-your-changes>`

note: this will commit ALL of your changes

To push your changes to your new branch:
`git push origin HEAD`

This will then generate a link to your new branch, where you are able to create a pull request.

## Contribution Guide
Please do not push to the main branch. This could involve overwriting someone else's work! Instead, please follow the steps above to push to a branch that you have created, and open a pull request. 

The pull request should have a descriptive title, e.g. "FR1 System must allow for the creation of new accounts" and mark someone to review your changes. 
Once someone else in the team has reviewed, they can merge the changes!

If you have any questions, just ask @caitlingbailey

## Running New Bank

First, run the server. This is located in newbank/server/NewBankServer.java.

Once this is running, proceed to run the client from newbank/client/ExampleClient.java

## User Information

The product allows banking using just the command line. Below, we will show you how you can log in and begin banking using New Bank. 

### Potential issues

When running the program, you might be faced with an error stating "java 16 not found". To avoid that, make sure your machine has the latest version of Java.

You might also get the error message "cannot determine path to 'tools.jar' library". Double-check if your IDE is on the latest version and run the program again.

### Logging In
After following the steps in Running New Bank, you should be given the option to either create a new user account or to log in with your existing username and password details. You should be able to proceed with the option by entering 1 or 2 respectively. 

### Functionality

| Command          | Description                                                                                                                                                                                            | Example                                                                            |
|------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------|
| `SHOWMYACCOUNTS` | Enter '1' or type SHOWMYACCOUNTS into the command line to use this functionality. Returns a list of all the customer's accounts, alongside their balances.                                             | `SHOWMYACCOUNTS`                                                                   |
| `NEWACCOUNT`     | Enter '2' or type NEWACCOUNT into the command line to use this functionality. Allows an authenticated user to create a new account by specifying a new account name, followed by its starting balance. | `2, NewAccountName, 200`                                                           |
| `WITHDRAW`       | Enter '3' or type WITHDRAW into the command line to use this functionality. Allows an authenticated user to withdraw an amount from a specified account.                                               | `3, AccountToWithdrawFrom, 200`                                                    |
| `DEPOSIT`        | Enter '4' or type DEPOSIT into the command line to use this functionality. Allows an authenticated user to deposit an amount to a specified account.                                                   | `4, AccountToDepositTo, 200`                                                       |
| `TRANSFER`       | Enter '5' or type TRANSFER into the command line to use this functionality. Allows an authenticated user to transfer an amount from one account to another account.                                    | `5, AccountToTransferFrom, AccountToTransferTo, 200`                               |
| `CLOSEACCOUNT`   | Enter '6' or type CLOSEACCOUNT into the command line to close an account.                                                                                                                              | `6, AccountToClose`                                                                |
| `PAY`            | Enter '7' or type PAY into the command line to pay to someone's account.                                                                                                                               | `7, UserNameOfCustomer, CustomersAccountName, AmountToPay, AccountToMoveMoneyFrom` |
| `DELETEUSERDATA` | Enter '8' or type DELETEUSERDATA into the command line to delete all data associated with the username.                                                                                                | `8`                                                                                |
| `HELP`           | Enter 'h' or type HELP into the command line to go back to the navigation menu.                                                                                                                        | `h`                                                                                |
| `EXIT`           | Enter 'x' or type EXIT into the command line to log out and exit the program.                                                                                                                          | `x`                                                                                |
