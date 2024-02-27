/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ChirpChirpSrc;

/**
 * Interfaccia che definisce una factory che consente di creare oggetti di tipo User (Factory Method)
 * @author Daniele
 */
public interface UserFactory {
    public abstract User createUser(String username);
}
