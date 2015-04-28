package driver;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.PreparedStatement;

/**
 * 
 * @author Toaster
 * 
 * Special Thanks to youtube user luv2code as my abilitiy to connect mysql to java came from him
 * source learning video: https://www.youtube.com/watch?v=2i4t-SL1VsU
 *
 */
public class Driver {	

	private Scanner scanner = null;
	
	Connection connection = null;
	Statement statement = null;
	
	PreparedStatement addAccountPrivilege;
	PreparedStatement addCanAccess;
	PreparedStatement addHasRole;
	PreparedStatement addIsOwner;
	PreparedStatement addPrivilege;
	PreparedStatement addRelationPrivilege;
	PreparedStatement addTable;
	PreparedStatement addUserAccount;
	PreparedStatement addUserRolePrivilege;
	PreparedStatement addUserRole;
	PreparedStatement addUserRoleAttribute;
	
	PreparedStatement deleteHasRole;
	
	PreparedStatement queryUserAccount;
	PreparedStatement queryUserRole;
	PreparedStatement queryTable;
	PreparedStatement queryAccountPrivilege;
	PreparedStatement queryCanAccess;
	PreparedStatement queryHasRole;
	PreparedStatement queryIsOwner;
	PreparedStatement queryPrivilege;
	PreparedStatement queryRelationPrivilege;
	PreparedStatement queryUserRoleAttribute;
	PreparedStatement queryUserRolePrivilege;
	
	PreparedStatement queryRolePrivilege;
	PreparedStatement queryRoleCanAccess;
	PreparedStatement querySpecificUserHasRole;
	PreparedStatement queryRoleCanAccessPrivilege;
	PreparedStatement queryRolePrivilegePrivilege;
	/**
	 * 
	 */
	public Driver(String account, String password){
		//add MySQl driver even though I don't think this is specifically necessary
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Get connection to database			
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/security", account, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Create a statement
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Create prepared statements
		String stmt;
		try {
			stmt="insert into security.USER_ACCOUNT(USER_ID_NUMBER,USER_NAME,PHONE_NUMBER) values (?,?,?)";
			addUserAccount = connection.prepareStatement(stmt);
			stmt="insert into security.USER_ROLE(ROLE_NAME) values (?)";
			addUserRole = connection.prepareStatement(stmt);
			stmt="insert into security.`TABLE`(TABLE_NAME) values (?)";
			addTable = connection.prepareStatement(stmt);
			stmt="insert into security.`PRIVILEGE`(PRIVILEGE_NAME,PRIVILEGE_TYPE) values (?,?)";
			addPrivilege = connection.prepareStatement(stmt);
			stmt="insert into security.RELATION_PRIVILEGE(PRIVILEGE_NAME) values (?)";
			addRelationPrivilege = connection.prepareStatement(stmt);
			stmt="insert into security.ACCOUNT_PRIVILEGE(PRIVILEGE_NAME) values (?)";
			addAccountPrivilege = connection.prepareStatement(stmt);
			stmt="insert into security.IS_OWNER(USER_ID_NUMBER,TABLE_NAME) values (?,?)";
			addIsOwner = connection.prepareStatement(stmt);
			stmt="insert into security.HAS_ROLE(USER_ID_NUMBER,ROLE_NAME) values (?,?)";
			addHasRole = connection.prepareStatement(stmt);
			stmt="insert into security.USER_ROLE_PRIVILEGE(ROLE_NAME,PRIVILEGE_NAME) values (?,?)";
			addUserRolePrivilege = connection.prepareStatement(stmt);
			stmt="insert into security.CAN_ACCESS(ROLE_NAME,TABLE_NAME,PRIVILEGE_NAME) values (?,?,?)";
			addCanAccess = connection.prepareStatement(stmt);
			stmt="insert into security.USER_ROLE_ATTRIBUTE(ATTRIBUTE_TYPE,ROLE_NAME,ATTRIBUTE_DATA) VALUES (?,?,?)";
			addUserRoleAttribute = connection.prepareStatement(stmt);
			
			stmt="delete from security.HAS_ROLE where USER_ID_NUMBER=?";
			deleteHasRole = connection.prepareStatement(stmt);
			
			stmt="SELECT * FROM USER_ACCOUNT";
			queryUserAccount = connection.prepareStatement(stmt);
			stmt="SELECT * FROM USER_ROLE";
			queryUserRole = connection.prepareStatement(stmt);
			stmt="SELECT * FROM `TABLE`";
			queryTable = connection.prepareStatement(stmt);
			stmt="SELECT * FROM ACCOUNT_PRIVILEGE";
			queryAccountPrivilege = connection.prepareStatement(stmt);
			stmt="SELECT * FROM CAN_ACCESS";
			queryCanAccess = connection.prepareStatement(stmt);
			stmt="SELECT * FROM HAS_ROLE";
			queryHasRole = connection.prepareStatement(stmt);
			stmt="SELECT * FROM IS_OWNER";
			queryIsOwner = connection.prepareStatement(stmt);
			stmt="SELECT * FROM `PRIVILEGE`";
			queryPrivilege = connection.prepareStatement(stmt);
			stmt="SELECT * FROM RELATION_PRIVILEGE";
			queryRelationPrivilege = connection.prepareStatement(stmt);
			stmt="SELECT * FROM USER_ROLE_ATTRIBUTE";
			queryUserRoleAttribute = connection.prepareStatement(stmt);
			stmt="SELECT * FROM USER_ROLE_PRIVILEGE";
			queryUserRolePrivilege = connection.prepareStatement(stmt);
			
			//Add stuff here for part 8...
			stmt="SELECT * FROM CAN_ACCESS WHERE ROLE_NAME = ?";
			queryRoleCanAccess = connection.prepareStatement(stmt);
			stmt="SELECT * FROM USER_ROLE_PRIVILEGE WHERE ROLE_NAME = ?";
			queryRolePrivilege = connection.prepareStatement(stmt);
			stmt="SELECT * FROM HAS_ROLE WHERE USER_ID_NUMBER = ?";
			querySpecificUserHasRole = connection.prepareStatement(stmt);
			stmt="SELECT * FROM CAN_ACCESS WHERE ROLE_NAME = ? and PRIVILEGE_NAME = ?";
			queryRoleCanAccessPrivilege = connection.prepareStatement(stmt);
			stmt="SELECT * FROM USER_ROLE_PRIVILEGE WHERE ROLE_NAME = ?";
			queryRolePrivilegePrivilege = connection.prepareStatement(stmt);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 */
	public void enterRecords() {
		File accountPrivilegeFile = new File("data\\ACCOUNT_PRIVILEGE.txt");
		File canAccessFile = new File("data\\CAN_ACCESS.txt");
		File hasRoleFile = new File("data\\HAS_ROLE.txt");
		File isOwnerFile = new File("data\\IS_OWNER.txt");
		File privilegeFile = new File("data\\PRIVILEGE.txt");
		File relationPrivilegeFile = new File("data\\RELATION_PRIVILEGE.txt");
		File tableFile = new File("data\\TABLE.txt");
		File userAccountFile = new File("data\\USER_ACCOUNT.txt");
		File userRolePrivilegeFile = new File("data\\USER_ROLE_PRIVILEGE.txt");
		File userRoleFile = new File("data\\USER_ROLE.txt");
		
		try{
			//disable foreign key checks due to all employee entries violating key constraints
			//statement.execute("SET foreign_key_checks = 0");
			
			String currentInput = "";
			String currentInputParams[];
			
			//add user accounts
			newFileScanner(userAccountFile);
			while (scanner.hasNextLine()){
				currentInput = scanner.nextLine();
				currentInputParams = currentInput.split(",");
				this.addUserAccount(currentInputParams[0],currentInputParams[1],currentInputParams[2]);
			}
			
			//add roles
			newFileScanner(userRoleFile);
			while (scanner.hasNextLine()){
				currentInput = scanner.nextLine();
				this.addUserRole(currentInput);
			}
			
			//add tables
			newFileScanner(tableFile);
			while (scanner.hasNextLine()){
				currentInput = scanner.nextLine();
				this.addTable(currentInput);
			}
			
			//add privileges
			newFileScanner(privilegeFile);
			while (scanner.hasNextLine()){
				currentInput = scanner.nextLine();
				currentInputParams = currentInput.split(",");
				this.addPrivilege(currentInputParams[0],currentInputParams[1]);
			}
			
			//add account privileges
			newFileScanner(accountPrivilegeFile);
			while (scanner.hasNextLine()){
				currentInput = scanner.nextLine();
				this.addAccountPrivilege(currentInput);
			}
			
			//add relation privileges
			newFileScanner(relationPrivilegeFile);
			while (scanner.hasNextLine()){
				currentInput = scanner.nextLine();
				this.addRelationPrivilege(currentInput);
			}
			
			//add owners
			newFileScanner(isOwnerFile);
			while (scanner.hasNextLine()){
				currentInput = scanner.nextLine();
				currentInputParams = currentInput.split(",");
				this.addIsOwner(currentInputParams[0],currentInputParams[1]);
			}
			
			//add has roles
			newFileScanner(hasRoleFile);
			while (scanner.hasNextLine()){
				currentInput = scanner.nextLine();
				currentInputParams = currentInput.split(",");
				this.addHasRole(currentInputParams[0],currentInputParams[1]);
			}
			
			//add role privileges
			newFileScanner(userRolePrivilegeFile);
			while (scanner.hasNextLine()){
				currentInput = scanner.nextLine();
				currentInputParams = currentInput.split(",");
				this.addUserRolePrivilege(currentInputParams[0],currentInputParams[1]);
			}
			
			//add can access
			newFileScanner(canAccessFile);
			while (scanner.hasNextLine()){
				currentInput = scanner.nextLine();
				currentInputParams = currentInput.split(",");
				this.addCanAccess(currentInputParams[0],currentInputParams[1],currentInputParams[2]);
			}
			
			//re-enable foreign key checks for future referential integrity
			//statement.execute("SET foreign_key_checks = 1;");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void deleteAll() {
		try {
			statement.execute("DELETE FROM USER_ROLE_PRIVILEGE");
			statement.execute("DELETE FROM CAN_ACCESS");
			statement.execute("DELETE FROM IS_OWNER");
			statement.execute("DELETE FROM HAS_ROLE");
			statement.execute("DELETE FROM RELATION_PRIVILEGE");
			statement.execute("DELETE FROM ACCOUNT_PRIVILEGE");
			statement.execute("DELETE FROM `TABLE`");
			statement.execute("DELETE FROM USER_ROLE_ATTRIBUTE");
			statement.execute("DELETE FROM USER_ACCOUNT");
			statement.execute("DELETE FROM USER_ROLE");
			statement.execute("DELETE FROM `PRIVILEGE`");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void reInitializeDatabase() {
		deleteAll();
		enterRecords();
	}
	
	/**
	 * 
	 * @param USER_ID_NUMBER
	 * @param USER_NAME
	 * @param PHONE_NUMBER
	 * @throws SQLException
	 */
	public void addUserAccount(String USER_ID_NUMBER, String USER_NAME, String PHONE_NUMBER) throws SQLException {
		addUserAccount.clearParameters();
		addUserAccount.setString(1, USER_ID_NUMBER);
		addUserAccount.setString(2, USER_NAME);
		addUserAccount.setString(3, PHONE_NUMBER);
		addUserAccount.executeUpdate();
	}
	/**
	 * 
	 * @param ROLE_NAME
	 * @throws SQLException
	 */
	public void addUserRole(String ROLE_NAME) throws SQLException {
		addUserRole.clearParameters();
		addUserRole.setString(1, ROLE_NAME);
		addUserRole.executeUpdate();
	}
	/**
	 * 
	 * @param TALBE_NAME
	 * @throws SQLException
	 */
	public void addTable(String TALBE_NAME) throws SQLException {
		addTable.clearParameters();
		addTable.setString(1, TALBE_NAME);
		addTable.executeUpdate();
	}
	/**
	 * 
	 * @param PRIVILEGE_NAME
	 * @param PRIVILEGE_TYPE
	 * @throws SQLException
	 */
	public void addPrivilege(String PRIVILEGE_NAME, String PRIVILEGE_TYPE) throws SQLException {
		addPrivilege.clearParameters();
		addPrivilege.setString(1, PRIVILEGE_NAME);
		addPrivilege.setString(2, PRIVILEGE_TYPE);
		addPrivilege.executeUpdate();
	}
	/**
	 * 
	 * @param PRIVILEGE_NAME
	 * @throws SQLException
	 */
	public void addRelationPrivilege(String PRIVILEGE_NAME) throws SQLException {
		addRelationPrivilege.clearParameters();
		addRelationPrivilege.setString(1, PRIVILEGE_NAME);
		addRelationPrivilege.executeUpdate();
	}
	/**
	 * 
	 * @param PRIVILEGE_NAME
	 * @throws SQLException
	 */
	public void addAccountPrivilege(String PRIVILEGE_NAME) throws SQLException {
		addAccountPrivilege.clearParameters();
		addAccountPrivilege.setString(1, PRIVILEGE_NAME);
		addAccountPrivilege.executeUpdate();
	}
	/**
	 * 
	 * @param USER_ID_NUMBER
	 * @param TABLE_NAME
	 * @throws SQLException
	 */
	public void addIsOwner(String USER_ID_NUMBER, String TABLE_NAME) throws SQLException {
		addIsOwner.clearParameters();
		addIsOwner.setString(1, USER_ID_NUMBER);
		addIsOwner.setString(2, TABLE_NAME);
		addIsOwner.executeUpdate();

	}
	/**
	 * 
	 * @param USER_ID_NUMBER
	 * @param ROLE_NAME
	 * @throws SQLException
	 */
	public void addHasRole(String USER_ID_NUMBER, String ROLE_NAME) throws SQLException {
		addHasRole.clearParameters();
		addHasRole.setString(1, USER_ID_NUMBER);
		addHasRole.setString(2, ROLE_NAME);
		addHasRole.executeUpdate();
	}
	public void deleteHasRole(String USER_ID_NUMBER) throws SQLException {
		deleteHasRole.clearParameters();
		deleteHasRole.setString(1, USER_ID_NUMBER);
		deleteHasRole.executeUpdate();
	}
	/**
	 * 
	 * @param ROLE_NAME
	 * @param PRIVILEGE_NAME
	 * @throws SQLException
	 */
	public void addUserRolePrivilege(String ROLE_NAME, String PRIVILEGE_NAME) throws SQLException {
		addUserRolePrivilege.clearParameters();
		addUserRolePrivilege.setString(1, ROLE_NAME);
		addUserRolePrivilege.setString(2, PRIVILEGE_NAME);
		addUserRolePrivilege.executeUpdate();
	}
	/**
	 * 
	 * @param ATTRIBUTE_TYPE
	 * @param ROLE_NAME
	 * @param ATTRIBUTE_DATA
	 * @throws SQLException
	 */
	public void addUserRoleAttribute(String ATTRIBUTE_TYPE, String ROLE_NAME, String ATTRIBUTE_DATA) throws SQLException {
		addUserRoleAttribute.clearParameters();
		addUserRoleAttribute.setString(1, ATTRIBUTE_TYPE);
		addUserRoleAttribute.setString(2, ROLE_NAME);
		addUserRoleAttribute.setString(3, ATTRIBUTE_DATA);
		addUserRoleAttribute.executeUpdate();
		
		
	}
	/**
	 * 
	 * @param ROLE_NAME
	 * @param TABLE_NAME
	 * @param PRIVILEGE_NAME
	 * @throws SQLException
	 */
	public void addCanAccess(String ROLE_NAME, String TABLE_NAME, String PRIVILEGE_NAME) throws SQLException {
		addCanAccess.clearParameters();
		addCanAccess.setString(1, ROLE_NAME);
		addCanAccess.setString(2, TABLE_NAME);
		addCanAccess.setString(3, PRIVILEGE_NAME);
		addCanAccess.executeUpdate();
	}
	
	public void queryUserAccount() throws SQLException {
		ResultSet resultSet = queryUserAccount.executeQuery();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		System.out.printf("%-20s%-20s%-20s",rsmd.getColumnName(1),rsmd.getColumnName(2),rsmd.getColumnName(3));
		System.out.println();
		while (resultSet.next()) {
			System.out.printf("%-20s%-20s%-20s",resultSet.getString(1),resultSet.getString(2),resultSet.getString(3));
		    System.out.println("");
		}
	    System.out.println("");
	}
	
	public void queryUserRole() throws SQLException {
		ResultSet resultSet = queryUserRole.executeQuery();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		for (int i = 1; i <= columnsNumber; i++) {
	        if (i > 1) System.out.print("\t");
	        System.out.print(rsmd.getColumnName(i));
	    }
		System.out.println();
		while (resultSet.next()) {
		    for (int i = 1; i <= columnsNumber; i++) {
		    	if (i > 1) System.out.print("\t");
		    	if (i == 2) System.out.print("\t");
		        String columnValue = resultSet.getString(i);
		        System.out.print(columnValue);
		    }
		    System.out.println("");
		}
	    System.out.println("");
	}
	
	public void queryTable() throws SQLException {
		ResultSet resultSet = queryTable.executeQuery();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		for (int i = 1; i <= columnsNumber; i++) {
	        if (i > 1) System.out.print("\t");
	        System.out.print(rsmd.getColumnName(i));
	    }
		System.out.println();
		while (resultSet.next()) {
		    for (int i = 1; i <= columnsNumber; i++) {
		    	if (i > 1) System.out.print("\t");
		    	if (i == 2) System.out.print("\t");
		        String columnValue = resultSet.getString(i);
		        System.out.print(columnValue);
		    }
		    System.out.println("");
		}
	    System.out.println("");
	}
	
	public void queryAccountPrivilege() throws SQLException {
		ResultSet resultSet = queryAccountPrivilege.executeQuery();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		for (int i = 1; i <= columnsNumber; i++) {
	        System.out.print(rsmd.getColumnName(i));
	    }
		System.out.println();
		while (resultSet.next()) {
		    for (int i = 1; i <= columnsNumber; i++) {
		        String columnValue = resultSet.getString(i);
		        System.out.print(columnValue);
		    }
		    System.out.println("");
		}
	    System.out.println("");
	}
	
	public void queryCanAccess() throws SQLException {
		ResultSet resultSet = queryCanAccess.executeQuery();
		ResultSetMetaData rsmd = resultSet.getMetaData();
        System.out.printf("%-20s%-20s%-20s",rsmd.getColumnName(1),rsmd.getColumnName(2),rsmd.getColumnName(3));
		System.out.println();
		while (resultSet.next()) {
			System.out.printf("%-20s%-20s%-20s",resultSet.getString(1),resultSet.getString(2),resultSet.getString(3));
		    System.out.println("");
		}
	    System.out.println("");
	}
	
	public void queryHasRole() throws SQLException {
		ResultSet resultSet = queryHasRole.executeQuery();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		System.out.printf("%-20s%-20s",rsmd.getColumnName(1),rsmd.getColumnName(2));
		System.out.println();
		while (resultSet.next()) {
			System.out.printf("%-20s%-20s",resultSet.getString(1),resultSet.getString(2));
		    System.out.println("");
		}
	    System.out.println("");
	}
	
	public void queryIsOwner() throws SQLException {
		ResultSet resultSet = queryIsOwner.executeQuery();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		System.out.printf("%-20s%-20s",rsmd.getColumnName(1),rsmd.getColumnName(2));
		System.out.println();
		while (resultSet.next()) {
			System.out.printf("%-20s%-20s",resultSet.getString(1),resultSet.getString(2));
		    System.out.println("");
		}
	    System.out.println("");
	}
	
	public void queryPrivilege() throws SQLException {
		ResultSet resultSet = queryPrivilege.executeQuery();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		System.out.printf("%-20s%-20s",rsmd.getColumnName(1),rsmd.getColumnName(2));
		System.out.println();
		while (resultSet.next()) {
			System.out.printf("%-20s%-20s",resultSet.getString(1),resultSet.getString(2));
		    System.out.println("");
		}
	    System.out.println("");
	}
	
	public void queryRelationPrivilege() throws SQLException {
		ResultSet resultSet = queryRelationPrivilege.executeQuery();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		for (int i = 1; i <= columnsNumber; i++) {
	        if (i > 1) System.out.print("\t");
	        System.out.print(rsmd.getColumnName(i));
	    }
		System.out.println();
		while (resultSet.next()) {
		    for (int i = 1; i <= columnsNumber; i++) {
		    	if (i > 1) System.out.print("\t");
		    	if (i == 2) System.out.print("\t");
		        String columnValue = resultSet.getString(i);
		        System.out.print(columnValue);
		    }
		    System.out.println("");
		}
	    System.out.println("");
	}
	
	public void queryUserRoleAttribute() throws SQLException {
		ResultSet resultSet = queryUserRoleAttribute.executeQuery();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		System.out.printf("%-20s%-20s%-20s",rsmd.getColumnName(1),rsmd.getColumnName(2),rsmd.getColumnName(3));
		System.out.println();
		while (resultSet.next()) {
			System.out.printf("%-20s%-20s%-20s",resultSet.getString(1),resultSet.getString(2),resultSet.getString(3));
		    System.out.println("");
		}
	    System.out.println("");
	}
	
	/*public void queryRolePrivilege() throws SQLException {
		ResultSet resultSet = queryRolePrivilege.executeQuery();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		System.out.printf("%-20s%-20s%-20s",rsmd.getColumnName(1),rsmd.getColumnName(2),rsmd.getColumnName(3));
		System.out.println();
		while (resultSet.next()) {
			System.out.printf("%-20s%-20s%-20s",resultSet.getString(1),resultSet.getString(2),resultSet.getString(3));
		    System.out.println("");
		}
	    System.out.println("");
	}*/
	
	public void queryUserRolePrivilege() throws SQLException {
		ResultSet resultSet = queryUserRolePrivilege.executeQuery();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		System.out.printf("%-20s%-20s",rsmd.getColumnName(1),rsmd.getColumnName(2));
		System.out.println();
		while (resultSet.next()) {
			System.out.printf("%-20s%-20s",resultSet.getString(1),resultSet.getString(2));
		    System.out.println("");
		}
	    System.out.println("");
	}
	
	public void queryRoleCanAccess (String roleName) throws SQLException {
		queryRoleCanAccess.clearParameters();
		queryRoleCanAccess.setString(1, roleName);
		ResultSet resultSet = queryRoleCanAccess.executeQuery();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		System.out.printf("%-20s%-20s%-20s",rsmd.getColumnName(1),rsmd.getColumnName(2),rsmd.getColumnName(3));
		System.out.println();
		while (resultSet.next()) {
			System.out.printf("%-20s%-20s%-20s",resultSet.getString(1),resultSet.getString(2),resultSet.getString(3));
		    System.out.println("");
		}
	    System.out.println("");
	    
	    queryRolePrivilege.clearParameters();
	    queryRolePrivilege.setString(1, roleName);
	    resultSet = queryRolePrivilege.executeQuery();
		rsmd = resultSet.getMetaData();
		System.out.printf("%-20s%-20s",rsmd.getColumnName(1),rsmd.getColumnName(2));
		System.out.println();
		while (resultSet.next()) {
			System.out.printf("%-20s%-20s",resultSet.getString(1),resultSet.getString(2));
		    System.out.println("");
		}
	    System.out.println("");
	}
	
	public void queryUserHasPrivilege(String idNum) throws SQLException {
		querySpecificUserHasRole.clearParameters();
		querySpecificUserHasRole.setString(1, idNum);
		ResultSet resultSet = querySpecificUserHasRole.executeQuery();
		resultSet.next();
		queryRoleCanAccess(resultSet.getString(2));		
	}
	
	public void queryIfPrivilegeIsGrantedToUser(String privType, String idNum) throws SQLException {
		//Query for User role
		querySpecificUserHasRole.clearParameters();
		querySpecificUserHasRole.setString(1, idNum);
		ResultSet resultSet = querySpecificUserHasRole.executeQuery();
		//get user role
		resultSet.next();
		String roleName = resultSet.getString(2);
		System.out.println(roleName);
		//search use role for role privileges
		/*queryRolePrivilege.clearParameters();
		queryRolePrivilege.setString(1, roleName);
		queryRolePrivilege.executeQuery();*/
		//search user for can access privileges
		queryRoleCanAccessPrivilege.clearParameters();
		queryRoleCanAccessPrivilege.setString(1, roleName);
		queryRoleCanAccessPrivilege.setString(2, privType);
		resultSet = queryRoleCanAccessPrivilege.executeQuery();
		if(resultSet.next())
			System.out.println("User " + idNum + " does have access to privilege " + privType);	
		else
			System.out.println("User " + idNum + " does not have access to privilege " + privType);
	}
	
	private void newFileScanner(File file)
	{
		try {
			this.scanner = new Scanner(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

}
