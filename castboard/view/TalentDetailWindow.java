package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.Border;
import javax.swing.DefaultComboBoxModel;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Component;
import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.io.IOException;
import javafx.embed.swing.JFXPanel;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.net.URLEncoder;
import java.net.URL;

public class TalentDetailWindow extends Window
{
	private JPanel pnlTop;
	private JPanel pnlBottom;
	private ArrayList<ArrayList<String>> values;
	private JPanel pnlLargerMedia;
	private JPanel pnlMedia;
	private MediaPanel pnlLargeVideo;
	private ArrayList<String> largesMedia;
	private ArrayList<JLabel> media;
	private int currentLargeMedia;
	private String id;
	private String projectId;
	private String roleName;
	private boolean isPreselection;


	public TalentDetailWindow (String id)
	{
		this(id, "", "");

		isPreselection = false;
	}
	public TalentDetailWindow (String id, String projectId, String roleName)
	{
		this.id = id;
		this.projectId = projectId;
		this.roleName = roleName;
		isPreselection = true;
		masterFrame = MasterFrame.getInstance();

		init();
	}

	protected void init ()
	{
		TalentDetailWindow detail = this;

		SwingWorker worker = new SwingWorker<Void, Void>()
		{
			protected Void doInBackground ()
			{
				values = CatalogsHandler.get(id, CatalogsHandler.TALENT_SET);

				try
				{
					createPnlTop();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				createPnlBottom();

				return null;
			}

			protected void done ()
			{
				masterFrame.stopWaitingLayer();

				detail.setLayout(new BoxLayout(detail, BoxLayout.Y_AXIS));

				detail.add(pnlTop);
				detail.add(pnlBottom);
			}
		};
		worker.execute();

		masterFrame.startWaitingLayer();
	}

	private void createPnlTop ()
	{
		CardLayout lytCard = new CardLayout();
		JPanel pnlActions = new JPanel();
		JPanel pnlInnerTop = new JPanel();
		JLabel lblLargeFacePhoto = new JLabel (masterFrame.scale(new ImageIcon(values.get(0).get(0)), 344, 344));
		JLabel lblLargeMidPhoto = new JLabel(masterFrame.scale(new ImageIcon(values.get(0).get(1)), 344, 344));
		JLabel lblLargeFullPhoto = new JLabel(masterFrame.scale(new ImageIcon(values.get(0).get(2)), 344, 344));
		JLabel lblFacePhoto = new JLabel (masterFrame.scale(new ImageIcon(values.get(0).get(0)), 160, 160));
		JLabel lblMidPhoto = new JLabel(masterFrame.scale(new ImageIcon(values.get(0).get(1)), 160,160));
		JLabel lblFullPhoto = new JLabel(masterFrame.scale(new ImageIcon(values.get(0).get(2)), 160,160));
		JButton btnUpdate = new JButton("Actualizar");
		JButton btnDelete = new JButton("Eliminar");
		JButton btnPreselect = new JButton("Preseleccionar");
		JButton btnSwitch = new JButton("Cambiar estatus");

		Toolkit tk = Toolkit.getDefaultToolkit();
		URL url = getClass().getClassLoader().getResource("castboard/res/thumbnails/vdo_160_cb.png");
		Image img = tk.getImage(url);

		JLabel lblVideo = new JLabel(new ImageIcon(img));

		pnlLargeVideo = new MediaPanel(values.get(0).get(3));

		largesMedia = new ArrayList<String>();
		media = new ArrayList<JLabel>();

		pnlTop = new JPanel();
		pnlTop.setAlignmentX(Component.LEFT_ALIGNMENT);

		pnlMedia = new JPanel();

		pnlLargerMedia = new JPanel();
		pnlLargerMedia.setLayout(lytCard);
		pnlLargerMedia.setPreferredSize(new Dimension(344, 344));

		pnlInnerTop.setLayout(new BoxLayout(pnlInnerTop, BoxLayout.Y_AXIS));
		pnlInnerTop.setPreferredSize(new Dimension(644, 424));

		lblFacePhoto.setPreferredSize(new Dimension(160, 160));
		lblMidPhoto.setPreferredSize(new Dimension(160, 160));
		lblFullPhoto.setPreferredSize(new Dimension(160, 160));
		lblVideo.setPreferredSize(new Dimension(160, 160));

		lblFacePhoto.setToolTipText("Ampliar foto del rostro del talento");
		lblMidPhoto.setToolTipText("Ampliar foto a medio cuerpo del talento");
		lblFullPhoto.setToolTipText("Ampliar foto a cuerpo completo del talento");
		lblVideo.setToolTipText("Reproducir reel del talento");

		btnUpdate.setToolTipText("Actualizar este talento");
		btnDelete.setToolTipText("Eliminar este talento");
		btnPreselect.setToolTipText("Preseleccionar este talento");
		btnSwitch.setToolTipText("Cambiar el estatus del talento");

		lblFacePhoto.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked (MouseEvent e)
			{
				enlarge(lblFacePhoto);
			}
		});
		lblMidPhoto.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked (MouseEvent e)
			{
				enlarge(lblMidPhoto);
			}
		});
		lblFullPhoto.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked (MouseEvent e)
			{
				enlarge(lblFullPhoto);
			}
		});
		lblVideo.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked (MouseEvent e)
			{
				enlarge(lblVideo);
			}
		});

		btnUpdate.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				masterFrame.displayTalentUpdate(id, values);
			}
		});
		btnDelete.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				delete();
			}
		});
		btnPreselect.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				preselect();
			}
		});
		btnSwitch.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent e)
			{
				switchStatus();
			}
		});

		media.add(lblFacePhoto);
		media.add(lblMidPhoto);
		media.add(lblFullPhoto);
		media.add(lblVideo);

		largesMedia.add("Rostro");
		largesMedia.add("Medio");
		largesMedia.add("Completo");
		largesMedia.add("Demo");

		pnlMedia.add(lblMidPhoto);
		pnlMedia.add(lblFullPhoto);
		pnlMedia.add(lblVideo);

		pnlActions.add(btnUpdate);
		pnlActions.add(btnDelete);
		pnlActions.add(btnPreselect);
		pnlActions.add(btnSwitch);

		pnlLargerMedia.add(lblLargeFacePhoto, "Rostro");
		pnlLargerMedia.add(lblLargeMidPhoto, "Medio");
		pnlLargerMedia.add(lblLargeFullPhoto, "Completo");
		pnlLargerMedia.add(pnlLargeVideo, "Demo");

		pnlInnerTop.add(Box.createRigidArea(new Dimension(76, 76)));
		pnlInnerTop.add(pnlMedia);
		pnlInnerTop.add(Box.createRigidArea(new Dimension(38, 38)));
		pnlInnerTop.add(pnlActions);
		pnlInnerTop.add(Box.createRigidArea(new Dimension(76, 76)));

		pnlTop.add(pnlLargerMedia);
		pnlTop.add(pnlInnerTop);

		lytCard.show(pnlLargerMedia, "Rostro");
		currentLargeMedia = 0;
	}
	private void createPnlBottom ()
	{
		Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		JPanel pnlInnerLeftBottom = new JPanel();
		JPanel pnlInnerRightBottom = new JPanel();
		JPanel pnlGenerals = new JPanel();
		JPanel pnlProfile = new JPanel();
		JPanel pnlMeasures = new JPanel();
		JPanel pnlContact = new JPanel();
		JPanel pnlAptitudes = new JPanel();
		JPanel pnlLeftContact = new JPanel();
		JPanel pnlRightContact = new JPanel();
		JPanel pnlTopAptitudes = new JPanel();
		JPanel pnlBottomAptitudes = new JPanel();
		JLabel lblSkills = new JLabel("<html>Habilidades: <small>" + values.get(5).get(0) + "</small></html>");
		JLabel lblLanguages = new JLabel("<html>Idiomas: <small>" + values.get(5).get(1) + "</small></html>");
		JLabel lblAcademicLevel = new JLabel("<html>Nivel Académico: <small>" + values.get(5).get(2) +
											 "</small></html>");
		String[] generals = {"Nombre", "Edad", "Sexo"};
		String[] profile = {"Tipo", "Estatura", "Complexión", "Tono de piel", "Textura de pelo",
							"Color de pelo", "Color de ojos"};
		String[] measures = {"Talla de camisa", "Talla de pantalón", "Talla de zapatos"};
		String[] leftContact = {"Teléfono móvil", "Facebook", "Twitter", "Email", "Domicilio"};
		String[] rightContact = {"Teléfono fijo", "Instagram", "Estatus", "Ciudad"};
		String[] bottomAptitudes = {"Experiencia artística", "Horario disponible", "Pasatiempos"};

		pnlBottom = new JPanel();
		pnlBottom.setAlignmentX(Component.LEFT_ALIGNMENT);

		pnlInnerLeftBottom.setLayout(new BoxLayout(pnlInnerLeftBottom, BoxLayout.Y_AXIS));
		pnlInnerLeftBottom.setPreferredSize(new Dimension(344, 424));

		pnlInnerRightBottom.setLayout(new BoxLayout(pnlInnerRightBottom, BoxLayout.Y_AXIS));
		pnlInnerRightBottom.setPreferredSize(new Dimension(644, 424));

		pnlContact.setLayout(new GridLayout(1, 2));
		pnlContact.setBorder(BorderFactory.createTitledBorder(etched, "Contacto"));

		pnlAptitudes.setLayout(new BorderLayout());
		pnlAptitudes.setBorder(BorderFactory.createTitledBorder(etched, "Aptitudes"));
		pnlAptitudes.setPreferredSize(pnlContact.getPreferredSize());

		pnlTopAptitudes.setLayout(new GridLayout(2, 2));
		pnlTopAptitudes.setPreferredSize(new Dimension(12, 28));

		prepareField(pnlGenerals, "Generales", generals, values.get(1));
		prepareField(pnlProfile, "Perfil", profile, values.get(2));
		prepareField(pnlMeasures, "Medidas", measures, values.get(3));
		prepareField(pnlLeftContact, null, leftContact, new ArrayList<String>(values.get(4).subList(0, 5)));
		prepareField(pnlRightContact, null, rightContact, new ArrayList<String>(values.get(4).subList(5, 9)));
		prepareField(pnlBottomAptitudes, null, bottomAptitudes, new ArrayList<String>(values.get(5).subList(3, values.get(5).size())));

		pnlContact.add(pnlLeftContact);
		pnlContact.add(pnlRightContact);

		pnlTopAptitudes.add(lblSkills);
		pnlTopAptitudes.add(Box.createRigidArea(new Dimension(6, 6)));
		pnlTopAptitudes.add(lblLanguages);
		pnlTopAptitudes.add(lblAcademicLevel);

		pnlAptitudes.add(pnlTopAptitudes, BorderLayout.NORTH);
		pnlAptitudes.add(pnlBottomAptitudes, BorderLayout.CENTER);

		pnlInnerLeftBottom.add(pnlGenerals);
		pnlInnerLeftBottom.add(pnlProfile);
		pnlInnerLeftBottom.add(pnlMeasures);

		pnlInnerRightBottom.add(pnlContact);
		pnlInnerRightBottom.add(pnlAptitudes);

		pnlBottom.add(pnlInnerLeftBottom);
		pnlBottom.add(pnlInnerRightBottom);
	}

	private void prepareField (JPanel pnlField, String title, String[] fields, ArrayList<String> values)
	{
		Border etched;
		JLabel label;

		pnlField.setLayout(new BoxLayout(pnlField, BoxLayout.Y_AXIS));

		if (title != null)
		{
			etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
			pnlField.setBorder(BorderFactory.createTitledBorder(etched, title));
		}

		for (int i = 0; i < fields.length; i++)
		{
			label = new JLabel("<html>" + fields[i] + ": <small>" + values.get(i) + "</small></html>");

			label.setAlignmentX(Component.LEFT_ALIGNMENT);

			pnlField.add(label);
		}
	}

	private void enlarge (JLabel lblMedia)
	{
		int mediaIndex = media.indexOf(lblMedia);
		CardLayout lytCard = (CardLayout) pnlLargerMedia.getLayout();

		lytCard.show(pnlLargerMedia, largesMedia.get(mediaIndex));
		pnlMedia.remove(lblMedia);

		if (mediaIndex == 3)
			((MediaPanel) pnlLargerMedia.getComponents()[3]).start();
		else if (currentLargeMedia == 3)
			((MediaPanel) pnlLargerMedia.getComponents()[3]).stop();

		pnlMedia.add(media.get(currentLargeMedia), ((mediaIndex == 0) ? 0 : (mediaIndex - 1)));
		currentLargeMedia = mediaIndex;

		pnlLargerMedia.revalidate();
		pnlMedia.revalidate();
		this.repaint();
	}

	private void delete ()
	{
		if ((new ConfirmationPopUp(masterFrame)).display("El talento será eliminado"))
		{
			if (CatalogsHandler.remove(id))
			{
				(new SuccessNotificationPopUp(masterFrame)).display("El talento se ha eliminado");
				masterFrame.previousWindow(this);
			}
			else
				(new FailureNotificationPopUp(masterFrame)).display("El talento no se ha eliminado");
		}
	}

	private void switchStatus ()
	{
		if ((new ConfirmationPopUp(masterFrame)).display("El talento cambiará de estatus"))
		{
			if (CatalogsHandler.switchStatus(id, values.get(4).get(7)))
				(new SuccessNotificationPopUp(masterFrame)).display("El talento ha cambiado de estatus");
			else
				(new FailureNotificationPopUp(masterFrame)).display("El talento no ha cambiado de estatus");
		}
	}

	@SuppressWarnings("unchecked")
	private void preselect ()
	{
		ArrayList<ArrayList<String>> projects;
		ArrayList<ArrayList<String>> project;
		String[] titles;
		String[] ids;
		String[] names;
		String[] preselect;
		JLabel lblProject;
		JLabel lblRole;
		JComboBox cbbProject;
		JComboBox cbbRole;
		JPanel pnlProject;
		JPanel pnlRole;
		JPanel pnlPreselection;
		boolean wasPreselected = false;

		if (!isPreselection)
		{
			projects = CatalogsHandler.getSet(CatalogsHandler.PROJECT_SET);
			ids = new String[projects.size()];
			titles = new String[projects.size()];

			for (int i = 0; i < projects.size(); i++)
			{
				if (projects.get(i).get(4).equals("Audicion"))
				{
					ids[i] = projects.get(i).get(0);
					titles[i] = projects.get(i).get(1);
				}
			}

			if (titles[0]!=null)
			{
				lblProject = new JLabel("Proyecto");
				lblRole = new JLabel("Rol");

				cbbProject = new JComboBox(titles);

				project = CatalogsHandler.get(ids[cbbProject.getSelectedIndex()], CatalogsHandler.PROJECT_SET);
				names = new String[project.size() - 1];

				for (int i = 1; i < project.size(); i++)
				{
					if (project.get(i).size() == 1 && project.get(i).get(0).contains("["))
					{
						names[i-1] = project.get(i).get(0).substring(0, project.get(i).get(0).indexOf("["));
					}
				}

				cbbRole = new JComboBox(names);

				cbbProject.setToolTipText("Seleccionar el proyecto");
				cbbRole.setToolTipText("Seleccionar el rol");

				cbbProject.addItemListener(new ItemListener()
				{
					public void itemStateChanged (ItemEvent e)
					{
						if (e.getStateChange() == ItemEvent.SELECTED)
						{
							ArrayList<ArrayList<String>> project = CatalogsHandler.get(ids[cbbProject.getSelectedIndex()], CatalogsHandler.PROJECT_SET);
							String[] names = new String[project.size() - 1];

							for (int i = 1; i < project.size(); i++)
							{
								if (project.get(i).size() == 1 && project.get(i).get(0).contains("["))
								{
									names[i-1] = project.get(i).get(0).substring(0, project.get(i).get(0).indexOf("["));
								}
							}

							cbbRole.setModel(new DefaultComboBoxModel(names));
						}
					}
				});

				pnlProject = new JPanel();
				pnlRole = new JPanel();
				pnlPreselection = new JPanel();

				pnlPreselection.setLayout(new BoxLayout(pnlPreselection, BoxLayout.Y_AXIS));

				pnlProject.add(lblProject);
				pnlProject.add(cbbProject);

				pnlRole.add(lblRole);
				pnlRole.add(cbbRole);

				pnlPreselection.add(pnlProject);
				pnlPreselection.add(pnlRole);

				preselect = (new PetitionPopUp(masterFrame)).display(pnlPreselection);

				if (preselect[0]!=null && preselect[1]!=null)
				{
					projectId = ids[cbbProject.getSelectedIndex()];
					roleName = preselect[1];
				}
			}
			else
				(new CautionPopUp(masterFrame)).display("No hay proyectos en audición");
		}

		if (!(projectId.equals("") || roleName.equals("")))
		{
			wasPreselected = CatalogsHandler.preselect(id, projectId, roleName);

			if (wasPreselected)
				(new SuccessNotificationPopUp(masterFrame)).display("¡El talento ha sido preseleccionado!");
			else
				(new FailureNotificationPopUp(masterFrame)).display("El talento no ha sido preseleccionado");
		}

		projectId = "";
		roleName = "";

		if (isPreselection)
			masterFrame.goBackWindows(this, 2);
	}

	public void close ()
	{
		pnlLargeVideo.stop();
	}

/*
 * Copyright (c) 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

	private class MediaPanel extends JFXPanel
	{
		private MediaControl mediaControl;

		public MediaPanel (final String MEDIA_URL)
		{
			Platform.setImplicitExit(false);
			MediaPanel panel = this;

			Platform.runLater(new Runnable()
			{
		        public void run()
		        {
					Group root = new Group();
			        Scene scene = new Scene(root, 344, 344);
			        Media media = null;

			        // create media player
					try
					{
			        	media = new Media("file:/" + URLEncoder.encode(MEDIA_URL, "UTF-8"));
			    	}
			    	catch (Exception e) 
			    	{
			    		e.printStackTrace();	
			    	}

			        MediaPlayer mediaPlayer = new MediaPlayer(media);
			        mediaPlayer.setAutoPlay(false);
			        mediaControl = new MediaControl(mediaPlayer);
			        scene.setRoot(mediaControl);

			        panel.setScene(scene);
			    }
			});
		}

	  	public void start ()
	   	{
	        mediaControl.play();
	  	}
	   	public void stop ()
	   	{
	   		mediaControl.pause();
	   	}

	    private class MediaControl extends BorderPane
		{
		    private MediaPlayer mediaPlayer;
		    private MediaView mediaView;
		    private final boolean repeat = true;
		    private boolean stopRequested = false;
		    private boolean atEndOfMedia = false;
		    private Duration duration;
		    private Slider timeSlider;
		    private Label playTime;
		    private Slider volumeSlider;
		    private HBox mediaBar;

		    public MediaControl(final MediaPlayer mp)
		    {
		        this.mediaPlayer = mp;

		        setStyle("-fx-background-color: #bfc2c7;");
		        mediaView = new MediaView();
		        mediaView.setMediaPlayer(mediaPlayer);
		        scale(344, 344);
		        StackPane mvPane = new StackPane();
		        mvPane.getChildren().add(mediaView);
		        mvPane.setStyle("-fx-background-color: black;");
		        StackPane.setAlignment(mediaView, Pos.CENTER);
		        setCenter(mvPane);

		        mediaBar = new HBox();
		        mediaBar.setAlignment(Pos.CENTER);
		        mediaBar.setPadding(new Insets(5, 10, 5, 10));
		        BorderPane.setAlignment(mediaBar, Pos.CENTER);

		        final Button playButton = new Button("\u25B8");

		        playButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
		            public void handle(javafx.event.ActionEvent e) {
		                Status status = mediaPlayer.getStatus();

		                if (status == Status.UNKNOWN || status == Status.HALTED) {
		                    // don't do anything in these states
		                    return;
		                }

		                if (status == Status.PAUSED
		                        || status == Status.READY
		                        || status == Status.STOPPED) {
		                    // rewind the movie if we're sitting at the end
		                    if (atEndOfMedia) {
		                        mediaPlayer.seek(mediaPlayer.getStartTime());
		                        atEndOfMedia = false;
		                    }
		                    mediaPlayer.play();
		                } else {
		                    mediaPlayer.pause();
		                }
		            }
		        });
		        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
		            public void invalidated(Observable ov) {
		                updateValues();
		            }
		        });

		        mediaPlayer.setOnPlaying(new Runnable() {
		            public void run() {
		                if (stopRequested) {
		                    mediaPlayer.pause();
		                    stopRequested = false;
		                } else {
		                    playButton.setText("||");
		                }
		            }
		        });

		        mediaPlayer.setOnPaused(new Runnable() {
		            public void run() {
		                System.out.println("onPaused");
		                playButton.setText("\u25B8");
		            }
		        });

		        mediaPlayer.setOnReady(new Runnable() {
		            public void run() {
		                duration = mediaPlayer.getMedia().getDuration();
		                updateValues();
		            }
		        });

		        mediaPlayer.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
		        mediaPlayer.setOnEndOfMedia(new Runnable() {
		            public void run() {
		                if (!repeat) {
		                    playButton.setText("\u25B8");
		                    stopRequested = true;
		                    atEndOfMedia = true;
		                }
		            }
		        });

		        mediaBar.getChildren().add(playButton);
		        // Add spacer
		        Label spacer = new Label("   ");
		        mediaBar.getChildren().add(spacer);

		        // Add Time label
		        Label timeLabel = new Label("Time: ");
		        mediaBar.getChildren().add(timeLabel);

		        // Add time slider
		        timeSlider = new Slider();
		        HBox.setHgrow(timeSlider, Priority.ALWAYS);
		        timeSlider.setMinWidth(50);
		        timeSlider.setMaxWidth(Double.MAX_VALUE);
		        timeSlider.valueProperty().addListener(new InvalidationListener() {
		            public void invalidated(Observable ov) {
		                if (timeSlider.isValueChanging()) {
		                    // multiply duration by percentage calculated by slider position
		                    mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100.0));
		                }
		            }
		        });
		        mediaBar.getChildren().add(timeSlider);

		        // Add Play label
		        playTime = new Label();
		        playTime.setPrefWidth(130);
		        playTime.setMinWidth(50);
		        mediaBar.getChildren().add(playTime);

		        // Add the volume label
		        Label volumeLabel = new Label("Vol: ");
		        mediaBar.getChildren().add(volumeLabel);

		        // Add Volume slider
		        volumeSlider = new Slider();
		        volumeSlider.setPrefWidth(70);
		        volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
		        volumeSlider.setMinWidth(30);
		        volumeSlider.valueProperty().addListener(new InvalidationListener() {
		            public void invalidated(Observable ov) {
		                if (volumeSlider.isValueChanging()) {
		                    mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
		                }
		            }
		        });
		        mediaBar.getChildren().add(volumeSlider);

		        setBottom(mediaBar);
		    }

		    @Deprecated
		    protected void updateValues() {
		        if (playTime != null && timeSlider != null && volumeSlider != null) {
		            Platform.runLater(new Runnable() {
		                public void run() {
		                    Duration currentTime = mediaPlayer.getCurrentTime();
		                    playTime.setText(formatTime(currentTime, duration));
		                    timeSlider.setDisable(duration.isUnknown());
		                    if (!timeSlider.isDisabled()
		                            && duration.greaterThan(Duration.ZERO)
		                            && !timeSlider.isValueChanging()) {
		                        timeSlider.setValue(currentTime.divide(duration).toMillis()
		                                * 100.0);
		                    }
		                    if (!volumeSlider.isValueChanging()) {
		                        volumeSlider.setValue((int) Math.round(mediaPlayer.getVolume()
		                                * 100));
		                    }
		                }
		            });
		        }
		    }

		    private String formatTime(Duration elapsed, Duration duration) {
		        int intElapsed = (int) Math.floor(elapsed.toSeconds());
		        int elapsedHours = intElapsed / (60 * 60);
		        if (elapsedHours > 0) {
		            intElapsed -= elapsedHours * 60 * 60;
		        }
		        int elapsedMinutes = intElapsed / 60;
		        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
		                - elapsedMinutes * 60;

		        if (duration.greaterThan(Duration.ZERO)) {
		            int intDuration = (int) Math.floor(duration.toSeconds());
		            int durationHours = intDuration / (60 * 60);
		            if (durationHours > 0) {
		                intDuration -= durationHours * 60 * 60;
		            }
		            int durationMinutes = intDuration / 60;
		            int durationSeconds = intDuration - durationHours * 60 * 60
		                    - durationMinutes * 60;
		            if (durationHours > 0) {
		                return String.format("%d:%02d:%02d/%d:%02d:%02d",
		                        elapsedHours, elapsedMinutes, elapsedSeconds,
		                        durationHours, durationMinutes, durationSeconds);
		            } else {
		                return String.format("%02d:%02d/%02d:%02d",
		                        elapsedMinutes, elapsedSeconds, durationMinutes,
		                        durationSeconds);
		            }
		        } else {
		            if (elapsedHours > 0) {
		                return String.format("%d:%02d:%02d", elapsedHours,
		                        elapsedMinutes, elapsedSeconds);
		            } else {
		                return String.format("%02d:%02d", elapsedMinutes,
		                        elapsedSeconds);
		            }
		        }
		    }

		    public void play ()
		   	{
		        mediaPlayer.play();
		  	}
		   	public void pause ()
		   	{
		   		mediaPlayer.pause();
		   	}

		 	private void scale (double width, double height)
			{
		        if(mediaView.getFitWidth() >= mediaView.getFitHeight())
		        	mediaView.setFitWidth(width);
		        else
		        	mediaView.setFitHeight(height);
		    }
		}
	}
}