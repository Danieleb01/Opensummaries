/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChirpChirpSrc;
import java.util.ArrayList;

/**
 * Classe che implementa l'interfaccia CCMessage
 * @author Daniele
 */
public class CCMessageImpl implements CCMessage{
    private String body;    // Testo del messaggio
    private ArrayList<String> hashtags; // Hashtag del messaggio
    
    public CCMessageImpl(String msgBody, String msgTags)    // Costruttore
    {
        setMsgBody(msgBody);    // Imposta il testo
        if(msgTags != null) // Se sono presenti degli hashtag...
        {
            /* Inseriscili nell'array hashtags tramite l'apposito metodo */
            hashtags= new ArrayList<String>();
            setHashtags(msgTags);   
        }
    }
    
    // Metodo per impostare il testo del messaggio
    @Override
    public void setMsgBody(String content)
    {
        this.body= content;
    }
    
    // Metodo per impostare gli hashtag
    @Override
    public void setHashtags(String msgTags)
    {
        // Converti la stringa ottenuta in un array di stringhe (singole parole) separandole in base alla virgola
        String[] parole= msgTags.split(",");
        // Aggiungile all'array hashtags
        for(String parola: parole)
            hashtags.add(parola);
    }
}
