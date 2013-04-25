
import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;
import java.io.*;
import java.util.*;


///////////////////////////// Mutlithreaded Server /////////////////////////////

public class PetServer
{
	final static int port = 4040;

	static void printUsage() {
		System.out.println("In another window type:");
		System.out.println("telnet sslabXX.cs.purdue.edu " + port);
		System.out.println("CloseVote|root|ttgwzmt5fz|hostname");
		System.out.println("GET-ALL-RES|root|ttgwzmt5fz|hostname");
		System.out.println("InitDB|root|ttgwzmt5fz|hostname|username");
		System.out.println("ADDRES|root|ttgwzmt5fz|hostname|restaurantName");
		System.out.println("voteRes|root|ttgwzmt5fz|hostname|restaurantName1|name2|.....");
		System.out.println("Register|root|ttgwzmt5fz|username|password");
		System.out.println("Login|root|ttgwzmt5fz|username|password");
	}

	
	public static void main(String[] args )
	{  
		try
		{  
			printUsage();
			int i = 1;
			ServerSocket s = new ServerSocket(port);
			while (true)
			{  
				Socket incoming = s.accept();
				System.out.println("Spawning " + i);
				Runnable r = new ThreadedHandler(incoming);
				Thread t = new Thread(r);
				t.start();
				i++;
			}
		}
		catch (IOException e)
		{  
			e.printStackTrace();
		}
	}
}

/**
   This class handles the client input for one server socket connection. 
 */
class ThreadedHandler implements Runnable
{ 
	final static String ServerUser = "root";
	final static String ServerPassword = "ttgwzmt5fz";

	public ThreadedHandler(Socket i)
	{ 
		incoming = i; 
	}

	public static Connection getConnection() throws SQLException, IOException
	{
		Properties props = new Properties();
		FileInputStream in = new FileInputStream("database.properties");
		props.load(in);
		in.close();
		String drivers = props.getProperty("jdbc.drivers");
		if (drivers != null)
			System.setProperty("jdbc.drivers", drivers);
		String url = props.getProperty("jdbc.url");
		String username = props.getProperty("jdbc.username");
		String password = props.getProperty("jdbc.password");

		System.out.println("url="+url+" user="+username+" password="+password);

		return DriverManager.getConnection( url, username, password);
	}
	void CloseVote(String [] args, PrintWriter out){
		Connection conn = null;
		try{
			conn = getConnection();
			Statement stat = conn.createStatement();
			stat.executeUpdate("USE CS252FINAL");
			stat.executeUpdate("UPDATE "+args[3]+" SET status = 'closed'");
		}
		catch (Exception e) {
			System.out.println(e.toString());
			//out.println(e.toString());
		}
		finally
		{
			try {
				if (conn!=null) conn.close();
			}
			catch (Exception e) {
			}
		}
	}
	void Login(String []  args, PrintWriter out){
		Connection conn = null;
		try{
			conn = getConnection();
			Statement stat = conn.createStatement();
			stat.executeUpdate("USE CS252FINAL");
			DatabaseMetaData dbm = conn.getMetaData();
			// check if "employee" table is there
			ResultSet tables = dbm.getTables(null, null, "USERINFO", null);
			ResultSet result = stat.executeQuery( "SELECT * FROM USERINFO WHERE username = \'"+args[3]+"\'");
			if(result.next()){
				if(result.getString(2).equals(args[4])){
					out.println(1);
				}
				else{
					out.println(2);
				}
			}
			else{
				out.println(2);

			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
			//out.println(e.toString());
		}
		finally
		{
			try {
				if (conn!=null) conn.close();
			}
			catch (Exception e) {
			}
		}
	}
	void Register(String []  args, PrintWriter out){
		Connection conn = null;
		try{
			conn = getConnection();
			Statement stat = conn.createStatement();
			stat.executeUpdate("USE CS252FINAL");
			DatabaseMetaData dbm = conn.getMetaData();
			// check if "employee" table is there
			ResultSet tables = dbm.getTables(null, null, "USERINFO", null);
			if (tables.next()) {
				// Table exists

				stat.executeUpdate( "INSERT INTO USERINFO VALUES(\'"+args[3]+"\', \'"+args[4]+"\')");
			}
			else {
				// Table does not exist
				//stat.executeUpdate("CREATE TABLE USERINFO(username VARCHAR(20), password VARCHAR(20))");
				stat.executeUpdate( "INSERT INTO USERINFO VALUES(\'"+args[3]+"\', \'"+args[4]+"\')");
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
			//out.println(e.toString());
		}
		finally
		{
			try {
				if (conn!=null) conn.close();
			}
			catch (Exception e) {
			}
		}
	}
	void InitDB(String [] args, PrintWriter out){
		Connection conn = null;
		//out.println("caoooo");
		String hostname  = args[3];
		try{
			conn = getConnection();
			Statement stat = conn.createStatement();
			stat.executeUpdate("USE CS252FINAL");
			String sql = "CREATE TABLE "+hostname+"(name VARCHAR(20), number DECIMAL(10, 0), owner VARCHAR(20), status VARCHAR(20))";
			stat.executeUpdate(sql);
			stat.executeUpdate("INSERT INTO "+hostname+" VALUES(\'kibu\', 0, \'"+args[4]+"\', \'open\')");
			stat.executeUpdate("INSERT INTO "+hostname+" VALUES(\'Dongfang\', 0, \'"+args[4]+"\', \'open\')");
			stat.executeUpdate("INSERT INTO "+hostname+" VALUES(\'GreatWall\', 0, \'"+args[4]+"\', \'open\')");
			stat.executeUpdate("INSERT INTO "+hostname+" VALUES(\'FuLam\', 0, \'"+args[4]+"\', \'open\')");
			stat.executeUpdate("INSERT INTO "+hostname+" VALUES(\'Hensei\', 0, \'"+args[4]+"\', \'open\')");

			//stat.executeUpdate("UPDATE "+hostname+" SET number = 0");
		}
		catch (Exception e) {
			System.out.println(e.toString());
			out.println(e.toString());
		}
		finally
		{
			try {
				if (conn!=null) conn.close();
			}
			catch (Exception e) {
			}
		}

	}
	void getAllRes( String [] args, PrintWriter out) {

		String hostname = args[3];
		Connection conn=null;
		try
		{
			conn = getConnection();
			Statement stat = conn.createStatement();
			ResultSet result = stat.executeQuery( "SELECT * FROM "+hostname);
			String output = "";
			if(result.next()){
				output = output+result.getString(4)+"|"+result.getString(3)+"|"+result.getString(1)+"|";
			    if(result.getString(4).equals("closed")) output += result.getString(2)+"|";
			}
			while(result.next()) {
				output += result.getString(1)+"|";
				//out.print(result.getString(2)+"|");
				//out.print(result.getString(3)+"|");
				//out.print(result.getString(4)+"|");
				if(result.getString(4).equals("closed")) output += result.getString(2)+"|";
				//out.println("");
			}
			out.print(output);
			result.close();

			/*
 	stat.executeUpdate(
           "CREATE TABLE Greetings (Message CHAR(20))");
        stat.executeUpdate(
           "INSERT INTO Greetings VALUES ('Hello, World!')");
	ResultSet result = 
            stat.executeQuery(
               "SELECT * FROM Greetings");
         while(result.next())
            System.out.println(result.getString(1));
         result.close();
         stat.executeUpdate("DROP TABLE Greetings");
			 */

		}
		catch (Exception e) {
			System.out.println(e.toString());
			out.println(e.toString());
		}
		finally
		{
			try {
				if (conn!=null) conn.close();
			}
			catch (Exception e) {
			}
		}
	}

	void addRes( String [] args, PrintWriter out) {

		Connection conn=null;
		try{
			conn = getConnection();
			Statement stat = conn.createStatement();
			stat.executeUpdate("USE CS252FINAL");
			ResultSet result = stat.executeQuery( "SELECT * FROM "+args[3]);
			String owner = "";
			if(result.next()){
				owner += result.getString(3);
			}
			stat.executeUpdate("INSERT INTO "+args[3]+" VALUES(\'"+args[4]+"\', 0, \'"+owner+"\', \'open\')");
			//stat.executeUpdate("UPDATE "+hostname+" SET number = 0");
		}
		catch (Exception e) {
			System.out.println(e.toString());
			out.println(e.toString());
		}
		finally
		{
			try {
				if (conn!=null) conn.close();
			}
			catch (Exception e) {
			}
		}
	}
	void voteRes( String [] args, PrintWriter out) {
		Connection conn=null;
		try{
			conn = getConnection();
			Statement stat = conn.createStatement();
			stat.executeUpdate("USE CS252FINAL");
			for(int i=4;i<args.length;i++){
				stat.executeUpdate("UPDATE "+args[3]+" SET number = number + 1 WHERE name Like \""+args[i]+"\"");
			}
			//stat.executeUpdate("UPDATE "+hostname+" SET number = 0");
		}
		catch (Exception e) {
			System.out.println(e.toString());
			out.println(e.toString());
		}
		finally
		{
			try {
				if (conn!=null) conn.close();
			}
			catch (Exception e) {
			}
		}
	}
	void handleRequest( InputStream inStream, OutputStream outStream) {
		//System.out.println("sadasdas");
		Scanner in = new Scanner(inStream);         
		/*try {
			ObjectInputStream tmp = new ObjectInputStream(inStream);
			Object Rin = tmp.readObject();
			//ArrayList<String> myString = (ArrayList<String>)Rin;
			//System.out.println((String)Rin);
			/*for(int i=0; i<myString.size(); ++i) {
				System.out.println(myString.get(i));
			}
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			System.out.println("sadasdas");
			e1.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		PrintWriter out = new PrintWriter(outStream, 
				true /* autoFlush */);
		//out.print("QWE");
		// Get parameters of the call
		String request = in.nextLine();
		//out.println("cani");
		System.out.println("Request="+request);

		String requestSyntax = "Syntax: COMMAND|USER|PASSWORD|OTHER|ARGS";

		try {
			// Get arguments.
			// The format is COMMAND|USER|PASSWORD|OTHER|ARGS...
			String [] args = request.split("\\|");

			// Print arguments
			for (int i = 0; i < args.length; i++) {
				System.out.println("Arg "+i+": "+args[i]);
			}

			// Get command and password
			String command = args[0];
			String user = args[1];
			String password = args[2];


			// Check user and password. Now it is sent in plain text.
			// You should use Secure Sockets (SSL) for a production environment.
			if ( !user.equals(ServerUser) || !password.equals(ServerPassword)) {
				System.out.println("Bad user or password");
				out.println("Bad user or password");
				return;
			}

			// Do the operation
			if (command.equals("GET-ALL-RES")) {
				getAllRes(args, out);
			}
			else if(command.equals("InitDB")){
				InitDB(args, out);
			}
			else if(command.equals("Register")){
				Register(args, out);
			}
			else if (command.equals("Login")){
				Login(args, out);
			}
			else if (command.equals("ADDRES")) {
				addRes(args, out);
			}
			else if (command.equals("NEWVOTE")){
				InitDB(args, out);
			}
			else if(command.equals("voteRes")){
				voteRes(args, out);
			}
			else if(command.equals("CloseVote")){
				CloseVote(args, out);
			}

		}
		catch (Exception e) {		
			System.out.println(requestSyntax);
			out.println(requestSyntax);

			System.out.println(e.toString());
			out.println(e.toString());
		}

	}

	public void run() {  
		try
		{  
			try
			{
				InputStream inStream = incoming.getInputStream();
				OutputStream outStream = incoming.getOutputStream();

				handleRequest(inStream, outStream);

			}
			catch (IOException e)
			{  
				e.printStackTrace();
			}
			finally
			{
				incoming.close();
			}
		}
		catch (IOException e)
		{  
			e.printStackTrace();
		}
	}

	private Socket incoming;
}

