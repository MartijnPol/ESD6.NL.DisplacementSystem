package domain;


import java.io.Serializable;

public enum AuthorizedApplications implements Serializable {

	AAS("uUQYP0AjSmFJ5rMAYoK5"),
	DRIVER("oSCOrcWPw3tbNxp802mR"),
	SIM("XD2DZafI3eq1Pg7fnYF1"),
	POL("kTRGkE488lp2v3Fyp2ZN");
	private String App;

	AuthorizedApplications(String app){
		this.App = app;
	}

	public String App(){
		return App;
	}
}
