/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChirpChirpSrc;

/**
 * Classe che implementa l'interfaccia UserFactory e restituisce un oggetto di tipo CCAdminImpl
 * @author Daniele
 */
public class CCAdminFactory implements UserFactory {
    @Override
    public CCAdmin createUser(String username)
    {
        return new CCAdminImpl(username);
    }
    
}
