package ch.makery.soccer;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ch.makery.soccer.model.Person;
import ch.makery.soccer.model.PersonListWrapper;
import ch.makery.soccer.view.PersonEditDialogController;
import ch.makery.soccer.view.PersonOverviewController;
import ch.makery.soccer.view.RootLayoutController;

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    /**
     * ������, � ���� ������������ ������ ���������.
     */
    private ObservableList<Person> personData = FXCollections.observableArrayList();
    /**
     * �����������
     */
    public MainApp() {
        // � �������� ������� ��������� ��������� ������
        personData.add(new Person("���������", "�������"));
        personData.add(new Person("������", "�������"));
        personData.add(new Person("�����", "�������"));
        personData.add(new Person("������", "������"));
        personData.add(new Person("�����", "�����"));
        personData.add(new Person("���������", "�����"));
        personData.add(new Person("����", "-"));
        personData.add(new Person("�������", "��������"));
        personData.add(new Person("������", "������"));
        personData.add(new Person("������", "������"));
        personData.add(new Person("�����", " ����"));
        personData.add(new Person("������", "�������"));
        personData.add(new Person("������", "�������"));
        personData.add(new Person("����", "����"));
        personData.add(new Person("�������", "�����"));
        personData.add(new Person("����", "������"));
        personData.add(new Person("����", "��������"));
        personData.add(new Person("���������", "��������"));
        personData.add(new Person("������", "�������"));
        personData.add(new Person("����", "�������"));
        personData.add(new Person("��������", "��������"));
        personData.add(new Person("�����", "������"));
        personData.add(new Person("���������", "��������"));
    }
    /**
     * ���������� ������ � ���� ������������ ������ ���������.
     * @return
     */
    public ObservableList<Person> getPersonData() {
        return personData;
    }
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("���������� ����");

        this.primaryStage.getIcons().add(new Image("file:resources/images/fc_krasnodar_logo.svg.png"));

        initRootLayout();

        showPersonOverview();
    }
    /**
     * �������������� �������� ����� � �������� ��������� ��������� ��������
     * ���� � ����������.
     */
    public void initRootLayout() {
        try {
            // ��������� �������� ����� �� fxml �����.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // ���������� �����, ���������� �������� �����.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // ��� ����������� ������ � �������� ����������.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // �������� ��������� ��������� �������� ���� � ����������.
        File file = getPersonFilePath();
        if (file != null) {
            loadPersonDataFromFile(file);
        }
    }

   public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);
            // ��� ����������� ������ � �������� ����������.
            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   /**
    * ��������� ���������� ���� ��� ��������� ������� ���������� ��������.
    * ���� ������������ ������� OK, �� ��������� ����������� � ���������������
    * ������� �������� � ������������ �������� true.
    *
    * @param person - ������ ��������, ������� ���� ��������
    * @return true, ���� ������������ ������� OK, � ��������� ������ false.
    */
   public boolean showPersonEditDialog(Person person) {
       try {
           // ��������� fxml-���� � ������ ����� �����
           // ��� ������������ ����������� ����.
           FXMLLoader loader = new FXMLLoader();
           loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
           AnchorPane page = (AnchorPane) loader.load();

           // ������ ���������� ���� Stage.
           Stage dialogStage = new Stage();
           dialogStage.setTitle("Edit Person");
           dialogStage.initModality(Modality.WINDOW_MODAL);
           dialogStage.initOwner(primaryStage);
           Scene scene = new Scene(page);
           dialogStage.setScene(scene);

           // ������� �������� � ����������.
           PersonEditDialogController controller = loader.getController();
           controller.setDialogStage(dialogStage);
           controller.setPerson(person);

           dialogStage.getIcons().add(new Image("file:resources/images/103748_edit_close_user_icon.png"));

           // ���������� ���������� ���� � ���, ���� ������������ ��� �� �������
           dialogStage.showAndWait();

           return controller.isOkClicked();
       } catch (IOException e) {
           e.printStackTrace();
           return false;
       }
   }
   public File getPersonFilePath() {
	    Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	    String filePath = prefs.get("filePath", null);
	    if (filePath != null) {
	        return new File(filePath);
	    } else {
	        return null;
	    }
	}

	/**
	 * ����� ���� �������� ������������ �����. ���� ���� �����������
	 * � �������, ����������� ��� ���������� ������������ �������.
	 *
	 * @param file - ���� ��� null, ����� ������� ����
	 */
	public void setPersonFilePath(File file) {
	    Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	    if (file != null) {
	        prefs.put("filePath", file.getPath());

	        // ���������� �������� �����.
	        primaryStage.setTitle("SoccerTeam - " + file.getName());
	    } else {
	        prefs.remove("filePath");

	        // ���������� �������� �����.
	        primaryStage.setTitle("SoccerTeam");
	    }
	}
	public void loadPersonDataFromFile(File file) {
	    try {
	        JAXBContext context = JAXBContext
	                .newInstance(PersonListWrapper.class);
	        Unmarshaller um = context.createUnmarshaller();

	        // ������ XML �� ����� � ��������������.
	        PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

	        personData.clear();
	        personData.addAll(wrapper.getPersons());

	        // ��������� ���� � ����� � �������.
	        setPersonFilePath(file);

	    } catch (Exception e) { // catches ANY exception
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Could not load data");
	        alert.setContentText("Could not load data from file:\n" + file.getPath());

	        alert.showAndWait();
	    }
	}

	/**
	 * ��������� ������� ���������� �� ��������� � ��������� �����.
	 *
	 * @param file
	 */
	public void savePersonDataToFile(File file) {
	    try {
	        JAXBContext context = JAXBContext
	                .newInstance(PersonListWrapper.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	        // ��������� ���� ������ �� ���������.
	        PersonListWrapper wrapper = new PersonListWrapper();
	        wrapper.setPersons(personData);

	        // ������������ � ��������� XML � ����.
	        m.marshal(wrapper, file);

	        // ��������� ���� � ����� � �������.
	        setPersonFilePath(file);
	    } catch (Exception e) { // catches ANY exception
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Could not save data");
	        alert.setContentText("Could not save data to file:\n" + file.getPath());

	        alert.showAndWait();
	    }
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

    public static void main(String[] args) {
        launch(args);
    }
}
