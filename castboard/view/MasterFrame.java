package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.Box;
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;

public class MasterFrame extends JFrame
{
	private JMenuBar mnbActions;
	private JToolBar tlbNavigation;
	private JPanel pnlBody;
	private JPanel pnlWrapper;
	private JScrollPane sclBody;
	private LoginPopUp login;
	private boolean isConnected;

	private ArrayList<JLabel> links;
	private ArrayList<JPanel> windows;

	private static MasterFrame instance = null;

	private MasterFrame ()
	{
		super("CastBoard");

		links = new ArrayList<JLabel>();
		windows = new ArrayList<JPanel>();

		createMnbActions();
		createTlbNavigation();
		createPnlBody();
		createPnlWrapper();
		
		isConnected = false;

		ImageIcon icon = new ImageIcon("castboard/res/icons/" + 
														"ico_128_cb.png");
		this.setIconImage(icon.getImage());
		this.setSize(800, 600);
		this.add(pnlWrapper);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing (WindowEvent e)
			{
				closeWindow();
			}
		});
		
		this.setVisible(true);
		
		login= new LoginPopUp(this);
		login.display();
	}

	public static MasterFrame getInstance ()
	{
		if (instance == null)
			instance = new MasterFrame();

		return instance;
	}

	private void createMnbActions ()
	{
		JMenu mnEntry = new JMenu("Ingreso");
		JMenu mnSearch = new JMenu("Búsqueda");
		JMenu mnBin = new JMenu("Papelera");
		JMenu mnLog = new JMenu("Iniciar Sesión");
		JMenuItem mniTalentEntry = new JMenuItem("Talento");
		JMenuItem mniProjectEntry = new JMenuItem("Proyecto");
		JMenuItem mniTalentSearch = new JMenuItem("Talento");
		JMenuItem mniProjectSearch = new JMenuItem("Proyecto");

		mnbActions = new JMenuBar();
		
		mnEntry.setToolTipText("Menú de ingreso");
		mnSearch.setToolTipText("Menú de búsqueda");
		mnBin.setToolTipText("Papelera de eliminados");
		mnLog.setToolTipText("Inicio de sesión");
		mniTalentEntry.setToolTipText("Ingreso de talento");
		mniProjectEntry.setToolTipText("Ingreso de proyecto");
		mniTalentSearch.setToolTipText("Búsqueda de talento");
		mniProjectSearch.setToolTipText("Búsqueda de proyecto");
		
		mnBin.addMenuListener(new MenuListener()
		{
			public void menuSelected (MenuEvent e)
			{
				if (isConnected)
					{}//TODO call bin window}
				else
					(new CautionPopUp(getInstance())).display("Debe iniciar sesión para acceder a la" +
														   " papelera");
			}
			public void menuDeselected (MenuEvent e) {}
			public void menuCanceled (MenuEvent e) {}
		});
		mnLog.addMenuListener(new MenuListener()
		{
			public void menuSelected (MenuEvent e)
			{
				if (isConnected)
					logOut();
				else
					login.display();
			}
			public void menuDeselected (MenuEvent e) {}
			public void menuCanceled (MenuEvent e) {}
		});
		mniTalentEntry.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (isConnected)
					{}//TODO call talent entry window}
				else
					(new CautionPopUp(getInstance())).display("Debe iniciar sesión para acceder al " +
														   "ingreso");
			}
		});
		mniProjectEntry.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (isConnected)
					{}//TODO call project entry window
				else
					(new CautionPopUp(getInstance())).display("Debe iniciar sesión para acceder al " +
														   "ingreso");
			}
		});
		mniTalentSearch.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (isConnected)
					{}//TODO call talent search window}
				else
					(new CautionPopUp(getInstance())).display("Debe iniciar sesión para acceder a la" +
														   "búsqueda");
			}
		});
		mniProjectSearch.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (isConnected)
					{}//TODO call project search window
				else
					(new CautionPopUp(getInstance())).display("Debe iniciar sesión para acceder a la" + 
														   "búsqueda");
			}
		});
		//TODO: set mnemonics and listeners

		mnEntry.add(mniTalentEntry);
		mnEntry.add(mniProjectEntry);
		mnSearch.add(mniTalentSearch);
		mnSearch.add(mniProjectSearch);
		
		mnbActions.add(mnEntry);
		mnbActions.add(mnSearch);
		mnbActions.add(mnBin);
		mnbActions.add(Box.createHorizontalGlue());
		mnbActions.add(mnLog);
	}
	private void createTlbNavigation ()
	{
		tlbNavigation = new JToolBar();
		tlbNavigation.setFloatable(false);
		
		pushLblLink("");
	}
	private void createPnlBody ()
	{
		pnlBody = new JPanel();
		pnlBody.setLayout(new CardLayout());
		
		sclBody = new JScrollPane(pnlBody);
	}
	private void createPnlWrapper()
	{
		pnlWrapper = new JPanel();
		pnlWrapper.setLayout(new BorderLayout());
		
		pnlWrapper.add(mnbActions, BorderLayout.PAGE_START);
		pnlWrapper.add(sclBody, BorderLayout.CENTER);
		pnlWrapper.add(tlbNavigation, BorderLayout.PAGE_END);
	}
	
	public void pushLblLink (String link)
	{
		JLabel lblLink = new JLabel(" \u25B8" + link);

		lblLink.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked (MouseEvent e)
			{
				if (links.size() > 1)
				{
					JLabel lblLink = (JLabel) e.getSource();
					String windowTitle = lblLink.getText();
					CardLayout lytCard = (CardLayout) pnlBody.getLayout();

					lytCard.show(pnlBody, windowTitle);
					popWindows(windows.get(links.indexOf(lblLink) - 1));
					popLblLinks(lblLink);
				}
			}
		});
		
		if (!(links.isEmpty()))
			styleLink();
		
		links.add(lblLink);
		tlbNavigation.add(lblLink);
	}
	public void popLblLinks (JLabel lblLink)
	{
		int fromIndex = links.indexOf(lblLink) + 1;
		int toIndex = links.size();
		ArrayList<JLabel> popLinks= new ArrayList<JLabel>(links.subList(fromIndex, toIndex));

		for (int i = 0; i < popLinks.size(); i++)
		{
			tlbNavigation.remove(popLinks.get(i));
		}
		links.removeAll(popLinks);
	}
	public void styleLink ()
	{
		JLabel link = links.get(links.size() - 1);

		Font font = link.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		link.setFont(font.deriveFont(attributes));

		link.setForeground(Color.CYAN);
	}

	public void popWindows (JPanel window)
	{
		int fromIndex = windows.indexOf(window) + 1;
		int toIndex = windows.size();
		ArrayList<JPanel> popWindows = new ArrayList<JPanel>(windows.subList(fromIndex, toIndex));

		for (int i = 0; i < popWindows.size(); i++)
		{
			pnlBody.remove(popWindows.get(i));
		}
		windows.removeAll(popWindows);
	}
	
	public void logIn()
	{
		isConnected = true;
		mnbActions.getMenu(4).setText("Cerrar sesión");
		displayFront();

					System.out.println("log");
	}
	public void logOut()
	{
		isConnected = false;
		mnbActions.getMenu(4).setText("Iniciar sesión");
		displayBlank();
	}
	
	public void displayFront ()
	{

					System.out.println(isConnected);
		if (isConnected)
			{pnlBody.add(new JLabel("" + isConnected), "conexion");
			
								System.out.println("in");}
	}
	public void displayBlank ()
	{
		for (int i = 0; i < windows.size(); i++)
		{
			pnlBody.remove(windows.get(i));
		}
		for (int i = 1; i < links.size(); i++)
		{
			tlbNavigation.remove(links.get(i));
		}
		links.removeAll(links.subList(1, links.size()));
		windows.clear();
	}
	public void displayException (String message)
	{
		(new ExceptionPopUp(this)).display(message);
	}
	
	public void setIsConnected (boolean isConnected)
	{
		this.isConnected = isConnected;
	}

	private void closeWindow ()
	{
		boolean isConfirmed = (new ConfirmationPopUp(getInstance())).display("Se cerrará la aplicación " +
																			", el trabajo sin guardar" +
																			" se perderá");

		if (isConfirmed)
		{
			if (isConnected)
				CatalogsHandler.disconnect();

			getInstance().dispose();
		}
	}
}