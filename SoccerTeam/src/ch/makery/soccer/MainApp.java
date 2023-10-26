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
     * Данные, в виде наблюдаемого списка адресатов.
     */
    private ObservableList<Person> personData = FXCollections.observableArrayList();
    /**
     * Конструктор
     */
    public MainApp() {
        // В качестве образца добавляем некоторые данные
        personData.add(new Person("Станислав", "Агкацев"));
        personData.add(new Person("Матвей", "Сафонов"));
        personData.add(new Person("Витор", "Тормена"));
        personData.add(new Person("Хуниор", "Алонсо"));
        personData.add(new Person("Лукас", "Оласа"));
        personData.add(new Person("Александр", "Эктов"));
        personData.add(new Person("Кайо", "-"));
        personData.add(new Person("Георгий", "Арутюнян"));
        personData.add(new Person("Сергей", "Волков"));
        personData.add(new Person("Сергей", "Петров"));
        personData.add(new Person("Кевин", " Пина"));
        personData.add(new Person("Ильзат", "Ахметов"));
        personData.add(new Person("Эдуард", "Сперцян"));
        personData.add(new Person("Жуан", "Баши"));
        personData.add(new Person("Михайло", "Баняц"));
        personData.add(new Person("Кади", "Боржис"));
        personData.add(new Person("Юрий", "Железнов"));
        personData.add(new Person("Александр", "Черников"));
        personData.add(new Person("Никита", "Кривцов"));
        personData.add(new Person("Джон", "Кордоба"));
        personData.add(new Person("Олакунле", "Олусегун"));
        personData.add(new Person("Мозес", "Кобнан"));
        personData.add(new Person("Александр", "Кокшаров"));
    }
    /**
     * Возвращает данные в виде наблюдаемого списка адресатов.
     * @return
     */
    public ObservableList<Person> getPersonData() {
        return personData;
    }
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Футбольный клуб");

        this.primaryStage.getIcons().add(new Image("file:resources/images/fc_krasnodar_logo.svg.png"));

        initRootLayout();

        showPersonOverview();
    }
    /**
     * Инициализирует корневой макет и пытается загрузить последний открытый
     * файл с адресатами.
     */
    public void initRootLayout() {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Даём контроллеру доступ к главному прилодению.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Пытается загрузить последний открытый файл с адресатами.
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
            // Даём контроллеру доступ к главному приложению.
            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   /**
    * Открывает диалоговое окно для изменения деталей указанного адресата.
    * Если пользователь кликнул OK, то изменения сохраняются в предоставленном
    * объекте адресата и возвращается значение true.
    *
    * @param person - объект адресата, который надо изменить
    * @return true, если пользователь кликнул OK, в противном случае false.
    */
   public boolean showPersonEditDialog(Person person) {
       try {
           // Загружаем fxml-файл и создаём новую сцену
           // для всплывающего диалогового окна.
           FXMLLoader loader = new FXMLLoader();
           loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
           AnchorPane page = (AnchorPane) loader.load();

           // Создаём диалоговое окно Stage.
           Stage dialogStage = new Stage();
           dialogStage.setTitle("Edit Person");
           dialogStage.initModality(Modality.WINDOW_MODAL);
           dialogStage.initOwner(primaryStage);
           Scene scene = new Scene(page);
           dialogStage.setScene(scene);

           // Передаём адресата в контроллер.
           PersonEditDialogController controller = loader.getController();
           controller.setDialogStage(dialogStage);
           controller.setPerson(person);

           dialogStage.getIcons().add(new Image("file:resources/images/103748_edit_close_user_icon.png"));

           // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
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
	 * Задаёт путь текущему загруженному файлу. Этот путь сохраняется
	 * в реестре, специфичном для конкретной операционной системы.
	 *
	 * @param file - файл или null, чтобы удалить путь
	 */
	public void setPersonFilePath(File file) {
	    Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	    if (file != null) {
	        prefs.put("filePath", file.getPath());

	        // Обновление заглавия сцены.
	        primaryStage.setTitle("SoccerTeam - " + file.getName());
	    } else {
	        prefs.remove("filePath");

	        // Обновление заглавия сцены.
	        primaryStage.setTitle("SoccerTeam");
	    }
	}
	public void loadPersonDataFromFile(File file) {
	    try {
	        JAXBContext context = JAXBContext
	                .newInstance(PersonListWrapper.class);
	        Unmarshaller um = context.createUnmarshaller();

	        // Чтение XML из файла и демаршализация.
	        PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

	        personData.clear();
	        personData.addAll(wrapper.getPersons());

	        // Сохраняем путь к файлу в реестре.
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
	 * Сохраняет текущую информацию об адресатах в указанном файле.
	 *
	 * @param file
	 */
	public void savePersonDataToFile(File file) {
	    try {
	        JAXBContext context = JAXBContext
	                .newInstance(PersonListWrapper.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	        // Обёртываем наши данные об адресатах.
	        PersonListWrapper wrapper = new PersonListWrapper();
	        wrapper.setPersons(personData);

	        // Маршаллируем и сохраняем XML в файл.
	        m.marshal(wrapper, file);

	        // Сохраняем путь к файлу в реестре.
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
