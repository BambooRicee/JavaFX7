package ch.makery.soccer.view;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ch.makery.soccer.MainApp;
import ch.makery.soccer.model.Person;
import ch.makery.soccer.util.DateUtil;
public class PersonOverviewController {
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label positionLabel;
    @FXML
    private Label heigthLabel;
    @FXML
    private Label weightLabel;
    @FXML
    private Label birthdayLabel;
    // ������ �� ������� ����������.
    private MainApp mainApp;
    /**
     * �����������.
     * ����������� ���������� ������ ������ initialize().
     */
    public PersonOverviewController() {
    }
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        // Clear person details.
        showPersonDetails(null);

        // Listen for selection changes and show the person details when changed.
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }
    /**
     * ���������� ������� �����������, ������� ��� �� ���� ������.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // ���������� � ������� ������ �� ������������ ������
        personTable.setItems(mainApp.getPersonData());
    }
    /**
     * ��������� ��� ��������� ����, ��������� ����������� �� ��������.
     * ���� ��������� ������� = null, �� ��� ��������� ���� ���������.
     *
     * @param person � ������� ���� Person ��� null
     */
    private void showPersonDetails(Person person) {
        if (person != null) {
            // ��������� ����� ����������� �� ������� person.
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            positionLabel.setText(person.getPosition());
            heigthLabel.setText(Integer.toString(person.getHeigth()));
            weightLabel.setText(Integer.toString(person.getWeight()));
            birthdayLabel.setText(DateUtil.format(person.getBirthday()));
        } else {
            // ���� Person = null, �� ������� ���� �����.
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            positionLabel.setText("");
            heigthLabel.setText("");
            weightLabel.setText("");
            birthdayLabel.setText("");
        }
    }
    /**
     * ����������, ����� ������������ ������� �� ������ ��������.
     */
    @FXML
    private void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            personTable.getItems().remove(selectedIndex);
        } else {
            // ������ �� �������.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");

            alert.showAndWait();
        }
    }
    /**
     * ����������, ����� ������������ ������� �� ������ New...
     * ��������� ���������� ���� � �������������� ����������� ������ ��������.
     */
    @FXML
    private void handleNewPerson() {
        Person tempPerson = new Person();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            mainApp.getPersonData().add(tempPerson);
        }
    }

    /**
     * ����������, ����� ������������ ������� �� ������ Edit...
     * ��������� ���������� ���� ��� ��������� ���������� ��������.
     */
    @FXML
    private void handleEditPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked) {
                showPersonDetails(selectedPerson);
            }

        } else {
            // ������ �� �������.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
    }
}