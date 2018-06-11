package domain;


import java.io.Serializable;

public enum AuthorizedApplications implements Serializable {

	AAS("uUQYP0AjSmFJ5rMAYoK5"),;
	private String App;

	AuthorizedApplications(String app){
		this.App = app;
	}

	public String App(){
		return App;
	}
}
