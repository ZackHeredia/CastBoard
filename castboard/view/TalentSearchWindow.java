package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.SwingWorker;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.SpinnerNumberModel;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class TalentSearchWindow extends JPanel
{
	private MasterFrame masterFrame;
	private JPanel pnlSearchForm;
	private SetWindow pnlMatches;
	private ButtonGroup btgSex;
	private JRadioButton rdbMale;
	private JRadioButton rdbFemale;	
	private JComboBox cbbPhysique;
	private JComboBox cbbHairTexture;
	private JTextField txtHairColor;
	private JComboBox cbbSkinTone;
	private JTextField txtEyeColor;
	private JCheckBox chkActing;
	private JCheckBox chkDancing;
	private JCheckBox chkMusic;
	private JCheckBox chkSinging;
	private JCheckBox chkOther;
	private JTextField txtOther;
	private JSpinner spnAge;
	private JSpinner spnFeet;
	private JSpinner spnInches;
	private JComboBox cbbProfileType;
	private JComboBox cbbCity;

	public TalentSearchWindow ()
	{
		masterFrame = MasterFrame.getInstance();
		TalentSearchWindow search = this;
		
		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			protected Void doInBackground ()
			{
				createPnlSearchForm();
				createPnlMatches();

				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				search.setLayout(new BoxLayout(search, BoxLayout.Y_AXIS));

				search.add(pnlSearchForm);
				search.add(Box.createVerticalGlue());
				search.add(pnlMatches);
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}

	@SuppressWarnings("unchecked")
	private void createPnlSearchForm ()
	{
		JButton btnSearch = new JButton("Buscar");
		JLabel lblSex = new JLabel("Sexo: ");
		JLabel lblPhysique = new JLabel("Complexión");
		JLabel lblHairTexture = new JLabel("Textura de pelo");
		JLabel lblHairColor = new JLabel("Color de pelo");
		JLabel lblSkinTone = new JLabel("Tono de piel");
		JLabel lblEyeColor = new JLabel("Color de ojos");
		JLabel lblSkills = new JLabel("Habilidades:");
		JLabel lblAge = new JLabel("Edad");
		JLabel lblStature = new JLabel("Estatura: ");
		JLabel lblFeet = new JLabel("'");
		JLabel lblInches = new JLabel("''");
		JLabel lblProfileType = new JLabel("Tipo de perfil");
		JLabel lblCity = new JLabel("Provincia");
		JPanel pnlForm = new JPanel();
		JPanel pnlInputBlock1 = new JPanel();
		JPanel pnlInputBlock2 = new JPanel();
		JPanel pnlSex = new JPanel();
		JPanel pnlInnerSex = new JPanel();
		JPanel pnlPhysique= new JPanel();
		JPanel pnlHairTexture = new JPanel();
		JPanel pnlHairColor = new JPanel();
		JPanel pnlSkinTone = new JPanel();
		JPanel pnlEyeColor = new JPanel();
		JPanel pnlSkills = new JPanel();
		JPanel pnlInnerSkills = new JPanel();
		JPanel pnlOther = new JPanel();
		JPanel pnlAge = new JPanel();
		JPanel pnlStature = new JPanel();
		JPanel pnlFeet = new JPanel();
		JPanel pnlInches = new JPanel();
		JPanel pnlProfileType = new JPanel();
		JPanel pnlCity = new JPanel();
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		String title = "Perfil buscado";
		SpinnerNumberModel ageModel = new SpinnerNumberModel(1, 1, 100, 1);
		SpinnerNumberModel feetModel = new SpinnerNumberModel(1, 1, 8, 1);
		SpinnerNumberModel inchesModel = new SpinnerNumberModel(1, 1, 12, 1);
		String[] physiques = {"Delgada", "Atletica", "Musculosa", "Normal", "Gruesa"};
		String[] hairTextures = {"Lacio", "Ondulado", "Rizado", "Afro"};
		String[] skinTones = {"Palido", "Blanco", "Oliva", "Marron", "Negro"};
		String[] profileTypes = {"A", "B", "C"};
		String[] cities =  {"Distrito Nacional", "Altagracia", "Azua", "Bahoruco", "Barahona", "Dajabon", 
						   "Duarte", "El Seybo", "Elias Piña", "Espaillat", "Hato Mayor", "Independencia", 
						   "La Romana", "La Vega", "Maria Trinidad Sanchez", "Monseñor Nouel", "Montecristi", 
						   "Monte Plata", "Pedernales", "Peravia", "Puerto Plata", "Hermanas Mirabal", 
						   "Samana", "San Cristobal", "San Juan", "San Pedro de Macoris", "Sanchez Ramirez", 
						   "Santiago de los Caballeros", "Santiago Rodriguez", "Valverde", "San Jose de Ocoa", 
						   "Santo Domingo"};

		rdbMale = new JRadioButton("M");
		rdbFemale = new JRadioButton("F");	
		cbbPhysique = new JComboBox(physiques);
		cbbHairTexture = new JComboBox(hairTextures);
		txtHairColor = new JTextField(16);
		cbbSkinTone = new JComboBox(skinTones);
		txtEyeColor = new JTextField(16);
		chkActing = new JCheckBox("Actuacion");
		chkDancing = new JCheckBox("Baile");
		chkMusic = new JCheckBox("Musica");
		chkSinging = new JCheckBox("Canto");
		chkOther = new JCheckBox("Otra:");
		txtOther = new JTextField(16);
		spnAge = new JSpinner(ageModel);
		spnFeet = new JSpinner(feetModel);
		spnInches = new JSpinner(inchesModel);
		cbbProfileType = new JComboBox(profileTypes);
		cbbCity = new JComboBox(cities);

		btgSex = new ButtonGroup();

		pnlSearchForm = new JPanel();
		pnlSearchForm.setBorder(BorderFactory.createTitledBorder(etched, title));
		pnlSearchForm.setLayout(new BoxLayout(pnlSearchForm, BoxLayout.Y_AXIS));

		pnlInputBlock1.setLayout(new BoxLayout(pnlInputBlock1, BoxLayout.Y_AXIS));
		pnlInputBlock2.setLayout(new BoxLayout(pnlInputBlock2, BoxLayout.Y_AXIS));

		pnlInnerSex.setLayout(new BoxLayout(pnlInnerSex, BoxLayout.Y_AXIS));
		pnlInnerSkills.setLayout(new BoxLayout(pnlInnerSkills, BoxLayout.Y_AXIS));

		btnSearch.setAlignmentX(Component.CENTER_ALIGNMENT);

		rdbMale.setAlignmentX(Component.LEFT_ALIGNMENT);
		rdbFemale.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnlSex.setAlignmentX(Component.CENTER_ALIGNMENT);

		chkActing.setAlignmentX(Component.LEFT_ALIGNMENT);
		chkDancing.setAlignmentX(Component.LEFT_ALIGNMENT);
		chkMusic.setAlignmentX(Component.LEFT_ALIGNMENT);
		chkSinging.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnlOther.setAlignmentX(Component.LEFT_ALIGNMENT);

		rdbMale.setSelected(true);

		rdbMale.setActionCommand(rdbMale.getText());
		rdbFemale.setActionCommand(rdbFemale.getText());

		txtOther.setEditable(false);

		chkOther.addItemListener(new ItemListener()
		{
			public void itemStateChanged (ItemEvent e)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
					txtOther.setEditable(true);
				else
				{
					txtOther.setEditable(false);
					txtOther.setText("");
				}
			}
		});

		btnSearch.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				search();
			}
		});

		btgSex.add(rdbMale);
		btgSex.add(rdbFemale);		
		pnlInnerSex.add(rdbMale);
		pnlInnerSex.add(rdbFemale);
		pnlSex.add(lblSex);
		pnlSex.add(pnlInnerSex);

		pnlPhysique.add(lblPhysique);
		pnlPhysique.add(cbbPhysique);

		pnlHairTexture.add(lblHairTexture);
		pnlHairTexture.add(cbbHairTexture);

		pnlHairColor.add(lblHairColor);
		pnlHairColor.add(txtHairColor);

		pnlSkinTone.add(lblSkinTone);
		pnlSkinTone.add(cbbSkinTone);

		pnlEyeColor.add(lblEyeColor);
		pnlEyeColor.add(txtEyeColor);

		pnlOther.add(chkOther);
		pnlOther.add(txtOther);
		pnlInnerSkills.add(chkActing);
		pnlInnerSkills.add(chkDancing);
		pnlInnerSkills.add(chkMusic);
		pnlInnerSkills.add(chkSinging);
		pnlInnerSkills.add(pnlOther);
		pnlSkills.add(lblSkills);
		pnlSkills.add(pnlInnerSkills);

		pnlAge.add(lblAge);
		pnlAge.add(spnAge);

		pnlFeet.add(spnFeet);
		pnlFeet.add(lblFeet);
		pnlInches.add(spnInches);
		pnlInches.add(lblInches);
		pnlStature.add(lblStature);
		pnlStature.add(pnlFeet);
		pnlStature.add(pnlInches);

		pnlProfileType.add(lblProfileType);
		pnlProfileType.add(cbbProfileType);

		pnlCity.add(lblCity);
		pnlCity.add(cbbCity);

		pnlSex.setToolTipText("Seleccione el sexo del talento buscado");
		pnlPhysique.setToolTipText("Seleccione la complexión del talento buscado");
		pnlHairTexture.setToolTipText("Seleccione la textura de pelo del talento buscado");
		pnlHairColor.setToolTipText("Escriba el color de pelo del talento buscado");
		pnlSkinTone.setToolTipText("Seleccione el tono de piel del talento buscado");
		pnlEyeColor.setToolTipText("Escriba el color de ojos del talento buscado");
		pnlSkills.setToolTipText("Seleccione las habilidades del talento buscado");
		pnlAge.setToolTipText("Seleccione la edad aproximada del talento buscado");
		pnlStature.setToolTipText("Seleccione la estatura del talento buscado");
		pnlProfileType.setToolTipText("Seleccione el tipo de perfil del talento buscado");
		pnlCity.setToolTipText("Seleccione la provincia del talento buscado");

		pnlInputBlock1.add(pnlSex);
		pnlInputBlock1.add(pnlPhysique);
		pnlInputBlock1.add(pnlHairTexture);
		pnlInputBlock1.add(pnlHairColor);
		pnlInputBlock1.add(pnlSkinTone);

		pnlInputBlock2.add(pnlEyeColor);
		pnlInputBlock2.add(pnlAge);
		pnlInputBlock2.add(pnlStature);
		pnlInputBlock2.add(pnlProfileType);
		pnlInputBlock2.add(pnlCity);

		pnlForm.add(pnlInputBlock1);
		pnlForm.add(pnlSkills);
		pnlForm.add(pnlInputBlock2);
		
		pnlSearchForm.add(pnlForm);
		pnlSearchForm.add(btnSearch);
	}
	private void createPnlMatches ()
	{
		pnlMatches = new SetWindow("Talentos")
		{
			protected final int THUMBNAIL_MAX = 15;

			protected void createPnlGrid ()
			{
				pnlGrid = new JPanel();
				pnlGrid.setLayout(new GridLayout(0, 5, 4, 4));
			}

			public void createSet ()
			{
				createPnlGrid();
				createPnlNavigation();

				this.add(pnlGrid);
				this.add(pnlNavigation);
			}

			public boolean initThumbnails (ArrayList<String> args)
			{
				ArrayList<ArrayList<String>> talents = CatalogsHandler.search(args, CatalogsHandler.TALENT_SET);

				if (talents.isEmpty())
					return false;

				thumbnails = masterFrame.makeTalentThumbnails(talents);
				indexAcum = 0;

				return true;
			}

			public void fillPnlGrid ()
			{
				super.fillPnlGrid();
			}
		};

		pnlMatches.createSet();
	}

	private void search ()
	{
		ArrayList<String> args = new ArrayList<String>();

		args.add(btgSex.getSelection().getActionCommand());
		args.add((String) cbbPhysique.getSelectedItem());
		args.add((String) cbbHairTexture.getSelectedItem());
		args.add(txtHairColor.getText());
		args.add((String) cbbSkinTone.getSelectedItem());
		args.add(txtEyeColor.getText());
		args.add(((Integer) spnFeet.getValue()).toString());
		args.add(((Integer) spnInches.getValue()).toString());
		args.add((String) cbbProfileType.getSelectedItem());
		args.add((String) cbbCity.getSelectedItem());
		args.add(((Integer) spnAge.getValue()).toString());
		args.add(chkActing.isSelected() ? chkActing.getText() : "");
		args.add(chkDancing.isSelected() ? chkDancing.getText() : "");
		args.add(chkMusic.isSelected() ? chkMusic.getText() : "");
		args.add(chkSinging.isSelected() ? chkSinging.getText() : "");
		args.add(chkOther.isSelected() ? txtOther.getText() : "");

		System.out.println("searchbtn");

		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			private boolean matchesFound;

			protected Void doInBackground ()
			{
				matchesFound = pnlMatches.initThumbnails(args);

				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				if(matchesFound)
					pnlMatches.fillPnlGrid();
				else
					(new NoMatchesNotificationPopUp(masterFrame)).display("No hay talentos coincidentes");
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}
}