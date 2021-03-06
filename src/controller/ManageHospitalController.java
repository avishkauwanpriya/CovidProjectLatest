package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import database.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.hospitalModel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManageHospitalController {
    public AnchorPane mngHospitalWindow;
    public JFXTextField txtId;
    public JFXTextField txtHospitalName;
    public JFXTextField txtCity;
    public JFXTextField txtCapacity;
    public JFXTextField txtDirector;
    public JFXTextField txtDirectorContactNo;
    public JFXTextField txtHospitalContactNo1;
    public JFXTextField txtHospitalContactNo2;
    public JFXTextField txtHospitalFaxNo;
    public JFXTextField txtHospitalEMail;
    public JFXComboBox cmbDistrict;
    public JFXButton btnDelete;
    public JFXButton btnSave;

    public JFXButton btnNewUser;
    public JFXTextField txtSearchHospital;
    public JFXListView lstHospital;

    public  void initialize(){
        setHospitalInitialize();


        ArrayList<String> hospitalNames=new ArrayList<>();
        try {
            Connection connection = DBConnection.getDBConnection().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT hospitalName FROM hospital");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                hospitalNames.add(resultSet.getString(1));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        txtSearchHospital.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
               lstHospital.getItems().clear();
                for (String hospitalName:hospitalNames) {
                    if(hospitalName.contains(newValue)){
                        lstHospital.getItems().add(hospitalName);
                       lstHospital.refresh();
                    }


                }


            }
        });


    }


    public void btnHome_OnClick(MouseEvent mouseEvent) {
        try {
            Parent root= FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml"));
            Scene newScene=new Scene(root);
            Stage existingStage=(Stage)mngHospitalWindow.getScene().getWindow();
            existingStage.setScene(newScene);
            existingStage.setTitle("COVID 19 Surveillance System");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void btnAddNew_OnAction(ActionEvent actionEvent) {
        setNewHospitalOnClick();
        setId();
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getDBConnection().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM hospital WHERE hospitalId=(?)");
            preparedStatement.setObject(1,txtId.getText());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
        if(checkForEmptyFields()==false){
            new Alert(Alert.AlertType.ERROR,"Empty Fields", ButtonType.OK).show();
            return;

        }
        else if(validateCapacity()==false){
            new Alert(Alert.AlertType.ERROR,"Invalid Capacity", ButtonType.OK).show();
            return;

        }
        else if(validateContactNumbers(txtDirectorContactNo.getText())==false||validateContactNumbers(txtHospitalContactNo1.getText())==false||validateContactNumbers(txtHospitalContactNo2.getText())==false){
            new Alert(Alert.AlertType.ERROR,"Invalid ContactNo", ButtonType.OK).show();
            return;


        }


        try {
            
            Connection connection= DBConnection.getDBConnection().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO hospital VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setObject(1,txtId.getText()); 
            preparedStatement.setObject(2,txtHospitalName.getText()); 
            preparedStatement.setObject(3,txtCity.getText());
            preparedStatement.setObject(4,cmbDistrict.getSelectionModel().getSelectedItem());
            preparedStatement.setObject(5,txtCapacity.getText());
            preparedStatement.setObject(6,txtDirector.getText());
            preparedStatement.setObject(7,txtDirectorContactNo.getText());
            preparedStatement.setObject(8,txtHospitalContactNo1.getText());
            preparedStatement.setObject(9,txtHospitalContactNo2.getText());
            preparedStatement.setObject(10,txtHospitalFaxNo.getText());
            preparedStatement.setObject(11,txtHospitalEMail.getText());
            preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        btnSave.setDisable(true);

    }


    private void setNewHospitalOnClick(){
        txtId.clear();
        txtHospitalName.clear();
        txtCity.clear();
        txtCapacity.clear();
        txtDirector.clear();
        txtDirectorContactNo.clear();
        txtHospitalContactNo1.clear();
        txtHospitalContactNo2.clear();
        txtHospitalEMail.clear();
        txtHospitalFaxNo.clear();
        cmbDistrict.getSelectionModel().clearSelection();

        txtId.setDisable(false);
        txtHospitalName.setDisable(false);
        txtCity.setDisable(false);
        txtCapacity.setDisable(false);
        txtDirector.setDisable(false);
        txtDirectorContactNo.setDisable(false);
        txtHospitalContactNo1.setDisable(false);
        txtHospitalContactNo2.setDisable(false);
        txtHospitalEMail.setDisable(false);
        txtHospitalFaxNo.setDisable(false);


        cmbDistrict.setDisable(false);


        btnSave.setDisable(false);
        btnDelete.setDisable(false);


    }
    private void setHospitalInitialize(){
        ObservableList list= FXCollections.observableArrayList("Colombo","Ampara","Anuradhapura","Badulla","Batticaloa","Colombo","Galle","Gampaha","Hambantota","Jaffna","Kalutara","Kandy","Kegalle","Kilinochchi","Kurunegala","Mannar","Matale","Matara","Moneragala","Mullaitivu","Nuwara Eliya","Polonnaruwa","Puttalam","Ratnapura","Trincomalee","Vavuniya");
        cmbDistrict.setItems(list);

        txtId.setDisable(true);
        txtHospitalName.setDisable(true);
        txtCity.setDisable(true);
        txtCapacity.setDisable(true);
        txtDirector.setDisable(true);
        txtDirectorContactNo.setDisable(true);
        txtHospitalContactNo1.setDisable(true);
        txtHospitalContactNo2.setDisable(true);
        txtHospitalEMail.setDisable(true);
        txtHospitalFaxNo.setDisable(true);


        cmbDistrict.setDisable(true);


        btnSave.setDisable(true);








    }
    private void setId(){
        ArrayList<String> hospitalIds=new ArrayList<>();
        try {
            Connection connection = DBConnection.getDBConnection().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM hospital");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                    hospitalIds.add(resultSet.getString(1));
                }
            if(hospitalIds.size()==0){
                txtId.setText("H001");
            }
            else {
                int lastIdNum = Integer.parseInt(hospitalIds.get(hospitalIds.size() - 1).substring(1, 4));
                if (lastIdNum < 9) {
                    txtId.setText("H00" + (lastIdNum + 1));
                } else if (lastIdNum < 99) {
                    txtId.setText("H0" + (lastIdNum + 1));

                } else {
                    txtId.setText("H" + (lastIdNum + 1));


                }
            }

            connection.close();

            } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }



    private boolean checkForEmptyFields(){

        boolean flag=true;
        if(txtHospitalName.getText().trim().length()==0||txtCity.getText().trim().length()==0||txtCapacity.getText().trim().length()==0||txtHospitalContactNo1.getText().trim().length()==0||txtHospitalContactNo2.getText().trim().length()==0||txtHospitalEMail.getText().trim().length()==0||txtHospitalFaxNo.getText().trim().length()==0||txtDirector.getText().trim().length()==0||txtDirectorContactNo.getText().trim().length()==0||cmbDistrict.getSelectionModel().getSelectedItem()==null){
            flag=false;
        }


        return flag;
    }

    private boolean validateCapacity(){
        boolean flag=true;
        String capacity=txtCapacity.getText();
        for(int i=0;i<capacity.length();i++){
            if(Character.isDigit(capacity.charAt(i))==false){
                flag=false;
                break;
            }

        }
        return flag;



    }
    private boolean validateContactNumbers(String str){
        boolean flag=true;
        /*String directorContactNo = txtDirectorContactNo.getText();
        String hospitalContactNo1= txtHospitalContactNo1.getText();
        String hospitalContactNo2 = txtHospitalContactNo2.getText();
        */
        if(str.length()!=11){
            flag=false;
        }
        else {

            for (int i = 0; i < str.length(); i++) {
                if (Character.isDigit(str.charAt(i)) == false) {
                    if (i == 3) {
                        if(str.charAt(i)=='-'){
                            flag = true;}
                        else{
                            flag=false;
                        }
                    } else {
                        flag = false;
                    }
                }

            }
        }

        return flag;
    }

}
