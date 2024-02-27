/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChirpChirpSrc;
/**
 * Classe che implementa UserFactory e che serve a creare oggetti di tipo CCUserImpl
 * @author Daniele
 */
public class CCUserFactory implements UserFactory {
    @Override
    public CCUser createUser(String username)
    {
        return new CCUserImpl(username);
    }
}
