package controles;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Don;
import models.Organisation;
import services.DonService;
import services.OrganisationService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class editDon {

    private DonService ds = new DonService();

    @FXML
    private TextField description;

    @FXML
    private TextField donImage;

    @FXML
    private ImageView donImageView;

    @FXML
    private TextField email;

    @FXML
    private ImageView ima;

    @FXML
    private TextField image;

    @FXML
    private TextField montant;

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;


    @FXML
    private TextField search;

    @FXML
    private Button selectImageButton;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private Button update;



    @FXML
    private ComboBox<Integer> organisationComboBox;
    
    Don data =Don.getInstance();

    @FXML
    void initialize() throws SQLException {
        nom.setText(data.getNom());
        prenom.setText(data.getPrenom());
        email.setText(data.getEmail());
        description.setText(data.getDescription());
        typeComboBox.setValue(data.getType());
        //organisation.setValue(data.getOrganisation_id());
        montant.setText(String.valueOf(data.getMontant()));

        typeComboBox.setItems(FXCollections.observableArrayList("Dons Monétaires", "Dons d'équipements"));


    }

    @FXML
    void retour(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/listDon.fxml"));
        nom.getScene().setRoot(root);
        prenom.getScene().setRoot(root);
        email.getScene().setRoot(root);
        description.getScene().setRoot(root);
        typeComboBox.getScene().setRoot(root);
        montant.getScene().setRoot(root);
        organisationComboBox.getScene().setRoot(root);
    }



    @FXML
    void update(ActionEvent event) throws SQLException, IOException {
        if (nom.getText().isEmpty() || prenom.getText().isEmpty() || email.getText().isEmpty() || typeComboBox.getValue() == null || description.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        if (!email.getText().matches(emailPattern)) {
            showAlert("Erreur", "Veuillez saisir une adresse e-mail valide.");
            return;
        }

        try {
            int montantValue = Integer.parseInt(montant.getText());
            data.setMontant(montantValue);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez saisir un montant valide.");
            return;
        }

        if (!nom.getText().matches("[a-zA-Z ]+")) {
            showAlert("Erreur", "Le nom ne doit contenir que des lettres et des espaces.");
            return;
        }

        if (!prenom.getText().matches("[a-zA-Z ]+")) {
            showAlert("Erreur", "Le prénom ne doit contenir que des lettres et des espaces.");
            return;
        }

        data.setNom(nom.getText());
        data.setPrenom(prenom.getText());
        data.setEmail(email.getText());
        data.setType(typeComboBox.getValue());
        data.setDescription(description.getText());
        // data.setOrganisation_id(organisationComboBox.getValue());
        data.setOrganisation_id(1);

        if (showConfirmation("Confirmation", "Voulez-vous vraiment mettre à jour les données ?")) {
            ds.update(data);
            showSuccess("Succès", "Don mis à jour avec succès.");
            Parent root = FXMLLoader.load(getClass().getResource("/listDon.fxml"));
            update.getScene().setRoot(root);
        }
    }


    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void initData(Don d) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        nom.setText(d.getNom());
        prenom.setText(d.getPrenom());
        email.setText(d.getEmail());
        description.setText(d.getDescription());
        montant.setText(String.valueOf(d.getMontant()));
        organisationComboBox.setValue(d.getOrganisation_id());
        Parent root = fxmlLoader.load(getClass().getResource("/editDon.fxml"));
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    @FXML
    void goToDon(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listDon.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void goToDon2(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listDon.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void goToHome(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Home2.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void orgF(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listOrgF.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void BrowseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {

            Image image = new Image(file.toURI().toString());
            donImageView.setImage(image);
            donImage.setText(file.toURI().toString());

        }
    }




}

