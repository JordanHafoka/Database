package database.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import database.model.QueryInfo;
import database.view.DatabaseFrame;

/**
 * DatabaseController class used to control the connection between Eclipse and our Database.
 * @author jhaf4009
 *
 */
public class DatabaseController
{
	private  ArrayList<QueryInfo> queryList;
	private DatabaseFrame baseFrame;
	private String connectionString;
	private Connection databaseConnection;
	private DatabaseAppController baseController;
	private String query;
	private String currentQuery;
	private long queryTime;
	
	/**
	 * The Constructor for the DatabaseController class.
	 * @param baseController
	 */
	public DatabaseController(DatabaseAppController baseController)
	{
		this.baseController = baseController;
		this.connectionString = "jdbc:mysql://localhost/games?user=root";
		queryTime = 0;
		
		checkDriver();
		connectionStringBuilder(connectionString, connectionString, connectionString, connectionString);
		setupConnection();
	}
	/**
	 * Sets the connection to the database.
	 */
	public void setupConnection()
	{
		try
		{
			databaseConnection = DriverManager.getConnection(connectionString);
		}
		catch(SQLException currentException)
		{
			displayErrors(currentException);
		}
	}
	/**
	 * Closes connection to the database.
	 */
	private void closeConnection()
	{
		try
		{
			databaseConnection.close();
		}
		catch(SQLException currentException)
		{
			displayErrors(currentException);
		}
	}
	/**
	 * Checks the driver and displays an error if connection can not be made.
	 */
	private void checkDriver()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(Exception currentException)
		{
			displayErrors(currentException);
			System.exit(1);
		}
	}
	/**
	 * Returns a String[] that contains the Meta Data from answers.
	 * @return columns
	 */
	public String[] getMetaDataTitles()
	{
		String[] columns = null;
		String tableNames = "";
		String query = "SELECT * FROM `INNODB_SYSCOLUMNS`";
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answers = firstStatement.executeQuery(query);
			
			ResultSetMetaData answerData = answers.getMetaData();
			columns = new String[answerData.getColumnCount()];
			
			for(int column=0; column<answerData.getColumnCount(); column++)
			{
				columns[column] = answerData.getColumnName(column+1);
			}
			
			answers.close();
			firstStatement.close();
			endTime = System.currentTimeMillis();
		}
		catch(SQLException currentException)
		{
			endTime = System.currentTimeMillis();
			columns = new String[] {"empty"};
			displayErrors(currentException);
		}
		queryTime = endTime - startTime;
		baseController.getQueryList().add(new QueryInfo(query, queryTime));
		return columns;
	}
	public void connectionStringBuilder(String pathToDBServer, String databaseName,String userName, String password)
	{
		connectionString = "jdbc:mysql://";
		connectionString += pathToDBServer;
		connectionString += "/"+ databaseName;
		connectionString += "?user=" + userName;
		connectionString += "&password=" + password;
	}
	/**
	 * Checks the query for any DDL statements.
	 * @return
	 */
	private boolean checkQueryForDataViolation()
	{
		if(query.toUpperCase().contains(" DROP ")
				||query.toUpperCase().contains(" TRUNCATE ")
				||query.toUpperCase().contains(" SET ")
				||query.toUpperCase().contains(" ALTER "))
			return true;
		else
			return false;
		
		
	}
	/**
	 * Returns a statement depicting what was dropped, if anything.
	 */
	public void dropStatement()
	{
		String results;
		try
		{
			if(checkForStructureViolation())
			{
				throw new SQLException("You do not have the required persmissions for action:", "Dropping Databases",
						Integer.MIN_VALUE);
			}
			if(currentQuery.toUpperCase().contains(" INDEX "))
			{
				results = "The index was ";
			}
			else
			{
				results = "The Table was ";
			}
			Statement dropStatement = databaseConnection.createStatement();
			int affected = dropStatement.executeUpdate(currentQuery);
			
			dropStatement.close();
			
			if(affected == 0)
			{
				results += "dropped";
			}
			JOptionPane.showMessageDialog(baseController.getBaseFrame(), results);
		}
		catch(SQLException dropError)
		{
			displayErrors(dropError);
		}
	}
	
	private boolean checkForStructureViolation()
	{
		if(currentQuery.toUpperCase().contains(" DATABASE " ))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * 
	 * @param query	
	 * @return
	 */
	public String [][] selectQueryResults(String query)
	{
		String[][] results;
		this.query = query;
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		try
		{
			if(checkQueryForDataViolation())
			{
				throw new SQLException("There was an attempt at a data violation.","You are not allowed to "
						+ "access the data in here.", Integer.MIN_VALUE);
			}
			
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answers = firstStatement.executeQuery(query);
			int columnCount = answers.getMetaData().getColumnCount();
			
			answers.last();
			int numberOfRows = answers.getRow();
			answers.beforeFirst();
			
			results = new String[numberOfRows][columnCount];
			while(answers.next())
			{
				for(int col = 0; col < columnCount; col++)
				{
					results[answers.getRow()-1][col] = answers.getString(col + 1);
				}
			}
			answers.close();
			firstStatement.close();
			endTime = System.currentTimeMillis();
		}
		catch(SQLException currentException)
		{
			results = new String[][] {{"empty"}};
			displayErrors(currentException);
			endTime = System.currentTimeMillis();
		}
		queryTime = endTime - startTime;
		return results;
	}
	/**
	 * Returns a 2D array named results that contains Strings in answers.
	 * @return results
	 */
	public String[][] testResults()
	{
		String[][] results;
		String query = "SHOW TABLES";
		
		try
		{
			Statement firstStatement =  databaseConnection.createStatement();
			ResultSet answers = firstStatement.executeQuery(query);
			answers.last();
			int numberOfRows = answers.getRow();
			
			results = new String[numberOfRows][1];
			
			while(answers.next())
			{
				results[answers.getRow()-1][0] = answers.getString(1);
			}
			
			answers.close();
			firstStatement.close();
		}
		catch(SQLException currentException)
		{
			results = new String[][] {{"empty"}};
			displayErrors(currentException);
		}
		return results;
	}
	/**
	 * A method used to display errors should they come up.
	 * @param currentException
	 */
	public void displayErrors(Exception currentException)
	{
		JOptionPane.showMessageDialog(baseController.getBaseFrame(), "Exception: "+ currentException.getMessage());
		if(currentException instanceof SQLException)
		{
			JOptionPane.showMessageDialog(baseController.getBaseFrame(), "SQL State: " +((SQLException)currentException).getSQLState());
			JOptionPane.showMessageDialog(baseController.getBaseFrame(), "SQL Error Code: "+((SQLException) currentException).getErrorCode());
		}
	}
	public String displayTables()
	{
		String tableNames = "";
		String query = "SHOW TABLES";
		
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answers = firstStatement.executeQuery(query);
			while(answers.next())
			{
				tableNames += answers.getString(1) + "\n";
			}
		}
		catch(SQLException currentError)
		{
			displayErrors(currentError);
		}
		return tableNames;
	}
	public int insertSample()
	{
		int rowsAffected = -1;
		String query = "INSERT INTO `games`.`hearthstone` (`card_name`,`mana_cost`, `rarity`,`damage/health`,`description`,"
				+ "`deck_type`) VALUES (`Novice Engineer`,`2`,`Free`,`1/1`,`Draw A Card.`,`1`);";
		
		try
		{
			Statement insertStatement = databaseConnection.createStatement();
			rowsAffected = insertStatement.executeUpdate(query);
			insertStatement.close();
		}
		catch(SQLException currentError)
		{
			displayErrors(currentError);
		}
		return rowsAffected;
	}
	public String[][] realResults()
	{
		String[][] results;
		query = "SELECT * FROM `INNODB_SYS_COLUMNS`";
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answers = firstStatement.executeQuery(query);
			int columnCount = answers.getMetaData().getColumnCount();
			
			answers.last();
			int numberOfRows = answers.getRow();
			answers.beforeFirst();
			
			results = new String[numberOfRows][columnCount];
			while(answers.next())
			{
				for(int col = 0; col < columnCount; col++)
				{
					results[answers.getRow()-1][col] = answers.getString(col + 1);
				}
			}
			answers.close();
			firstStatement.close();
		}
		catch(SQLException currentException)
		{
			results = new String[][] {{"empty"}};
			displayErrors(currentException);
		}
		return results;
	}
	public String getQuery()
	{
		return query;
	}
	public void setQuery(String query)
	{
		this.query = query;
	}
	public ArrayList<QueryInfo> getQueryList()
	{
		return queryList;
	}
	
}
