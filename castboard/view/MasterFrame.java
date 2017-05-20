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
import javax.swing.Timer;
import javax.swing.JComponent;
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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Composite;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import javax.swing.BorderFactory;
import javax.swing.JLayer;
import javax.swing.plaf.LayerUI;
import javax.swing.JScrollBar;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.beans.PropertyChangeEvent;
import javafx.application.Platform;

public class MasterFrame extends JFrame
{
	private JMenuBar mnbActions;
	private JToolBar tlbNavigation;
	private JPanel pnlBody;
	private JPanel pnlWrapper;
	private JScrollPane sclBody;
	private WaitLayerUI luiBody;
	private JLayer<JScrollPane> lyrBody;
	private boolean isConnected;

	private ArrayList<JLabel> links;
	private ArrayList<Window> windows;

	private static MasterFrame instance = null;

	private MasterFrame ()
	{
		super("CastBoard");

		links = new ArrayList<JLabel>();
		windows = new ArrayList<Window>();

		createMnbActions();
		createTlbNavigation();
		createPnlBody();
		createPnlWrapper();
		
		isConnected = false;

		ImageIcon icon = new ImageIcon("castboard/res/icons/ico_128_cb.png");

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
					displayWasteBasket();
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
				{
					boolean isUnique = true;

					for (JLabel lblLink : links)
					{
						if (lblLink.getText().equals(" \u25B8Ingreso de Talento"))
						{
							isUnique = false;
							break;
						}
					}

					if (isUnique)
						displayTalentEntry();
					else if (getInstance().getTitle().equals("Ingreso de Talento - CastBoard"))
						(new CautionPopUp(getInstance())).display("Ya se encuentra en el ingreso de " +
																  "talento");
					else
						(new CautionPopUp(getInstance())).display("Regrese al ingreso de talento " + 
																  "mediante la barra de navegación");
				}
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
				{
					boolean isUnique = true;

					for (JLabel lblLink : links)
					{
						if (lblLink.getText().equals(" \u25B8Ingreso de Proyecto"))
						{
							isUnique = false;
							break;
						}
					}

					if (isUnique)
						displayProjectEntry();
					else if (getInstance().getTitle().equals("Ingreso de Proyecto - CastBoard"))
						(new CautionPopUp(getInstance())).display("Ya se encuentra en el ingreso de " +
																  "proyecto");
					else
						(new CautionPopUp(getInstance())).display("Regrese al ingreso de proyecto " +
																  "mediante la barra de navegación");
				}
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
				{
					boolean isUnique = true;

					for (JLabel lblLink : links)
					{
						if (lblLink.getText().equals(" \u25B8Búsqueda de Talentos"))
						{
							isUnique = false;
							break;
						}
					}

					if (isUnique)
						displayTalentSearch();
					else if (getInstance().getTitle().equals("Búsqueda de Talentos - CastBoard"))
						(new CautionPopUp(getInstance())).display("Ya se encuentra en la búsqueda de " +
																  "talentos");
					else
						(new CautionPopUp(getInstance())).display("Regrese a la búsqueda de talentos " + 
																  "mediante la barra de navegación");
				}
				else
					(new CautionPopUp(getInstance())).display("Debe iniciar sesión para acceder a la " +
														   	  "búsqueda");
			}
		});
		mniProjectSearch.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				if (isConnected)
				{
					boolean isUnique = true;

					for (JLabel lblLink : links)
					{
						if (lblLink.getText().equals(" \u25B8Búsqueda de Proyectos"))
						{
							isUnique = false;
							break;
						}
					}

					if (isUnique)
						displayProjectSearch();
					else if (getInstance().getTitle().equals("Búsqueda de Proyectos - CastBoard"))
						(new CautionPopUp(getInstance())).display("Ya se encuentra en la búsqueda de " +
																  "proyectos");
					else
						(new CautionPopUp(getInstance())).display("Regrese a la búsqueda de proyectos " +
																  "mediante la barra de navegación");
				}
				else
					(new CautionPopUp(getInstance())).display("Debe iniciar sesión para acceder a la " + 
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

		luiBody = new WaitLayerUI();
		lyrBody = new JLayer<JScrollPane>(sclBody, luiBody);
	}
	private void createPnlWrapper()
	{
		pnlWrapper = new JPanel();
		pnlWrapper.setLayout(new BorderLayout());
		
		pnlWrapper.add(mnbActions, BorderLayout.PAGE_START);
		pnlWrapper.add(lyrBody, BorderLayout.CENTER);
		pnlWrapper.add(tlbNavigation, BorderLayout.PAGE_END);
	}

	public void displayLogin ()
	{
		if (!isConnected)
			(new LoginPopUp(this)).display();
	}
	public void displayFront ()
	{
		FrontWindow front;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			front = new FrontWindow();
			scrollTop();
			title = "Portada";
			lytCard = (CardLayout) pnlBody.getLayout();

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
	public void displayTalentSet ()
	{
		TalentSetWindow talentSet;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			talentSet = new TalentSetWindow();
			scrollTop();
			title = "Catálogo de Talentos";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(talentSet, title);
			windows.add(talentSet);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}
	public void displayProjectSet ()
	{
		ProjectSetWindow projectSet;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			projectSet = new ProjectSetWindow();
			scrollTop();
			title = "Catálogo de Proyectos";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(projectSet, title);
			windows.add(projectSet);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}
	public void displaySequenceBreakdown (String id, TreeMap<String, String> roles)
	{
		SequenceBreakdownWindow sequenceBreakdown;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			sequenceBreakdown = new SequenceBreakdownWindow(id, roles);
			scrollTop();
			title = "Desglose de Secuencias";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(sequenceBreakdown, title);
			windows.add(sequenceBreakdown);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}
	public void displayTalentSearch ()
	{
		TalentSearchWindow talentSearch;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			talentSearch = new TalentSearchWindow();
			scrollTop();
			title = "Búsqueda de Talentos";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(talentSearch, title);
			windows.add(talentSearch);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}public void displayTalentSearch (String projectId, String roleName)
	{
		TalentSearchWindow talentSearch;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			talentSearch = new TalentSearchWindow(projectId, roleName);
			scrollTop();
			title = "Búsqueda de Talentos";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(talentSearch, title);
			windows.add(talentSearch);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}
	public void displayProjectSearch ()
	{
		ProjectSearchWindow projectSearch;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			projectSearch = new ProjectSearchWindow();
			scrollTop();
			title = "Búsqueda de Proyectos";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(projectSearch, title);
			windows.add(projectSearch);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}
	public void displayTalentDetail (String id)
	{
		TalentDetailWindow talentDetail;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			talentDetail = new TalentDetailWindow(id);
			scrollTop();
			title = "Detalle de Talento";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(talentDetail, title);
			windows.add(talentDetail);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}public void displayTalentDetail (String id, String projectId, String roleName)
	{
		TalentDetailWindow talentDetail;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			talentDetail = new TalentDetailWindow(id, projectId, roleName);
			scrollTop();
			title = "Detalle de Talento";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(talentDetail, title);
			windows.add(talentDetail);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}
	public void displayProjectDetail (String id)
	{
		ProjectDetailWindow projectDetail;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			projectDetail = new ProjectDetailWindow(id);
			scrollTop();
			title = "Detalle de Proyecto";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(projectDetail, title);
			windows.add(projectDetail);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}
	public void displaySequenceDetail (String id, String pTitle)
	{
		SequenceDetailWindow sequenceDetail;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			sequenceDetail = new SequenceDetailWindow(id, pTitle);
			scrollTop();
			title = "Detalle de Secuencias";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(sequenceDetail, title);
			windows.add(sequenceDetail);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}
	public void displayTalentEntry ()
	{
		TalentEntryWindow talentEntry;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			talentEntry = new TalentEntryWindow();
			scrollTop();
			title = "Ingreso de Talento";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(talentEntry, title);
			windows.add(talentEntry);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}
	public void displayProjectEntry ()
	{
		ProjectEntryWindow projectEntry;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			projectEntry = new ProjectEntryWindow();
			scrollTop();
			title = "Ingreso de Proyecto";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(projectEntry, title);
			windows.add(projectEntry);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}
	public void displaySequenceEntry (String pId, TreeMap<String, String> roles)
	{
		SequenceEntryWindow sequenceEntry;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			sequenceEntry = new SequenceEntryWindow(pId, roles);
			scrollTop();
			title = "Creación de Secuencias";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(sequenceEntry, title);
			windows.add(sequenceEntry);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}
	public void displayTalentUpdate (String id, ArrayList<ArrayList<String>> values)
	{
		TalentUpdateWindow talentUpdate;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			talentUpdate = new TalentUpdateWindow(id, values);
			scrollTop();
			title = "Actualización de Talento";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(talentUpdate, title);
			windows.add(talentUpdate);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}
	public void displayProjectUpdate (String id, ArrayList<ArrayList<String>> values)
	{
		ProjectUpdateWindow projectUpdate;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			projectUpdate = new ProjectUpdateWindow(id, values);
			scrollTop();
			title = "Actualización de Proyecto";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(projectUpdate, title);
			windows.add(projectUpdate);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}
	public void displayWasteBasket ()
	{
		WasteBasketWindow wasteBasket;
		String title;
		CardLayout lytCard;

		if (isConnected)
		{
			wasteBasket = new WasteBasketWindow();
			scrollTop();
			title = "Papelera";
			lytCard = (CardLayout) pnlBody.getLayout();

			pnlBody.add(wasteBasket, title);
			windows.add(wasteBasket);

			lytCard.show(pnlBody, title);

			pushLblLink(title);
			this.setTitle(title + " - CastBoard");
		}
	}
	public void displayException (String message)
	{
		(new ExceptionPopUp(this)).display(message);
	}
	
	public void pushLblLink (String link)
	{
		JLabel lblLink = new JLabel(" \u25B8" + link);
		boolean isUnique = true;

		for (JLabel label : links)
		{
			if (lblLink.getText().equals(label.getText()))
			{
				isUnique = false;
				break;
			}
		}

		if (isUnique)
		{
			lblLink.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked (MouseEvent e)
				{
					if (links.size() > 1)
					{
						JLabel lblLink = (JLabel) e.getSource();
						String windowTitle = lblLink.getText().substring(2);
						CardLayout lytCard = (CardLayout) pnlBody.getLayout();

						lytCard.show(pnlBody, windowTitle);
						scrollTop();
						getInstance().setTitle(windowTitle + " - CastBoard");

						unstyleLink(lblLink);

						popWindows(windows.get(links.indexOf(lblLink)));
						popLblLinks(lblLink);
					}
				}

				public void mouseEntered (MouseEvent e)
				{
					JLabel lblLink = (JLabel) e.getSource();
					String windowTitle = lblLink.getText().substring(2);

					if(lblLink != links.get(links.size() - 1))
							lblLink.setToolTipText("Regresar a " + windowTitle);

					if(lblLink != links.get(links.size() - 1))
						lblLink.setForeground(new Color(210, 210, 210));
				}

				public void mouseExited (MouseEvent e)
				{
					JLabel lblLink = (JLabel) e.getSource();

					if(lblLink != links.get(links.size() - 1))
						lblLink.setForeground(new Color(0, 146, 182));
				}
			});
			
			if (!(links.isEmpty()))
				styleLink(links.get(links.size() - 1));

				links.add(lblLink);
				tlbNavigation.add(lblLink);

			tlbNavigation.revalidate();
			this.repaint();
		}
	}
	private void popLblLinks (JLabel lblLink)
	{
		int fromIndex = links.indexOf(lblLink) + 1;
		int toIndex = links.size();
		ArrayList<JLabel> popLinks= new ArrayList<JLabel>(links.subList(fromIndex, toIndex));

		for (int i = 0; i < popLinks.size(); i++)
		{
			tlbNavigation.remove(popLinks.get(i));
		}
		links.removeAll(popLinks);

		tlbNavigation.revalidate();
		this.repaint();
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
	@SuppressWarnings("unchecked")
	public void unstyleLink (JLabel link)
	{
		Font font = link.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, (-1));
		link.setFont(font.deriveFont(attributes));

		link.setForeground(Color.BLACK);
	}

	private void popWindows (Window window)
	{
		int fromIndex = windows.indexOf(window) + 1;
		int toIndex = windows.size();
		ArrayList<Window> popWindows = new ArrayList<Window>(windows.subList(fromIndex, toIndex));

		for (int i = 0; i < popWindows.size(); i++)
		{
			popWindows.get(i).close();
			pnlBody.remove(popWindows.get(i));
		}
		windows.removeAll(popWindows);
		windows.get(windows.size() - 1).reload();
	}
	public void previousWindow(Window window)
	{
		CardLayout lytCard = (CardLayout) pnlBody.getLayout();

		lytCard.previous(pnlBody);

		popLblLinks(links.get(windows.indexOf(window)-1));
		popWindows(windows.get(windows.indexOf(window)-1));

		unstyleLink(links.get(links.size()-1));
	}
	public void goBackWindows(Window window, int stepsBack)
	{
		CardLayout lytCard = (CardLayout) pnlBody.getLayout();

		lytCard.show(pnlBody, links.get(windows.indexOf(window) - stepsBack).getText().substring(2));

		popLblLinks(links.get(windows.indexOf(window)-stepsBack));
		popWindows(windows.get(windows.indexOf(window)-stepsBack));

		unstyleLink(links.get(links.size()-1));
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
			this.setTitle("CastBoard");
			displayBlank();
			(new SuccessNotificationPopUp(this)).display("¡La sesión ha sido cerrada!");
			displayLogin();
		}
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
					displayProjectDetail(id);
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
		JPanel photoWrapper;
		JLabel photo;
		JPanel list;
		JLabel name;
		JLabel age;
		JLabel profileType;

		for (ArrayList<String> talent : talents)
		{
			thumbnail = new JButton("");
			thumbnail.setLayout(new BoxLayout(thumbnail, BoxLayout.Y_AXIS));
			thumbnail.setPreferredSize(new Dimension(132, 196));

			photoWrapper = new JPanel();
			photoWrapper.setLayout(new BorderLayout());
			// photoWrapper.setPreferredSize(new Dimension(128, 128));
			// photoWrapper.setBorder(BorderFactory.createLineBorder(Color.black));
			// photoWrapper.setBackground(new Color(210, 210, 210));

			list = new JPanel();
			list.setLayout(new BorderLayout());
			list.setOpaque(false);

			thumbnail.setName(talent.get(0));
			photo = new JLabel(scale((new ImageIcon(talent.get(1))), 128, 128));
			name = new JLabel("\u2022 " + talent.get(2));
			age = new JLabel("\u2022 " + talent.get(3));
			profileType = new JLabel("\u2022 " + talent.get(4));

			thumbnail.setToolTipText("Ver detalle del talento");
			photoWrapper.setToolTipText("Foto");
			name.setToolTipText("Nombre");
			age.setToolTipText("Edad");
			profileType.setToolTipText("Tipo de perfil");

			photoWrapper.add(photo);

			list.add(name, BorderLayout.NORTH);
			list.add(age, BorderLayout.CENTER);
			list.add(profileType, BorderLayout.SOUTH);

			thumbnail.add(photoWrapper);
			thumbnail.add(list);

			thumbnail.addActionListener(new ActionListener()
			{
				public void actionPerformed (ActionEvent e)
				{
					String id = ((JButton) e.getSource()).getName();
					displayTalentDetail(id);
				}
			});

			thumbnails.add(thumbnail);
		}

		return thumbnails;
	}
	public ArrayList<JButton> makeTalentThumbnails (ArrayList<ArrayList<String>> talents, String projectId, String roleName)
	{
		ArrayList<JButton> thumbnails = new ArrayList<JButton>();
		JButton thumbnail;
		JPanel photoWrapper;
		JLabel photo;
		JPanel list;
		JLabel name;
		JLabel age;
		JLabel profileType;

		for (ArrayList<String> talent : talents)
		{
			thumbnail = new JButton("");
			thumbnail.setLayout(new BoxLayout(thumbnail, BoxLayout.Y_AXIS));
			thumbnail.setPreferredSize(new Dimension(132, 196));

			photoWrapper = new JPanel();
			photoWrapper.setLayout(new BorderLayout());
			// photoWrapper.setPreferredSize(new Dimension(128, 128));
			// photoWrapper.setBorder(BorderFactory.createLineBorder(Color.black));
			// photoWrapper.setBackground(new Color(210, 210, 210));

			list = new JPanel();
			list.setLayout(new BorderLayout());
			list.setOpaque(false);

			thumbnail.setName(talent.get(0));
			photo = new JLabel(scale((new ImageIcon(talent.get(1))), 128, 128));
			name = new JLabel("\u2022 " + talent.get(2));
			age = new JLabel("\u2022 " + talent.get(3));
			profileType = new JLabel("\u2022 " + talent.get(4));

			thumbnail.setToolTipText("Ver detalle del talento");
			photoWrapper.setToolTipText("Foto");
			name.setToolTipText("Nombre");
			age.setToolTipText("Edad");
			profileType.setToolTipText("Tipo de perfil");

			photoWrapper.add(photo);

			list.add(name, BorderLayout.NORTH);
			list.add(age, BorderLayout.CENTER);
			list.add(profileType, BorderLayout.SOUTH);

			thumbnail.add(photoWrapper);
			thumbnail.add(list);

			thumbnail.addActionListener(new ActionListener()
			{
				public void actionPerformed (ActionEvent e)
				{
					String id = ((JButton) e.getSource()).getName();
					displayTalentDetail(id, projectId, roleName);
				}
			});

			thumbnails.add(thumbnail);
		}

		return thumbnails;
	}
	public ArrayList<JButton> makeSequenceThumbnails (ArrayList<ArrayList<String>> sequences, String title)
	{
		ArrayList<JButton> thumbnails = new ArrayList<JButton>();
		JButton thumbnail;
		JPanel list;
		JLabel number;
		JLabel filmingDate;
		JLabel location;
		JLabel scriptPage;

		for (ArrayList<String> sequence : sequences)
		{
			thumbnail = new JButton("");
			thumbnail.setLayout(new BoxLayout(thumbnail, BoxLayout.Y_AXIS));
			thumbnail.setPreferredSize(new Dimension(132, 196));

			list = new JPanel();
			list.setLayout(new BorderLayout());
			list.setOpaque(false);

			thumbnail.setName(sequence.get(0));
			number = new JLabel(sequence.get(1));
			filmingDate = new JLabel("\u2022 " + sequence.get(2));
			location = new JLabel("\u2022 " + sequence.get(3));
			scriptPage = new JLabel("\u2022 " + sequence.get(4));

			number.setFont(new Font("Serif", Font.BOLD, 18));
			number.setAlignmentX(Component.CENTER_ALIGNMENT);

			list.add(filmingDate, BorderLayout.NORTH);
			list.add(location, BorderLayout.CENTER);
			list.add(scriptPage, BorderLayout.SOUTH);

			thumbnail.setToolTipText("Ver detalle de la sequencia");
			number.setToolTipText("Número");
			filmingDate.setToolTipText("Fecha de rodaje");
			location.setToolTipText("Locación");
			scriptPage.setToolTipText("Pagina en el guion");

			thumbnail.add(Box.createRigidArea(new Dimension(60, 60)));
			thumbnail.add(number);
			thumbnail.add(Box.createRigidArea(new Dimension(60, 60)));
			thumbnail.add(list);

			thumbnail.addActionListener(new ActionListener()
			{
				public void actionPerformed (ActionEvent e)
				{
					String id = ((JButton) e.getSource()).getName();
					displaySequenceDetail(id, title);
				}
			});

			thumbnails.add(thumbnail);
		}

		return thumbnails;
	}

	public void scrollTop ()
	{
		JScrollBar sclVertical = sclBody.getVerticalScrollBar();
	    JScrollBar sclHorizontal = sclBody.getHorizontalScrollBar();

	    sclVertical.setValue(sclVertical.getMinimum());
	    sclHorizontal.setValue(sclHorizontal.getMinimum());
	}

	public ImageIcon scale (ImageIcon image, int width, int height)
	{
		int scaledWidth = image.getIconWidth();
        int scaledHeight = image.getIconHeight();

        if(image.getIconWidth() > width)
        {
          scaledWidth = width;
          scaledHeight = (scaledWidth * image.getIconHeight()) / image.getIconWidth();
        }

        if(scaledHeight > height)
        {
          scaledHeight = height;
          scaledWidth = (image.getIconWidth() * scaledHeight) / image.getIconHeight();
        }

		return new ImageIcon((image.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH)));
	}

	public void startWaitingLayer ()
	{
		luiBody.start();
	}
	public void stopWaitingLayer ()
	{
		luiBody.stop();
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
				Platform.exit();
				getInstance().dispose();
				System.exit(0);
			}
		}
		else
		{
			Platform.exit();
			getInstance().dispose();
			System.exit(0);
		}
	}

	/*
	* Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
	*
	* Redistribution and use in source and binary forms, with or without
	* modification, are permitted provided that the following conditions
	* are met:
	*
	*   - Redistributions of source code must retain the above copyright
	*     notice, this list of conditions and the following disclaimer.
	*
	*   - Redistributions in binary form must reproduce the above copyright
	*     notice, this list of conditions and the following disclaimer in the
	*     documentation and/or other materials provided with the distribution.
	*
	*   - Neither the name of Oracle or the names of its
	*     contributors may be used to endorse or promote products derived
	*     from this software without specific prior written permission.
	*
	* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
	* IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
	* THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
	* PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
	* CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
	* EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
	* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
	* PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
	* LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
	* NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
	* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
	*/
	private class WaitLayerUI extends LayerUI<JScrollPane> implements ActionListener {
	  private boolean mIsRunning;
	  private boolean mIsFadingOut;
	  private Timer mTimer;

	  private int mAngle;
	  private int mFadeCount;
	  private int mFadeLimit = 15;

	  @Override
	  public void paint (Graphics g, JComponent c) {
	    int w = c.getWidth();
	    int h = c.getHeight();

	    // Paint the view.
	    super.paint (g, c);

	    if (!mIsRunning) {
	      return;
	    }

	    Graphics2D g2 = (Graphics2D)g.create();

	    float fade = (float)mFadeCount / (float)mFadeLimit;
	    // Gray it out.
	    Composite urComposite = g2.getComposite();
	    g2.setComposite(AlphaComposite.getInstance(
	        AlphaComposite.SRC_OVER, .5f * fade));
	    g2.fillRect(0, 0, w, h);
	    g2.setComposite(urComposite);

	    // Paint the wait indicator.
	    int s = Math.min(w, h) / 5;
	    int cx = w / 2;
	    int cy = h / 2;
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	        RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setStroke(
	        new BasicStroke(s / 4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	    g2.setPaint(Color.white);
	    g2.rotate(Math.PI * mAngle / 180, cx, cy);
	    for (int i = 0; i < 12; i++) {
	      float scale = (11.0f - (float)i) / 11.0f;
	      g2.drawLine(cx + s, cy, cx + s * 2, cy);
	      g2.rotate(-Math.PI / 6, cx, cy);
	      g2.setComposite(AlphaComposite.getInstance(
	          AlphaComposite.SRC_OVER, scale * fade));
	    }

	    g2.dispose();
	  }

	  public void actionPerformed(ActionEvent e) {
	    if (mIsRunning) {
	      firePropertyChange("tick", 0, 1);
	      mAngle += 3;
	      if (mAngle >= 360) {
	        mAngle = 0;
	      }
	      if (mIsFadingOut) {
	        if (--mFadeCount == 0) {
	          mIsRunning = false;
	          mTimer.stop();
	        }
	      }
	      else if (mFadeCount < mFadeLimit) {
	        mFadeCount++;
	      }
	    }
	  }

	  public void start() {
	    if (mIsRunning) {
	      return;
	    }
	    
	    // Run a thread for animation.		
	    mIsRunning = true;
	    mIsFadingOut = false;
	    mFadeCount = 0;
	    int fps = 24;
	    int tick = 1000 / fps;
	    mTimer = new Timer(tick, this);
	    mTimer.start();
	  }

	  public void stop() {
	    mIsFadingOut = true;
	    mIsRunning = false;

	    pnlBody.revalidate();
		getInstance().repaint();
	  }

	  @Override
	  public void applyPropertyChange(PropertyChangeEvent pce, JLayer l) {
	    if ("tick".equals(pce.getPropertyName())) {
	      l.repaint();
	    }
	  }
	}
}