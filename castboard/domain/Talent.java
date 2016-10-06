package castboard.domain;

import static castboard.domain.Enumeration.*;

import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Date;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class Talent extends Item
{
	private String name;
	private String surname;
	private Date birthdate;
	private Sex sex;
	private TreeMap<String, String> address;
	private String homePhone;
	private String mobilePhone;
	private String email;
	private TreeMap<String, String> socialNetworks;
	private float stature;
	private Physique physique;
	private SkinTone skinTone;
	private HairTexture hairTexture;
	private String hairColor;
	private String eyeColor;
	private ProfileType profileType;
	private ShirtSize shirtSize;
	private String pantSize;
	private float shoeSize;
	private String academicLevel;
	private String hobbies;
	private ArrayList<String> artisticSkills;
	private ArrayList<String> dominatedLanguages;
	private String scheduleAvailable;
	private String artisticExperience;
	private TreeMap<String, BufferedImage> photos;
	private TreeMap<String, InputStream> videos;
	private Status status;
	
	public Talent () {}
	
	public void setName (String name)
	{
		this.name = name;
	}
	public void setSurname (String surname)
	{
		this.surname = surname;
	}
	public void setBirthdate (Date birthdate)
	{
		this.birthdate = birthdate;
	}
	public void setSex (Sex sex)
	{
		this.sex = sex;
	}
	public void setAddress (TreeMap<String, String> address)
	{
		this.address = address;
	}
	public void setHomePhone (String homePhone)
	{
		this.homePhone = homePhone;
	}
	public void setMobilePhone (String mobilePhone)
	{
		this.mobilePhone = mobilePhone;
	}
	public void setEmail (String email)
	{
		this.email = email;
	}
	public void setSocialNetworks (TreeMap<String, String> socialNetworks)
	{
		this.socialNetworks = socialNetworks;
	}
	public void setStature (float stature)
	{
		this.stature = stature;
	}
	public void setPhysique (Physique physique)
	{
		this.physique = physique;
	}
	public void setSkinTone (SkinTone skinTone)
	{
		this.skinTone = skinTone;
	}
	public void setHairTexture (HairTexture hairTexture)
	{
		this.hairTexture = hairTexture;
	}
	public void setHairColor (String hairColor)
	{
		this.hairColor = hairColor;
	}
	public void setEyeColor (String eyeColor)
	{
		this.eyeColor = eyeColor;
	}
	public void setProfileType (ProfileType profileType)
	{
		this.profileType = profileType;
	}
	public void setShirtSize (ShirtSize shirtSize)
	{
		this.shirtSize = shirtSize;
	}
	public void setPantSize (String pantSize)
	{
		this.pantSize = pantSize;
	}
	public void setShoeSize (float shoeSize)
	{
		this.shoeSize = shoeSize;
	}
	public void setAcademicLevel (String academicLevel)
	{
		this.academicLevel = academicLevel;
	}
	public void setHobbies (String hobbies)
	{
		this.hobbies = hobbies;
	}
	public void setArtisticSkills (ArrayList<String> artisticSkills)
	{
		this.artisticSkills = artisticSkills;
	}
	public void setDominatedLanguages (ArrayList<String> dominatedLanguages)
	{
		this.dominatedLanguages = dominatedLanguages;
	}
	public void setScheduleAvailable (String scheduleAvailable)
	{
		this.scheduleAvailable = scheduleAvailable;
	}
	public void setArtisticExperience (String artisticExperience)
	{
		this.artisticExperience = artisticExperience;
	}
	public void setPhotos (TreeMap<String, BufferedImage> photos)
	{
		this.photos = photos;
	}
	public void setVideos (TreeMap<String, InputStream> videos)
	{
		this.videos = videos;
	}
	public void setStatus (Status status)
	{
		this.status = status;
	}
	
	public String getName ()
	{
		return name;
	}
	public String getSurname ()
	{
		return surname;
	}
	public Date getBirthdate ()
	{
		return birthdate;
	}
	public Sex getSex ()
	{
		return sex;
	}
	public TreeMap<String, String> getAddress ()
	{
		return address;
	}
	public String getHomePhone ()
	{
		return homePhone;
	}
	public String getMobilePhone ()
	{
		return mobilePhone;
	}
	public String getEmail ()
	{
		return email;
	}
	public TreeMap<String, String> getSocialNetworks ()
	{
		return socialNetworks;
	}
	public float getStature ()
	{
		return stature;
	}
	public Physique getPhysique ()
	{
		return physique;
	}
	public SkinTone getSkinTone ()
	{
		return skinTone;
	}
	public HairTexture getHairTexture ()
	{
		return hairTexture;
	}
	public String getHairColor ()
	{
		return hairColor;
	}
	public String getEyeColor ()
	{
		return eyeColor;
	}
	public ProfileType getProfileType ()
	{
		return profileType;
	}
	public ShirtSize getShirtSize ()
	{
		return shirtSize;
	}
	public String getPantSize ()
	{
		return pantSize;
	}
	public float getShoeSize ()
	{
		return shoeSize;
	}
	public String getAcademicLevel ()
	{
		return academicLevel;
	}
	public String getHobbies ()
	{
		return hobbies;
	}
	public ArrayList<String> getArtisticSkills ()
	{
		return artisticSkills;
	}
	public ArrayList<String> getDominatedLanguages ()
	{
		return dominatedLanguages;
	}
	public String getScheduleAvailable ()
	{
		return scheduleAvailable;
	}
	public String getArtisticExperience ()
	{
		return artisticExperience;
	}
	public TreeMap<String, BufferedImage> getPhotos ()
	{
		return photos;
	}
	public TreeMap<String, InputStream> getVideos ()
	{
		return videos;
	}
	public Status getStatus ()
	{
		return status;
	}
}