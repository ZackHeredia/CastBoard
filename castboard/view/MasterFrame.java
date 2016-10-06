package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.JButton;
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
import javax.swing.BorderFactory;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Map;

public class MasterFrame extends JFrame
{
	private JMenuBar mnbActions;
	private JToolBar tlbNavigation;
	private JPanel pnlBody;
	private JPanel pnlWrapper;
	private JScrollPane sclBody;
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
		this.setSize(800, 640);
		this.add(pnlWrapper);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowOpened (WindowEvent e)
			{
				displayLogin();
			}
			public void windowClosing (WindowEvent e)
			{
				closeWindow();
			}
		});

		this.setVisible(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
					displayLogin();
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

					lblLink.setToolTipText("Regresar a " + link);

					lytCard.show(pnlBody, windowTitle);
					popWindows(windows.get(links.indexOf(lblLink)));
					popLblLinks(lblLink);
				}
			}
		});
		
		if (!(links.isEmpty()))
			styleLink(links.get(links.size()));

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
	@SuppressWarnings("unchecked")
	public void styleLink (JLabel link)
	{
		Font font = link.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		link.setFont(font.deriveFont(attributes));

		link.setForeground(new Color(0, 146, 182));
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
	}
	public void logOut()
	{
		boolean isConfirmed = (new ConfirmationPopUp(getInstance())).display("Se cerrará la sesión " +
																			", el trabajo sin guardar" +
																			" se perderá");

		if (isConfirmed)
		{
			CatalogsHandler.disconnect();
			isConnected = false;
			mnbActions.getMenu(4).setText("Iniciar sesión");
			displayBlank();
		}
	}
	
	public void displayLogin ()
	{
		if (!isConnected)
			(new LoginPopUp(this)).display();
	}
	public void displayFront ()
	{
		FrontWindow front = new FrontWindow();
		String title = "Portada";
		CardLayout lytCard = (CardLayout) pnlBody.getLayout();

		if (isConnected)
		{
			pnlBody.add(front, title);
			windows.add(front);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}
	public void displayBlank ()
	{
		for (int i = 0; i < windows.size(); i++)
		{
			pnlBody.remove(windows.get(i));
			tlbNavigation.remove(links.get(i));
		}
		pnlBody.revalidate();
		tlbNavigation.revalidate();
		this.repaint();

		links.clear();
		windows.clear();
	}
	public void displayProjectSet ()
	{

	}
	public void displayTalentSet ()
	{

	}
	public void displayException (String message)
	{
		(new ExceptionPopUp(this)).display(message);
	}
	
	public void setIsConnected (boolean isConnected)
	{
		this.isConnected = isConnected;
	}

	public ArrayList<JButton> makeProjectThumbnails (ArrayList<ArrayList<String>> projects)
	{
		ArrayList<JButton> thumbnails = new ArrayList<JButton>();
		JButton thumbnail;
		JPanel list;
		JLabel title;
		JLabel type;
		JLabel producer;
		JLabel state;

		for (ArrayList<String> project : projects)
		{
			thumbnail = new JButton("");
			thumbnail.setLayout(new BoxLayout(thumbnail, BoxLayout.Y_AXIS));
			thumbnail.setPreferredSize(new Dimension(132, 196));

			list = new JPanel();
			list.setLayout(new BorderLayout());
			list.setOpaque(false);

			thumbnail.setName(project.get(0));
			title = new JLabel(project.get(1));
			type = new JLabel("\u2022 " + project.get(2));
			producer = new JLabel("\u2022 " + project.get(3));
			state = new JLabel("\u2022 " + project.get(4));

			title.setFont(new Font("Serif", Font.BOLD, 18));
			title.setAlignmentX(Component.CENTER_ALIGNMENT);

			list.add(type, BorderLayout.NORTH);
			list.add(producer, BorderLayout.CENTER);
			list.add(state, BorderLayout.SOUTH);

			thumbnail.setToolTipText("Ver detalle del proyecto");
			title.setToolTipText("Titulo");
			type.setToolTipText("Tipo");
			producer.setToolTipText("Productor");
			state.setToolTipText("Estado");

			thumbnail.add(Box.createRigidArea(new Dimension(60, 60)));
			thumbnail.add(title);
			thumbnail.add(Box.createRigidArea(new Dimension(60, 60)));
			thumbnail.add(list);

			thumbnail.addActionListener(new ActionListener()
			{
				public void actionPerformed (ActionEvent e)
				{
					String id = ((JButton) e.getSource()).getName();
					//CatalogsHandler.get(id, CatalogsHandler.PROJECT_SET);
				}
			});

			thumbnails.add(thumbnail);
		}

		return thumbnails;
	}
	public ArrayList<JButton> makeTalentThumbnails (ArrayList<ArrayList<String>> talents)
	{
		ArrayList<JButton> thumbnails = new ArrayList<JButton>();
		JButton thumbnail;
		JPanel photo;//TODO photo code
		JPanel list;
		JLabel name;
		JLabel age;
		JLabel profileType;

		for (ArrayList<String> talent : talents)
		{
			thumbnail = new JButton("");
			thumbnail.setLayout(new BoxLayout(thumbnail, BoxLayout.Y_AXIS));
			thumbnail.setPreferredSize(new Dimension(132, 196));

			photo = new JPanel();
			photo.setBorder(BorderFactory.createLineBorder(Color.black));
			photo.setBackground(new Color(210, 210, 210));

			list = new JPanel();
			list.setLayout(new BorderLayout());
			list.setOpaque(false);

			thumbnail.setName(talent.get(0));
			name = new JLabel("\u2022 " + talent.get(1));
			age = new JLabel("\u2022 " + talent.get(2));
			profileType = new JLabel("\u2022 " + talent.get(3));

			thumbnail.setToolTipText("Ver detalle del talento");
			photo.setToolTipText("Foto");
			name.setToolTipText("Nombre");
			age.setToolTipText("Edad");
			profileType.setToolTipText("Tipo de perfil");

			photo.add(Box.createRigidArea(new Dimension(128, 128)));

			list.add(name, BorderLayout.NORTH);
			list.add(age, BorderLayout.CENTER);
			list.add(profileType, BorderLayout.SOUTH);

			thumbnail.add(photo);
			thumbnail.add(list);

			thumbnail.addActionListener(new ActionListener()
			{
				public void actionPerformed (ActionEvent e)
				{
					String id = ((JButton) e.getSource()).getName();
					//CatalogsHandler.get(id, CatalogsHandler.TALENT_SET);
				}
			});

			thumbnails.add(thumbnail);
		}

		return thumbnails;
	}

	private void closeWindow ()
	{
		if (isConnected)
		{
			boolean isConfirmed = (new ConfirmationPopUp(getInstance())).display("Se cerrará la aplicación " +
																					", el trabajo sin guardar" +
																					" se perderá");
		
			if (isConfirmed)
			{	
				CatalogsHandler.disconnect();
				getInstance().dispose();
			}
		}
		else
			getInstance().dispose();
	}
}