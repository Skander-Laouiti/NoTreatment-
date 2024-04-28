package controles;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Don;
import models.Organisation;
import services.DataValidation;
import services.DonService;
import services.EmailService;
import services.OrganisationService;


import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static controles.PDFGenerator.generateDonationListPDF;

public class ajouterDon implements Initializable {

    @FXML
    private Button ajouter;

    @FXML
    private TextField description;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextField email;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField montant;

    @FXML
    private Label montantLabel;

    @FXML
    private TextField nom;
    @FXML
    private Button enterButton;

    @FXML
    private Label nomLabel;

    @FXML
    private TextField prenom;

    @FXML
    private Label prenomLabel;

    @FXML
    private TextField donImage;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private ComboBox<String> organisationComboBox;

    @FXML
    private ImageView donImageView;


    private final DonService ds = new DonService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        typeComboBox.setItems(FXCollections.observableArrayList("Dons Monétaires", "Dons d'équipements"));

        try {
            OrganisationService ls = new OrganisationService();
            List<Organisation> lsss = ls.read();
            List<String> names = lsss.stream()
                    .map(Organisation::getNom)
                    .toList();
            organisationComboBox.setItems(FXCollections.observableArrayList(names));
        } catch (SQLException e) {
            e.printStackTrace();
        }

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


    @FXML
    void ajouter(ActionEvent event) throws SQLException, MessagingException, IOException {
        if (nom.getText().isEmpty() || prenom.getText().isEmpty() || email.getText().isEmpty() || typeComboBox.getValue() == null) {
            showErrorAlert("Veuillez remplir tous les champs obligatoires.");
            return;
        }

        if (!isValidEmail(email.getText())) {
            showErrorAlert("Veuillez saisir une adresse e-mail valide.");
            return;
        }

        if (!isAlphaWithSpaces(nom.getText())) {
            showErrorAlert("Veuillez saisir uniquement des caractères alphabétiques pour le nom.");
            return;
        }

        if (!isAlphaWithSpaces(prenom.getText())) {
            showErrorAlert("Veuillez saisir uniquement des caractères alphabétiques pour le prénom.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(montant.getText());
        } catch (NumberFormatException ex) {
            showErrorAlert("Veuillez saisir un montant de don valide.");
            return;
        }

        String type = typeComboBox.getValue();
        String selectedOrganisation = organisationComboBox.getValue();

        if (selectedOrganisation == null) {
            showErrorAlert("Veuillez sélectionner une organisation.");
            return;
        }

        long amountInCents = (long) (amount * 100);

        Don d = new Don();
        d.setNom(nom.getText());
        d.setPrenom(prenom.getText());
        d.setEmail(email.getText());
        d.setType(type);
        OrganisationService ls = new OrganisationService();
        d.setOrganisation_id(ls.getnomm(selectedOrganisation));
        d.setDescription(description.getText());
        d.setMontant((int) amountInCents);
        ds.create(d);

        if (showConfirmationAlert("Voulez-vous ajouter ce don ?")) {
            showSuccessAlert("Don ajouté avec succès.");
        }

        navback(event);
    }


    private boolean isAlphaWithSpaces(String str) {
        return str.matches("[a-zA-Z ]+");
    }

    private boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }

    private boolean isValidEmail(String email) {
        return email.matches("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
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
    void goHome(javafx.event.ActionEvent event) throws IOException {
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


    private boolean fieldsAreValid() {
        return !nom.getText().isEmpty() && !prenom.getText().isEmpty() && !email.getText().isEmpty() &&
                typeComboBox.getValue() != null && !description.getText().isEmpty() && !montant.getText().isEmpty();
    }

    @FXML
    void navback(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/listDon.fxml"));
            nom.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
