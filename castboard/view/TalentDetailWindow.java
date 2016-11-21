package castboard.view;

import castboard.domain.CatalogsHandler;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.Border;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Component;
import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;
import javax.media.Buffer;

public class TalentDetailWindow extends JPanel
{
	private MasterFrame masterFrame;
	private JPanel pnlTop;
	private JPanel pnlBottom;
	private ArrayList<ArrayList<String>> values;
	private JPanel pnlLargerMedia;
	private JPanel pnlMedia;
	private ArrayList<String> largesMedia;
	private ArrayList<JLabel> media;
	private int currentLargeMedia;
	private String id;

	public TalentDetailWindow (String id)
	{
		this.id = id;
		masterFrame = MasterFrame.getInstance();
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
				catch (MalformedURLException e)
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

	private void createPnlTop () throws MalformedURLException
	{
		CardLayout lytCard = new CardLayout();
		JPanel pnlActions = new JPanel();
		JPanel pnlInnerTop = new JPanel();
		JLabel lblLargeFacePhoto = new JLabel (masterFrame.scale(new ImageIcon(values.get(0).get(0)), 344, 344));
		JLabel lblLargeMidPhoto = new JLabel(masterFrame.scale(new ImageIcon(values.get(0).get(1)), 344, 344));
		JLabel lblLargeFullPhoto = new JLabel(masterFrame.scale(new ImageIcon(values.get(0).get(2)), 344, 344));
		MediaPanel pnlLargeVideo = new MediaPanel(new URL("file://" + values.get(0).get(3)));
		JLabel lblFacePhoto = new JLabel (masterFrame.scale(new ImageIcon(values.get(0).get(0)), 160, 160));
		JLabel lblMidPhoto = new JLabel(masterFrame.scale(new ImageIcon(values.get(0).get(1)), 160,160));
		JLabel lblFullPhoto = new JLabel(masterFrame.scale(new ImageIcon(values.get(0).get(2)), 160,160));
		JLabel lblVideo = new JLabel(new ImageIcon("castboard/res/thumbnails/vdo_160_cb.png"));
		JButton btnUpdate = new JButton("Actualizar");
		JButton btnDelete = new JButton("Elimnar");
		JButton btnPreselect = new JButton("Preseleccionar");
		JButton btnSwitch = new JButton("Cambiar estatus");

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
				//masterFrame.displayTalentUpdate(values);
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
				//preselect();
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
				(new SuccessNotificationPopUp(masterFrame)).display("El talento se ha eliminado");
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

	// Fig 21.6: MediaPanel.java
	// A JPanel the plays media from a URL
	// [Note: This tutorial is an excerpt (Section 21.6) of Chapter 21, Multimedia, from our 
	// textbook Java How to Program, 6/e. This tutorial may refer to other chapters or sections of 
	// the book that are not included here. Permission Information: Deitel, Harvey M. and Paul J., 
	// JAVA HOW TO PROGRAM, ©2005, pp.992-996. Electronically reproduced by permission of Pearson 
	// Education, Inc., Upper Saddle River, New Jersey.]
	
	private class MediaPanel extends JPanel
	{
	   private Player mediaPlayer;
	   private Component controls;
	   private Component video;

	   public MediaPanel(URL mediaURL)
	   {

	      setLayout( new BorderLayout() ); // use a BorderLayout
	      
	      // Use lightweight components for Swing compatibility
	      Manager.setHint( Manager.LIGHTWEIGHT_RENDERER, true );
	      
	      try
	      {
	         // create a player to play the media specified in the URL
	         mediaPlayer = Manager.createRealizedPlayer( mediaURL );
	         
	         // get the components for the video and the playback controls
	         video = mediaPlayer.getVisualComponent();
	         controls = mediaPlayer.getControlPanelComponent();

	   		 add( video, BorderLayout.CENTER );
	   		 add( controls, BorderLayout.SOUTH );

	         scalePanel(344, 344);

	      } // end try
	      catch ( NoPlayerException noPlayerException )
	      {
	         System.err.println( "No media player found" );
	      } // end catch
	      catch ( CannotRealizeException cannotRealizeException )
	      {
	         System.err.println( "Could not realize media player" );
	      } // end catch
	      catch ( IOException iOException )
	      {
	         System.err.println( "Error reading from the source" );
	      } // end catch
	   } // end MediaPanel constructor

	   public void start ()
	   {
	         mediaPlayer.start(); // start playing the media clip
	   }
	   public void stop ()
	   {
	   		mediaPlayer.stop();
	   }

		private void scalePanel (int width, int height)
		{
			int scaledWidth = this.getWidth();
	        int scaledHeight = this.getHeight();

	        if(this.getWidth() > width)
	        {
	          scaledWidth = width;
	          scaledHeight = (scaledWidth * this.getHeight()) / this.getWidth();
	        }

	        if(scaledHeight > height)
	        {
	          scaledHeight = height;
	          scaledWidth = (this.getWidth() * scaledHeight) / this.getHeight();
	        }

	        this.setMaximumSize(new Dimension(scaledWidth, scaledHeight));
	    } 
	} // end class MediaPanel
}