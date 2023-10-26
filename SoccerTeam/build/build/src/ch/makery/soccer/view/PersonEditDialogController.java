package ch.makery.soccer.view;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ch.makery.soccer.model.Person;
import ch.makery.soccer.util.DateUtil;
/**
 * ���� ��� ��������� ���������� �� ��������.
 *
 * @author Marco Jakob
 */
public class PersonEditDialogController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField positionField;
    @FXML
    private TextField heigthField;
    @FXML
    private TextField weightField;
    @FXML
    private TextField birthdayField;
    private Stage dialogStage;
    private Person person;
    private boolean okClicked = false;

    /**
     * �������������� �����-����������. ���� ����� ���������� �������������
     * ����� ����, ��� fxml-���� ����� ��������.
     */
    @FXML
    private void initialize() {
    }
    /**
     * ������������� ����� ��� ����� ����.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * ����� ��������, ���������� � ������� ����� ������.
     *
     * @param person
     */
    public void setPerson(Person person) {
        this.person = person;

        firstNameField.setText(person.getFirstName());
        lastNameField.setText(person.getLastName());
        positionField.setText(person.getPosition());
        heigthField.setText(Integer.toString(person.getHeigth()));
        weightField.setText(Integer.toString(person.getWeight()));
        birthdayField.setText(DateUtil.format(person.getBirthday()));
        birthdayField.setPromptText("dd.mm.yyyy");
    }

    /**
     * Returns true, ���� ������������ ������� OK, � ������ ������ false.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    /**
     * ����������, ����� ������������ ������� �� ������ OK.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            person.setFirstName(firstNameField.getText());
            person.setLastName(lastNameField.getText());
            person.setPosition(positionField.getText());
            person.setHeigth(Integer.parseInt(heigthField.getText()));
            person.setWeight(Integer.parseInt(weightField.getText()));
            person.setBirthday(DateUtil.parse(birthdayField.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }
    /**
     * ����������, ����� ������������ ������� �� ������ Cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    /**
     * ��������� ���������������� ���� � ��������� �����.
     *
     * @return true, ���� ���������������� ���� ���������
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            errorMessage += "No valid first name!\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errorMessage += "No valid last name!\n";
        }
        if (positionField.getText() == null || positionField.getText().length() == 0) {
            errorMessage += "No valid position!\n";
        }

        if (heigthField.getText() == null || heigthField.getText().length() == 0) {
            errorMessage += "No valid heigth!\n";
        } else {
            // �������� ������������� �������� ��� � int.
            try {
                Integer.parseInt(heigthField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid heigth (must be an integer)!\n";
            }
        }

        if ( weightField.getText() == null ||  weightField.getText().length() == 0) {
            errorMessage += "No valid weight!\n";
        } else {
            // �������� ������������� �������� ��� � int.
            try {
                Integer.parseInt( weightField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid  weight (must be an integer)!\n";
            }
        }


        if (birthdayField.getText() == null || birthdayField.getText().length() == 0) {
            errorMessage += "No valid birthday!\n";
        } else {
            if (!DateUtil.validDate(birthdayField.getText())) {
                errorMessage += "No valid birthday. Use the format dd.mm.yyyy!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // ���������� ��������� �� ������.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}
