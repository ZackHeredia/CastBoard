package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.SwingWorker;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class WasteBasketWindow extends Window
{
	JPanel pnlAction;
	JPanel pnlTable;
	JButton btnView;
	JButton btnRestore;
	String[][] items;

	public WasteBasketWindow ()
	{
		masterFrame = MasterFrame.getInstance();
		
		init();
	}

	protected void init ()
	{
		WasteBasketWindow wasteBasket = this;

		SwingWorker worker = new SwingWorker<Void, Void>()
		{

			protected Void doInBackground ()
			{
				items = CatalogsHandler.getSuppressed();

				createPnlAction();
				createPnlTable();

				return null;
			}
			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				wasteBasket.setLayout(new BoxLayout(wasteBasket, BoxLayout.Y_AXIS));

				wasteBasket.add(pnlAction);
				wasteBasket.add(pnlTable);
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}

	private void createPnlAction ()
	{
		btnView = new JButton("Ver");
		btnRestore = new JButton("Restaurar");
		pnlAction = new JPanel();

		btnView.setEnabled(false);
		btnRestore.setEnabled(false);

		btnView.setToolTipText("Seleccione una fila para ver el item");
		btnRestore.setToolTipText("Seleccione una fila para restaurar el item");

		pnlAction.add(btnView);
		pnlAction.add(btnRestore);
	}
	private void createPnlTable ()
	{
		String[] columns = {"ID", "Fecha de creación", "Nombre/Titulo"};
		JTable tblItems = new JTable(items, columns)
		{
			public boolean isCellEditable(int row, int column)
			{                
                return false; 
            } 
		};
		JScrollPane sclItems = new JScrollPane(tblItems);

		pnlTable = new JPanel();

		pnlTable.setLayout(new BorderLayout());

		tblItems.setFillsViewportHeight(true);

		tblItems.setRowSelectionAllowed(true);
		tblItems.setColumnSelectionAllowed(false);
		tblItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tblItems.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged (ListSelectionEvent e)
			{
				String selectedItemId = items[tblItems.getSelectedRow()][0];
				String selectedItemType = items[tblItems.getSelectedRow()][3];

				try
				{
					btnView.removeActionListener(btnView.getActionListeners()[0]);
					btnRestore.removeActionListener(btnRestore.getActionListeners()[0]);
				}
				finally
				{
					if (selectedItemType.equals("Talento"))
					{
						btnView.addActionListener(new ActionListener()
						{
							public void actionPerformed (ActionEvent e)
							{
								masterFrame.displayTalentDetail(selectedItemId);
							}
						});
					}
					else
					{
						btnView.addActionListener(new ActionListener()
						{
							public void actionPerformed (ActionEvent e)
							{
								masterFrame.displayProjectDetail(selectedItemId);
							}
						});
					}

					btnRestore.addActionListener(new ActionListener()
					{
						public void actionPerformed (ActionEvent e)
						{
							CatalogsHandler.restore(selectedItemId);
						}
					});

					btnView.setToolTipText("Ver el item seleccionado");
					btnRestore.setToolTipText("Restaurar el item seleccionado");

					btnView.setEnabled(true);
					btnRestore.setEnabled(true);
				}
			}
		});

		pnlTable.add(sclItems);
	}
}