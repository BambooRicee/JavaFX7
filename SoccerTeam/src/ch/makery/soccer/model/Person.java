package ch.makery.soccer.model;
import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ch.makery.soccer.util.LocalDateAdapter;
public class Person {
	private final StringProperty firstName;
	private final StringProperty lastName;
	private final StringProperty position;
	private final IntegerProperty heigth;
	private final IntegerProperty weight;
	private final ObjectProperty<LocalDate> birthday;
	/**
	 * Конструктор по умолчанию.
	 */
	public Person() {
		this(null, null);
	}
	/**
	 * Конструктор с некоторыми начальными данными.
	 * 
	 * @param firstName
	 * @param lastName
	 */
	public Person(String firstName, String lastName) {
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		
		this.position = new SimpleStringProperty("Позиция");
		this.heigth = new SimpleIntegerProperty(1234);
		this.weight = new SimpleIntegerProperty(1234);
		this.birthday = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, 2, 21));
	}
	
	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}
	
	public StringProperty firstNameProperty() {
		return firstName;
	}

	public String getLastName() {
		return lastName.get();
	}

	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}
	
	public StringProperty lastNameProperty() {
		return lastName;
	}

	public String getPosition() {
		return position.get();
	}

	public void setPosition(String position) {
		this.position.set(position);
	}
	
	public StringProperty positionProperty() {
		return position;
	}

	public int getHeigth() {
		return heigth.get();
	}

	public void setHeigth(int heigth) {
		this.heigth.set(heigth);
	}
	
	public IntegerProperty heigthProperty() {
		return heigth;
	}

	public int getWeight() {
		return weight.get();
	}

	public void setWeight(int weight) {
		this.weight.set(weight);
	}
	
	public IntegerProperty weightProperty() {
		return weight;
	}

	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getBirthday() {
	    return birthday.get();
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday.set(birthday);
	}
	
	public ObjectProperty<LocalDate> birthdayProperty() {
		return birthday;
	}
}