package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.Border;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class TalentEntryWindow extends Window
{
	private JPanel pnlGenerals;
	private JPanel pnlContact;
	private JPanel pnlProfile;
	private JPanel pnlMeasures;
	private JPanel pnlAptitudes;
	private JPanel pnlMedia;
	private JPanel pnlBottom;
	protected JButton btnEnter;
	protected ArrayList<Object> generalInputs;
	protected ArrayList<Object> contactInputs;
	protected ArrayList<Object> profileInputs;
	protected ArrayList<Object> measuresInputs;
	protected ArrayList<Object> aptitudesInputs;
	protected ArrayList<Object> skillInputs;
	protected ArrayList<Object> languageInputs;
	protected ArrayList<Object> mediaInputs;
	private String requireMark;
	private int filledCounter;

	protected final String ACTION;

	public TalentEntryWindow ()
	{
		this("ingresar");
	}
	public TalentEntryWindow (String action)
	{
		masterFrame = MasterFrame.getInstance();
		generalInputs = new ArrayList<Object>();
		contactInputs = new ArrayList<Object>();
		profileInputs = new ArrayList<Object>();
		measuresInputs = new ArrayList<Object>();
		aptitudesInputs = new ArrayList<Object>();
		skillInputs = new ArrayList<Object>();
		languageInputs = new ArrayList<Object>();
		mediaInputs = new ArrayList<Object>();
		requireMark = "<font color='red'>*</font>";
		filledCounter = 0;
		ACTION = action;
		
		init();
	}

	protected void init ()
	{
		TalentEntryWindow entry = this;
		
		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			protected Void doInBackground ()
			{
				createPnlGenerals();
				createPnlContact();
				createPnlProfile();
				createPnlMeasures();
				createPnlAptitudes();
				createPnlMedia();
				createPnlBottom();
				reform();

				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				entry.setLayout(new BoxLayout(entry, BoxLayout.Y_AXIS));

				entry.add(pnlGenerals);
				entry.add(pnlContact);
				entry.add(pnlProfile);
				entry.add(pnlMeasures);
				entry.add(pnlAptitudes);
				entry.add(pnlMedia);
				entry.add(pnlBottom);
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}

	private void createPnlGenerals ()
	{
		JLabel lblName = new JLabel("<html>" + requireMark + "Nombre</html>");
		JLabel lblSurname = new JLabel("<html>" + requireMark + "Apellido</html>");
		JLabel lblBirthdate = new JLabel("<html>" + requireMark + "Fecha de nacimiento: </html>");
		JLabel lblDay = new JLabel("Dia");
		JLabel lblMonth = new JLabel("Mes");
		JLabel lblYear = new JLabel("Año");
		JLabel lblSex = new JLabel("<html>" + requireMark + "Sexo: </html>");
		JTextField txtName = new JTextField(16);
		JTextField txtSurname = new JTextField(16);
		SpinnerNumberModel dayModel = new SpinnerNumberModel(1, 1, 31, 1);
		SpinnerNumberModel monthModel = new SpinnerNumberModel(1, 1, 12, 1);
		SpinnerNumberModel yearModel = new SpinnerNumberModel(2000, 1900, 2100, 1);
		JSpinner spnDay = new JSpinner(dayModel);
		JSpinner spnMonth = new JSpinner(monthModel);
		JSpinner spnYear = new JSpinner(yearModel);
		ButtonGroup btgSex = new ButtonGroup();
		JRadioButton rdbMale = new JRadioButton("M");
		JRadioButton rdbFemale = new JRadioButton("F");
		JPanel pnlName = new JPanel();
		JPanel pnlSurname = new JPanel();
		JPanel pnlBirtdate = new JPanel();
		JPanel pnlInnerBirtdate = new JPanel();
		JPanel pnlDay = new JPanel();
		JPanel pnlMonth = new JPanel();
		JPanel pnlYear = new JPanel();
		JPanel pnlSex = new JPanel();
		JPanel pnlInnerSex = new JPanel();
		JPanel pnlFirstColumn = new JPanel();
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		String title = "Generales";

		pnlGenerals = new JPanel();
		pnlGenerals.setBorder(BorderFactory.createTitledBorder(etched, title));

		pnlInnerBirtdate.setLayout(new BoxLayout(pnlInnerBirtdate, BoxLayout.Y_AXIS));
		pnlInnerSex.setLayout(new BoxLayout(pnlInnerSex, BoxLayout.Y_AXIS));
		pnlFirstColumn.setLayout(new BoxLayout(pnlFirstColumn, BoxLayout.Y_AXIS));

		rdbMale.setAlignmentX(Component.RIGHT_ALIGNMENT);
		rdbFemale.setAlignmentX(Component.RIGHT_ALIGNMENT);

		pnlDay.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pnlMonth.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pnlYear.setAlignmentX(Component.RIGHT_ALIGNMENT);

		rdbMale.setSelected(true);

		rdbMale.setActionCommand(rdbMale.getText());
		rdbFemale.setActionCommand(rdbFemale.getText());

		spnDay.setName("" + 1);
		spnMonth.setName("" + 1);
		spnYear.setName("" + 2000);

		spnYear.setEditor(new JSpinner.NumberEditor(spnYear, "####"));

		txtName.getDocument().putProperty("parent", txtName);
		txtSurname.getDocument().putProperty("parent", txtSurname);

		txtName.getDocument().addDocumentListener(new RequiredListener());
		txtSurname.getDocument().addDocumentListener(new RequiredListener());

		pnlName.add(lblName);
		pnlName.add(txtName);

		pnlSurname.add(lblSurname);
		pnlSurname.add(txtSurname);

		pnlDay.add(lblDay);
		pnlDay.add(spnDay);

		pnlMonth.add(lblMonth);
		pnlMonth.add(spnMonth);

		pnlYear.add(lblYear);
		pnlYear.add(spnYear);

		pnlInnerBirtdate.add(pnlDay);
		pnlInnerBirtdate.add(pnlMonth);
		pnlInnerBirtdate.add(pnlYear);

		pnlBirtdate.add(lblBirthdate);
		pnlBirtdate.add(pnlInnerBirtdate);

		btgSex.add(rdbMale);
		btgSex.add(rdbFemale);

		pnlInnerSex.add(rdbMale);
		pnlInnerSex.add(rdbFemale);

		pnlSex.add(lblSex);
		pnlSex.add(pnlInnerSex);

		pnlFirstColumn.add(pnlName);
		pnlFirstColumn.add(pnlSex);

		pnlName.setToolTipText("Escriba el nombre del talento a " + ACTION + "");
		pnlSurname.setToolTipText("Escriba el apellido del talento a " + ACTION + "");
		pnlBirtdate.setToolTipText("Seleccione la fecha de nacimiento del talento a " + ACTION + "");
		pnlSex.setToolTipText("Seleccione el sexo del talento a " + ACTION + "");

		pnlGenerals.add(pnlFirstColumn);
		pnlGenerals.add(pnlSurname);
		pnlGenerals.add(pnlBirtdate);

		generalInputs.add(txtName);
		generalInputs.add(txtSurname);
		generalInputs.add(spnDay);
		generalInputs.add(spnMonth);
		generalInputs.add(spnYear);
		generalInputs.add(btgSex);
	}
	@SuppressWarnings("unchecked")
	private void createPnlContact ()
	{
		String[] prefixes = {"809", "829", "849"};
		String[] cities =  {"Distrito Nacional", "Altagracia", "Azua", "Bahoruco", "Barahona", "Dajabon", 
						   "Duarte", "El Seybo", "Elias Piña", "Espaillat", "Hato Mayor", "Independencia", 
						   "La Romana", "La Vega", "Maria Trinidad Sanchez", "Monseñor Nouel", "Montecristi", 
						   "Monte Plata", "Pedernales", "Peravia", "Puerto Plata", "Hermanas Mirabal", 
						   "Samana", "San Cristobal", "San Juan", "San Pedro de Macoris", "Sanchez Ramirez", 
						   "Santiago de los Caballeros", "Santiago Rodriguez", "Valverde", "San Jose de Ocoa", 
						   "Santo Domingo"};
		SpinnerNumberModel mobileModel1 =  new SpinnerNumberModel(0, 0, 999, 1);
		SpinnerNumberModel homeModel1 =  new SpinnerNumberModel(0, 0, 999, 1);
		SpinnerNumberModel mobileModel2 =  new SpinnerNumberModel(0, 0, 9999, 1);
		SpinnerNumberModel homeModel2 =  new SpinnerNumberModel(0, 0, 9999, 1);
		JLabel lblMobile = new JLabel("<html>" + requireMark + "Teléfono movil</html>");
		JLabel lblHome = new JLabel("<html>" + requireMark + "Teléfono fijo</html>");
		JLabel lblNetwork = new JLabel("Redes sociales: ");
		JLabel lblFacebook = new JLabel("Facebook");
		JLabel lblInstagram = new JLabel("Instagram");
		JLabel lblTwitter = new JLabel("Twitter");
		JLabel lblAddress = new JLabel("Dirección: ");
		JLabel lblNumber = new JLabel("Casa no.");
		JLabel lblStreet = new JLabel("Calle");
		JLabel lblNeighborhood = new JLabel("Sector");
		JLabel lblCity = new JLabel("Provincia");
		JLabel lblEmail = new JLabel("<html>" + requireMark + "Email</html>");
		JComboBox cbbMobile = new JComboBox(prefixes);
		JSpinner spnMobile1 = new JSpinner(mobileModel1);
		JSpinner spnMobile2 = new JSpinner(mobileModel2);
		JComboBox cbbHome = new JComboBox(prefixes);
		JSpinner spnHome1 = new JSpinner(homeModel1);
		JSpinner spnHome2 = new JSpinner(homeModel2);
		JTextField txtFacebook = new JTextField(16);
		JTextField txtInstagram = new JTextField(16);
		JTextField txtTwitter = new JTextField(16);
		JTextField txtNumber = new JTextField(16);
		JTextField txtStreet = new JTextField(16);
		JTextField txtNeighborhood = new JTextField(16);
		JComboBox cbbCity = new JComboBox(cities);
		JTextField txtEmail = new JTextField(16);
		JPanel pnlMobile = new JPanel();
		JPanel pnlHome = new JPanel();
		JPanel pnlNetwork = new JPanel();
		JPanel pnlFacebook = new JPanel();
		JPanel pnlInstagram = new JPanel();
		JPanel pnlTwitter = new JPanel();
		JPanel pnlAddress = new JPanel();
		JPanel pnlInnerAddress = new JPanel();
		JPanel pnlNumber = new JPanel();
		JPanel pnlStreet = new JPanel();
		JPanel pnlNeighborhood = new JPanel();
		JPanel pnlCity = new JPanel();
		JPanel pnlEmail = new JPanel();
		JPanel pnlFirstColumn = new JPanel();
		JPanel pnlThirdColumn = new JPanel();
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		String title = "Contacto";

		pnlContact = new JPanel();
		pnlContact.setBorder(BorderFactory.createTitledBorder(etched, title));

		pnlInnerAddress.setLayout(new BoxLayout(pnlInnerAddress, BoxLayout.Y_AXIS));
		pnlNetwork.setLayout(new BoxLayout(pnlNetwork, BoxLayout.Y_AXIS));
		pnlFirstColumn.setLayout(new BoxLayout(pnlFirstColumn, BoxLayout.Y_AXIS));
		pnlThirdColumn.setLayout(new BoxLayout(pnlThirdColumn, BoxLayout.Y_AXIS));

		spnMobile1.setEditor(new JSpinner.NumberEditor(spnMobile1, "000"));
		spnHome1.setEditor(new JSpinner.NumberEditor(spnHome1, "000"));

		spnMobile2.setEditor(new JSpinner.NumberEditor(spnMobile2, "0000"));
		spnHome2.setEditor(new JSpinner.NumberEditor(spnHome2, "0000"));

		((JSpinner.NumberEditor) spnMobile1.getEditor()).getTextField().setText("");
		((JSpinner.NumberEditor) spnMobile2.getEditor()).getTextField().setText("");
		((JSpinner.NumberEditor) spnHome1.getEditor()).getTextField().setText("");
		((JSpinner.NumberEditor) spnHome2.getEditor()).getTextField().setText("");

		spnMobile1.setName("000");
		spnHome1.setName("000");

		spnMobile2.setName("0000");
		spnHome2.setName("0000");

		hideArrow(spnMobile1);
		hideArrow(spnMobile2);
		hideArrow(spnHome1);
		hideArrow(spnHome2);

		txtEmail.getDocument().putProperty("parent", txtEmail);
		txtEmail.getDocument().addDocumentListener(new RequiredListener());

		pnlMobile.add(lblMobile);
		pnlMobile.add(cbbMobile);
		pnlMobile.add(spnMobile1);
		pnlMobile.add(spnMobile2);

		pnlHome.add(lblHome);
		pnlHome.add(cbbHome);
		pnlHome.add(spnHome1);
		pnlHome.add(spnHome2);

		pnlFacebook.add(lblFacebook);
		pnlFacebook.add(txtFacebook);

		pnlInstagram.add(lblInstagram);
		pnlInstagram.add(txtInstagram);

		pnlTwitter.add(lblTwitter);
		pnlTwitter.add(txtTwitter);

		pnlNumber.add(lblNumber);
		pnlNumber.add(txtNumber);

		pnlStreet.add(lblStreet);
		pnlStreet.add(txtStreet);

		pnlNeighborhood.add(lblNeighborhood);
		pnlNeighborhood.add(txtNeighborhood);

		pnlCity.add(lblCity);
		pnlCity.add(cbbCity);

		pnlEmail.add(lblEmail);
		pnlEmail.add(txtEmail);

		pnlInnerAddress.add(pnlNumber);
		pnlInnerAddress.add(pnlStreet);
		pnlInnerAddress.add(pnlNeighborhood);
		pnlInnerAddress.add(pnlCity);

		pnlAddress.add(lblAddress);
		pnlAddress.add(pnlInnerAddress);

		pnlNetwork.add(lblNetwork);
		pnlNetwork.add(pnlFacebook);
		pnlNetwork.add(pnlInstagram);
		pnlNetwork.add(pnlTwitter);

		pnlFirstColumn.add(pnlMobile);
		pnlFirstColumn.add(pnlAddress);

		pnlThirdColumn.add(pnlNetwork);
		pnlThirdColumn.add(pnlEmail);

		pnlMobile.setToolTipText("Escriba el teléfono movil del talento a " + ACTION + "");
		pnlHome.setToolTipText("Escriba el teléfono fijo del talento a " + ACTION + "");
		pnlNetwork.setToolTipText("Escriba las redes del talento a " + ACTION + "");
		pnlAddress.setToolTipText("Escriba la dirección del talento a " + ACTION + "");
		pnlEmail.setToolTipText("Escriba el email del talento a " + ACTION + "");

		pnlContact.add(pnlFirstColumn);
		pnlContact.add(pnlHome);
		pnlContact.add(pnlThirdColumn);

		contactInputs.add(pnlMobile);
		contactInputs.add(pnlHome);
		contactInputs.add(txtFacebook);
		contactInputs.add(txtInstagram);
		contactInputs.add(txtTwitter);
		contactInputs.add(txtNumber);
		contactInputs.add(txtStreet);
		contactInputs.add(txtNeighborhood);
		contactInputs.add(cbbCity);
		contactInputs.add(txtEmail);
	}
	@SuppressWarnings("unchecked")
	private void createPnlProfile ()
	{
		String[] physiques = {"Delgada", "Atletica", "Musculosa", "Normal", "Gruesa"};
		String[] hairTextures = {"Lacio", "Ondulado", "Rizado", "Afro"};
		String[] skinTones = {"Palido", "Blanco", "Oliva", "Marron", "Negro"};
		String[] profileTypes = {"A", "B", "C"};
		SpinnerNumberModel feetModel = new SpinnerNumberModel(1, 1, 8, 1);
		SpinnerNumberModel inchesModel = new SpinnerNumberModel(1, 1, 12, 1);
		JLabel lblPhysique = new JLabel("<html>" + requireMark + "Complexión<html>");
		JLabel lblSkinTone = new JLabel("<html>" + requireMark + "Tono de piel<html>");
		JLabel lblStature = new JLabel("Estatura: ");
		JLabel lblFeet = new JLabel("'");
		JLabel lblInches = new JLabel("''");
		JLabel lblHairTexture = new JLabel("<html>" + requireMark + "Textura de pelo</html>");
		JLabel lblEyeColor = new JLabel("<html>" + requireMark + "Color de ojos</html>");
		JLabel lblProfileType = new JLabel("<html>" + requireMark + "Tipo de perfil</html>");
		JLabel lblHairColor = new JLabel("<html>" + requireMark + "Color de pelo</html>");
		JComboBox cbbPhysique = new JComboBox(physiques);
		JComboBox cbbSkinTone = new JComboBox(skinTones);
		JSpinner spnFeet = new JSpinner(feetModel);
		JSpinner spnInches = new JSpinner(inchesModel);
		JComboBox cbbHairTexture = new JComboBox(hairTextures);
		JTextField txtEyeColor = new JTextField(16);
		JComboBox cbbProfileType = new JComboBox(profileTypes);
		JTextField txtHairColor = new JTextField(16);
		JPanel pnlFirstColumn = new JPanel();
		JPanel pnlSecondColumn = new JPanel();
		JPanel pnlThirdColumn = new JPanel();
		JPanel pnlPhysique= new JPanel();
		JPanel pnlSkinTone = new JPanel();
		JPanel pnlStature = new JPanel();
		JPanel pnlFeet = new JPanel();
		JPanel pnlInches = new JPanel();
		JPanel pnlHairTexture = new JPanel();
		JPanel pnlEyeColor = new JPanel();
		JPanel pnlProfileType = new JPanel();
		JPanel pnlHairColor = new JPanel();
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		String title = "Perfil";

		pnlProfile = new JPanel();
		pnlProfile.setBorder(BorderFactory.createTitledBorder(etched, title));

		pnlFirstColumn.setLayout(new BoxLayout(pnlFirstColumn, BoxLayout.Y_AXIS));
		pnlSecondColumn.setLayout(new BoxLayout(pnlSecondColumn, BoxLayout.Y_AXIS));
		pnlThirdColumn.setLayout(new BoxLayout(pnlThirdColumn, BoxLayout.Y_AXIS));

		spnInches.setName("" + 1);
		spnFeet.setName("" + 1);

		txtEyeColor.getDocument().putProperty("parent", txtEyeColor);
		txtHairColor.getDocument().putProperty("parent", txtHairColor);

		txtEyeColor.getDocument().addDocumentListener(new RequiredListener());
		txtHairColor.getDocument().addDocumentListener(new RequiredListener());

		pnlPhysique.add(lblPhysique);
		pnlPhysique.add(cbbPhysique);

		pnlSkinTone.add(lblSkinTone);
		pnlSkinTone.add(cbbSkinTone);

		pnlFeet.add(spnFeet);
		pnlFeet.add(lblFeet);
		pnlInches.add(spnInches);
		pnlInches.add(lblInches);
		pnlStature.add(lblStature);
		pnlStature.add(pnlFeet);
		pnlStature.add(pnlInches);

		pnlHairTexture.add(lblHairTexture);
		pnlHairTexture.add(cbbHairTexture);

		pnlEyeColor.add(lblEyeColor);
		pnlEyeColor.add(txtEyeColor);

		pnlProfileType.add(lblProfileType);
		pnlProfileType.add(cbbProfileType);

		pnlHairColor.add(lblHairColor);
		pnlHairColor.add(txtHairColor);

		pnlPhysique.setToolTipText("Seleccione la complexión del talento a " + ACTION + "");
		pnlSkinTone.setToolTipText("Seleccione el tono de piel del talento a " + ACTION + "");
		pnlStature.setToolTipText("Seleccione la estatura del talento a " + ACTION + "");
		pnlHairTexture.setToolTipText("Seleccione la textura de pelo del talento a " + ACTION + "");
		pnlEyeColor.setToolTipText("Escriba el color de ojos del talento a " + ACTION + "");
		pnlProfileType.setToolTipText("Seleccione el tipo de perfil del talento a " + ACTION + "");
		pnlHairColor.setToolTipText("Escriba el color de pelo del talento a " + ACTION + "");

		pnlFirstColumn.add(pnlPhysique);
		pnlFirstColumn.add(pnlHairTexture);
		pnlFirstColumn.add(pnlHairColor);

		pnlSecondColumn.add(pnlSkinTone);
		pnlSecondColumn.add(pnlEyeColor);

		pnlThirdColumn.add(pnlStature);
		pnlThirdColumn.add(pnlProfileType);

		pnlProfile.add(pnlFirstColumn);
		pnlProfile.add(pnlSecondColumn);
		pnlProfile.add(pnlThirdColumn);
		
		profileInputs.add(cbbPhysique);
		profileInputs.add(cbbSkinTone);
		profileInputs.add(spnFeet);
		profileInputs.add(spnInches);
		profileInputs.add(cbbHairTexture);
		profileInputs.add(txtEyeColor);
		profileInputs.add(cbbProfileType);
		profileInputs.add(txtHairColor);
	}
	@SuppressWarnings("unchecked")
	private void createPnlMeasures ()
	{
		String[] sizes = {"XS", "S", "M", "L", "XL"};
		JLabel lblShirt = new JLabel("Talla de camisa");
		JLabel lblPants = new JLabel("Talla de pantalón");
		JLabel lblShoes = new JLabel("Talla de zapatos");
		JComboBox cbbShirt = new JComboBox(sizes);
		JTextField txtPants = new JTextField(16);
		JTextField txtShoes = new JTextField(16);
		JPanel pnlShirt = new JPanel();
		JPanel pnlPants = new JPanel();
		JPanel pnlShoes = new JPanel();
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		String title = "Medidas";

		pnlMeasures = new JPanel();
		pnlMeasures.setBorder(BorderFactory.createTitledBorder(etched, title));

		pnlShirt.add(lblShirt);
		pnlShirt.add(cbbShirt);

		pnlPants.add(lblPants);
		pnlPants.add(txtPants);

		pnlShoes.add(lblShoes);
		pnlShoes.add(txtShoes);

		pnlShirt.setToolTipText("Seleccione la talla de camisa del talento a " + ACTION + "");
		pnlPants.setToolTipText("Escriba la talla de pantalón del talento a " + ACTION + "");
		pnlShoes.setToolTipText("Escriba la talla de zapatos del talento a " + ACTION + "");
		
		pnlMeasures.add(pnlShirt);
		pnlMeasures.add(pnlPants);
		pnlMeasures.add(pnlShoes);

		measuresInputs.add(cbbShirt);
		measuresInputs.add(txtPants);
		measuresInputs.add(txtShoes);
	}
	private void createPnlAptitudes ()
	{
		JLabel lblAcademic = new JLabel("Nivel académico");
		JLabel lblHobbies = new JLabel("Pasatiempos");
		JLabel lblExperience = new JLabel("Experiencia artística");
		JLabel lblSchedule= new JLabel("Horario disponible");
		JLabel lblSkills = new JLabel("Habilidades:");
		JLabel lblLanguages = new JLabel("Idiomas:");
		JTextField txtAcademic = new JTextField(16);
		JTextArea txaHobbies = new JTextArea(8, 24);
		JTextArea txaExperience = new JTextArea(8, 24);
		JTextArea txaSchedule = new JTextArea(8, 24);
		JScrollPane sclAcademic = new JScrollPane(txtAcademic);
		JScrollPane sclHobbies = new JScrollPane(txaHobbies);
		JScrollPane sclExperience = new JScrollPane(txaExperience);
		JScrollPane sclSchedule = new JScrollPane(txaSchedule);
		JCheckBox chkActing = new JCheckBox("Actuación");
		JCheckBox chkDancing = new JCheckBox("Baile");
		JCheckBox chkMusic = new JCheckBox("Música");
		JCheckBox chkSinging = new JCheckBox("Canto");
		JCheckBox chkSkill = new JCheckBox("Otra:");
		JTextField txtSkill = new JTextField(16);
		JCheckBox chkEnglish = new JCheckBox("Inglés");
		JCheckBox chkFrench = new JCheckBox("Francés");
		JCheckBox chkLanguage = new JCheckBox("Otro:");
		JTextField txtLanguage = new JTextField(16);
		JPanel pnlAcademic = new JPanel();
		JPanel pnlHobbies = new JPanel();
		JPanel pnlExperience = new JPanel();
		JPanel pnlSchedule= new JPanel();
		JPanel pnlSkills = new JPanel();
		JPanel pnlInnerSkills = new JPanel();
		JPanel pnlSkill = new JPanel();
		JPanel pnlLanguages = new JPanel();
		JPanel pnlInnerLanguages = new JPanel();
		JPanel pnlLanguage = new JPanel();
		JPanel pnlFirstColumn = new JPanel();
		JPanel pnlSecondColumn = new JPanel();
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		String title = "Aptitudes";

		pnlAptitudes = new JPanel();
		pnlAptitudes.setBorder(BorderFactory.createTitledBorder(etched, title));

		pnlInnerSkills.setLayout(new BoxLayout(pnlInnerSkills, BoxLayout.Y_AXIS));
		pnlInnerLanguages.setLayout(new BoxLayout(pnlInnerLanguages, BoxLayout.Y_AXIS));
		pnlFirstColumn.setLayout(new BoxLayout(pnlFirstColumn, BoxLayout.Y_AXIS));
		pnlSecondColumn.setLayout(new BoxLayout(pnlSecondColumn, BoxLayout.Y_AXIS));

		chkActing.setAlignmentX(Component.LEFT_ALIGNMENT);
		chkDancing.setAlignmentX(Component.LEFT_ALIGNMENT);
		chkMusic.setAlignmentX(Component.LEFT_ALIGNMENT);
		chkSinging.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnlSkill.setAlignmentX(Component.LEFT_ALIGNMENT);

		chkEnglish.setAlignmentX(Component.LEFT_ALIGNMENT);
		chkFrench.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnlLanguage.setAlignmentX(Component.LEFT_ALIGNMENT);

		txtSkill.setEditable(false);
		txtLanguage.setEditable(false);

		chkSkill.addItemListener(new ItemListener()
		{
			public void itemStateChanged (ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
					txtSkill.setEditable(true);
				else
				{
					txtSkill.setEditable(false);
					txtSkill.setText("");
				}
			}
		});
		chkLanguage.addItemListener(new ItemListener()
		{
			public void itemStateChanged (ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
					txtLanguage.setEditable(true);
				else
				{
					txtLanguage.setEditable(false);
					txtLanguage.setText("");
				}
			}
		});

		pnlAcademic.add(lblAcademic);
		pnlAcademic.add(txtAcademic);

		pnlHobbies.add(lblHobbies);
		pnlHobbies.add(sclHobbies);

		pnlExperience.add(lblExperience);
		pnlExperience.add(sclExperience);

		pnlSchedule.add(lblSchedule);
		pnlSchedule.add(sclSchedule);

		pnlSkill.add(chkSkill);
		pnlSkill.add(txtSkill);
		pnlInnerSkills.add(chkActing);
		pnlInnerSkills.add(chkDancing);
		pnlInnerSkills.add(chkMusic);
		pnlInnerSkills.add(chkSinging);
		pnlInnerSkills.add(pnlSkill);
		pnlSkills.add(lblSkills);
		pnlSkills.add(pnlInnerSkills);

		pnlLanguage.add(chkLanguage);
		pnlLanguage.add(txtLanguage);
		pnlInnerLanguages.add(chkEnglish);
		pnlInnerLanguages.add(chkFrench);
		pnlInnerLanguages.add(pnlLanguage);
		pnlLanguages.add(lblLanguages);
		pnlLanguages.add(pnlInnerLanguages);

		pnlFirstColumn.add(pnlAcademic);
		pnlFirstColumn.add(pnlHobbies);
		pnlFirstColumn.add(pnlExperience);
		pnlFirstColumn.add(pnlSchedule);

		pnlSecondColumn.add(pnlSkills);
		pnlSecondColumn.add(pnlLanguages);

		pnlAcademic.setToolTipText("Escriba el nivel académico del talento a " + ACTION + "");
		pnlHobbies.setToolTipText("Escriba los pasatiempos del talento a " + ACTION + "");
		pnlExperience.setToolTipText("Escriba la experiencia artistica del talento a " + ACTION + "");
		pnlSchedule.setToolTipText("Escriba el horario disponible del talento a " + ACTION + "");
		pnlSkills.setToolTipText("Escriba las habilidades artísticas del talento a " + ACTION + "");
		pnlLanguages.setToolTipText("Escriba los idiomas que domina del talento a " + ACTION + "");

		pnlAptitudes.add(pnlFirstColumn);
		pnlAptitudes.add(pnlSecondColumn);

		aptitudesInputs.add(txtAcademic);
		aptitudesInputs.add(txaHobbies);
		aptitudesInputs.add(txaExperience);
		aptitudesInputs.add(txaSchedule);

		skillInputs.add(chkActing);
		skillInputs.add(chkDancing);
		skillInputs.add(chkMusic);
		skillInputs.add(chkSinging);
		skillInputs.add(txtSkill);

		languageInputs.add(chkEnglish);
		languageInputs.add(chkFrench);
		languageInputs.add(txtLanguage);
	}
	private void createPnlMedia ()
	{
		JLabel lblFace = new JLabel("<html>" + requireMark + "Foto del rostro: </html>");
		JLabel lblMid = new JLabel("<html>" + requireMark + "Foto a medio cuerpo: </html>");
		JLabel lblFull = new JLabel("<html>" + requireMark + "Foto a cuerpo completo: </html>");
		JLabel lblVideo = new JLabel("<html>" + requireMark + "video: </html>");
		JLabel lblRouteFace = new JLabel(" ...");
		JLabel lblRouteMid = new JLabel(" ...");
		JLabel lblRouteFull = new JLabel(" ...");
		JLabel lblRouteVideo = new JLabel(" ...");
		JButton btnFace = new JButton("Examinar");
		JButton btnMid = new JButton("Examinar");
		JButton btnFull = new JButton("Examinar");
		JButton btnVideo = new JButton("Examinar");
		JPanel pnlFace = new JPanel();
		JPanel pnlMid = new JPanel();
		JPanel pnlFull = new JPanel();
		JPanel pnlVideo = new JPanel();
		JPanel pnlFirstColumn = new JPanel();
		JPanel pnlSecondColumn = new JPanel();
		JFileChooser fcsMedia = new JFileChooser();
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		String title = "Multimedia";

		pnlMedia = new JPanel();
		pnlMedia.setBorder(BorderFactory.createTitledBorder(etched, title));

		pnlFirstColumn.setLayout(new BoxLayout(pnlFirstColumn, BoxLayout.Y_AXIS));
		pnlSecondColumn.setLayout(new BoxLayout(pnlSecondColumn, BoxLayout.Y_AXIS));

		btnFace.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				String route;

				fcsMedia.setFileFilter(new FileNameExtensionFilter("Fotos jpg", "jpg"));

				route = chooseMedia(fcsMedia);
				lblRouteFace.setText(route);
			}
		});
		btnMid.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				String route;

				fcsMedia.setFileFilter(new FileNameExtensionFilter("Fotos jpg", "jpg"));

				route = chooseMedia(fcsMedia);
				lblRouteMid.setText(route);
			}
		});
		btnFull.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				String route;

				fcsMedia.setFileFilter(new FileNameExtensionFilter("Fotos jpg", "jpg"));

				route = chooseMedia(fcsMedia);
				lblRouteFull.setText(route);
			}
		});
		btnVideo.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				String route;

				fcsMedia.setFileFilter(new FileNameExtensionFilter("Videos mp4", "mp4"));

				route = chooseMedia(fcsMedia);
				lblRouteVideo.setText(route);
			}
		});
		lblRouteFace.addPropertyChangeListener("text", (new RequiredListener()));
		lblRouteMid.addPropertyChangeListener("text", (new RequiredListener()));
		lblRouteFull.addPropertyChangeListener("text", (new RequiredListener()));
		lblRouteVideo.addPropertyChangeListener("text", (new RequiredListener()));

		pnlFace.add(lblFace);
		pnlFace.add(btnFace);
		pnlFace.add(lblRouteFace);

		pnlMid.add(lblMid);
		pnlMid.add(btnMid);
		pnlMid.add(lblRouteMid);

		pnlFull.add(lblFull);
		pnlFull.add(btnFull);
		pnlFull.add(lblRouteFull);

		pnlVideo.add(lblVideo);
		pnlVideo.add(btnVideo);
		pnlVideo.add(lblRouteVideo);

		pnlFirstColumn.add(pnlFace);
		pnlFirstColumn.add(pnlMid);
		pnlFirstColumn.add(pnlFull);

		pnlSecondColumn.add(pnlVideo);

		pnlMedia.add(pnlFirstColumn);
		pnlMedia.add(pnlSecondColumn);

		mediaInputs.add(lblRouteFace);
		mediaInputs.add(lblRouteMid);
		mediaInputs.add(lblRouteFull);
		mediaInputs.add(lblRouteVideo);
	}

	private void createPnlBottom ()
	{
		JLabel lblNote = new JLabel("<html>(" + requireMark + ") Campo obligatorio</html>");
		
		pnlBottom = new JPanel();
		pnlBottom.setLayout(new BoxLayout(pnlBottom, BoxLayout.Y_AXIS));

		btnEnter = new JButton("Ingresar");
		btnEnter.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnEnter.setEnabled(false);
		btnEnter.setToolTipText("Llene los campos obligatorios para " + ACTION + " al talento");

		lblNote.setAlignmentX(Component.CENTER_ALIGNMENT);

		btnEnter.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				enter();
			}
		});

		pnlBottom.add(lblNote);
		pnlBottom.add(btnEnter);
	}

	private String chooseMedia (JFileChooser fcsMedia)
	{
		int option = fcsMedia.showDialog(masterFrame, "Elegir");

		if (option == JFileChooser.APPROVE_OPTION)
			return fcsMedia.getSelectedFile().getAbsolutePath();
		else
			return " ...";
	}

	protected void reform () {}

	protected void enter ()
	{
		TalentEntryWindow entry = this;

		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			private boolean wasEntered;

			protected Void doInBackground ()
			{
				ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();

				values.add(fillArray(generalInputs));
				values.add(fillArray(contactInputs));
				values.add(fillArray(profileInputs));
				values.add(fillArray(measuresInputs));
				values.add(fillArray(aptitudesInputs));
				values.add(fillArray(skillInputs));
				values.add(fillArray(languageInputs));
				values.add(fillArray(mediaInputs));

				wasEntered = CatalogsHandler.enter(values, CatalogsHandler.TALENT_SET);

				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				if(wasEntered)
				{
					(new SuccessNotificationPopUp(masterFrame)).display("¡El talento ha sido ingresado!");
					clearInputs(generalInputs);
					clearInputs(contactInputs);
					clearInputs(profileInputs);
					clearInputs(measuresInputs);
					clearInputs(aptitudesInputs);
					clearInputs(skillInputs);
					clearInputs(languageInputs);
					clearInputs(mediaInputs);
				}
				else
					(new FailureNotificationPopUp(masterFrame)).display("El talento no ha sido ingresado");
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}

	protected ArrayList<String> fillArray (ArrayList<Object> inputs)
	{
		ArrayList<String> inner = new ArrayList<String>();
		String phone;

		for (Object input : inputs)
		{
			if (input instanceof JTextField)
				inner.add(((JTextField) input).getText());
			else if (input instanceof JComboBox)
				inner.add((String) ((JComboBox) input).getSelectedItem());
			else if (input instanceof ButtonGroup)
				inner.add(((ButtonGroup) input).getSelection().getActionCommand());
			else if (input instanceof JTextArea)
				inner.add(((JTextArea) input).getText());
			else if (input instanceof JSpinner)
				inner.add(((Integer) ((JSpinner) input).getValue()).toString());
			else if (input instanceof JCheckBox && ((JCheckBox) input).isSelected())
				inner.add(((JCheckBox) input).getText());
			else if (input instanceof JLabel)
				inner.add(((JLabel) input).getText());
			else if (input instanceof JPanel) 
			{
				phone = "";

				for (Component component : ((JPanel) input).getComponents())
				{
					if (component instanceof JComboBox)
						phone = phone.concat((String) ((JComboBox) component).getSelectedItem());
					else if (component instanceof JSpinner)
						phone = phone.concat(((JSpinner.NumberEditor) ((JSpinner) component).getEditor()).getTextField().getText());
				}

				inner.add(phone);
			}
		}

		return inner;
	}

	private void clearInputs (ArrayList<Object> inputs)
	{
		for (Object input : inputs)
		{
			if (input instanceof JTextField)
				((JTextField) input).setText("");
			else if (input instanceof JComboBox)
				((JComboBox) input).setSelectedItem(0);
			else if (input instanceof ButtonGroup)
			{
				if (((ButtonGroup) input).getSelection().getActionCommand().equals("F"))
					((ButtonGroup) input).setSelected(((ButtonGroup) input).getSelection(), false);
			}
			else if (input instanceof JTextArea)
				((JTextArea) input).setText("");
			else if (input instanceof JSpinner)
				((JSpinner) input).setValue(Integer.parseInt(((JSpinner) input).getName()));
			else if (input instanceof JCheckBox && ((JCheckBox) input).isSelected())
				((JCheckBox) input).setSelected(false);
			else if (input instanceof JLabel)
				((JLabel) input).setText(" ...");
			else if (input instanceof JPanel) 
			{
				for (Component component : ((JPanel) input).getComponents())
				{
					if (component instanceof JComboBox)
						((JComboBox) component).setSelectedItem(0);
					else if (component instanceof JSpinner)
						((JSpinner.NumberEditor) ((JSpinner) component).getEditor()).getTextField().setText("");
				}
			}
		}
	}

	private void hideArrow(JSpinner spinner) 
	{
        Dimension d = spinner.getPreferredSize();
        d.width = 35;

        spinner.setUI(new BasicSpinnerUI() 
        {
            protected Component createNextButton() 
            {
                return null;
            }
            protected Component createPreviousButton() 
            {
                return null;
            }
        });

        spinner.setPreferredSize(d);
    }

	private class RequiredListener implements DocumentListener, PropertyChangeListener
	{
		private int qtyRequired;
		private boolean wasFilled;

		public RequiredListener ()
		{
			qtyRequired = 9;
			wasFilled = false;
		}

		public void propertyChange (PropertyChangeEvent e)
		{
			valid((JLabel) e.getSource());

		}

		public void insertUpdate (DocumentEvent e) 
		{
        	valid((JTextField) e.getDocument().getProperty("parent"));
    	}
    	public void removeUpdate (DocumentEvent e) 
    	{
        	valid((JTextField) e.getDocument().getProperty("parent"));
    	}
    	public void changedUpdate (DocumentEvent e) {}

		private void valid (JLabel input)
    	{
    		filledCounter += (!input.getText().equals(" ...") && !wasFilled) ? 1 : (filledCounter==0 || !input.getText().equals(" ...")) ? 0 : -1;
    		wasFilled = (!input.getText().equals(" ..."));

    		if (filledCounter < qtyRequired)
    		{
    			btnEnter.setEnabled(false);
    			btnEnter.setToolTipText("Llene los campos obligatorios para " + ACTION + " el talento");
    		}
    		else
    		{
    			btnEnter.setEnabled(true);
    			btnEnter.setToolTipText("Ingresar el talento");
    		}
    	}
    	private void valid (JTextField input)
    	{
    		filledCounter += (!input.getText().equals("") && !wasFilled) ? 1 : (filledCounter==0 || !input.getText().equals("")) ? 0 : -1;
    		wasFilled = (!input.getText().equals(""));

    		if (filledCounter < qtyRequired)
    		{
    			btnEnter.setEnabled(false);
    			btnEnter.setToolTipText("Llene los campos obligatorios para " + ACTION + " el talento");
    		}
    		else
    		{
    			btnEnter.setEnabled(true);
    			btnEnter.setToolTipText(btnEnter.getText() + " el talento");
    		}
    	}
	}
}