package treasureHunt.model;

public class Hunt {

	private long id;
	private String nomChasse;
	private int numIndice;
	private String indice;
	private double longitude;
	private double latitude;
	public final String doubleQuote="\"";
	
	public Hunt(){
	}
	
	public Hunt(String nomChasse,int numIndice,double longitude,double latitude){
		this.nomChasse=nomChasse;
		this.numIndice=numIndice;
		this.longitude=longitude;
		this.latitude=latitude;
	}
	
	public Hunt(String nomChasse,int numIndice,String indice,double longitude,double latitude){
		this.nomChasse=nomChasse;
		this.numIndice=numIndice;
		this.indice=indice;
		this.longitude=longitude;
		this.latitude=latitude;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNomChasse() {
		return nomChasse;
	}

	public void setNomChasse(String nomChasse) {
		this.nomChasse = nomChasse;
	}

	public int getNumIndice() {
		return numIndice;
	}

	public void setNumIndice(int numIndice) {
		this.numIndice = numIndice;
	}

	public String getIndice() {
		return indice;
	}

	public void setIndice(String indice) {
		this.indice = indice;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public String toString(){
		return "{"+doubleQuote+"nomChasse"+doubleQuote+":"+doubleQuote+nomChasse+doubleQuote+
				","+doubleQuote+"numIndice"+doubleQuote+":"+doubleQuote+numIndice+doubleQuote+
				","+doubleQuote+"indice"+doubleQuote+":"+doubleQuote+indice+doubleQuote+","+
				doubleQuote+"longitude"+doubleQuote+":"+doubleQuote+longitude+doubleQuote+","+
				doubleQuote+"latitude"+doubleQuote+":"+doubleQuote+latitude+doubleQuote+"}";
	}
}
