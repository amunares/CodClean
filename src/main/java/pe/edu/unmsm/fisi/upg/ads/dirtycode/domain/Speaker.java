package pe.edu.unmsm.fisi.upg.ads.dirtycode.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pe.edu.unmsm.fisi.upg.ads.dirtycode.exceptions.NoSessionsApprovedException;
import pe.edu.unmsm.fisi.upg.ads.dirtycode.exceptions.SpeakerDoesntMeetRequirementsException;

public class Speaker {
	private String firstName;
	private String lastName;
	private String email;
	private int yearsExperience;
	private boolean hasBlog;
	private String blogURL;
	private WebBrowser browser;
	private List<String> certifications;
	private String employer;
	private int registrationFee;
	private List<Session> sessions;

	public Integer register(IRepository repository) throws Exception {
		
		Integer speakerId = null;
		
		ValidateRegistration();
		
		speakerId = repository.saveSpeaker(this);
			
		return speakerId;
	}
	
	private void ValidateRegistration() throws SpeakerDoesntMeetRequirementsException, NoSessionsApprovedException{
		validateData();
		boolean speakerAppearsQualified = appearsExceptional() || !obviousRedFlags();
		if (!speakerAppearsQualified) {
			throw new SpeakerDoesntMeetRequirementsException("Speaker doesn't meet our abitrary and capricious standards.");
		}
		
		approveSessions();
	}

	private void approveSessions() throws NoSessionsApprovedException {
		for (Session session : sessions) {
			session.setApproved(!sessionIsAboutOldTechnology(session));
		}

		List<Session> cantSessionsApproved = sessions.stream()
	              .filter(s -> s.isApproved())
	              .collect(Collectors.toList());
		
		boolean noSessionsApproved = cantSessionsApproved.size() == 0;
		//boolean noSessionsApproved = false;
		if (noSessionsApproved) throw new NoSessionsApprovedException("No sessions approved");
	}
	
	private boolean sessionIsAboutOldTechnology(Session session){
		String[] oldTechnologies = new String[] { "Cobol", "Punch Cards", "Commodore", "VBScript" };
		for (String oldTech : oldTechnologies) {
			if (session.getTitle().contains(oldTech) || session.getDescription().contains(oldTech))return true;	
		}
		return false;
	}
	
	public boolean obviousRedFlags() {
		String[] splitted = this.email.split("@");
		String emailDomain = splitted[splitted.length - 1];
		List<String> ancientEmailDomains = Arrays.asList("aol.com", "hotmail.com", "prodigy.com", "compuserve.com");
		return (ancientEmailDomains.contains(emailDomain) || ((browser.getName() == WebBrowser.BrowserName.InternetExplorer && browser.getMajorVersion() < 9)));
	}
	
	public boolean appearsExceptional() {
		if (this.yearsExperience > 10) return true;
		if (this.hasBlog) return true;
		if (this.certifications.size() > 3) return true;
		List<String> preferredEmployers = Arrays.asList("Pluralsight", "Microsoft", "Google", "Fog Creek Software", "37Signals", "Telerik");
		if (preferredEmployers.contains(this.employer)) return true;
		return false;
	}
	
	public void validateData(){
		if (this.firstName.isEmpty()) throw new IllegalArgumentException("First Name is required");
		if (this.lastName.isEmpty()) throw new IllegalArgumentException("Last name is required.");
		if (this.email.isEmpty()) throw new IllegalArgumentException("Email is required.");
		if (this.sessions.size() == 0) throw new IllegalArgumentException("Can't register speaker with no sessions to present.");
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getYearsExperience() {
		return yearsExperience;
	}

	public void setYearsExperience(int exp) {
		this.yearsExperience = exp;
	}

	public boolean isHasBlog() {
		return hasBlog;
	}

	public void setHasBlog(boolean hasBlog) {
		this.hasBlog = hasBlog;
	}

	public String getBlogURL() {
		return blogURL;
	}

	public void setBlogURL(String blogURL) {
		this.blogURL = blogURL;
	}

	public WebBrowser getBrowser() {
		return browser;
	}

	public void setBrowser(WebBrowser browser) {
		this.browser = browser;
	}

	public List<String> getCertifications() {
		return certifications;
	}

	public void setCertifications(List<String> certifications) {
		this.certifications = certifications;
	}

	public String getEmployer() {
		return employer;
	}

	public void setEmployer(String employer) {
		this.employer = employer;
	}

	public int getRegistrationFee() {
		return registrationFee;
	}

	public void setRegistrationFee(int registrationFee) {
		this.registrationFee = registrationFee;
	}
}