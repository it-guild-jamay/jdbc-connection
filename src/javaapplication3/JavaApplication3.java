/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication3;

import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Ricardo
 */
public class JavaApplication3 {

    /**
     * @param args the command line arguments
     */
    
    private static String headerFormat = "|", rowFormat = "|", separator = "+";
    protected static Connection co;
    public static void main(String[] args) {
        // TODO code application logic here
        
        try {
            //cargamos el driver de mysql
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            //creamos la conexion a la bd
            co = DriverManager.getConnection("jdbc:mysql://"+DataDB.DBRemoteUrl+":3306/classicmodels", DataDB.user, DataDB.pass);
            //co = DriverManager.getConnection("jdbc:mysql://localhost:3306/My_dn_name", "root", "my_password");
            //Creamos el objeto de tipo statement para poder hacer las consultas a la bd
            
            //JavaApplication3.getCustomerData();
            //JavaApplication3.getCustomersByCredit(1000, 20000);
            //JavaApplication3.insertOffice();
            //JavaApplication3.innerJoin();
            JavaApplication3.leftJoin();
            JavaApplication3.rightJoin();
            
            System.out.println("Conectado correctamente a la Base de Datos");
        } catch (ClassNotFoundException e) {
            System.out.println("Clase no encontrada: "+e);
        } catch (SQLException e) {
            System.out.println("Error de conexion: "+e);
        } catch (Exception e) {
            System.out.println("Error desconocido: "+e);
        }
    }
    
    private static void getCustomerData(){
        try {
            Statement stm = co.createStatement();
            ResultSet result = stm.executeQuery("select * from customers limit 1");
            
            showformatResponse(result);
        } catch (SQLException ex) {
            Logger.getLogger(JavaApplication3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void getCustomersByCredit(Integer start, Integer end){
         try {
            Statement stm = co.createStatement();
            ResultSet result = stm.executeQuery("SELECT customerName,creditLimit FROM customers WHERE creditLimit BETWEEN '"+start+"' AND '"+end+"'");
            
            showformatResponse(result);
        } catch (SQLException ex) {
            Logger.getLogger(JavaApplication3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void innerJoin(){
         try {
            Statement stm = co.createStatement();
            ResultSet result = stm.executeQuery("SELECT offices.officeCode,employees.firstName,employees.email,employees.jobTitle FROM employees INNER JOIN offices ON employees.officeCode = offices.officeCode limit 10");
            
            showformatResponse(result);
        } catch (SQLException ex) {
            Logger.getLogger(JavaApplication3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void leftJoin(){
         try {
            Statement stm = co.createStatement();
            ResultSet result = stm.executeQuery("SELECT * FROM employees LEFT JOIN offices ON employees.officeCode = offices.officeCode");
            
            showformatResponse(result);
        } catch (SQLException ex) {
            Logger.getLogger(JavaApplication3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void insertOffice(){
         try {
            Statement stm = co.createStatement();
            stm.executeUpdate("INSERT INTO offices (officeCode,city, phone, addressLine1, state, country, postalCode, territory) VALUES (9,'Guadalajara','123345876', 'ejemplo', 'JAL', 'MX', '49000', 'NA')");
            
             System.out.println("SE INSERTO LA OFICINA");
        } catch (SQLException ex) {
            Logger.getLogger(JavaApplication3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void rightJoin(){
         try {
            Statement stm = co.createStatement();
            ResultSet result = stm.executeQuery("SELECT * FROM employees RIGHT JOIN offices ON employees.officeCode = offices.officeCode");
            
            showformatResponse(result);
        } catch (SQLException ex) {
            Logger.getLogger(JavaApplication3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void showformatResponse(ResultSet result){
        try {
            JavaApplication3.createHeaders(result);
            ResultSetMetaData meta = result.getMetaData();
            String columns[] = new String[meta.getColumnCount()];
            while(result.next()){
                for(int i = 1; i <= meta.getColumnCount(); i++){
                    if( result.getString(i) != null){
                        if(meta.getColumnName(i).length()+10 < result.getString(i).length() ){
                            columns[i-1] = (result.getString(i)).substring(0, ( (meta.getColumnName(i).length()+10) -1));
                        }else{
                            columns[i-1] = result.getString(i);
                        }
                    }else{
                        columns[i-1] = result.getString(i);
                    }
                    
                }
                System.out.format(JavaApplication3.headerFormat, columns);
                System.out.println(JavaApplication3.separator);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaApplication3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void createHeaders(ResultSet r){
        try {
            JavaApplication3.headerFormat = "|";
            JavaApplication3.separator = "+";
            ResultSetMetaData meta = r.getMetaData();
            System.out.println("Este es el total de columnas: "+meta.getColumnCount());
            getColumnType(meta);
            
            String columns[] = new String[meta.getColumnCount()];
            for(int i = 1; i <= meta.getColumnCount(); i++){
                columns[i-1] = meta.getColumnName(i);
            }
            //IMPRIMIMOS EL ENCABEZADO DE LA TABLA
            System.out.println(JavaApplication3.separator);
            System.out.format(JavaApplication3.headerFormat, columns);
            System.out.println(JavaApplication3.separator);
        } catch (SQLException ex) {
            Logger.getLogger(JavaApplication3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void getColumnType(ResultSetMetaData meta){
        try {
            for(int i = 1; i <= meta.getColumnCount(); i++){
                String symbol = switch (meta.getColumnTypeName(i)) {
                    case "INT" -> "%-"+(meta.getColumnName(i).length())+"d";
                    case "VARCHAR" -> "%-"+(meta.getColumnName(i).length())+"s";
                    case "DECIMAL" -> "%-"+(meta.getColumnName(i).length())+"d";
                    default -> "%-15s";   
                };
                //format for rows
                JavaApplication3.rowFormat += " "+symbol+" |";
                //format for rows
                JavaApplication3.separator += "-".repeat(meta.getColumnName(i).length()+11)+"+";
                //format for headers
                JavaApplication3.headerFormat += " %-"+(meta.getColumnName(i).length()+9)+"s |";
            }
            
            //format for rows
            JavaApplication3.headerFormat += "%n";
            //format for headers
            JavaApplication3.rowFormat += "%n";
        } catch (SQLException ex) {
            Logger.getLogger(JavaApplication3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
