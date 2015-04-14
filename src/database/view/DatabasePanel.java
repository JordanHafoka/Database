package database.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.controller.DatabaseAppController;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DatabasePanel extends JPanel
{
	private DatabaseAppController baseController;
	private SpringLayout baseLayout;
	private JButton queryButton;
	private JScrollPane displayPane;
	private JTextArea displayArea;
	private JTable resultsTable;
	private JPasswordField samplePassword;
	
	
	public DatabasePanel(DatabaseAppController baseController)
	{
		
		this.baseController = baseController;
		baseLayout = new SpringLayout();
		queryButton = new JButton();
		queryButton.setText("Click here to test the query");
		displayPane = new JScrollPane();
		displayArea = new JTextArea();
		resultsTable = new JTable();
		samplePassword = new JPasswordField();
		
		

		setupTable();
		setupPanel();
		setupLayout();
		setupListeners();
		
		
	}


	private void setupTable()
	{
		DefaultTableModel basicData = new DefaultTableModel(baseController.getDataController().testResults(),
				baseController.getDataController().getMetaDataTitles());
		resultsTable = new JTable(basicData);
		displayPane = new JScrollPane(resultsTable);
		
	}


	private void setupLayout()
	{
		baseLayout.putConstraint(SpringLayout.NORTH, queryButton, 34, SpringLayout.NORTH, this);
		baseLayout.putConstraint(SpringLayout.WEST, queryButton, 232, SpringLayout.WEST, this);
		baseLayout.putConstraint(SpringLayout.WEST, samplePassword, 25, SpringLayout.WEST, this);
		baseLayout.putConstraint(SpringLayout.EAST, samplePassword, 199, SpringLayout.WEST, this);
		baseLayout.putConstraint(SpringLayout.NORTH, displayArea, 63, SpringLayout.SOUTH, queryButton);
		baseLayout.putConstraint(SpringLayout.WEST, displayArea, 27, SpringLayout.WEST, this);
		baseLayout.putConstraint(SpringLayout.SOUTH, displayArea, 254, SpringLayout.NORTH, this);
		baseLayout.putConstraint(SpringLayout.EAST, displayArea, 263, SpringLayout.WEST, this);
		baseLayout.putConstraint(SpringLayout.NORTH, displayPane, 18, SpringLayout.SOUTH, displayArea);
		baseLayout.putConstraint(SpringLayout.WEST, displayPane, 0, SpringLayout.WEST, displayArea);
		baseLayout.putConstraint(SpringLayout.SOUTH, displayPane, 709, SpringLayout.NORTH, this);
		baseLayout.putConstraint(SpringLayout.EAST, displayPane, 562, SpringLayout.WEST, this);
		
		
	}


	private void setupPanel()
	{
		this.setLayout(baseLayout);
		this.setBackground(Color.MAGENTA);
		this.setSize(1000,1000);
		this.add(displayPane);
		this.add(displayArea);
		this.add(queryButton);
		this.add(samplePassword);
//		samplePassword.setEchoChar((char)'');
		samplePassword.setFont(new Font("Serif", Font.BOLD, 32));
		samplePassword.setForeground(Color.MAGENTA);
		
		
	}
	private void setupListeners()
	{
		queryButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent click)
			{
				String[] temp = baseController.getDataController().getMetaDataTitles();
				for(String current : temp)
				{
					displayArea.setText(displayArea.getText() + "Column: " +current + "\n");
				}
				
				
			}
			
		});
	}
}
