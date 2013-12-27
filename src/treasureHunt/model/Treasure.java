package treasureHunt.model;

public class Treasure {

	private long id;
	private String nomChasse;
	private String dateOrganisation;
	
	public Treasure(){
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
	
}
