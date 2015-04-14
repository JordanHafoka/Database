package database.view;

import java.awt.Container;

import javax.swing.*;

import database.controller.DatabaseAppController;
/**
 * The Frame for the GUI used in the Database Project.
 * @author jhaf4009
 *
 */
public class DatabaseFrame extends JFrame
{
	private DatabasePanel basePanel;
	private DatabaseAppController baseController;
	/**
	 * The constructor for the DatabaseFrame class.
	 * @param baseController
	 */
	public DatabaseFrame(DatabaseAppController baseController)
	{
		this.baseController = baseController;
		basePanel = new DatabasePanel(baseController);
		
		setupFrame();
	}

	private void setupFrame()
	{
		this.setContentPane(basePanel);
		this.setSize(1000,1000);
		this.setVisible(true);
	}
}

