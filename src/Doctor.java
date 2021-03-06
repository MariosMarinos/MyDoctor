import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.swing.JOptionPane;


public class Doctor implements Serializable{
	
	private String amka;
	private String mail;
	private String name;
	private String surname;
	private int age;
	private boolean gender;    // 0 -> male , 1-> female
	private String telephone;
	private String password;
	private String cityName;
	private String tag;
	private String speciality;
	
	private  HashMap<Integer, Appointment[]> appointmentMap = new HashMap< Integer,Appointment[]>();
	private HashMap<Patient, Integer> patientMap = new HashMap<>() ; 
	private ArrayList<Review> reviewList = new ArrayList<>() ; 
	
	//statistics
	private float total_cleanliness;
	private float total_staff_cooperation;
	private float total_dignity;
	private float total_involvement;
	private float overall_rating;
	
	private int visits;


	public Doctor(String amka, String mail, String name, String surname, int age, boolean gender, String telephone,

			String password, String cityName,String tag,String speciality) {
	
		this.amka = amka;
		this.mail = mail;
		this.name = name;
		this.surname = surname;
		this.age = age;
		this.gender = gender;
		this.telephone = telephone;
		this.password = password;
		this.tag=tag;
		this.cityName = cityName;
		this.speciality = speciality;
		for (int i = 1 ;i<=30;i++) {
			Appointment[] tempAmp = new Appointment[20];
			appointmentMap.put(i, tempAmp );
		}
		
	}

	public int getVisits() {
		return visits;
	}
	
	public String getTag() {
		return tag;
	}


	public String getSpeciality() {
		return speciality;
	}


	public HashMap<Patient, Integer> getPatientMap() {
		return patientMap;
	}


	public ArrayList<Review> getReviewList() {
		return reviewList;
	}

	public String getAmka() {
		return amka;
	}

	public void setVisits(int visits) {
		this.visits = visits;
	}

	public String getMail() {
		return mail;
	}


	public void setMail(String mail) {
		this.mail = mail;
	}


	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public int getAge() {
		return age;
	}
	
	public boolean isGender() {
		return gender;
	}

	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getCityName() {
		return cityName;
	}


	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public float getTotal_cleanliness() {
		return total_cleanliness;
	}


	public float getTotal_staff_cooperation() {
		return total_staff_cooperation;
	}


	public float getTotal_dignity() {
		return total_dignity;
	}


	public float getTotal_involvement() {
		return total_involvement;
	}

	public float getOverall_rating() {
		return overall_rating;
	}
   
   public void configureInfo(String newPassword,String newEmail,String newTelephone) {
	   this.setPassword(newPassword);
	   this.setMail(newEmail);
	   this.setTelephone(newTelephone);

   }
   
   public ArrayList<String> showHours(int SelectedDate) {
	   ArrayList<String> AvailableHours = new ArrayList<String>();
	   Appointment[] appoint = appointmentMap.get(SelectedDate);
	   for (int i = 0 ;i<20;i++) {
		   if (appoint[i]==null)
			   AvailableHours.add(Registry.getAppointmenthours(i));
	   }
	   
	   return AvailableHours;
   }
   
   public ArrayList<String> showAppointedHours(int SelectedDate) { //returns dates with made appointments
	   ArrayList<String> hours = new ArrayList<String>();
	   Appointment[] appoint = appointmentMap.get(SelectedDate);
	   for (int i = 0 ;i<20;i++) {
		   if (appoint[i]!=null)
			   hours.add(Registry.getAppointmenthours(i));
	   }
	   return hours;
   }
   
   public void removeAppointment(String id) {
	   boolean flag = false;
	   int i = 1 ;
	   String TempId = null;
	   while (i<=appointmentMap.size() && !flag) {
		   Appointment[] appoint = appointmentMap.get(i);
		   int j = 0 ;
		   while (j<20 && !flag) {
			   Appointment TempApp = appoint[j];
			   if (TempApp!=null) {
			   		TempId = TempApp.getId();
			   		if (TempId.equals(TempId)) {
			   			appoint[j] = null;
			   			flag= true ;
			   		}
			   }
			   j++;
		   }
		   i++;
	   }
   }
   
   public void cancelAppointment(int dayIndex, int hourIndex) { //from doctor's gui
	   Appointment[] selectedDayApps = appointmentMap.get(dayIndex + 1);
	   Appointment app = selectedDayApps[hourIndex];
	   if(app != null) {
		   Patient temp = app.getPatient();
		   temp.cancelAppointment(app);
		   this.setVisits(getVisits()-1);
	   }
	}
   
   public void addPatient(Patient aPatient) {
	   
	   if(patientMap.containsKey(aPatient)){
		   patientMap.put(aPatient, patientMap.get(aPatient)+1) ;
	   }
	   else {
		   patientMap.put(aPatient,30) ; 
	   }
   }
   
   
   public void writePrescription(Patient aPatient,String prescription) {
	   aPatient.editPrescriptionList(prescription, this);
   }
   //add appointment in appointmentmap 
   public void addAppointment(int day ,String SelectedHour,Appointment appointment) {
	   String[] hours = Registry.getHours();
	   Appointment[] dayAppointments = this.appointmentMap.get(day);
	   int index = 0;
	   for (int i=0; i<20;i++) {
		   if (hours[i]==SelectedHour)
			   index  = i ;
	   }
	   dayAppointments[index] = appointment;
	   this.appointmentMap.put(day, dayAppointments);
   }
   
   public Patient searchPatientFromList(String amka)
   {
	   for(Patient key: this.getPatientMap().keySet()) {
		   if(key.getAmka().equals(amka))
			   return key;
	   }
	   return null;
   }
   
   //add review in reviewlist if  a  patient has reviewed already it just replace the new review with the old
   public void addReview(Review rev,Registry reg)
   {
	   boolean found = false ; 
	   int i = 0 ; 
	   for(Review r : reviewList)
		   if(rev.getId().equals(r.getId())) {
			   r = rev ; 
			   found = true ; 
			   break; 
		   }
			  
	   if(!found) {
		   reviewList.add(rev) ;
		   total_cleanliness+=rev.getCleanliness();
		   total_dignity=+rev.getDignity_and_respect();
		   total_involvement+=rev.getInvolvement_in_decisions();
		   total_staff_cooperation=+rev.getStaffCo_operation();
		   overall_rating+=rev.getGeneral_Rating();
	   }
	   
	   
	   Serialization.SaveToFile(reg);
   }

   public String toString () {
	   return this.name + (" ") + this.surname;
   }
   
   public void refreshStatistics() {
	   
	   for(Review aReview: this.getReviewList()) {
		   total_cleanliness+=aReview.getCleanliness();
		   total_staff_cooperation+=aReview.getStaffCo_operation();
		   total_dignity+=aReview.getDignity_and_respect();
		   total_involvement+=aReview.getInvolvement_in_decisions();
		   overall_rating+=aReview.getGeneral_Rating();
	   }
	   total_cleanliness/=this.getReviewList().size();
	   total_staff_cooperation/=this.getReviewList().size();
	   total_dignity/=this.getReviewList().size();
	   total_involvement/=this.getReviewList().size();
	   overall_rating/=this.getReviewList().size();
   }

}
