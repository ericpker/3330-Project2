package app;

import java.sql.SQLException;
import java.util.Scanner;

import driver.Driver;

public class MainApp {

	Driver driver = null;
	Scanner input = new Scanner(System.in);
	String choice = "";
	
	public MainApp() {
		driver = new Driver("root","3330db003");
		driver.reInitializeDatabase();
		
		while(choice!="0") {
			System.out.println("1. Add new User Account");
			System.out.println("2. Add new Role");
			System.out.println("3. Add new Table");
			System.out.println("4. Add new Privilege");
			System.out.println("5. Relate User Account to Role");
			System.out.println("6. Relate Account Privilege to Role");
			System.out.println("7. Relate Relation Privilege, Role, and Table");
			System.out.println("8. Query Role Privileges");
			System.out.println("9. Query User Account Privileges");
			System.out.println("10. Check if User has Privilege");
			System.out.println("0. Print All Tables and Relations");
			System.out.println("Q. Quit");
			System.out.print("Choose:");
			choice = input.nextLine();
			switch (choice) {
				case "q":
				case "Q": System.exit(0);
					break;
				case "1": promptUserAccount();
					break;
				case "2": promptUserRole();
					break;
				case "3": promptTable();
					break;
				case "4": promptPrivilege();
					break;
				case "5": promptHasRole();
					break;
				case "6": promptUserRolePrivilege();
					break;
				case "7": promptCanAccess();
					break;
				case "8": queryRolePrivileges();
					break;
				case "9": queryPrivilegsUserAccount();
					break;
				case "10": queryIfPrivilegeIsGrantedToUser();
					break;
				case "0": printAll();
					break;
			default: System.out.println("Invalid Selection");
				break;
			}
		}
	}

	public static void main(String[] args) {
		MainApp app = new MainApp();
	}
	
	public void printAll() {
		try {
			System.out.println("---User Accounts---");
			driver.queryUserAccount();
			System.out.println("---User Roles---");
			driver.queryUserRole();
			System.out.println("---Role Attributes---");
			driver.queryUserRoleAttribute();
			System.out.println("---Has Role Table---");
			driver.queryHasRole();
			System.out.println("---Tables---");
			driver.queryTable();
			System.out.println("---Owner Table---");
			driver.queryIsOwner();
			System.out.println("---Can Access Table---");
			driver.queryCanAccess();
			System.out.println("---Privileges Table---");
			driver.queryPrivilege();
			System.out.println("---Account Privilege Table---");
			driver.queryAccountPrivilege();
			System.out.println("---Relation Privilege Table---");
			driver.queryRelationPrivilege();
			System.out.println("---Role Privileges---");
			driver.queryUserRolePrivilege();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void promptUserAccount() {
		System.out.print("Enter New Account Number:");
		String acctNum = input.nextLine();
		System.out.print("Enter New Account Name:");
		String acctName = input.nextLine();
		System.out.print("Enter New Phone Number:");
		String acctPhone = input.nextLine();
		
		try {
			driver.addUserAccount(acctNum, acctName, acctPhone);
		} catch (SQLException e) {
			System.out.println("Unable to add User account");
		}		
	}
	
	public void promptUserRole() {
		System.out.print("Enter New Role:");
		String roleName = input.nextLine();
		try {
			driver.addUserRole(roleName);
			System.out.print("Enter Role Attribute?(y/n)");
			String yn = input.nextLine();
			while(yn.equals("Y")||yn.equals("y")) {
				System.out.print("Enter Attribute Type:");
				String attbType = input.nextLine();
				System.out.print("Enter Attribute Data:");
				String attbDscpt = input.nextLine();
				try {
					System.out.println(attbType + " " + roleName + " " + attbDscpt);
					driver.addUserRoleAttribute(attbType, roleName, attbDscpt);
				} catch (SQLException e) {
					System.out.println("Unable to add Role Attribute");
				}
				System.out.print("Enter Role Attribute?(y/n)");
				yn = input.nextLine();
			}
		} catch (SQLException e) {
			System.out.println("Unable to add User Role");
		}
	}
	
	public void promptTable() {
		System.out.print("Enter Table Name:");
		String tblName = input.nextLine();
		System.out.print("Enter Table Owner Id:");
		String tblOwner = input.nextLine();
		try {
			driver.addTable(tblName);
		} catch (SQLException e) {
			System.out.println("Could not create table");
		}
		try {
			driver.addIsOwner(tblOwner, tblName);
		} catch (SQLException e) {
			System.out.println("Coult not associate Owner");
		}
	}
	
	public void promptPrivilege() {
		Boolean valid = false;
		Boolean type = false;
		String privType = null;
		System.out.println("Enter Privilege Name");
		String privName = input.nextLine();
		while(!valid) {
			System.out.println("Enter Privilege Type");
			privType = input.nextLine();
			switch (privType) {
			case "ACCOUNT_PRIVILEGE": valid = true;
				type = true;
				break;
			case "RELATION_PRIVILEGE": valid = true;
				type = false;
				break;
			default: valid = false;
			}
		}
		try {
			driver.addPrivilege(privName, privType);
			if(type) {
				try {
					driver.addAccountPrivilege(privName);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				try {
					driver.addRelationPrivilege(privName);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} catch (SQLException e) {
			System.out.println("Could not create new Privilege");
		}
	}
	
	public void promptHasRole() {
		System.out.println("Enter User Account ID:");
		String userID = input.nextLine();
		System.out.println("Enter New Role:");
		String roleName = input.nextLine();
		try {
			driver.deleteHasRole(userID);
		} catch (SQLException e) {
			System.out.println("Unable to delete relation");
			e.printStackTrace();
		}
		try {
			driver.addHasRole(userID, roleName);
		} catch (SQLException e) {
			System.out.println("Unable to create relation");
		}
	}
	
	public void promptUserRolePrivilege() {
		System.out.print("Enter Role Name:");
		String roleName = input.nextLine();
		System.out.print("Enter Account Privilege Name:");
		String privName = input.nextLine();
		try {
			driver.addUserRolePrivilege(roleName, privName);
		} catch (SQLException e) {
			System.out.println("Unable to add " + privName + " to " + roleName);
		}
	}
	
	public void promptCanAccess() {
		System.out.print("Enter Relation Privilege Name:");
		String roleName = input.nextLine();
		System.out.print("Enter Role Name:");
		String tblName = input.nextLine();
		System.out.print("Enter Table Name");
		String privName = input.nextLine();
		try {
			driver.addCanAccess(roleName, tblName, privName);
		} catch (SQLException e) {
			System.out.println("Unable to add privilage " + privName + " to role " + roleName + "on table " + tblName);
		}
	}
	
	public void queryRolePrivileges() {
		System.out.print("Enter Role Name:");
		String roleName = input.nextLine();
		try {
			driver.queryRoleCanAccess(roleName);
		} catch (SQLException e) {
			System.out.println("Unable to query Role");
			e.printStackTrace();
		}
		
	}
	
	public void queryPrivilegsUserAccount() {
		System.out.print("Enter User ID:");
		String idNum = input.nextLine();
		try {
			driver.queryUserHasPrivilege(idNum);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void queryIfPrivilegeIsGrantedToUser() {
		System.out.print("Enter Privilege:");
		String privName = input.nextLine();
		System.out.print("Enter User ID");
		String idNum = input.nextLine();
		try {
			driver.queryIfPrivilegeIsGrantedToUser(privName, idNum);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
