package domain;


import java.io.Serializable;

public enum AuthorizedApplications implements Serializable {

	AAS("JgrTojsMQvZR4WFTh0gp"),;
	private String App;

	AuthorizedApplications(String app){
		this.App = app;
	}

	public String App(){
		return App;
	}
}
