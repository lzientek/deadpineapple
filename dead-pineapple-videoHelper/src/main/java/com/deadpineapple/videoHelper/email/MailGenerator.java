package com.deadpineapple.videoHelper.email;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;

@SuppressWarnings("Since15")
/**
 * Created by 15256 on 09/05/2016.
 */
public class MailGenerator {

    public static final String FICHIER_CONVERTIE_TEMPLATE = "fichierConvertie";
    public static final String PAYEMENT_CONFIRMER_TEMPLATE = "payementConfirmer";

    private final String templateName;
    private final Hashtable<String, String> tableauDeCorespondance;
    private String template;


    public MailGenerator(String templateName, Hashtable<String, String> tableauDeCorespondance) throws IOException {

        this.templateName = templateName;
        this.template = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir"),"dead-pineapple-videoHelper/src/main/java/com/deadpineapple/videoHelper/email/templates",templateName + ".html")));
        this.tableauDeCorespondance = tableauDeCorespondance;
    }

    public String generateTheEmail() {
        String result = template;
        for (String key :
                tableauDeCorespondance.keySet()) {
            result = result.replace("{{" + key + "}}", tableauDeCorespondance.get(key));
        }
        return result;
    }

    //static class to generate hashtables

    public static Hashtable<String,String> getConvertedFileConrespondanceTable(String userName, String fileName, String downloadLink, String downloadImgLink){
        Hashtable<String,String> result = new Hashtable<String, String>();
        result.put("userName",userName);
        result.put("nomFichier",fileName);
        result.put("lienDl",downloadLink);
        result.put("lienImg",downloadImgLink);
        return result;
    }

    public static Hashtable<String,String> getPayementConrespondanceTable(String userName, String fileName, String downloadLink, String downloadImgLink){
        Hashtable<String,String> result = new Hashtable<String, String>();
        result.put("userName",userName);
        result.put("nomFichier",fileName);
        result.put("lienDl",downloadLink);
        result.put("lienImg",downloadImgLink);
        return result;
    }

}
