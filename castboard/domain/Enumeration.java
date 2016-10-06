package castboard.domain;

import java.util.TreeMap;
import java.util.Collections;

public abstract class Enumeration
{
	public static enum Sex
	{
		M ("M"),
		F ("F");
		
		private final String text;
		private static final TreeMap<String, Sex> LOOKUP;

		static
		{
			LOOKUP = new TreeMap<String, Sex>();

			for (Sex sex : Sex.values())
			{
				LOOKUP.put(sex.toString(), sex);
			}
		}
		
		private Sex (String text)
		{
			this.text = text;
		}
		
		public boolean equals (String string)
		{
			return (string == null) ? false : text.equals(string);
		}
		
		public String toString ()
		{
			return text;
		}

		public static Sex identifierOf (String text)
		{
			return LOOKUP.get(text);
		}
	}
	public static enum Physique
	{
		THIN ("Delgada"),
		ATLHETIC ("Atletica"),
		MUSCULAR ("Musculosa"),
		AVERAGE ("Normal"),
		THICK ("Gruesa");
		
		private final String text;
		private static final TreeMap<String, Physique> LOOKUP;

		static
		{
			LOOKUP = new TreeMap<String, Physique>();

			for (Physique physique : Physique.values())
			{
				LOOKUP.put(physique.toString(), physique);
			}
		}
		
		private Physique (String text)
		{
			this.text = text;
		}
		
		public boolean equals (String string)
		{
			return (string == null) ? false : text.equals(string);
		}
		
		public String toString ()
		{
			return text;
		}

		public static Physique identifierOf (String text)
		{
			return LOOKUP.get(text);
		}
	}
	public static enum SkinTone
	{
		PALE ("Palido"),
		WHITE ("Blanco"),
		OLIVE ("Oliva"),
		BROWN ("Marron"),
		BLACK ("Negro");
		
		private final String text;
		private static final TreeMap<String, SkinTone> LOOKUP;

		static
		{
			LOOKUP = new TreeMap<String, SkinTone>();

			for (SkinTone skinTone : SkinTone.values())
			{
				LOOKUP.put(skinTone.toString(), skinTone);
			}
		}
		
		private SkinTone (String text)
		{
			this.text = text;
		}
		
		public boolean equals (String string)
		{
			return (string == null) ? false : text.equals(string);
		}
		
		public String toString ()
		{
			return text;
		}

		public static SkinTone identifierOf (String text)
		{
			return LOOKUP.get(text);
		}
	}
	public static enum HairTexture
	{
		STRAIGHT ("Lacio"),
		WAVY ("Ondulado"),
		CURLY ("Rizado"),
		KINKY ("Afro");
		
		private final String text;
		private static final TreeMap<String, HairTexture> LOOKUP;

		static
		{
			LOOKUP = new TreeMap<String, HairTexture>();

			for (HairTexture hairTexture : HairTexture.values())
			{
				LOOKUP.put(hairTexture.toString(), hairTexture);
			}
		}
		
		private HairTexture (String text)
		{
			this.text = text;
		}
		
		public boolean equals (String string)
		{
			return (string == null) ? false : text.equals(string);
		}
		
		public String toString ()
		{
			return text;
		}

		public static HairTexture identifierOf (String text)
		{
			return LOOKUP.get(text);
		}
	}
	public static enum ProfileType
	{
		A ("A"),
		B ("B"),
		C ("C");
		
		private final String text;
		private static final TreeMap<String, ProfileType> LOOKUP;

		static
		{
			LOOKUP = new TreeMap<String, ProfileType>();

			for (ProfileType profileType : ProfileType.values())
			{
				LOOKUP.put(profileType.toString(), profileType);
			}
		}
		
		private ProfileType (String text)
		{
			this.text = text;
		}
		
		public boolean equals (String string)
		{
			return (string == null) ? false : text.equals(string);
		}
		
		public String toString ()
		{
			return text;
		}

		public static ProfileType identifierOf (String text)
		{
			return LOOKUP.get(text);
		}
	}
	public static enum ShirtSize
	{
		XS ("XS"),
		S ("S"),
		M ("M"),
		L ("L"),
		XL ("XL");
		
		private final String text;
		private static final TreeMap<String, ShirtSize> LOOKUP;

		static
		{
			LOOKUP = new TreeMap<String, ShirtSize>();

			for (ShirtSize shirtSize : ShirtSize.values())
			{
				LOOKUP.put(shirtSize.toString(), shirtSize);
			}
		}
		
		private ShirtSize (String text)
		{
			this.text = text;
		}
		
		public boolean equals (String string)
		{
			return (string == null) ? false : text.equals(string);
		}
		
		public String toString ()
		{
			return text;
		}

		public static ShirtSize identifierOf (String text)
		{
			return LOOKUP.get(text);
		}
	}
	public static enum Status
	{
		TALENT ("Talento"),
		STAR ("Estrella");
		
		private final String text;
		private static final TreeMap<String, Status> LOOKUP;

		static
		{
			LOOKUP = new TreeMap<String, Status>();

			for (Status status : Status.values())
			{
				LOOKUP.put(status.toString(), status);
			}
		}
		
		private Status (String text)
		{
			this.text = text;
		}
		
		public boolean equals (String string)
		{
			return (string == null) ? false : text.equals(string);
		}
		
		public String toString ()
		{
			return text;
		}

		public static Status identifierOf (String text)
		{
			return LOOKUP.get(text);
		}
	}
	public static enum Type
	{
		COMERCIAL ("Comercial"),
		PHOTOGRAPHIC ("Fotografico"),
		CINEMATOGRAPHIC ("Cinematografico");
		
		private final String text;
		private static final TreeMap<String, Type> LOOKUP;

		static
		{
			LOOKUP = new TreeMap<String, Type>();

			for (Type type : Type.values())
			{
				LOOKUP.put(type.toString(), type);
			}
		}
		
		private Type (String text)
		{
			this.text = text;
		}
		
		public boolean equals (String string)
		{
			return (string == null) ? false : text.equals(string);
		}
		
		public String toString ()
		{
			return text;
		}

		public static Type identifierOf (String text)
		{
			return LOOKUP.get(text);
		}
	}
	public static enum Category
	{
		LEADING ("Principal"),
		SECONDARY ("Secundario"),
		SUPPORTING ("Reparto"),
		FIGURANT ("Figurante"),
		EXTRA ("Extra");
		
		private final String text;
		private static final TreeMap<String, Category> LOOKUP;

		static
		{
			LOOKUP = new TreeMap<String, Category>();

			for (Category category : Category.values())
			{
				LOOKUP.put(category.toString(), category);
			}
		}
		
		private Category (String text)
		{
			this.text = text;
		}
		
		public boolean equals (String string)
		{
			return (string == null) ? false : text.equals(string);
		}
		
		public String toString ()
		{
			return text;
		}

		public static Category identifierOf (String text)
		{
			return LOOKUP.get(text);
		}
	}
	public static enum State
	{
		AUDITION ("Audicion"),
		DEVELOPMENT ("Desarrollo"),
		TERMINATE ("Termino");
		
		private final String text;
		private static final TreeMap<String, State> LOOKUP;

		static
		{
			LOOKUP = new TreeMap<String, State>();

			for (State state : State.values())
			{
				LOOKUP.put(state.toString(), state);
			}
		}
		
		private State (String text)
		{
			this.text = text;
		}
		
		public boolean equals (String string)
		{
			return (string == null) ? false : text.equals(string);
		}
		
		public String toString ()
		{
			return text;
		}

		public static State identifierOf (String text)
		{
			return LOOKUP.get(text);
		}
	}
	public static enum LocationType
	{
		EXT ("EXT"),
		INT ("INT");
		
		private final String text;
		private static final TreeMap<String, LocationType> LOOKUP;

		static
		{
			LOOKUP = new TreeMap<String, LocationType>();

			for (LocationType locationType : LocationType.values())
			{
				LOOKUP.put(locationType.toString(), locationType);
			}
		}
		
		private LocationType (String text)
		{
			this.text = text;
		}
		
		public boolean equals (String string)
		{
			return (string == null) ? false : text.equals(string);
		}
		
		public String toString ()
		{
			return text;
		}

		public static LocationType identifierOf (String text)
		{
			return LOOKUP.get(text);
		}
	}
	public static enum DayMoment
	{
		MORNING ("Mañana"),
		AFTERNOON ("Tarde"),
		NIGHT ("Noche");
		
		private final String text;
		private static final TreeMap<String, DayMoment> LOOKUP;

		static
		{
			LOOKUP = new TreeMap<String, DayMoment>();

			for (DayMoment dayMoment : DayMoment.values())
			{
				LOOKUP.put(dayMoment.toString(), dayMoment);
			}
		}
		
		private DayMoment (String text)
		{
			this.text = text;
		}
		
		public boolean equals (String string)
		{
			return (string == null) ? false : text.equals(string);
		}
		
		public String toString ()
		{
			return text;
		}

		public static DayMoment identifierOf (String text)
		{
			return LOOKUP.get(text);
		}
	}
}