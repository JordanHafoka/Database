package database.controller;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import database.model.QueryInfo;
import database.view.DatabaseFrame;

public class DatabaseAppController
{
	private DatabaseFrame baseFrame;
	private DatabaseController dataController;
	private ArrayList<QueryInfo> queryList;
	
	
	public DatabaseAppController()
	{
		dataController = new DatabaseController(this);
		queryList = new ArrayList<QueryInfo>();
		baseFrame = new DatabaseFrame(this);
	} 
	
	public DatabaseFrame getBaseFrame()
	{
		return baseFrame;
	}
	public DatabaseController getDataController()
	{
		return dataController;
	}
	public ArrayList<QueryInfo> getQueryList()
	{
		return queryList;
	}
	
	public void start()
	{
				
	}
	
}
