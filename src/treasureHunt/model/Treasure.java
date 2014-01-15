package treasureHunt.model;

public class Treasure {

	private long id;
	private String nomChasse;
	private String dateOrganisation;
	private String mode;
	
	public Treasure(){
	}
	
	public Treasure(String nomChasse,String dateOrganisation,String mode){
		this.nomChasse=nomChasse;
		this.dateOrganisation=dateOrganisation;
		this.setMode(mode);
	}

	public Treasure(String nomChasse,String dateOrganisation){
		this.nomChasse=nomChasse;
		this.dateOrganisation=dateOrganisation;
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

	public String getDateOrganisation() {
		return dateOrganisation;
	}

	public void setDateOrganisation(String dateOrganisation) {
		this.dateOrganisation = dateOrganisation;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
}
