package com.deadpineapple.videoHelper;

import com.deadpineapple.videoHelper.email.EmailSender;
import com.deadpineapple.videoHelper.email.MailGenerator;

import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by 15256 on 09/05/2016.
 */
public class MailTestMain {
    public static void main(String[] args) throws IOException {
        Hashtable<String, String> values = MailGenerator.getConvertedFileConrespondanceTable("Sofiane AZIRI", "GoT_s6_e5.mkv", "http://google.com", "https://photos-6.dropbox.com/t/2/AAAEFI367SZuTY4cByBgm67lc9aFD7eu8hVPDisYUb4DBg/12/114048003/png/32x32/3/1463083200/0/2/donwload.png/EOnYhFgY7M8DIAIoAigE/orL1LMvZy8K1hgt2nVJbAo4dikC9dbhuFvM90hFuzn0?size_mode=5&size=32x32");
        MailGenerator mail = new MailGenerator(MailGenerator.FICHIER_CONVERTIE_TEMPLATE, values);
        EmailSender sender = new EmailSender("152565@supinfo.com", "Votre video est convertie", mail.generateTheEmail());
        sender.send();
    }
}
